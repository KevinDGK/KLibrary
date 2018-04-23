package com.dgk.demo.okhttp

import okhttp3.*
import okhttp3.RequestBody



/**
 * Created by daigaokai on 2018/4/13.
 */
object OkHttpUtil {

    var client : OkHttpClient = OkHttpClient()

    /**
     * 同步Get请求
     */
    fun getSync(url: String): String {
        // 构建请求对象
        val request = Request.Builder()
                .url(url)
                .get()
                .build()
        // 发送同步请求
        val response = client.newCall(request).execute()
        val body = response.body()?.string() ?: "null"
        // 输出日志
        printLog(request, response, body)
        // 返回String格式请求结果
        return body
    }

    /**
     * 异步Get请求
     */
    fun getAsync(url: String, callback: Callback) {
        // 构建请求对象
        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        // 发送异步请求
        client.newCall(request).enqueue(callback)
    }

    val MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8")

    /**
     * 同步Post请求
     */
    fun postSync(url: String, param: String): String {
        // 构建请求参数体
        val body = RequestBody.create(MEDIA_TYPE_JSON, param)
        // 构建请求对象
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
        // 发送同步请求
        val response = client.newCall(request).execute()
        // 返回String格式请求结果
        return response.body()!!.string()
    }

    /**
     * 异步Post请求
     */
    fun postAsync(url: String, param: String, callback: Callback) {
        // 构建请求参数体
        val body = RequestBody.create(MEDIA_TYPE_JSON, param)
        // 构建请求对象
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
        // 发送同步请求
        client.newCall(request).enqueue(callback)
    }

    /**
     * 打印日志
     */
    private fun printLog(request: Request, response: Response, body: String) {
        // 输出请求和响应
        println("请求：$request\n响应：$response\nBody：$body\n")
    }
}