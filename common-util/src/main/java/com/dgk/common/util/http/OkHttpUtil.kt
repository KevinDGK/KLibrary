package com.dgk.common.util.http

import com.dgk.common.util.log.KLogi
import okhttp3.*
import okhttp3.RequestBody
import java.io.File


/**
 * OKHttp工具类
 * 版本：OKHttp 3
 * Created by daigaokai on 2018/4/13.
 */
object OkHttpUtil {

    private lateinit var client: OkHttpClient

    /**
     * 初始化
     */
    fun init(client: OkHttpClient) {
        this.client = client
    }

    /**
     * Get请求(同步)
     */
    fun getSync(serverUrl: String = "", params: Map<String, String>?= null): String {
        val request = buildGetRequest(serverUrl, params)
        // 发送同步请求
        val response = client.newCall(request).execute()
        val responseBody = response.body()?.string() ?: "null"
        printResponse(response, responseBody)
        // 返回String格式请求结果
        return responseBody
    }

    /**
     * Get请求(异步)
     */
    fun getAsync(serverUrl: String, params: Map<String, String>? = null, callback: Callback) {
        val request = buildGetRequest(serverUrl, params)
        // 发送异步请求
        client.newCall(request).enqueue(callback)
    }

    /**
     * 构建Get请求的Request
     */
    private fun buildGetRequest(serverUrl: String, params: Map<String, String>?): Request {
        // 拼接GET请求的URL
        val url = StringBuilder()
        url.append(serverUrl)
        if (params != null && params.isNotEmpty()) {
            url.append("?")
            params.forEach { k, v ->
                url.append("$k=$v&")
            }
            url.removeSuffix("&")
        }
        // 构建请求对象
        val request = Request.Builder()
                .url(url.toString())
                .get()
                .build()
        printRequest(request, params)
        return request
    }

    /**
     * Post请求(同步)
     */
    fun postSync(serverUrl: String, params: Map<String, String>?= null): String {
        val request = buildPostRequest(params, serverUrl)
        // 发送同步请求
        val response = client.newCall(request).execute()
        val responseBody = response.body()?.string() ?: "null"
        printResponse(response, responseBody)
        // 返回String格式请求结果
        return responseBody
    }

    /**
     * Post请求(异步)
     */
    fun postAsync(serverUrl: String, params: Map<String, String>?= null, callback: Callback) {
        val request = buildPostRequest(params, serverUrl)
        // 发送异步请求
        client.newCall(request).enqueue(callback)
    }

    /**
     * 构建POST请求的Request
     */
    private fun buildPostRequest(params: Map<String, String>?, serverUrl: String): Request {
        // 构建表单请求参数体
        val builder = FormBody.Builder()
        // 添加参数，参数为非必需的
        params?.forEach { k, v -> builder.add(k, v) }
        // 如果在低版本，用上面的forEach抛出异常，可以改为下面的代码，暂时还没找到原因
//        if (params != null && params.isNotEmpty()) {
//            for (key in params.keys) {
//                val value = params[key]
//                if (!value.isNullOrEmpty()) {
//                    builder.add(key, value)
//                }
//            }
//        }

        // 构建请求对象
        val request = Request.Builder()
                .url(serverUrl)
                .post(builder.build())
                .build()
        printRequest(request, params)
        return request
    }

    /**
     * 上传单个文件(同步)
     */
    fun uploadFileSync(serverUrl: String, file: File, params: Map<String, String>?= null): String {
        val request = buildPostFileRequest(file, params, serverUrl)
        // 发送同步请求
        val response = client.newCall(request).execute()
        val responseBody = response.body()?.string() ?: "null"
        printResponse(response, responseBody)
        // 返回String格式请求结果
        return responseBody
    }

    /**
     * 上传单个文件(异步)
     */
    fun uploadFileASync(serverUrl: String, file: File, params: Map<String, String>?= null, callback: Callback) {
        val request = buildPostFileRequest(file, params, serverUrl)
        // 发送异步请求
        client.newCall(request).enqueue(callback)
    }

    /**
     * 构建POST请求的单文件上传的Request
     */
    private fun buildPostFileRequest(file: File, params: Map<String, String>?, serverUrl: String): Request {
        // 构建请求参数体: 参数+文件
        val builder = MultipartBody.Builder().setType(MultipartBody.ALTERNATIVE)

        // 文件请求体
        val fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file)
        // 添加文件，文件为必须的
        builder.addFormDataPart("file", file.name, fileBody)

        // 添加参数，参数为非必需的
        params?.forEach { k, v -> builder.addFormDataPart(k, v) }

        // 构建请求对象
        val request = Request.Builder()
                .url(serverUrl)
                .post(builder.build())
                .build()
        printRequest(request, params)
        return request
    }

    /**
     * 上传多个文件(同步)
     */
    fun uploadFileListSync(serverUrl: String, fileList: List<File>, params: Map<String, String>?= null): String {
        val request = buildPostFilesRequest(fileList, params, serverUrl)
        // 发送同步请求
        val response = client.newCall(request).execute()
        val responseBody = response.body()?.string() ?: "null"
        printResponse(response, responseBody)
        // 返回String格式请求结果
        return responseBody
    }

    /**
     * 上传多个文件(异步)
     */
    fun uploadFileListASync(serverUrl: String, fileList: List<File>, params: Map<String, String>?= null, callback: Callback) {
        val request = buildPostFilesRequest(fileList, params, serverUrl)
        // 发送异步请求
        client.newCall(request).enqueue(callback)
    }

    /**
     * 构建POST请求的多文件上传的Request
     */
    private fun buildPostFilesRequest(fileList: List<File>, params: Map<String, String>?, serverUrl: String): Request {
        // 构建请求参数体: 参数+文件
        val builder = MultipartBody.Builder().setType(MultipartBody.ALTERNATIVE)

        fileList.forEach {
            // 文件请求体
            val fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), it)
            // 添加文件，文件为必须的；将文件保存到name为files的表单数据中，在服务端接口中files参数对应的是一个File[]
            builder.addFormDataPart("files", it.name, fileBody) // 这里的name是接口中文件列表的参数名
        }

        // 添加参数，参数为非必需的
        params?.forEach { k, v -> builder.addFormDataPart(k, v) }

        // 构建请求对象
        val request = Request.Builder()
                .url(serverUrl)
                .post(builder.build())
                .build()
        printRequest(request, params)
        return request
    }

    /**
     * 打印请求日志
     */
    fun printRequest(request: Request, params: Map<String, String>?) {
        KLogi(this, "请求: $request" +
                "\n参数: $params")
    }

    /**
     * 打印响应日志
     */
    fun printResponse(response: Response?, body: String) {
        if (response == null) {
            KLogi(this, "响应:  null")
            return
        }
        // 计算耗时
        val requestTime = response.sentRequestAtMillis()
        val responseTime = response.receivedResponseAtMillis()
        val consumeTime = "${(responseTime - requestTime) / 1000}s ${(responseTime - requestTime) % 1000}ms"
        // 计算流量
        val tx = calculateFlow(response.request().body()?.contentLength() ?: -1)
        val rx = calculateFlow(response.body()?.contentLength() ?: -1)
        KLogi(this, "响应: $response" +
                "\n参数: $body" +
                "\n耗时: $consumeTime" +
                "\n流量: 上行$tx, 下行$rx")
    }

    /**
     * 计算流量消耗
     */
    private fun calculateFlow(n: Long): String {
        if (n == -1L) return "null"
        return "${n / 1000}KB ${n % 1000}B"
    }
}