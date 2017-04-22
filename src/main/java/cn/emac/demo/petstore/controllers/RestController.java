package cn.emac.demo.petstore.controllers;

import cn.emac.demo.petstore.common.JsonResult;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author Emac
 * @since 2017-04-22
 */
@org.springframework.web.bind.annotation.RestController
@Validated
public class RestController implements IController {

    private static final List<DayOfWeek> WEEKENDS = Lists.newArrayList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    @RequestMapping(value = "/isWeekend", method = RequestMethod.GET)
    public JsonResult<Boolean> isWeekend(@Valid VacationRequest request) {
        return JsonResult.ok(WEEKENDS.contains(request.getStart().getDayOfWeek()));
    }

    @RequestMapping(value = "/approve", method = RequestMethod.POST)
    public VacationApproval vacate(@RequestBody @Valid VacationRequest request) {
        return VacationApproval.approve(request);
    }

    @RequestMapping(value = "/deny", method = RequestMethod.POST)
    public VacationRequest deny(@Valid VacationRequest request) {
        return VacationApproval.deny(request);
    }
}

@AllArgsConstructor
@Getter
enum VacationType {
    PERSONAL(0), SICK(1);

    private Integer value;
}

@Data
class VacationRequest {
    @NotNull
    private VacationType type;

    private String reason;

    @NotNull
    private OffsetDateTime start;

    @NotNull
    private OffsetDateTime end;
}

@Data
class VacationApproval extends VacationRequest {
    private Boolean approved;

    public static VacationApproval approve(VacationRequest request) {
        VacationApproval approval = of(request);
        approval.setApproved(true);
        return approval;
    }

    public static VacationApproval deny(VacationRequest request) {
        VacationApproval approval = of(request);
        approval.setApproved(false);
        return approval;
    }

    private static VacationApproval of(VacationRequest request) {
        VacationApproval approval = new VacationApproval();
        BeanUtils.copyProperties(request, approval);
        return approval;
    }
}