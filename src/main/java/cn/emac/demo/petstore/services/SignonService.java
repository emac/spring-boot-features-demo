package cn.emac.demo.petstore.services;

import cn.emac.demo.petstore.common.pagination.PageBuilder;
import cn.emac.demo.petstore.domain.Tables;
import cn.emac.demo.petstore.domain.tables.daos.SignonDao;
import cn.emac.demo.petstore.domain.tables.pojos.Signon;
import cn.emac.demo.petstore.domain.tables.records.SignonRecord;
import org.jooq.DSLContext;
import org.jooq.SelectConditionStep;
import org.jooq.conf.ParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Emac
 * @since 2016-02-15
 */
@Service
@Transactional
@Validated
public class SignonService extends Tables {

    @Autowired
    private DSLContext dsl;

    @Autowired
    private SignonDao dao;

    public PageBuilder<Signon> findAllByPage(Pageable pageable) {
        SelectConditionStep<SignonRecord> where = dsl.selectFrom(SIGNON).where();
        String sqlBeforePage = where.getSQL(ParamType.INLINED);
        where.limit(pageable.getPageSize()).offset(pageable.getOffset());
        List<Signon> signons = where.fetchInto(Signon.class);
        return new PageBuilder<>(signons, pageable, sqlBeforePage, dsl);
    }

    @Cacheable(value="signonCache", key="'petstore:signon:'+#username", unless="#result==null")
    public Signon findByName(String username) {
        return dao.fetchOneByUsername(username);
    }

    @CacheEvict(value="signonCache", key="'petstore:signon:'+#user.username")
    public void update(@Valid Signon user) {
        dao.update(user);
    }
}
