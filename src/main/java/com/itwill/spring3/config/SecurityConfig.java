package com.itwill.spring3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
// Web Security를 실행시키겠다.
@EnableWebSecurity
// 스프링 컨테이너에서 빈(bean)으로 생성, 관리 - 필요한 곳에 의존성 주입.
@Configuration
public class SecurityConfig {
	
	// Spring Security 5 버전부터는 비밀번호는 반드시 암호화를 해야 함. - 암호화를 안한 비밀번호로는 로그인하지 못함.
	// 비밀번호를 암호화하지 않으면 HTTP 403(access denied, 접근 거부) 또는
	// HTTP 500(internal server error, 내부 서버 오류)가 발생함.
	// 비밀번호 인코더(Password encoder) 객체를 bean으로 생성해야 함.
	@Bean
	public PasswordEncoder passwordEncoder() {
		// BCryptPasswordEncoder: 암호화 알고리즘 중 하나
		return new BCryptPasswordEncoder();
	}
	
	// 로그인할 때 사용할 임시 사용자(메모리에 임시 저장되는 사용자) 생성 => 지울거임.
	// 그냥 구조를 알라고 하는 거임.
	@Bean
	public UserDetailsService inMemoryUserDetailsService() {
		// 사용자 상세 정보
		UserDetails user1 = User
								.withUsername("user1")							// 로그인할 때 사용할 사용자 이름
								.password(passwordEncoder().encode("1111"))		// 로그인할 때 사용할 사용자 비밀번호
								.roles("USER")									// 사용자 권한(USER, ADMIN, ...)
								.build();										// UserDetails 객체 생성.
		
		// 사용자 상세 정보
		UserDetails user2 = User
								.withUsername("user2")							// 로그인할 때 사용할 사용자 이름
								.password(passwordEncoder().encode("2222"))		// 로그인할 때 사용할 사용자 비밀번호
								.roles("USER, ADMIN")							// 사용자 권한(USER, ADMIN, ...)
								.build();										// UserDetails 객체 생성.
		
		// 사용자 상세 정보
		UserDetails user3 = User
								.withUsername("user3")							// 로그인할 때 사용할 사용자 이름
								.password(passwordEncoder().encode("3333"))		// 로그인할 때 사용할 사용자 비밀번호
								.roles("ADMIN")									// 사용자 권한(USER, ADMIN, ...)
								.build();										// UserDetails 객체 생성.
		
		return new InMemoryUserDetailsManager(user1, user2, user3);
	}
	
	// Security Filter 설정 bean - 필수
	// 로그인/로그아웃 설정
	// 로그인 페이지 설정, 로그아웃 이후 이동할 페이지.
	// 페이지 접근 권한 - 로그인해야만 접근 가능한 페이지, 로그인 없이 접근 가능한 페이지
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// CSRF(Cross Site Request Forgery) 기능 비활성화:
		
		// CSRF(Cross Site Request Forgery) 기능을 활성화하면,
		// Ajax POST/PUT/DELETE 요청에서 CSRF 토큰을 서버로 전송하지 않으면 403 에러가 발생.
		http.csrf((csrf) -> csrf.disable());
		
		// 로그인 페이지 설정 - 스프링에서 제공하는 기본 로그인 페이지를 사용.
		http.formLogin(Customizer.withDefaults());
		
		// 로그아웃 이후 이동할 페이지
		http.logout(logout -> logout.logoutSuccessUrl("/"));
		
		// 페이지 접근 권한 설정
		// Controller에서 설정할 것이기 때문에 주석처리....
		// 단점: 새로운 요청 경로, 컨트롤러를 작성할 때마다 Config 자바 코드를 수정해야 함.
		// -> 컨트롤러 메서드를 작성할 때 애너테이션을 사용해서 접근 권한을 설정할 수도 있음.
		// (1) SecurityConfig 클래스에서는 @EnableMethodSecurity 애너테이션 설정
		// (2) 각각의 컨트롤러 메서드에서 @PreAuthorize 또는 @PostAuthorize 애너테이션을 사용.
		
		/*
		http.authorizeHttpRequests((authRequest) -> 
			authRequest		// 접근 권한을 설정할 수 있는 객체
			// 권한이 필요한 페이지들을 설정
			.requestMatchers("/post/create", "/post/details", "/post/modify"
					, "/post/update", "/post/delete", "/api/reply/**")
			// 아래는 권한 여부에 상관없이 아이디/비밀번호가 일치하면
//			.authenticated()	// 위 페이지 대신에 이 함수를 쓰면 => 인증이 끝났으면 접근이 가능하게 한다.(권한이 나뉘어져 있지 않으면 사용에 용이함.)
			.hasRole("USER")	// 위에서 설정한 페이지들이 USER 권한을 요구함을 설정.
			.anyRequest()		// .requestMatchers("/**"). 위 페이지들 이외의 모든 페이지
			.permitAll()		// 권한없이 접근 허용.
		);
		
		// 익명함수로 만드는 방법
		http.authorizeHttpRequests(new Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {

			@Override
			public void customize(
					AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry t) {
				t.requestMatchers("")
				.hasRole("")
				.anyRequest()
				.permitAll();
			}
			
		});
		*/
		
		return http.build();
	}
	
	
	

}
