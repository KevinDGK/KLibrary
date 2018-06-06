package com.dgk.common.retrofit

import com.dgk.common.CONFIG_SERVER_URL
import com.dgk.common.retrofit.converter.FastJsonConverterFactory
import okhttp3.OkHttpClient
import org.junit.Test
import retrofit2.Retrofit
import com.lzy.okgo.interceptor.HttpLoggingInterceptor



class RetrofitUnitTest {

    fun createRequestInterface():RequestInterface {
        return Retrofit.Builder()
                .client(OkHttpClient().newBuilder().build())
                .baseUrl(CONFIG_SERVER_URL)
                .addConverterFactory(FastJsonConverterFactory.create())
                .build().create(RequestInterface::class.java)
    }

    @Test
    fun testLogin01() {

//        val logger = HttpLoggingInterceptor("klibrary")
//        logger.setPrintLevel(HttpLoggingInterceptor.Level.BODY)


        // 1.创建Retrofit实例
        val retrofit = Retrofit.Builder()
                .client(OkHttpClient().newBuilder().build())
                .baseUrl(CONFIG_SERVER_URL)
                .addConverterFactory(FastJsonConverterFactory.create())
                .build()

        // 2.创建Retrofit请求接口实例
        val requestInterface = retrofit.create(RequestInterface::class.java)

        // 3.调用相应接口API，获取Call对象
        val call = requestInterface.login01("Kevin0", "123456")

        // 4.发送网络请求同步
        val response = call.execute()

        println(call.request())

        // 响应类型是 ResponseBody！！！
        println(response)

        println("body: ${response.body()?.string()}")
    }

    @Test
    fun testLogin02() {

        // 1.创建Retrofit实例
        val retrofit = Retrofit.Builder()
                .client(OkHttpClient().newBuilder().build())
                .baseUrl(CONFIG_SERVER_URL)
                .addConverterFactory(FastJsonConverterFactory.create())
                .build()

        // 2.创建Retrofit请求接口实例
        val requestInterface = retrofit.create(RequestInterface::class.java)

        val params = HashMap<String, String>()
        params["name"] = "Kevin0"
        params["password"] = "123456"
        // 3.调用相应接口API，获取Call对象
        val call = requestInterface.login02(params)

        // 4.发送网络请求同步
        val response = call.execute()

        println(call.request())

        // 响应类型是 StandardResponse！！！
        println(response)

        println("body: ${response.body().toString()}")
    }

    @Test
    fun testRegister01() {

        // 1.创建Retrofit实例
        val retrofit = Retrofit.Builder()
                .client(OkHttpClient().newBuilder().build())
                .baseUrl(CONFIG_SERVER_URL)
                .addConverterFactory(FastJsonConverterFactory.create())
                .build()

        // 2.创建Retrofit请求接口实例
        val requestInterface = retrofit.create(RequestInterface::class.java)

        // 3.调用相应接口API，获取Call对象
        val call = requestInterface.register01("Kevin1", "654321")

        // 4.发送网络请求(同步或者异步)
        val response = call.execute()

        println(call.request())

        println(response)

        println("body: ${response.body().toString()}")
    }

    @Test
    fun testRegister02() {

        // 1.创建Retrofit实例
        val retrofit = Retrofit.Builder()
                .client(OkHttpClient().newBuilder().build())
                .baseUrl(CONFIG_SERVER_URL)
                .addConverterFactory(FastJsonConverterFactory.create())
                .build()

        // 2.创建Retrofit请求接口实例
        val requestInterface = retrofit.create(RequestInterface::class.java)

        val params = HashMap<String, String>()
        params["name"] = "Kevin1"
        params["password"] = "654321"

        // 3.调用相应接口API，获取Call对象
        val call = requestInterface.register02(params)

        // 4.发送网络请求(同步或者异步)
        val response = call.execute()

        println(call.request())

        println(response)

        println("body: ${response.body().toString()}")
    }

    @Test
    fun testUploadFile01() {

        // 1.创建Retrofit实例
        val retrofit = Retrofit.Builder()
                .client(OkHttpClient().newBuilder().build())
                .baseUrl(CONFIG_SERVER_URL)
                .addConverterFactory(FastJsonConverterFactory.create())
                .build()

        // 2.创建Retrofit请求接口实例
        val requestInterface = retrofit.create(RequestInterface::class.java)

        val params = HashMap<String, String>()
        params["name"] = "Kevin1"
        params["password"] = "654321"

        // 3.调用相应接口API，获取Call对象
        val call = requestInterface.register02(params)

        // 4.发送网络请求(同步或者异步)
        val response = call.execute()

        println(call.request())

        println(response)

        println("body: ${response.body().toString()}")
    }
}