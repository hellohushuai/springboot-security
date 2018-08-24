package com.us.example.service;

import com.us.example.dao.PermissionDao;
import com.us.example.dao.UserDao;
import com.us.example.domain.Permission;
import com.us.example.domain.SysRole;
import com.us.example.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @class_name: CustomUserService
 * @package: com.us.example.service
 * @describe: 自定义UserDetailsService
 * @author: shuaihu2
 * @creat_date: 2018/8/24
 * @creat_time: 19:16
 **/
@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    UserDao userDao;
    @Autowired
    PermissionDao permissionDao;

    /**
     * @method_name: loadUserByUsername
     * @param: [username]
     * @describe: 重写loadUserByUsername
     * @author: shuaihu2
     * @creat_date: 2018/8/24
     * @creat_time: 19:17
     **/
    @Override
    public UserDetails loadUserByUsername(String username) {
        SysUser user = userDao.findByUserName(username);
        if (user != null) {
            List<Permission> permissions = permissionDao.findByAdminUserId(user.getId());
            List<GrantedAuthority> grantedAuthorities = new ArrayList <>();
            for (Permission permission : permissions) {
                if (permission != null && permission.getName()!=null) {

                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getName());
                grantedAuthorities.add(grantedAuthority);
                }
            }
            return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
        } else {
            throw new UsernameNotFoundException("admin: " + username + " do not exist!");
        }
    }

}
