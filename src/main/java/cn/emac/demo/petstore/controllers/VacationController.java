package cn.emac.demo.petstore.controllers;

import cn.emac.demo.petstore.common.JsonResult;
import cn.emac.demo.petstore.domain.vacation.VacationApproval;
import cn.emac.demo.petstore.domain.vacation.VacationRequest;
import com.google.common.collect.Lists;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.DayOfWeek;
import java.util.List;

/**
 * @author Emac
 * @since 2017-04-22
 */
@RestController
@Validated
public class VacationController implements IController {

    private static final List<DayOfWeek> WEEKENDS = Lists.newArrayList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    /**
     * 转换GET请求参数
     */
    @RequestMapping(value = "/isWeekend", method = RequestMethod.GET)
    public JsonResult<Boolean> isWeekend(@Valid VacationRequest request) {
        return JsonResult.ok(WEEKENDS.contains(request.getStart().getDayOfWeek()));
    }

    /**
     * 转换POST请求体
     */
    @RequestMapping(value = "/approve", method = RequestMethod.POST)
    public JsonResult<VacationApproval> vacate(@RequestBody @Valid VacationRequest request) {
        return JsonResult.ok(VacationApproval.approve(request));
    }

    /**
     * 转换POST请求参数
     */
    @RequestMapping(value = "/deny", method = RequestMethod.POST)
    public JsonResult<VacationApproval> deny(@Valid VacationRequest request) {
        return JsonResult.ok(VacationApproval.deny(request));
    }
}