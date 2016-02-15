package cn.emac.demo.petstore.services;

import static cn.emac.demo.petstore.domain.jpetstore.Tables.*;

import cn.emac.demo.petstore.domain.jpetstore.tables.records.SignonRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Emac
 * @since 2016-02-15
 */
@Service
@Transactional
public class SignonService {

    @Autowired
    private DSLContext dsl;

    public List<SignonRecord> findAll() {
        return dsl.fetch(SIGNON);
    }

    public SignonRecord findByName(String username) {
        return dsl.selectFrom(SIGNON).where(SIGNON.USERNAME.eq(username)).fetchOne();
    }

}
