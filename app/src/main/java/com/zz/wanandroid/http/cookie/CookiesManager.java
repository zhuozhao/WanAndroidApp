package com.zz.wanandroid.http.cookie;

import com.zz.wanandroid.app.MyApplication;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * @author zhaozhuo
 * @date 2018/7/27 9:03
 */
public class CookiesManager implements CookieJar {

    private final PersistentCookieStore cookieStore = new PersistentCookieStore(MyApplication.getINSTANCE().getApplicationContext());

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }

}
