package com.piglet.foundation.api

import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException

const val CONNECT_TIMEOUT = 30.toLong()
const val READ_TIMEOUT = 30.toLong()

object UnsafeOkHttpClient {
    // Create a trust manager that does not validate certificate chains
    val unsafeOkHttpClient: OkHttpClient
        // Install the all-trusting trust manager
        // Create an ssl socket factory with our all-trusting manager
        get() = try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf(
                object : X509TrustManager {

                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier(hostnameVerifier = { _, _ -> true })
            builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            builder.build()
        } catch (e: Exception) {
            throw(RuntimeException(e))
        }
}
