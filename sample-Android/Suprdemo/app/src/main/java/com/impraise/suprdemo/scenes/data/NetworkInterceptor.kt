package com.impraise.suprdemo.scenes.data

import com.impraise.suprdemo.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest

class NetworkInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val ts = System.currentTimeMillis().toString()
        val apikey = PUBLIC_API_KEY
        val hashInput = "$ts$PRIVATE_API_KEY$PUBLIC_API_KEY"

        val httpUrl = originalRequest.url().newBuilder()
                .addQueryParameter(TS, ts)
                .addQueryParameter(API_KEY, apikey)
                .addQueryParameter(HASH, hashInput.md5()).build()

        val builder = originalRequest.newBuilder().url(httpUrl)
        val newRequest = builder.build()

        return chain.proceed(newRequest)
    }

    companion object {
        private val TS = "ts"
        private val API_KEY = "apikey"
        private val HASH = "hash"
        private const val PUBLIC_API_KEY = BuildConfig.PUBLIC_API_KEY
        private const val PRIVATE_API_KEY = BuildConfig.PRIVATE_API_KEY
    }
}

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}
