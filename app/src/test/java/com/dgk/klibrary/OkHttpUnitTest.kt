package com.dgk.klibrary

import com.dgk.common.util.KLogi
import com.dgk.demo.http.OkHttpUtil
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

    val URL_GET = "http://localhost:8080/mydemo/test/get"

    @Test
    fun testOkHttp() {

        OkHttpUtil.getSync(URL_GET)

        OkHttpUtil.getSync(URL_GET + "?name=Kevin&password=123456")

        OkHttpUtil.getAsync(URL_GET, object : Callback {
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

//        val url = "http://10.30.3.174:8080/mydemo/test/upload"
        val url = "http://localhost:8080/mydemo/uploadFile"

        val file = File("123.txt")
        file.writeText("1234567890")

        val mcode = "199359"
        val en = "1234567890"

        val params = HashMap<String, String>()
        params.put("mcode", mcode)
        params.put("en", en)

        OkHttpUtil.uploadFile(url, file, params)
    }

    @Test
    fun testOkHttpUploadFileList() {

        println("testOkHttpUploadFile")

//        val url = "http://10.30.3.174:8080/mydemo/test/upload"
        val url = "http://localhost:8080/mydemo/uploadFileList"

        val file1 = File("1.txt")
        file1.writeText("1234567890")

        val file2 = File("2.txt")
        file2.writeText("1234567890")

        val mcode = "199359"
        val en = "1234567890"

        val params = HashMap<String, String>()
        params.put("mcode", mcode)
        params.put("en", en)

        OkHttpUtil.uploadFileList(url, arrayOf(file1,file2), params)
    }
}
