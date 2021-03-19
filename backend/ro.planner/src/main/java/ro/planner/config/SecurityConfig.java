package ro.planner.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.planner.service.UserService;

/**
 * This class implements all the operations for the security part
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;

    @Autowired
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    /**
     * This method sets all the operations that user or an admin can do
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/*/login").permitAll()
                .antMatchers(HttpMethod.POST, "/user/create-user").permitAll()
                .antMatchers(HttpMethod.POST, "/activity").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/activity").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/activity/person/{personId}").hasRole( "USER")
                .antMatchers(HttpMethod.DELETE, "/activity/{id}").hasRole( "ADMIN")
                .antMatchers(HttpMethod.POST, "/calendar-entry").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/calendar-entry/complete-activity").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/calendar-entry/person-day").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/calendar-entry/{id}").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/category").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/category").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/category/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/completed-entry").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/completed-entry/person-day").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/diary-entry").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/diary-entry/person-day").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/person").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/role").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/user").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/user").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/user/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/recommended/{userId}").hasRole("USER")
                //.antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable()
                .httpBasic();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        /* To allow Pre-flight [OPTIONS] request from browser */
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public UserService getUserService() {
        return userService;
    }
}