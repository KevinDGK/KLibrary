package com.dgk.common

import com.dgk.common.util.http.OkHttpUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.junit.Test
import java.io.File

import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class OkHttpUnitTest {

    val SERVER_URL_MINGQING = "http://101.200.162.199:9080/MyDemo/"   // 明钦阿里云服务器
    val SERVER_URL_LOCAL = "http://10.30.3.174:8080/mydemo/"  // 本地
    val SERVER_URL = SERVER_URL_MINGQING

    @Test
    fun testOkHttp() {

        val url = SERVER_URL + "get"

        OkHttpUtil.getSync(url, null)

        val params = HashMap<String, String>()
        params["name"] = "dgk"
        params["password"] = "123456"

        OkHttpUtil.getSync(url, params)

        OkHttpUtil.getAsync(url,  params, object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                println("onFailure: $e")
            }

            override fun onResponse(call: Call?, response: Response?) {
                println("onResponse: ${response?.body()?.string() ?: "null"}")
            }
        })
    }

    @Test
    fun testOkHttpUploadFile() {

        println("testOkHttpUploadFile")

        val url = SERVER_URL + "uploadFile"

        val file = File("123.txt")
        file.writeText("1234567890")

        val mcode = "199359"
        val en = "1234567890"

        val params = HashMap<String, String>()
        params.put("mcode", mcode)
        params.put("en", en)

        OkHttpUtil.uploadFileSync(url, file, params)
    }

    @Test
    fun testOkHttpUploadFileList() {

        println("testOkHttpUploadFile")

        val url = SERVER_URL + "uploadFileList"

        val file1 = File("1.txt")
        file1.writeText("1234567890")

        val file2 = File("2.txt")
        file2.writeText("1234567890")

        val mcode = "199359"
        val en = "1234567890"

        val params = HashMap<String, String>()
        params.put("mcode", mcode)
        params.put("en", en)

        OkHttpUtil.uploadFileListSync(url, listOf(file1,file2), params)
    }
}
