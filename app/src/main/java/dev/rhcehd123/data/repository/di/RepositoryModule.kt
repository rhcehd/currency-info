package dev.rhcehd123.data.repository.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.rhcehd123.data.repository.CurrencyRepository
import dev.rhcehd123.data.remote.CurrencyService
import dev.rhcehd123.data.repository.FakeLocalRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun currencyRepository(
        currencyService: CurrencyService,
        compositeDisposable: CompositeDisposable
    ): CurrencyRepository {
        return CurrencyRepository(currencyService, compositeDisposable)
    }

    @Provides
    @Singleton
    fun fakeLocalRepository(): FakeLocalRepository {
        return FakeLocalRepository()
    }
}