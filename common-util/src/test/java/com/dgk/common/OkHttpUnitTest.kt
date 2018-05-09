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


    @Test
    fun testOkHttp() {

        val action = "get"

        OkHttpUtil.getSync(action = action, params = null)

        val params = HashMap<String, String>()
        params["name"] = "dgk"
        params["password"] = "123456"

        OkHttpUtil.getSync(action = action, params = params)

        OkHttpUtil.getAsync(action = action, params = params, callback = object : Callback {
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

        val action = "uploadFile"

        val file = File("123.txt")
        file.writeText("1234567890")

        val mcode = "199359"
        val en = "1234567890"

        val params = HashMap<String, String>()
        params.put("mcode", mcode)
        params.put("en", en)

        OkHttpUtil.uploadFileSync(action = action, file = file, params = params)
    }

    @Test
    fun testOkHttpUploadFileList() {

        println("testOkHttpUploadFile")

        val action = "uploadFileList"

        val file1 = File("1.txt")
        file1.writeText("1234567890")

        val file2 = File("2.txt")
        file2.writeText("1234567890")

        val mcode = "199359"
        val en = "1234567890"

        val params = HashMap<String, String>()
        params.put("mcode", mcode)
        params.put("en", en)

        OkHttpUtil.uploadFileListSync(action = action, fileList = listOf(file1,file2), params = params)
    }
}
