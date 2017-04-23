package cn.emac.demo.petstore.clients;

import cn.emac.demo.petstore.common.JsonResult;
import cn.emac.demo.petstore.common.hystrix.BaseJsonHystrixCommand;
import cn.emac.demo.petstore.common.hystrix.HystrixCommandConfig;
import cn.emac.demo.petstore.common.hystrix.HystrixThreadConfig;
import cn.emac.demo.petstore.common.retrofit.BaseClient;
import cn.emac.demo.petstore.common.retrofit.ObjectMapperFactory;
import cn.emac.demo.petstore.common.retrofit.RetrofitFactory;
import cn.emac.demo.petstore.domain.vacation.VacationApproval;
import cn.emac.demo.petstore.domain.vacation.VacationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit2.Response;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static cn.emac.demo.petstore.PetstoreConstants.ENUM_PROP_NAME;

/**
 * @author Emac
 * @since 2017-04-23
 */
public class VacationClient extends BaseClient<VacationApi> {

    private static ObjectMapper ERROR_BODY_MAPPER = ObjectMapperFactory.getObjectMapper(ENUM_PROP_NAME);

    public VacationClient(VacationClientConfig config) {
        super(RetrofitFactory.get(config).create(VacationApi.class), new HystrixCommandConfig("petstore", "vacation"), new HystrixThreadConfig("vacation-thread"));
    }

    public JsonResult<Boolean> isWeekend(VacationRequest request) {
        return executeCommand(new IsWeekendCommand(api, request));
    }

    public CompletableFuture<JsonResult<Boolean>> asyncIsWeekend(VacationRequest request) {
        return asyncExecuteCommand(new IsWeekendCommand(api, request));
    }

    public JsonResult<VacationApproval> approve(VacationRequest request) {
        return executeCommand(new ApproveCommand(api, request));
    }

    public CompletableFuture<JsonResult<VacationApproval>> asyncApprove(VacationRequest request) {
        return asyncExecuteCommand(new ApproveCommand(api, request));
    }

    public JsonResult<VacationApproval> deny(VacationRequest request) {
        return executeCommand(new DenyCommand(api, request));
    }

    public CompletableFuture<JsonResult<VacationApproval>> asyncDeny(VacationRequest request) {
        return asyncExecuteCommand(new DenyCommand(api, request));
    }

/*======================================= Hystrix Command =======================================*/

    private abstract class VacationCommand<R> extends BaseJsonHystrixCommand<R> {
        protected final VacationApi api;

        public VacationCommand(VacationApi api) {
            super(commandConfig, threadConfig, ERROR_BODY_MAPPER);
            this.api = api;
        }
    }

    private class IsWeekendCommand extends VacationCommand<Boolean> {
        private final VacationRequest request;

        public IsWeekendCommand(VacationApi api, VacationRequest request) {
            super(api);
            this.request = request;
        }

        @Override
        protected Response<JsonResult<Boolean>> doApiCall() throws IOException {
            return this.api.isWeekend(request.toMap()).execute();
        }
    }

    private class ApproveCommand extends VacationCommand<VacationApproval> {
        private final VacationRequest request;

        public ApproveCommand(VacationApi api, VacationRequest request) {
            super(api);
            this.request = request;
        }

        @Override
        protected Response<JsonResult<VacationApproval>> doApiCall() throws IOException {
            return this.api.approve(request).execute();
        }
    }

    private class DenyCommand extends VacationCommand<VacationApproval> {
        private final VacationRequest request;

        public DenyCommand(VacationApi api, VacationRequest request) {
            super(api);
            this.request = request;
        }

        @Override
        protected Response<JsonResult<VacationApproval>> doApiCall() throws IOException {
            return this.api.deny(request.toMap()).execute();
        }
    }
}
