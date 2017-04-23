package cn.emac.demo.petstore.common.hystrix;

import cn.emac.demo.petstore.common.JsonResult;
import cn.emac.demo.petstore.common.exceptions.ClientException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit2.Response;

import java.io.IOException;

/**
 * @author Emac
 * @since 2016-09-27
 */
public abstract class BaseJsonHystrixCommand<R> extends BaseHystrixCommand<JsonResult<R>> {

    private ObjectMapper errorBodyMapper;

    /**
     * @param commandConfig
     * @param threadConfig
     * @param errorBodyMapper
     */
    public BaseJsonHystrixCommand(HystrixCommandConfig commandConfig, HystrixThreadConfig threadConfig, ObjectMapper errorBodyMapper) {
        super(commandConfig, threadConfig);

        this.errorBodyMapper = errorBodyMapper;
    }

    @Override
    protected JsonResult<R> run() throws IOException {
        Response<JsonResult<R>> response = doApiCall();
        if (response.isSuccessful()) {
            return response.body();
        }

        // 客户端异常
        String errJson = response.errorBody().string();
        TypeReference<JsonResult<R>> typeReference = new TypeReference<JsonResult<R>>() {
        };
        JsonResult<R> r = errorBodyMapper.readValue(errJson, typeReference);

        // 服务端异常
        if (response.code() >= 500) {
            throw new ClientException("Server responds with " + response.code() + ": " +
                    r.getErrCode().orElse(0) + ", " + r.getErrMessage().orElse(""));
        } else {
            return r;
        }
    }
}
