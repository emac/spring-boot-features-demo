package cn.emac.demo.petstore.clients;

import cn.emac.demo.petstore.BaseTests;
import cn.emac.demo.petstore.common.JsonResult;
import cn.emac.demo.petstore.domain.vacation.VacationApproval;
import cn.emac.demo.petstore.domain.vacation.VacationRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.concurrent.ExecutionException;

import static cn.emac.demo.petstore.domain.vacation.VacationType.PERSONAL;
import static java.time.DayOfWeek.SUNDAY;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

/**
 * @author Emac
 * @since 2017-04-23
 */
// 使用配置文件里的服务端口，保证vacation.host有效
@SpringBootTest(webEnvironment = DEFINED_PORT)
public class VacationClientTests extends BaseTests {

    @Autowired
    private VacationClient vacationClient;

    @Test
    public void testAll() throws ExecutionException, InterruptedException {
        VacationRequest request = new VacationRequest();
        request.setType(PERSONAL);
        OffsetDateTime lastSunday = OffsetDateTime.now().with(TemporalAdjusters.previous(SUNDAY));
        request.setStart(lastSunday);
        request.setEnd(lastSunday.plusDays(1));

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
