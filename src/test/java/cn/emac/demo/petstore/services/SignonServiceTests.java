package cn.emac.demo.petstore.services;

import cn.emac.demo.petstore.BaseMockTests;
import cn.emac.demo.petstore.domain.tables.daos.SignonDao;
import cn.emac.demo.petstore.domain.tables.pojos.Signon;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Emac
 * @since 2017-04-30
 */
public class SignonServiceTests extends BaseMockTests {

    @Autowired
    private SignonService signonService;

    @MockBean
    private SignonDao dao;

    @Test
    public void testFindAll() {
        List<Signon> signons = signonService.findAll();
        assertTrue(CollectionUtils.isEmpty(signons));

        Signon signon = new Signon();
        signon.setUsername("foo");
        when(dao.findAll()).thenReturn(Lists.newArrayList(signon));

        signons = signonService.findAll();
        assertEquals(1, signons.size());
        assertEquals("foo", signons.get(0).getUsername());
    }
}
