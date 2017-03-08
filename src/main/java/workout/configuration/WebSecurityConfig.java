package workout.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import workout.component.*;

/**
 * Created by mihael on 2.2.2017..
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint()).and()
            .authorizeRequests()
                .antMatchers("/", "/login", "/api/register", "/logout", "/css/**", "/js/**", "/favicon.ico", "/dev/**").permitAll()
                .anyRequest().authenticated().and()
            .formLogin()
                .loginProcessingUrl("/login")
                .successHandler(new LoginSuccessHandler())
                .failureHandler(new LoginFailureHandler()).and()
            .logout().logoutSuccessHandler(new CustomLogoutSuccessHandler()).and()
            .cors().and()
            .csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedOrigins("*")
                        .allowedHeaders("*")
                        .allowedMethods("GET", "POST", "HEAD", "DELETE", "PATCH", "PUT", "OPTIONS");
            }
        };
    }

}
