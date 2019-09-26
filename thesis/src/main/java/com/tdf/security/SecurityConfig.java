//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tdf.security;

import com.tdf.session.SessionContextMap;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity(
        debug = true
)
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    @Autowired
    SecurityFilterInvocationSecurityMetadataSource securityFilterInvocationSecurityMetadataSource;
    @Autowired
    SecurityAccessDecisionManager securityAccessDecisionManager;
    @Autowired
    AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;
    @Autowired
    SecurityFilterSecurityInterceptor securityFilterSecurityInterceptor;

    public SecurityConfig() {
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        ((HttpSecurity)((HttpSecurity)((FormLoginConfigurer)((FormLoginConfigurer)((HttpSecurity)((AuthorizedUrl)((AuthorizedUrl)http.authorizeRequests().antMatchers(new String[]{"/login/**", "/images/**", "/styles/**", "/scripts/**", "/demo/**", "/home/**", "/ajax/checkLogin"})).permitAll().anyRequest()).authenticated().and()).formLogin().loginPage("/login").failureUrl("/login-error")).permitAll()).and()).exceptionHandling().accessDeniedHandler(this.authenticationAccessDeniedHandler).accessDeniedPage("/accessDeniedHandler").and()).logout().logoutUrl("/logout").logoutSuccessUrl("/login").logoutSuccessHandler(this.logoutSuccessHandler()).invalidateHttpSession(true).clearAuthentication(true).deleteCookies(new String[]{"JSESSIONID"}).permitAll();
        http.addFilterBefore(this.securityFilterSecurityInterceptor, FilterSecurityInterceptor.class);
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandler() {
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                try {
                    SecurityContextHolder.clearContext();
                    UserDetails user = (UserDetails)authentication.getPrincipal();
                    SessionContextMap.DelSession(user.getUsername());
                } catch (Exception var5) {
                    var5.printStackTrace();
                }

                response.sendRedirect(request.getContextPath() + "/login");
            }
        };
    }
}
