package cn.emac.demo.petstore.common.exceptions;

import cn.emac.demo.petstore.common.JsonResult;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * Client调用异常类
 *
 * @author Emac
 * @since 2016-10-13
 */
@Getter
public class ClientCallException extends CommonException {

    /**
     * API调用结果
     */
    private JsonResult result;

    /**
     * @param result
     */
    public ClientCallException(@NotNull JsonResult result) {
        super(StringUtils.join(result.getErrCode().orElse(0), result.getErrMessage().orElse("服务异常")));

        this.result = result;
    }
}
