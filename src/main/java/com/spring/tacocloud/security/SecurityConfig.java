package com.spring.tacocloud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepositoryUserDetailsService userDetailsService;

    //对myEncoder的调用，将会被拦截并返回容器中的passwordEncoder实例
    @Bean
    public PasswordEncoder myEncoder(){
        return new StandardPasswordEncoder("53cr3t");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .userDetailsService(userDetailsService)
                  .passwordEncoder(myEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                  .antMatchers("/design","/orders/current","/orders")
                     .hasRole("USER")
                  .antMatchers("/","/**").permitAll()


                .and()
                  .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/design",true)

                .and()
                  .logout()
                    .logoutSuccessUrl("/")

                .and()
                  .csrf()
                  .ignoringAntMatchers("/h2/**")

        ;

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2/**");
    }
}
