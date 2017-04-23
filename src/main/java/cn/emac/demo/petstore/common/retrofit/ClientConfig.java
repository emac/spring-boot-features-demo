package cn.emac.demo.petstore.common.retrofit;

import cn.emac.demo.petstore.common.retrofit.interceptors.HttpRequestLogInterceptor;
import cn.emac.demo.petstore.common.retrofit.interceptors.HttpResponseLogInterceptor;
import cn.emac.demo.petstore.common.utils.Asserts;
import com.google.common.collect.Lists;
import lombok.Getter;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Client接入配置，默认添加{@code HttpRequestLogInterceptor}和{@code HttpResponseLogInterceptor}。
 *
 * @author Emac
 * @since 2016-09-27
 */
@Getter
public abstract class ClientConfig {

    /**
     * 基础请求URL（以/结尾）
     */
    @NotNull
    private String baseUrl;

    /**
     * Application请求拦截器, 注意顺序和OkHttp是反的
     *
     * @see <a href="https://github.com/square/okhttp/wiki/Interceptors">https://github.com/square/okhttp/wiki/Interceptors</a>
     */
    private List<Interceptor> applicationInterceptors = Lists.newArrayList(new HttpResponseLogInterceptor());

    /**
     * Network请求拦截器, 注意顺序和OkHttp是反的
     *
     * @see <a href="https://github.com/square/okhttp/wiki/Interceptors">https://github.com/square/okhttp/wiki/Interceptors</a>
     */
    private List<Interceptor> networkInterceptors = Lists.newArrayList(new HttpRequestLogInterceptor());

    /**
     * OkHttpClient配置，优先于applicationInterceptors和networkInterceptors
     */
    private OkHttpClient.Builder okHttpClientBuilder;

    public ClientConfig(String baseUrl) {
        Asserts.notBlank(baseUrl);
        this.baseUrl = _normalize(baseUrl);
    }

    public ClientConfig(String baseUrl, OkHttpClient.Builder okHttpClientBuilder) {
        this(baseUrl);

        Asserts.notNull(okHttpClientBuilder);
        this.okHttpClientBuilder = okHttpClientBuilder;
    }

    /**
     * 用于序列化的枚举属性名称, SDK实现中覆写
     */
    abstract public String getEnumPropName();

    /**
     * 新增一个OkHttp应用拦截器, 新增的Intercept会添加再原有Intercept外层
     */
    public void addApplicationInterceptor(Interceptor interceptor) {
        applicationInterceptors.add(interceptor);
    }

    /**
     * 新增一个OkHttp网络拦截器, 新增的Intercept会添加再原有Intercept外层
     */
    public void addNetworkInterceptor(Interceptor interceptor) {
        this.networkInterceptors.add(interceptor);
    }

    /**
     * 自检
     */
    public boolean isValid() {
        return isNotBlank(baseUrl);
    }

    /**
     * 先去除首尾空白字符，然后添加末尾/（如果没有）
     */
    private String _normalize(String url) {
        String trimmed = url.trim();
        return trimmed.endsWith("/") ? trimmed : trimmed + "/";
    }
}
