package cn.emac.demo.petstore.services;

import cn.emac.demo.petstore.domain.jpetstore.tables.records.SignonRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SignonService signonService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new UsernameNotFoundException("username is blank");
        }

        SignonRecord record = signonService.findByName(username);
        if (record == null) {
            throw new UsernameNotFoundException("user doesn't exist");
        }

        return new org.springframework.security.core.userdetails.User(
//                username, "$2a$10$sdy7l9EvtCQ9EEjnRzrxVuogzxEw3tSxuxmANXLOyWeI09UPQU8Na", // 111111
                username, record.getPassword(),
                true,//是否可用
                true,//是否过期
                true,//证书不过期为true
                true,// 账户未锁定为true
                new HashSet<>());
    }
}
