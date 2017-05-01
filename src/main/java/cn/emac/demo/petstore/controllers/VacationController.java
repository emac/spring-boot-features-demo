package cn.emac.demo.petstore.controllers;

import cn.emac.demo.petstore.clients.VacationClient;
import cn.emac.demo.petstore.common.JsonResult;
import cn.emac.demo.petstore.domain.vacation.VacationApproval;
import cn.emac.demo.petstore.domain.vacation.VacationRequest;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.DayOfWeek;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @author Emac
 * @since 2017-04-22
 */
@RestController
@RequestMapping(produces = APPLICATION_JSON_UTF8_VALUE)
@Validated
public class VacationController implements IController {

    private static final List<DayOfWeek> WEEKENDS = Lists.newArrayList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    @Autowired
    private VacationClient vacationClient;

    /**
     * 转换GET请求参数
     */
    @RequestMapping(value = "/api/vacation/isWeekend", method = RequestMethod.GET)
    public JsonResult<Boolean> isWeekend(@Valid VacationRequest request) {
        return JsonResult.ok(WEEKENDS.contains(request.getStart().getDayOfWeek()));
    }

    /**
     * 转换POST请求体
     */
    @RequestMapping(value = "/api/vacation/approve", method = RequestMethod.POST, consumes = APPLICATION_JSON_UTF8_VALUE)
    public JsonResult<VacationApproval> vacate(@RequestBody @Valid VacationRequest request) {
        return JsonResult.ok(VacationApproval.approve(request));
    }

    /**
     * 转换POST请求参数
     */
    @RequestMapping(value = "/api/vacation/deny", method = RequestMethod.POST)
    public JsonResult<VacationApproval> deny(@Valid VacationRequest request) {
        return JsonResult.ok(VacationApproval.deny(request));
    }

    /*======================================= Proxy via Client =======================================*/

    @RequestMapping(value = "/vacation/isWeekend", method = RequestMethod.GET)
    public JsonResult<Boolean> isWeekendProxy(@Valid VacationRequest request) {
        return vacationClient.isWeekend(request);
    }
}