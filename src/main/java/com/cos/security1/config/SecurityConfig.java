package com.cos.security1.config;

import com.cos.security1.config.Oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    // @Bean 해당 메서드의 리턴되는 오브젝트를 Ioc로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                // "/manager/**" 이 주소로 오면 인증 뿐만 아니라 "hasRole(ROLE_ADMIN) or hasRole(ROLE_MANAGER)"있는 사람만 접근 가능
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
                // "/manager/**" 이 주소로 오면 인증 뿐만 아니라 "hasRole('ROLE_ADMIN')")
//                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                // 권한이 없는 페이지에 접근 시 자동으로 로그인 페이지로 가게 하는 것.
                .and()
                .formLogin()
                .loginPage("/loginForm")
                // login이라는 주소가 호출 되면 시큐리티가 낚아채서 대신 로그인을 진행
                // controller 안에 Mapping("/login")을 만들지 않아도 된다.
                .usernameParameter("username")
                .loginProcessingUrl("/login")
                // 성공하면 "/" 페이지 호출
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserService);
    }
}