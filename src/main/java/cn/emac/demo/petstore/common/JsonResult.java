package cn.emac.demo.petstore.common;

import cn.emac.demo.petstore.common.exceptions.ClientCallException;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * API调用结果类
 *
 * @author Emac
 * @since 2016-09-27
 */
@Data
public class JsonResult<T> {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 响应结果
     */
    private Optional<T> data = Optional.empty();

    /**
     * 错误码
     */
    private Optional<Integer> errCode = Optional.empty();

    /**
     * 错误消息
     */
    private Optional<String> errMessage = Optional.empty();

    /**
     * 检查传入的调用结果，如果成功并且响应结果不为空，则返回响应结果，否则抛出{@code ApiCallException}。
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> T checkAndExtract(@NotNull JsonResult<T> result) {
        if (result.isSuccess() && result.getData().isPresent()) {
            return result.getData().get();
        }
        throw new ClientCallException(result);
    }

    /**
     * 检查传入的调用结果，如果成功，则返回响应结果，否则抛出{@code ApiCallException}。
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> Optional<T> checkAndExtractOptional(@NotNull JsonResult<T> result) {
        if (result.isSuccess()) {
            return result.getData();
        }
        throw new ClientCallException(result);
    }

    /**
     * 生成服务调用成功响应
     *
     * @param <T>
     * @return
     */
    public static <T> JsonResult<T> ok() {
        JsonResult<T> result = new JsonResult<>();
        result.setSuccess(true);
        return result;
    }

    /**
     * 生成服务调用成功响应
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> JsonResult<T> ok(@NotNull T data) {
        JsonResult<T> result = new JsonResult<>();
        result.setSuccess(true);
        result.setData(Optional.of(data));
        return result;
    }

    /**
     * 生成服务调用错误响应
     *
     * @param <T>
     * @return
     */
    public static <T> JsonResult<T> error() {
        return error(Error.GENERAL_ERROR_CODE, Error.GENERAL_ERROR_MESSAGE);
    }

    /**
     * 生成服务调用错误响应
     *
     * @param errorMessage
     * @param <T>
     * @return
     */
    public static <T> JsonResult<T> error(String errorMessage) {
        return error(Error.GENERAL_ERROR_CODE, errorMessage);
    }

    /**
     * 生成服务调用错误响应
     *
     * @param error
     * @param <T>
     * @return
     */
    public static <T> JsonResult<T> error(Error error) {
        return error(error.getCode(), error.getMessage());
    }

    /**
     * 生成服务调用错误响应
     *
     * @param errorCode
     * @param errorMessage
     * @param <T>
     * @return
     */
    private static <T> JsonResult<T> error(Integer errorCode, String errorMessage) {
        JsonResult<T> result = new JsonResult<>();
        result.setErrCode(Optional.of(errorCode));
        result.setErrMessage(Optional.ofNullable(errorMessage));
        return result;
    }
}
