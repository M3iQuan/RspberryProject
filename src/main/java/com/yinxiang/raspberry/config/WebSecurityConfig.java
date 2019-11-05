package com.yinxiang.raspberry.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yinxiang.raspberry.model.Result;
import com.yinxiang.raspberry.model.UserUtils;
import com.yinxiang.raspberry.service.UserService;
import com.yinxiang.raspberry.validate.code.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;

    @Autowired
    private ValidateCodeFilter validateCodeFilter;

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_admin > ROLE_controller  ROLE_controller> ROLE_browser";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override  //解决静态资源被拦截的问题，下面的忽略拦截，下面的路径不会走安全验证
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/index.html", "login_p","/static/**","/code/image/**","/status/**");//,"/manage","/basic/**","/device/**");
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(cfisms());
                        object.setAccessDecisionManager(cadm());
                        return object;
                    }
                })
                .and()
                .formLogin()
                //.defaultSuccessUrl()  这个可以用在登录成功后默认跳转的页面，比如用户首页
                .loginPage("/login_p")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest req,
                                                        HttpServletResponse resp,
                                                        Authentication auth)
                            throws IOException {
                        Object principal = auth.getPrincipal();  //通过getPrincipal()可以获取到代表当前用户的信息，这个对象通常是UserDetails的实例。
                        System.out.println(principal.toString());
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        resp.setStatus(200);  ////设置状态码；
                        Map<String, Object> map = new HashMap<>();
                        map.put("status", 200);
                        map.put("msg", principal);
                        System.out.println("用户名："+ UserUtils.getCurrentUser().getUsername());
                        ObjectMapper om = new ObjectMapper();
                        out.write(om.writeValueAsString(map));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .failureHandler(new AuthenticationFailureHandler() {
                        @Override
                        public void onAuthenticationFailure(HttpServletRequest req,
                                         HttpServletResponse resp,
                                         AuthenticationException e)
                        throws IOException {
                            resp.setContentType("application/json;charset=utf-8");
                            PrintWriter out = resp.getWriter();
                            //resp.setStatus(401);
                            Map<String, Object> map = new HashMap<>();
                            map.put("status", 401);
                            if (e instanceof LockedException) {
                                map.put("msg", "账户被锁定，登录失败!");
                            } else if (e instanceof BadCredentialsException) {
                                map.put("msg", "账户名或密码输入错误，登录失败!");
                            } else if (e instanceof DisabledException) {
                                map.put("msg", "账户被禁用，登录失败!");
                            } else if (e instanceof AccountExpiredException) {
                                map.put("msg", "账户已过期，登录失败!");
                            } else if (e instanceof CredentialsExpiredException) {
                                map.put("msg", "密码已过期，登录失败!");
                            }
                            else {
                                map.put("msg", "登录失败!");
                            }
                            ObjectMapper om = new ObjectMapper();
                            out.write(om.writeValueAsString(map));
                            out.flush();
                            out.close();
                        }
 })
                //.permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .addLogoutHandler(new LogoutHandler() {
                    @Override
                    public void logout(HttpServletRequest req,
                                       HttpServletResponse resp,
                                       Authentication auth) {

                    }
                })
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest req,
                                                HttpServletResponse resp,
                                                Authentication auth)
                            throws IOException {
                        resp.setContentType("application/json;charset=utf-8");
                        Result respBean = new Result();
                        respBean.setStatus(203);
                        respBean.setSuccess(true);
                        ObjectMapper om = new ObjectMapper();
                        PrintWriter out = resp.getWriter();
                        out.write(om.writeValueAsString(respBean));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                .sessionManagement()
                .invalidSessionUrl("/status/xiaji1")
                .and()
                .csrf()
                .disable()
                .cors();

        http.sessionManagement().maximumSessions(1).expiredUrl("/status/xiaji2");
    }
    @Bean
    CustomFilterInvocationSecurityMetadataSource cfisms() {
        return new CustomFilterInvocationSecurityMetadataSource();
    }
    @Bean
    CustomAccessDecisionManager cadm() {
        return new CustomAccessDecisionManager();
    }
}
