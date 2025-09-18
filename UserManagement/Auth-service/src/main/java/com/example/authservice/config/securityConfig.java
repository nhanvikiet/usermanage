//package com.example.authservice.config;
//
//import com.example.authservice.Util.JwtAuthFilter;
//import com.example.authservice.repository.UserRepository;
//import com.example.authservice.service.CustomUserDetailsService;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//
//@RequiredArgsConstructor
//public class    securityConfig {
//
//    private final CustomUserDetailsService userDetailsService;
//
//    private final JwtAuthFilter jwtAuthFilter;
//
//    public securityConfig(CustomUserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    // Password encoder dùng để encode + verify password
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // Cấu hình AuthenticationManager để dùng UserDetailsService + PasswordEncoder
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    // Cấu hình filter chain
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/**", "/h2-console/**")
//                        .permitAll()
//                        .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//        // Cho phép hiển thị H2 console (frame)
//        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
//
//        return http.build();
//    }
//}
package com.example.authservice.config;

//import com.example.authservice.Util.JwtAuthFilter;
import com.example.authservice.Util.JwtAuthFilter;
import com.example.authservice.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
   //  tự động inject JwtAuthFilter qua constructor
public class securityConfig {

    //private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;
    public  securityConfig( CustomUserDetailsService customUserDetailsService) {
        //this.jwtAuthFilter = jwtAuthFilter;
        this.customUserDetailsService = customUserDetailsService;
    }
    // Mã hóa password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager cho login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Security filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                //.cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // Cho phép không cần token
                        .requestMatchers("/auth/login","/auth/users", "/h2-console/**").permitAll()
                        // Chỉ ADMIN mới được CRUID user
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/auth/users/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/auth/users/{id}").permitAll()
                        // Các request khác phải login
                        .anyRequest().authenticated()
                );
//                .SStateless session.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Thêm JWT Filter vào trước UsernamePasswordAuthenticationFilter
        //http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Cho phép hiển thị H2 console
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // 👈 cho phép React FE
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
