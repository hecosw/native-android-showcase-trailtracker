package com.hecosw.trailtracker.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.hecosw.trailtracker.BuildConfig
import com.hecosw.trailtracker.data.source.framework.FlickrPhotoApi
import com.hecosw.trailtracker.data.source.network.PhotoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJsonSerializer(): Json {
        return Json { ignoreUnknownKeys = true }
    }

    @Provides
    @Singleton
    fun provideHttpClient(@ApplicationContext context: Context): HttpClient {
        val chuckerCollector = ChuckerCollector(context = context)
        val chuckerInterceptor = ChuckerInterceptor.Builder(context)
            .collector(chuckerCollector)
            .maxContentLength(250_000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()

        return HttpClient(OkHttp) {
            defaultRequest {
                url(BuildConfig.FLICKR_API_BASE_URL)
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            engine {
                addInterceptor(chuckerInterceptor)
            }
            install(ContentNegotiation) {
                json()
            }
        }
    }

    @Provides
    @Singleton
    fun providePhotoApi(client: HttpClient): PhotoApi { // Ensure the return type matches your interface
        return FlickrPhotoApi(client)
    }
}
