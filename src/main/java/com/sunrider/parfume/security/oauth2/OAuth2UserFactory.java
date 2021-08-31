package com.sunrider.parfume.security.oauth2;

import com.sunrider.parfume.dto.OAuth2UserInfo;
import com.sunrider.parfume.exception.PerfumeException;
import com.sunrider.parfume.model.AuthProvider;
import org.apache.tomcat.websocket.AuthenticationException;

import java.util.Map;

public class OAuth2UserFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId,
                                                   Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())){
            return new GoogleOAuth2UserInfo(attributes);
        }else if(registrationId.equalsIgnoreCase(AuthProvider.FACEBOOK.toString())){
            return new FacebookOAuth2UserInfo(attributes);
        }else if(registrationId.equalsIgnoreCase(AuthProvider.GITHUB.toString())){
            return new GithubOAuth2UserInfo(attributes);
        }else {
            throw new PerfumeException("Error while authenticating");
        }


    }
}
