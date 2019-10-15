package com.datasource.retrofitsource.di

import android.content.Context
import android.os.Build
import com.datasource.R
import com.datasource.retrofitsource.TLSSocketFactory
import com.datasource.retrofitsource.TrendingListApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext

@Module
class RetrofitModule {

    @Provides
    @RetrofitScope
    fun okhttp(): OkHttpClient {
        if (Build.VERSION.SDK_INT in 19..20) {
            val sc = SSLContext.getInstance("TLSv1.2")
            sc.init(null, null, null)
            return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BODY
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .sslSocketFactory(TLSSocketFactory(sc.socketFactory))
                .build()

        } else {
            return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        }
    }

    @Provides
    @RetrofitScope
    fun trendingListApi(context: Context, okHttpClient: OkHttpClient): TrendingListApi {

        val moshi = Moshi.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.baseurl))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()

        return retrofit.create(TrendingListApi::class.java)
    }

}
