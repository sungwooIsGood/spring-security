package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인을 진행이 완료가 되면 시큐리티가 가지고 있는 session을 만들어 준다.
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; // 콤포지션

    // 생성자
    public PrincipalDetails(User user){
        this.user = user;
    }


    // 해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        user.getRole(); => String이다. 타입을 Collection으로 변환 해야함.
//        아래 코드는 변환 해주는 과정
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

//    이 계정이 만료됐는지 물어보는것
    @Override
    public boolean isAccountNonExpired() {
        return true; // 아니요
    }

    //    이 계정이 잠겨있는지
    @Override
    public boolean isAccountNonLocked() {
        return true; // 아니요
    }

    //    이 계정이 기간이 너무 오래 사용했니
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 아니요
    }

//    이 계정이 활성화 되었는지
    @Override
    public boolean isEnabled() {

        // 언제 false로 바꿔야 할까?
        // ex) 우리 사이트에서 1년동안 로그인을 안하면 휴먼 계정으로 돌림
        // 현재 시간 - 로그인 시간 => 1년을 초과하면 return false
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }
}
