package cn.emac.demo.petstore.services.security;

import cn.emac.demo.petstore.domain.tables.pojos.Signon;
import cn.emac.demo.petstore.services.SignonService;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author Emac
 * @since 2016-02-11
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private SignonService signonService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("username is blank");
        }

        Signon record = signonService.findByName(username);
        if (record == null) {
            throw new UsernameNotFoundException("user doesn't exist");
        }

        return new org.springframework.security.core.userdetails.User(
                username, record.getPassword(),
                true,//是否可用
                true,//是否过期
                true,//证书不过期为true
                true,// 账户未锁定为true
                Sets.newHashSet(new SimpleGrantedAuthority("ROLE_DOCTOR")));
    }
}
