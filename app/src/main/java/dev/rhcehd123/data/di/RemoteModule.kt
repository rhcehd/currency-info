package dev.rhcehd123.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.rhcehd123.BuildConfig
import dev.rhcehd123.data.remote.CurrencyService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Provides
    fun okhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                if(BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            )
            .build()
    }

    @Provides
    fun gsonConverter(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun rxJava3CallAdapter(): CallAdapter.Factory {
        return RxJava3CallAdapterFactory.create()
    }

    @Provides
    fun currencyService(okHttpClient: OkHttpClient, gsonConverter: Converter.Factory, rxjava3CallAdapter: CallAdapter.Factory): CurrencyService {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(gsonConverter)
            .addCallAdapterFactory(rxjava3CallAdapter)
            .baseUrl("https://api.apilayer.com/")
            .build()
            .create(CurrencyService::class.java)
    }

    @Provides
    @Singleton
    fun compositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }
}