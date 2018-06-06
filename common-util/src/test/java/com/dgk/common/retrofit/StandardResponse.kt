package com.dgk.common.retrofit

import com.alibaba.fastjson.JSONObject

class StandardResponse {
    var code: Int = -1
    var message: String = ""
    var data: JSONObject? = null
    override fun toString(): String {
        return "StandardResponse(code=$code, message='$message', data=$data)"
    }

}