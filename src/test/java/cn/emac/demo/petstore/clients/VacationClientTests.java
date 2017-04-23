package cn.emac.demo.petstore.clients;

import cn.emac.demo.petstore.common.JsonResult;
import cn.emac.demo.petstore.domain.vacation.VacationApproval;
import cn.emac.demo.petstore.domain.vacation.VacationRequest;
import cn.emac.demo.petstore.domain.vacation.VacationType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;
import java.util.concurrent.ExecutionException;

/**
 * @author Emac
 * @since 2017-04-23
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class VacationClientTests {

    @Autowired
    private VacationClient vacationClient;

    @Test
    public void testAll() throws ExecutionException, InterruptedException {
        VacationRequest request = new VacationRequest();
        request.setType(VacationType.PERSONAL);
        OffsetDateTime now = OffsetDateTime.now();
        request.setStart(now);
        request.setEnd(now.plusDays(1));

        JsonResult<Boolean> result1 = vacationClient.isWeekend(request);
        Assert.assertTrue(result1.isSuccess());
        Assert.assertTrue(JsonResult.checkAndExtract(result1));

        JsonResult<VacationApproval> result2 = vacationClient.approve(request);
        Assert.assertTrue(result2.isSuccess());
        Assert.assertTrue(JsonResult.checkAndExtract(result2).getApproved());

        vacationClient.asyncDeny(request).whenComplete((result, exception) -> {
            Assert.assertTrue(result != null && exception == null);
            Assert.assertFalse(JsonResult.checkAndExtract(result).getApproved());
        }).get();
    }
}
