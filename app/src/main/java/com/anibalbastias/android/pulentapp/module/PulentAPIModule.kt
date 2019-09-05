package com.anibalbastias.android.pulentapp.module

import com.anibalbastias.android.pulentapp.BuildConfig
import com.anibalbastias.android.pulentapp.PulentApplication
import com.anibalbastias.android.pulentapp.R
import com.anibalbastias.android.pulentapp.base.api.data.pulent.PulentAPIGSONManager
import com.anibalbastias.android.pulentapp.base.api.data.pulent.PulentApiService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class PulentAPIModule {

    companion object {
        private const val CONNECT_TIMEOUT = 120L
        private const val READ_TIMEOUT = 120L
        private const val WRITE_TIMEOUT = 120L
        private var BASE_ENDPOINT: String = ""
    }


    @Provides
    @Singleton
    @Named("provideRetrofitHttpClient")
    fun provideHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        val clientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        clientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        clientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        return clientBuilder.build()
    }

    @Provides
    @Singleton
    @Named("providePulentRetrofit")
    fun provideRetrofit(@Named("provideRetrofitHttpClient") okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(PulentApplication.appContext.getString(R.string.pulent_endpoint))
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(makeGson()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    private fun makeGson(): Gson {
        return PulentAPIGSONManager.createGsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()
    }

    @Provides
    fun providePulentAPI(@Named("providePulentRetrofit") retrofit: Retrofit): PulentApiService =
        retrofit.create(PulentApiService::class.java)
}