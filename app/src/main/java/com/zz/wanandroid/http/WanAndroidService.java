package com.zz.wanandroid.http;



import com.zz.wanandroid.http.api.WanAndroidApi;
import com.zz.wanandroid.http.cookie.CookiesManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author zhaozhuo
 * @date 2018/4/19 11:42
 */
public class WanAndroidService {

    private static final long DEFAULT_TIMEOUT = 30;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    private static WanAndroidApi wanAndroidApi;


    private static Retrofit iniOkHttpClient() {

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = okHttpClient.newBuilder()
                .addInterceptor(logging)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                /*.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .header("User-Agent", "(android; zh-cn) okhttp")
                                .build();
                        return chain.proceed(request);
                    }
                })*/
                .cookieJar(new CookiesManager())
                .build();

        String BASE_URL = "http://www.wanandroid.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();

        return retrofit;

    }


    /**
     * 获取api
     * @return
     */
    public static WanAndroidApi getWanAndroidApi() {

        if(wanAndroidApi==null){

            wanAndroidApi = iniOkHttpClient().create(WanAndroidApi.class);
        }
        return wanAndroidApi;
    }

}
