package cn.emac.demo.petstore.common.retrofit;

import cn.emac.demo.petstore.common.utils.Asserts;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Retrofit工厂, 根据ClientConfig缓存Retrofit对象。
 *
 * @author Emac
 * @since 2016-09-27
 */
public class RetrofitFactory {

    private static Map<ClientConfig, Retrofit> retrofits = Maps.newHashMap();

    /**
     * @param config
     * @return
     */
    public static Retrofit get(@NotNull ClientConfig config) {
        Asserts.notNull(config);
        Asserts.isTrue(config.isValid());

        Retrofit retrofit = retrofits.get(config);
        if (retrofit == null) {
            synchronized (RetrofitFactory.class) {
                // Double check, but it's safe with Map
                retrofit = retrofits.get(config);
                if (retrofit == null) {
                    retrofit = _create(config);
                    retrofits.put(config, retrofit);
                }
            }
        }
        return retrofit;
    }

    private static Retrofit _create(ClientConfig config) {
        // 构建OkHttpClient
        OkHttpClient client = _createOkHttpClient(config);

        // 构建ObjectMapper
        ObjectMapper mapper = ObjectMapperFactory.getObjectMapper(config.getEnumPropName());

        Retrofit retrofit = new Retrofit.Builder().
                client(client).
                addConverterFactory(JacksonConverterFactory.create(mapper)).
                addCallAdapterFactory(Java8CallAdapterFactory.create()).
                baseUrl(config.getBaseUrl()).
                build();

        return retrofit;
    }

    private static OkHttpClient _createOkHttpClient(ClientConfig config) {
        // 优先使用config里面的okHttpBuilder构建OkHttpClient
        if (config.getOkHttpClientBuilder() != null) {
            return config.getOkHttpClientBuilder().build();
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        List<Interceptor> applicationInterceptors = config.getApplicationInterceptors();
        Collections.reverse(applicationInterceptors);
        applicationInterceptors.forEach(builder::addInterceptor);
        List<Interceptor> networkInterceptors = config.getNetworkInterceptors();
        Collections.reverse(networkInterceptors);
        networkInterceptors.forEach(builder::addNetworkInterceptor);

        return builder.build();
    }
}
