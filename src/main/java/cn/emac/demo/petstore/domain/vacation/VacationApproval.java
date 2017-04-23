package cn.emac.demo.petstore.domain.vacation;

import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author Emac
 * @since 2017-04-23
 */
@Data
public class VacationApproval extends VacationRequest {

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
