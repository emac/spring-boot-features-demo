package cn.emac.demo.petstore.clients;

import cn.emac.demo.petstore.common.JsonResult;
import cn.emac.demo.petstore.domain.vacation.VacationApproval;
import cn.emac.demo.petstore.domain.vacation.VacationRequest;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * @author Emac
 * @since 2017-04-23
 */
public interface VacationApi {

    @GET("isWeekend")
    Call<JsonResult<Boolean>> isWeekend(@QueryMap Map<String, String> query);

    @POST("approve")
    Call<JsonResult<VacationApproval>> approve(@Body VacationRequest request);

    @POST("deny")
    @FormUrlEncoded
    Call<JsonResult<VacationApproval>> deny(@FieldMap Map<String, String> form);
}
