package com.alvcohen.travelhelper.config.security;

import com.alvcohen.travelhelper.exception.OAuth2AuthenticationProcessingException;
import com.alvcohen.travelhelper.model.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException(String.format("Login with %s is not supported", registrationId));
        }
    }
}
