package com.sunrider.parfume.service;

import com.sunrider.parfume.dto.OAuth2UserInfo;
import com.sunrider.parfume.model.AuthProvider;
import com.sunrider.parfume.model.Role;
import com.sunrider.parfume.model.User;
import com.sunrider.parfume.repository.UserRepository;
import com.sunrider.parfume.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.*;

@Service
public class AuthenticationService {

    private final JwtProvider jwtProvider;
    private final MailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${hostname}")
    private String hostname;

    public AuthenticationService(JwtProvider jwtProvider,
                                 MailSender mailSender,
                                 PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public Map<String,String> login(String email){
        User user = userRepository.findByEmail(email);
        String userRole = user.getRoles().iterator().next().name();
        String token = jwtProvider.createToken(email,userRole);

        Map<String,String> response = new HashMap<>();
        response.put("email", email);
        response.put("token", token);
        response.put("userRole", userRole);
        return response;
    }

    public boolean registerUser(User user) throws MessagingException {
        User userEntity = userRepository.findByEmail(user.getEmail());
        if(userEntity != null) return false;
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setProvider(AuthProvider.LOCAL);
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        String subject = "Activation code";
        String template = "registration-template";
        Map<String,Object> attributes = new HashMap<>();
        attributes.put("firstName", user.getFirstName());
        attributes.put("registrationUrl", "http://" + hostname + "/activate/" + user.getActivationCode());
        mailSender.sendMessageHtml(user.getEmail(), subject,template,attributes);
        return true;
    }

    public User registerOath2User(String provider, OAuth2UserInfo oAuth2UserInfo){
        User user = new User();
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setFirstName(oAuth2UserInfo.getFirstName());
        user.setLastName(oAuth2UserInfo.getLastName());
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setProvider(AuthProvider.valueOf(provider.toUpperCase(Locale.ROOT)));
        return userRepository.save(user);
    }

    public User updateOauth2User(User user, String provider, OAuth2UserInfo oAuth2UserInfo ){
        user.setFirstName(oAuth2UserInfo.getFirstName());
        user.setLastName(oAuth2UserInfo.getLastName());
        user.setProvider(AuthProvider.valueOf(provider.toUpperCase()));
        return userRepository.save(user);
    }

    public User findByPasswordResetCode(String code){
        return userRepository.findByPasswordResetCode(code);
    }

    public boolean sendPasswordResetCode(String email) throws MessagingException {
        User user = userRepository.findByEmail(email);
        if(user == null) return false;
        user.setPasswordResetCode(UUID.randomUUID().toString());
        userRepository.save(user);

        String subject = "Password reset";
        String template = "password reset-template";
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("firstName", user.getFirstName());
        attributes.put("resetUrl", "http://" + hostname + "/reset/" + user.getPasswordResetCode());
        mailSender.sendMessageHtml(user.getEmail(), subject,template, attributes);
        return true;
    }

    public boolean activateUser(String code){
        User user = userRepository.findByActivationCode(code);
        if(user == null) return false;
        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);
        return true;
    }

    public String passwordReset(String email, String password){
        User user = userRepository.findByEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPasswordResetCode(null);
        userRepository.save(user);
        return "Password reset";
    }
}
