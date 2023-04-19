package kz.bitlab.javapro.security.springsecuritydemo.config;

import kz.bitlab.javapro.security.springsecuritydemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(proxyTargetClass = true, securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling().accessDeniedPage("/forbidden");

        http.authorizeRequests().antMatchers("/css/**","/js/**").permitAll();

        http.formLogin()
                .loginProcessingUrl("/vhod")           //<form action="/vhod" method="POST">
                .usernameParameter("user-email")       //<input type="email" name="user-email">
                .passwordParameter("user-password")    //<input type="password" name="user-password">
                .defaultSuccessUrl("/profile")         //response.sendRedirect("/profile)
                .failureUrl("/enter?loginerror")   //response.sendRedirect("/enter?loginerror)
                .loginPage("/enter").permitAll();

        http.logout()
                .logoutUrl("/vyhod")            //<form action="/vyhod" method="POST">
                .logoutSuccessUrl("/enter");    //response.sendRedirect("/enter)
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
