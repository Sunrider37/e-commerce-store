package com.sunrider.parfume.config;

import com.sunrider.parfume.security.JwtConfigurer;
import com.sunrider.parfume.security.oauth2.CustomOAuth2UserService;
import com.sunrider.parfume.security.oauth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOAuth2UserService oAuth2Service;
    private final JwtConfigurer jwtConfigurer;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/**",
                        "/api/registration/**",
                        "/api/perfumes/**",
                        "/api/users/cart",
                        "/api/users/order/**",
                        "/api/users/review",
                        "/websocket", "/websocket/**",
                        "/img/**",
                        "/static/**").permitAll()
                .antMatchers("/auth/**","/oauth2/",
                        "/**/*swagger*/**", "/v2/api-docs")
                .permitAll().anyRequest().authenticated()
                .and()
                .oauth2Login()
                .authorizationEndpoint().baseUri("oath2/authorize")
                .and()
                .userInfoEndpoint().userService(oAuth2Service)
                .and()
                .successHandler(oAuth2SuccessHandler)
                .and()
                .apply(jwtConfigurer);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
