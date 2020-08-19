package cn.kuroneko.demos.commons.utils;

import cn.kuroneko.demos.commons.properties.HttpClientProperty;
import cn.kuroneko.demos.commons.selector.KuronekoHttpProxySelector;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-22 16:35
 **/
@Slf4j
public class HttpUtils {
    public static RestTemplate generateConfiguredRestTemplateWith(RestTemplateBuilder restTemplateBuilder, HttpClientProperty httpClientProperty, ExecutorService executorService) {
        RestTemplate template = restTemplateBuilder
                .setReadTimeout(Duration.ofSeconds(httpClientProperty.getReadTimeout()))
                .setConnectTimeout(Duration.ofSeconds(httpClientProperty.getConnectionTimeout()))
                .requestFactory(() -> {
                    Dispatcher dispatcher = new Dispatcher(executorService);
                    dispatcher.setMaxRequests(httpClientProperty.getMaxRequests());
                    dispatcher.setMaxRequestsPerHost(httpClientProperty.getMaxRequestsPerHost());
                    Builder builder = (new Builder()).dispatcher(dispatcher);
                    if (httpClientProperty.isEnableProxy()) {
                        InetSocketAddress address = new InetSocketAddress(httpClientProperty.getProxyHost(), httpClientProperty.getProxyPort());
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
                        builder.proxySelector(new KuronekoHttpProxySelector(Arrays.asList(new Proxy[]{proxy}), httpClientProperty.getNonProxyHosts()));
                    }

                    OkHttpClient okHttpClient = builder.build();
                    return new OkHttp3ClientHttpRequestFactory(okHttpClient);
        }).build();
        return template;
    }

    public static OkHttpClient okHttpClient(HttpClientProperty httpClientProperty){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS);

        if (httpClientProperty.isEnableProxy()) {
            List<String> nonProxyHosts =  httpClientProperty.getNonProxyHosts();
            InetSocketAddress address = new InetSocketAddress(httpClientProperty.getProxyHost(),
                    httpClientProperty.getProxyPort());
            Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
            builder.proxySelector(new KuronekoHttpProxySelector(Arrays.asList(proxy),
                    nonProxyHosts));
        }
        return builder.build();
    }

    /**
     * 发送get请求
     *
     * @param url
     * @return
     */
    public static String httpGetWithJson(String url,String json) throws ExecutionException, InterruptedException {
        log.info("httpGetWithJson url : {} , json : {}",url,json);
        ListenableFuture<Response> future = Dsl.asyncHttpClient()
                .prepareGet(url)
                .setBody(json)
                .addHeader("Content-Type", "application/json")
                .addHeader("charset", "utf-8")
                .execute();
        org.asynchttpclient.Response response = future.get();
        String responseBody = response.getResponseBody();
        log.info("httpGetWithJson back : {}",responseBody);
        return responseBody;
    }
}
