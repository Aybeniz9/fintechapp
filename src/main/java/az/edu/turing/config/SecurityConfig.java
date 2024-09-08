package az.edu.turing.config;

import org.springframework.context.annotation.Bean;

public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated()).formLogin(formLogin -> formLogin.loginPage("/login").permitAll()).logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/")).csrf(csrf -> csrf.disable());


        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/users/create").permitAll()
                                .anyRequest().authenticated()

                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
