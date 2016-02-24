package cn.emac.demo.petstore.services;

import cn.emac.demo.petstore.domain.tables.daos.SignonDao;
import cn.emac.demo.petstore.domain.tables.pojos.Signon;
import cn.emac.demo.petstore.domain.tables.records.SignonRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cn.emac.demo.petstore.domain.Tables.SIGNON;

/**
 * @author Emac
 * @since 2016-02-15
 */
@Service
@Transactional
public class SignonService {

    @Autowired
    private DSLContext dsl;

    @Autowired
    private SignonDao dao;

    public List<SignonRecord> findAll() {
        return dsl.fetch(SIGNON);
    }

    @Cacheable(value="signonCache", key="'petstore:signon:'+#username", unless="#result==null")
    public Signon findByName(String username) {
//        return dsl.selectFrom(SIGNON).where(SIGNON.USERNAME.eq(username)).fetchOne().into(Signon.class);
        return dao.fetchOneByUsername(username);
    }

}
