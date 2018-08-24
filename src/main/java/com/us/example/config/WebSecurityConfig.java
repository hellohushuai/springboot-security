package com.us.example.config;


import com.us.example.service.MyFilterSecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @class_name: WebSecurityConfig
 * @package: com.us.example.config
 * @describe: security的配置
 * @author: shuaihu2
 * @creat_date: 2018/8/24
 * @creat_time: 19:03
 **/
@Configuration
@EnableWebSecurity
/** 开启security注解 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /** 注入自定义的security拦截器 */
    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

    /** 注入自定义UserDetailsService */
    @Autowired
    UserDetailsService customUserService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //user Details Service验证
        auth.userDetailsService(customUserService);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //放出被拦截的css文件
                .antMatchers("/css/**").permitAll()
                .anyRequest().authenticated() //任何请求,登录后可以访问
                .and()
                    .formLogin()
                    .loginPage("/login") //登陆页面
                    .defaultSuccessUrl("/") //默认成功转跳url
                    .failureUrl("/login?error") //失败跳转网页
                    .permitAll() //登录页面用户任意访问
                .and()
                    .logout().permitAll(); //注销行为任意访问
        http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/css/**");
    }
}

