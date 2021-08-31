package com.sunrider.parfume.security.oauth2;

import com.sunrider.parfume.dto.OAuth2UserInfo;
import com.sunrider.parfume.model.User;
import com.sunrider.parfume.security.UserPrincipal;
import com.sunrider.parfume.service.AuthenticationService;
import com.sunrider.parfume.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthenticationService authenticationService;
    private final UserService userService;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User auth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserFactory.getOAuth2UserInfo(provider
        ,auth2User.getAttributes());
        User user = userService.findByEmail(oAuth2UserInfo.getEmail());
        if(user == null){
            user = authenticationService.registerOath2User(provider,oAuth2UserInfo);
        }else {
            user = authenticationService.updateOauth2User(user,provider,oAuth2UserInfo);
        }
        return UserPrincipal.create(user,oAuth2UserInfo.getAttributes());
    }
}
