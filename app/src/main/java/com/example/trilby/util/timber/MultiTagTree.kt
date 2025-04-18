package com.example.trilby.util.timber

import timber.log.Timber


// 參考網站
// https://jimmy4302001.medium.com/%E4%BD%BF%E7%94%A8timber%E4%BE%86%E5%B9%AB%E5%BF%99%E6%89%93%E5%8D%B0log-afc54aaa76d6
class MultiTagTree : Timber.DebugTree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?
    ) {
        val stackTrace = Throwable().stackTrace
        val element = stackTrace.getOrNull(5)
        val traceInfo = element?.let {
            "(${it.fileName}:${it.lineNumber})#${it.methodName}: "
        } ?: ""
        super.log(priority, tag, "$traceInfo$message", t)
    }
}