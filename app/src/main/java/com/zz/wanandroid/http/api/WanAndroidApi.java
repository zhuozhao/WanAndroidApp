package com.zz.wanandroid.http.api;

import com.zz.wanandroid.bean.Article;
import com.zz.wanandroid.bean.Banner;
import com.zz.wanandroid.bean.Hotkey;
import com.zz.wanandroid.bean.PageData;
import com.zz.wanandroid.bean.Result;
import com.zz.wanandroid.bean.Tree;
import com.zz.wanandroid.bean.User;
import com.zz.wanandroid.bean.Website;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author zhaozhuo
 * @date 2018/4/19 11:30
 */
public interface WanAndroidApi {

    /**
     * 获取首页文章列表
     * @param pageNum 页码
     * @return
     */
    @GET("article/list/{pageNum}/json")
    Observable<Result<PageData<Article>>> articleList(@Path("pageNum") int pageNum);


    /**
     * 获取首页banner
     * @return
     */
    @GET("banner/json")
    Observable<Result<List<Banner>>> banner();


    /**
     * 项目分类
     * @return
     */
    @GET("project/tree/json")
    Observable<Result<List<Tree>>> projectTree();

    /**
     * 获取项目列表数据
     * @param pageNum
     * @param cid
     * @return
     */
    @GET("project/list/{pageNum}/json")
    Observable<Result<PageData<Article>>> projectList(@Path("pageNum") int pageNum, @Query("cid") int cid);

    /**
     * 获取体系分类
     * @return
     */
    @GET("tree/json")
    Observable<Result<List<Tree>>> tree();


    /**
     * 获取体系下文章
     * @param pageNum
     * @param cid
     * @return
     */
    @GET("article/list/{pageNum}/json")
    Observable<Result<PageData<Article>>> articleListTree(@Path("pageNum") int pageNum, @Query("cid") int cid);


    /**
     * 热搜关键字
     * @return
     */
    @GET("hotkey/json")
    Observable<Result<List<Hotkey>>> hotkey();


    /**
     *
     * 搜索文章
     * @param pageNum
     * @param key
     * @return
     */
    @POST("article/query/{pageNum}/json")
    @FormUrlEncoded
    Observable<Result<PageData<Article>>> articleQuery(@Path("pageNum") int pageNum, @Field("k") String key);


    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @POST("user/login")
    @FormUrlEncoded
    Observable<Result<User>> login(@Field("username") String username,@Field("password") String password);

    /**
     * 注册
     * @param username
     * @param password
     * @param repassword
     * @return
     */
    @POST("user/register")
    @FormUrlEncoded
    Observable<Result<User>> register(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);


    /**
     * 6.2 收藏站内文章
     http://www.wanandroid.com/lg/collect/1165/json

     方法：POST
     参数： 文章id，拼接在链接中。
     注意链接中的数字，为需要收藏的id.
     */
    @POST("lg/collect/{id}/json")
    Observable<Result>  collect(@Path("id") int id);


    /**
     * 6.1 收藏文章列表
     http://www.wanandroid.com/lg/collect/list/0/json

     方法：GET
     参数： 页码：拼接在链接中，从0开始。
     * @return
     */
    @GET("lg/collect/list/{pageNum}/json")
    Observable<Result<PageData<Article>>> collectList(@Path("pageNum") int pageNum);


    /**
     * 6.4.2 我的收藏页面（该页面包含自己录入的内容）
     http://www.wanandroid.com/lg/uncollect/2805/json

     方法：POST
     参数：
     id:拼接在链接上
     originId:列表页下发，无则为-1
     如下图：id=2766，originId=2324
     * @return
     */
    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    Observable<Result> unCollect(@Path("id") int id,@Field("originId") int originId);


    /**
     * 6.4.1 文章列表
     http://www.wanandroid.com/lg/uncollect_originId/2333/json

     方法：POST
     参数：
     id:拼接在链接上
     id传入的是列表中文章的id。
     */
    @POST("lg/uncollect_originId/{id}/json")
    Observable<Result> unCollectOriginId(@Path("id") int id);


    /**
     * 1.3 常用网站
     http://www.wanandroid.com/friend/json

     方法：GET
     参数：无
     可直接点击查看示例：http://www.wanandroid.com/friend/json
     */

    @GET("friend/json")
    Observable<Result<List<Website>>> friend();

}
