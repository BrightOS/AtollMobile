package ru.divarteam.atoll.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.myrosmol.conductor.network.RetrofitClient
import ru.myrosmol.conductor.network.RestAPI
import ru.myrosmol.conductor.network.RetrofitService

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    private val BASE_URL = "https://api.atoll.divarteam.ru/api/v1/"

    @Provides
    @Reusable
    fun provideRetrofitService(): RetrofitService =
        RetrofitService(RetrofitClient.getClient(BASE_URL).create(RestAPI::class.java))
}