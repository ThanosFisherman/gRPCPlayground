package com.thanosfisherman.grpcplayground.data.sources

import com.thanosfisherman.grpcplayground.domain.models.DCredential
import com.thanosfisherman.grpcplayground.protos.Credentials
import com.thanosfisherman.grpcplayground.protos.CredentialsServiceGrpc
import io.grpc.ManagedChannel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * RemoteSource communicates with the backend
 */
class RemoteSource(
    private val channel: ManagedChannel,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val stub by lazy {
        CredentialsServiceGrpc.newBlockingStub(channel)
    }

    suspend fun getCredentials(userId: String): List<DCredential>? {
        return withContext(ioDispatcher) {
            val request = Credentials.GetCredentialsRequest.newBuilder()
                .setUserId(userId).build()
            return@withContext try {
                stub.getCredentials(request).mapToDomain(userId)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun shutDownChannel() {
        channel.shutdown().awaitTermination(1, TimeUnit.SECONDS)
    }
}

fun Credentials.GetCredentialsResponse.mapToDomain(userId: String): List<DCredential> {
    return this.credentialsList.map {
        DCredential(
            userId,
            it.id,
            it.issuedOn,
            it.subject,
            it.issuer,
            it.title
        )
    }
}