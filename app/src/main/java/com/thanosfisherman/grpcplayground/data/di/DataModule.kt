package com.thanosfisherman.grpcplayground.data.di

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import com.google.protobuf.InvalidProtocolBufferException
import com.thanosfisherman.grpcplayground.ServerConfig
import com.thanosfisherman.grpcplayground.data.repos.CredentialsRepoImpl
import com.thanosfisherman.grpcplayground.data.repos.PrefsRepoImpl
import com.thanosfisherman.grpcplayground.data.sources.DataStoreSource
import com.thanosfisherman.grpcplayground.data.sources.RemoteSource
import com.thanosfisherman.grpcplayground.domain.repos.CredentialsRepo
import com.thanosfisherman.grpcplayground.domain.repos.PrefsRepo
import com.thanosfisherman.grpcplayground.protos.Credentials
import com.thanosfisherman.grpcplayground.protos.UserProfile
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.InputStream
import java.io.OutputStream

val dataModule = module {
    val channel =
        ManagedChannelBuilder.forAddress(ServerConfig.BASE_URL, ServerConfig.PORT).usePlaintext()
            .build()
    single(named("userProfile")) { provideTokenDataStore(androidContext()) }
    single(named("userCredentials")) { provideCredentialsDataStore(androidContext()) }
    single { RemoteSource(channel, Dispatchers.IO) }
    single { DataStoreSource(get(named("userProfile")), get(named("userCredentials"))) }
    factory<CredentialsRepo> { CredentialsRepoImpl(get(), get()) }
    factory<PrefsRepo> { PrefsRepoImpl(get()) }
}

private fun provideTokenDataStore(context: Context): DataStore<UserProfile> {

    return DataStoreFactory.create(
        serializer = UserProfileSerializer,
        produceFile = { context.dataStoreFile("user_profile_prefs.pb") }
    )
}

private fun provideCredentialsDataStore(context: Context): DataStore<Credentials.CredentialsList> {

    return DataStoreFactory.create(
        serializer = CredentialsListSerializer,
        produceFile = { context.dataStoreFile("credentials_prefs.pb") }
    )
}

private object UserProfileSerializer : Serializer<UserProfile> {
    override val defaultValue: UserProfile
        get() = UserProfile.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserProfile {
        try {
            return UserProfile.parseFrom(input)
        } catch (ex: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read token proto.", ex)
        }
    }

    override suspend fun writeTo(t: UserProfile, output: OutputStream) {
        t.writeTo(output)
    }
}

private object CredentialsListSerializer : Serializer<Credentials.CredentialsList> {
    override val defaultValue: Credentials.CredentialsList
        get() = Credentials.CredentialsList.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Credentials.CredentialsList {
        try {
            return Credentials.CredentialsList.parseFrom(input)
        } catch (ex: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read token proto.", ex)
        }
    }

    override suspend fun writeTo(t: Credentials.CredentialsList, output: OutputStream) {
        t.writeTo(output)
    }
}


