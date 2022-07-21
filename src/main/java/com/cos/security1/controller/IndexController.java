package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    // 암호화
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping({"","/"})
    public String index(){
        return "index";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user(){
        return "user";

    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager(){
        return "admin";
    }

    // 스프링 시큐리티가 해당 주소를 낚아 채버린다.("/login")
    // securityConfig 파일 생성 후 작동을 안한다.
    @GetMapping("/loginForm")
    public String login(){

        return "loginForm";
    }

    @PostMapping("/login")
    public String logindo(){

        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        user.setRole("ROLE_USER");

        // 그냥 save를 하면 회원가입 잘됨 but 비밀번호 1234로 저장된다.
        // 시큐리티로 패스워드 암호화 시켜야한다.
        // securityConfig @Override 시킨다 => BCryptPasswordEncoder
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    @ResponseBody
    public String info(){
        return "개인정보";
    }

    @ResponseBody
    @GetMapping("/test/login")
    public String loginTest(Authentication authentication, @AuthenticationPrincipal UserDetails userDetails){
        System.out.println("/test/login ===========");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication.getPrincipal() = " + principalDetails.getUser());

        System.out.println("userDetails.getUsername() = " + userDetails.getUsername());
        return "세션 정보 확인하기";
    }

    @ResponseBody
    @GetMapping("/test/oauth/login")
    public String testOAuthLogin(Authentication authentication,@AuthenticationPrincipal OAuth2User oauth){
        System.out.println("/test/oauth/login ===========");
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println(oauth2User.getAttributes());

        System.out.println("=============");
        System.out.println("authentication: " + oauth2User.getAttributes());
        System.out.println("@AuthenticationPrincipal 어노테이션 사용 : " + oauth.getAttributes());
        return "OAuth 세션 정보 확인하기";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalExHandler(IllegalArgumentException e){
        return "fin";
    }

    @GetMapping("/api2/members")
    public String getMember() {
        throw new IllegalArgumentException("잘못된 입력 값");
    }
}
