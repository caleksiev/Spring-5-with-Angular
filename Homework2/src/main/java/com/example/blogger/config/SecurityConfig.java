package com.example.blogger.config;

import com.example.blogger.security.RestAuthenticationEntryPoint;
import com.example.blogger.services.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImp userDetailsService;

    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    SecurityHandler securityHandler;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.userDetailsService);
        authProvider.setPasswordEncoder(this.encoder());
        auth.authenticationProvider(authProvider);
    }


    @Autowired
    private LoggingAccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/js/**",
                        "/css/**",
                        "/img/**",
                        "/webjars/**").permitAll()
                .antMatchers(HttpMethod.GET,"/users/user-form").permitAll()
                .antMatchers(HttpMethod.POST,"/users/user-form").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginPage("/login?success")
                .successHandler(securityHandler)
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(restAuthenticationEntryPoint)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/actuator/info").permitAll()
//                .antMatchers("/actuator/health").permitAll()
//                .antMatchers("/v2/api-docs").permitAll()
//                .antMatchers("/swagger*/**").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
//                .antMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
//                .antMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("BLOGGER","ADMIN")
//                .antMatchers(HttpMethod.PUT, "/api/users/{id}").hasAnyRole("BLOGGER","ADMIN")
//                .antMatchers(HttpMethod.DELETE, "/api/users{id}").hasAnyRole("BLOGGER","ADMIN")
//                .antMatchers(HttpMethod.GET, "/posts/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/posts/**").authenticated()
//                .antMatchers(HttpMethod.PUT, "/api/posts/**").authenticated()
//                .antMatchers(HttpMethod.DELETE, "/api/posts/**").authenticated()
//                .antMatchers(
//                "/",
//                "/resources/**",
//                "/js/**",
//                "/css/**",
//                "/img/**",
//                "/webjars/**").permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .successForwardUrl("/posts")
//                .permitAll()
//                .successHandler(authenticationSuccessHandler)
//                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
//                .and()
//                .logout()
//                .deleteCookies("JSESSIONID")
//                .logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)));
//    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(10);
    }
}
