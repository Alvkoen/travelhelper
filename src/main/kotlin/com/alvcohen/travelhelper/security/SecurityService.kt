package com.alvcohen.travelhelper.security

import com.alvcohen.travelhelper.AuthProvider
import com.alvcohen.travelhelper.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
open class CustomUserDetailsService(@Autowired val userRepository: UserRepository) : UserDetailsService {

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
                .orElseThrow { UsernameNotFoundException(String.format("User with email %s not found", email)) }
        return User
                .withUsername(user.email)
                .password(user.password)
                .authorities("ROLE_USER")
                .build()
    }
}


@Service
class OAuth2UserService(@Autowired val userRepository: UserRepository) : OidcUserService() {

    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(oAuth2UserRequest: OidcUserRequest): OidcUser {
        val oAuth2User = super.loadUser(oAuth2UserRequest)
        return try {
            processOAuthUser(oAuth2UserRequest, oAuth2User)
        } catch (ex: Exception) {
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    private fun processOAuthUser(oAuth2UserRequest: OidcUserRequest, oAuth2User: OidcUser): OidcUser {
        val userOptional = userRepository.findByEmail(oAuth2User.email)
        val user: com.alvcohen.travelhelper.User
        if (userOptional.isPresent) {
            user = userOptional.get()
            if (user.provider != AuthProvider.valueOf(oAuth2UserRequest.clientRegistration.registrationId.toUpperCase())) {
                throw OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.provider + " account. Please use your " + user.provider +
                        " account to login.")
            }
            updateExistingUser(user, oAuth2User)
        } else {
            registerNewUser(oAuth2UserRequest, oAuth2User)
        }
        return oAuth2User
    }

    private fun registerNewUser(oAuth2UserRequest: OAuth2UserRequest, oAuth2UserInfo: OidcUser) {
        val user = com.alvcohen.travelhelper.User(null,
                oAuth2UserInfo.email,
                oAuth2UserInfo.name,
                false,
                mutableSetOf(),
                mutableSetOf(),
                null,
                AuthProvider.GOOGLE,
                oAuth2UserRequest.clientRegistration.registrationId.toUpperCase())

        userRepository.save(user)
    }

    private fun updateExistingUser(existingUser: com.alvcohen.travelhelper.User, oAuth2UserInfo: OidcUser) {
        existingUser.name = oAuth2UserInfo.name
        userRepository.save(existingUser)
    }
}

class OAuth2AuthenticationProcessingException(msg: String?) : AuthenticationException(msg)

