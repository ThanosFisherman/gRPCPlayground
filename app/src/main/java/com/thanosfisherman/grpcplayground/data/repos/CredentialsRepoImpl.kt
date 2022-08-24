package com.thanosfisherman.grpcplayground.data.repos

import com.thanosfisherman.grpcplayground.data.sources.DataStoreSource
import com.thanosfisherman.grpcplayground.data.sources.RemoteSource
import com.thanosfisherman.grpcplayground.domain.models.DCredential
import com.thanosfisherman.grpcplayground.domain.repos.CredentialsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import timber.log.Timber

class CredentialsRepoImpl(
    private val sourceRemote: RemoteSource,
    private val sourceCache: DataStoreSource
) : CredentialsRepo {
    override suspend fun getCredentials(onlyFromCache: Boolean): List<DCredential> =
        withContext(Dispatchers.IO) {
            val user = sourceCache.getUserProfileModel().firstOrNull()
            if (user == null || user.userId.isEmpty())
                return@withContext emptyList()
            val defCache = async { sourceCache.getCredentials(user.userId) }
            val defRemote = async { sourceRemote.getCredentials(user.userId) }
            val cacheResult = defCache.await().firstOrNull()
            if (onlyFromCache) {
                return@withContext cacheResult ?: emptyList()
            }

            val remoteResult = try {
                defRemote.await()
            } catch (e: Exception) {
                null
            }
            Timber.i("REMOTE RESULT $remoteResult")
            if (remoteResult != null) {
                sourceCache.storeCredentialsListModel(remoteResult)
                return@withContext remoteResult
            }

            return@withContext cacheResult ?: emptyList()
        }

    override suspend fun deleteCredentials() {
        sourceCache.removeCredentials()
    }
}