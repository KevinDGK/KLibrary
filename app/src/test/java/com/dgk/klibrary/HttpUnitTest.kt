package com.dgk.klibrary

import com.dgk.demo.okhttp.OkHttpUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.junit.Test

import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class HttpUnitTest {

    val URL_GET = "http://localhost:8080/mydemo/test/get"

    @Test
    fun testOkHttpUtil() {

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
}
