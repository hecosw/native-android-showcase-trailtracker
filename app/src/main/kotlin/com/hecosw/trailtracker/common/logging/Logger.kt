package com.hecosw.trailtracker.common.logging

interface Logger {
    fun verbose(message: String)
    fun debug(message: String)
    fun info(message: String)
    fun warning(message: String)
    fun error(message: String, throwable: Throwable? = null)
    fun assert(message: String)
}
