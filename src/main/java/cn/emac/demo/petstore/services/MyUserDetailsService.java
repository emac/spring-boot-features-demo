package cn.emac.demo.petstore.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Emac
 * @since 2016-02-11
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String loginName) {
        if (StringUtils.isBlank(loginName)) {
            throw new UsernameNotFoundException("loginName is blank");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();

        return new org.springframework.security.core.userdetails.User(
                loginName, "",
                true,//是否可用
                true,//是否过期
                true,//证书不过期为true
                true,// 账户未锁定为true
                authorities);
    }
}
