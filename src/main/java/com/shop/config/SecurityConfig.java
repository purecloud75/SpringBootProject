package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity  // 오버라이딩을 통해 보안 설정을 커스터마이징 할 수 있음.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http 요청에 대한 보안을 설정함. 페이지 권한설정, 로그인 페이지설정 등.
        http.formLogin()
                .loginPage("/members/login")  // 로그인 페이지의 URL을 설정함.
                .defaultSuccessUrl("/")  // 로그인 성공 시 이동할 URL을 설정함. main.html을 띄운다는 의미이다.
                .usernameParameter("email")  // 로그인 시 사용할 파라미터 이름으로 email을 지정함. 그니까 아이디는 email, 비밀번호는 password 이다.
                .failureUrl("/members/login/error")  // 로그인 실패 시 이동할 URL을 설정함.
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))  // 로그아웃 성공 시 이동할 URL을 설정함.
                .logoutSuccessUrl("/");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // 비밀번호를 DB에 그대로 저장하지 않고, BCryptPasswordEncoder의 해시함수를 이용하여 비밀번호를 암호화하여 DB에 저장함.
    // BCryptPasswordEncoder를 빈으로 등록하여 사용할 것이다.

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
    // 스프링 시큐리티에서 인증은 AuthenticationManager 를 통해 이루어짐.
    // AuthenticationManagerBuilder 가 AuthenticationManager 를 생성함.
    // UserDetailsService 인터페이스를 구현하고 있는 객체로 MemberService 클래스를 지정함.
    // 비밀번호 암호화를 위해 passwordEncoder 인터페이스를 지정함.
}
