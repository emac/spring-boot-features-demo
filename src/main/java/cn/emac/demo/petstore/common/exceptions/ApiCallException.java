package cn.emac.demo.petstore.common.exceptions;

import cn.emac.demo.petstore.common.JsonResult;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * API调用异常类,描述调用API相关的异常
 *
 * @author Emac
 * @since 2016-10-13
 */
@Getter
public class ApiCallException extends CommonException {

    /**
     * API调用结果
     */
    private JsonResult result;

    /**
     * @param result
     */
    public ApiCallException(@NotNull JsonResult result) {
        super(StringUtils.join(result.getErrCode().orElse(0), result.getErrMessage().orElse("服务异常")));

        this.result = result;
    }
}
