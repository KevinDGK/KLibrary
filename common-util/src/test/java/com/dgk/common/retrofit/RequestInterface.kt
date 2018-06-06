package com.dgk.common.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RequestInterface {

    /**
     * 登陆接口
     * - 入参使用 @Query(k) 表示，即本接口会被拼接成   http://localhost:8080/klibrary/login?name=Kevin&password=123456
     * - 出参为Call<T>，使用Call发起网络请求后，响应就是Response<T>，它的body就是<T>, 本接口响应的body类型为okHttp的ResponseBody，即原始响应
     */
    @GET("login")
    fun login01(@Query("name") name: String, @Query("password") password: String): Call<ResponseBody>

    /**
     * 登陆接口
     * - 入参使用 @QueryMap(map<k,v>) 表示，将map中的k,v取出，拼接成http://localhost:8080/klibrary/login?k1=v1&k2=v2...
     * - 出参为Call<T>，, 即本接口响应的body类型为自定义的StandardResponse，服务端返回的响应的body为Json字符串，然后通过converter转换成StandardResponse
     */
    @GET("login")
    fun login02(@QueryMap params: Map<String, String>): Call<StandardResponse>

    /**
     * 注册接口
     * - 入参为 @Field("k") v，表示表单中的字段
     * - 出参为 Call<T>
     */
    @POST("register")
    @FormUrlEncoded
    fun register01(@Field("name") name: String, @Field("password") password: String): Call<StandardResponse>

    /**
     * 注册接口
     * - 入参为 @FieldMap map<k,v>，会将map的每个参数都作为表单的一项
     * - 出参为 Call<T>
     */
    @POST("register")
    @FormUrlEncoded
    fun register02(@FieldMap params: Map<String, String>): Call<StandardResponse>


    /**
     * 上传文件
     * - 入参为 @Part(k)v
     */
    @POST("uploadFile")
    @Multipart
    fun uploadFile(@Part("en") en: String, @Part("mcode") mcode: String, @Part file: MultipartBody.Part): Call<StandardResponse>
}