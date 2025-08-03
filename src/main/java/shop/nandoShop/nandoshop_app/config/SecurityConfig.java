package shop.nandoShop.nandoshop_app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shop.nandoShop.nandoshop_app.config.filters.JwtAuthenticationFilter;
import shop.nandoShop.nandoshop_app.services.impl.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(
                                "/v1/auth/login",
                                "/v1/auth/register",
                                "/v1/auth/me",
                                "/v1/auth/google",
                                "/v1/products/all",
                                "/v1/products/random/8",
                                "/v1/payments/create",
                                "/v1/product/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/my_products").hasAuthority("SELLER")
                        .requestMatchers(HttpMethod.POST, "/v1/product").hasAnyAuthority("SELLER")
                        .requestMatchers(HttpMethod.DELETE, "/v1/product/**").hasAnyAuthority("SELLER")
                        .requestMatchers(HttpMethod.PUT, "/v1/product/stock/**").hasAnyAuthority("SELLER")
                        .requestMatchers(HttpMethod.PUT, "/v1/product/price/**").hasAnyAuthority("SELLER")
                        .requestMatchers(
                                "/v1/admin/grant-seller-role",
                                "/v1/category"
                        ).hasAnyAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
