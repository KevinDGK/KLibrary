package com.dgk.common.util.log

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.dgk.common.util.http.OkHttpUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.IOException

/**
 * 日志上传服务
 * Created by hongge on 2018/5/6.
 */
class UploadLogIntentService : IntentService(UploadLogIntentService::class.java.simpleName) {

    companion object {
        /**
         * 启动日志上传服务
         * @param context 上下文
         * @param date yyyyMMdd 需要上传哪天的日志文件，格式为yyyyMMdd
         */
        fun launch(context: Context, date: String, serverUrl: String) {
            launch(context, arrayListOf(date), serverUrl)
        }

        /**
         * 启动日志上传服务
         * @param context 上下文
         * @param dateList 需要上传哪些天的日志文件，格式为yyyyMMdd
         */
        fun launch(context: Context, dateList: ArrayList<String>, serverUrl: String) {
            KLogi(this, "launch: dateList=$dateList, serverUrl=$serverUrl")
            val intent = Intent(context, UploadLogIntentService::class.java)
            intent.putStringArrayListExtra("dateList", dateList)
            intent.putExtra("serverUrl", serverUrl)
            context.startService(intent)
        }
    }

    // 文件服务器地址
    private var serverUrl = ""
    // 指定上传哪天的日志
    private var dateList : java.util.ArrayList<String>? = null

    override fun onHandleIntent(intent: Intent?) {

        if (intent == null) return

        // 在上传日志之前主动触发一次将缓存中的日志写入文件
        KLog.flush(true)

        dateList = intent.getStringArrayListExtra("dateList")
        serverUrl = intent.getStringExtra("serverUrl")

        if (dateList != null && dateList!!.isNotEmpty()) {
            checkFile()
        }
    }

    /**
     * 检查文件
     */
    private fun checkFile() {

        val fileList = mutableListOf<File>()

        dateList?.forEach {
            val file = isFileExist(it)
            if (file != null) {
                fileList.add(file)
            }
        }

        KLogi(this, "待上传的文件总数：${fileList.size}")

        if (fileList.isNotEmpty()) {
            uploadFile(fileList)
        }
    }

    /**
     * 判断文件是否存在
     */
    private fun isFileExist(fileName: String): File? {

        val fileInSD = getFileInSD(fileName)
        if (fileInSD.exists()) {
            return fileInSD
        }

        val fileInCache = getFileInCache(fileName)
        if (fileInCache.exists()) {
            return fileInCache
        }

        KLoge(this,"文件 $fileName 不存在")

        return null
    }

    /**
     * 上传文件
     */
    private fun uploadFile(fileList: List<File>) {

        val params = HashMap<String, String>()
        params["mcode"] = "123456"
        params["en"] = "a2b3d4c5"

        val callback = object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                e?.printStackTrace()
                KLogi(this@UploadLogIntentService, "onFailure: ${e.toString()}")
            }

            override fun onResponse(call: Call?, response: Response?) {
                val responseBody = response?.body()?.string() ?: "null"
                OkHttpUtil.printResponse(response, responseBody)
            }
        }

        if (fileList.size == 1) {
            OkHttpUtil.uploadFileASync(serverUrl,fileList[0], params, callback)
        } else if (fileList.size > 1) {
            OkHttpUtil.uploadFileListASync(serverUrl, fileList, params, callback)
        }
    }

    /**
     * 从SD卡中获取日志文件
     */
    private fun getFileInSD(fileName: String): File {
        val filePath = KLog.getFilePath(this) + "/" + KLog.getFileName(fileName)
        return File(filePath)
    }

    /**
     * 从缓存目录获取日志文件
     */
    private fun getFileInCache(fileName: String): File {
        val filePath = KLog.getCashPath(this) + "/" + KLog.getFileName(fileName)
        return File(filePath)
    }
}