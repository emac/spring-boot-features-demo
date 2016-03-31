package cn.emac.demo.petstore.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public final class JsonResult {

    private Boolean success = false;

    private Object data;

    @JsonSerialize(using = OptionalSerializer.class)
    private Optional<Integer> errorCode;

    @JsonSerialize(using = OptionalSerializer.class)
    private Optional<String> errorMessage;

    public static JsonResult ok() {
        JsonResult result = new JsonResult();
        result.setSuccess(true);
        return result;
    }

    public static JsonResult ok(Object data) {
        JsonResult result = new JsonResult();
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    public static JsonResult error(ApiError apiError) {
        JsonResult result = new JsonResult();
        result.setErrorCode(Optional.of(apiError.getCode()));
        result.setErrorMessage(Optional.of(apiError.getMessage()));
        return result;
    }
}
