package com.alvcohen.travelhelper.config;

import com.alvcohen.travelhelper.config.security.CustomUserDetailsService;
import com.alvcohen.travelhelper.config.security.HttpCookieOAuth2AuthorizationRequestRepository;
import com.alvcohen.travelhelper.config.security.OAuth2AuthenticationFailureHandler;
import com.alvcohen.travelhelper.config.security.OAuth2AuthenticationSuccessHandler;
import com.alvcohen.travelhelper.config.security.OAuth2UserService;
import com.alvcohen.travelhelper.config.security.RestAuthenticationEntryPoint;
import com.alvcohen.travelhelper.config.security.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomUserDetailsService customUserDetailsService;
    private OAuth2UserService OAuth2UserService;
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    public void setCustomUserDetailsService(final CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Autowired
    public void setOAuth2UserService(final com.alvcohen.travelhelper.config.security.OAuth2UserService OAuth2UserService) {
        this.OAuth2UserService = OAuth2UserService;
    }

    @Autowired
    public void setoAuth2AuthenticationSuccessHandler(final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler) {
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
    }

    @Autowired
    public void setoAuth2AuthenticationFailureHandler(final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler) {
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
    }

    @Autowired
    public void setHttpCookieOAuth2AuthorizationRequestRepository(final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    /*
      By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
      the authorization request. But, since our service is stateless, we can't save it in
      the session. We'll save the request in a Base64 encoded cookie instead.
    */
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .exceptionHandling()
            .authenticationEntryPoint(new RestAuthenticationEntryPoint())
            .and()
            .authorizeRequests()
            .antMatchers("/",
                "/error",
                "/favicon.ico",
                "/**/*.png",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",

                "/auth/**",
                "/oauth2/**"
            )
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .oauth2Login()
            .authorizationEndpoint()
            .baseUri("/oauth2/authorize")
            .authorizationRequestRepository(cookieAuthorizationRequestRepository())
            .and()
            .redirectionEndpoint()
            .baseUri("/oauth2/callback/*")
            .and()
            .userInfoEndpoint()
            .userService(OAuth2UserService)
            .and()
            .successHandler(oAuth2AuthenticationSuccessHandler)
            .failureHandler(oAuth2AuthenticationFailureHandler);

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}