package com.thanosfisherman.grpcplayground.data.sources

import androidx.datastore.core.DataStore
import com.thanosfisherman.grpcplayground.domain.models.DCredential
import com.thanosfisherman.grpcplayground.domain.models.DUserProfileModel
import com.thanosfisherman.grpcplayground.protos.Credentials
import com.thanosfisherman.grpcplayground.protos.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

typealias DataStoreProfile = DataStore<UserProfile>
typealias DataStoreCredentials = DataStore<Credentials.CredentialsList>

/**
 * Local caching using proto datastore
 */
class DataStoreSource(
    private val dataStoreUserProfile: DataStoreProfile,
    private val dataStoreCredentials: DataStoreCredentials
) {

    //As a future improvement I would like to store and retrieve this data in an encrypted fashion
    suspend fun storeUserProfileModel(userProfile: DUserProfileModel) {
        dataStoreUserProfile.updateData { prof ->
            prof.newBuilderForType()
                .setUserId(userProfile.userId)
                .setEmail(userProfile.email)
                .setUsername(userProfile.username)
                .build()
        }
    }

    suspend fun removeCurrentUser() {
        dataStoreUserProfile.updateData { prof ->
            prof.defaultInstanceForType
        }
    }

    fun getUserProfileModel(): Flow<DUserProfileModel> {
        return dataStoreUserProfile.data.catch {
            if (it is IOException) {
                Timber.e(it, "Error reading sort order preferences.")
                emit(UserProfile.getDefaultInstance())
            } else {
                throw it
            }
        }.map {
            DUserProfileModel(it.userId, it.email, it.username)
        }
    }

    suspend fun storeCredentialsListModel(dCredentials: List<DCredential>) {
        val credentialsData = dCredentials.map {
            Credentials.Credential.newBuilder().setId(it.id).setIssuedOn(it.issuedOn)
                .setIssuer(it.issuer).setSubject(it.subject).setTitle(it.title).build()
        }

        dataStoreCredentials.updateData { creds ->
            creds.newBuilderForType()
                .addAllCredentials(credentialsData).build()
        }
    }

    suspend fun removeCredentials() {
        dataStoreCredentials.updateData { creds ->
            creds.defaultInstanceForType
            creds.newBuilderForType().clearCredentials().build()
        }
    }

    fun getCredentials(userId: String): Flow<List<DCredential>> {
        return dataStoreCredentials.data.catch {
            if (it is IOException) {
                Timber.e(it, "Error reading sort order preferences.")
                emit(Credentials.CredentialsList.getDefaultInstance())
            } else {
                throw it
            }
        }.map { creds ->
            creds.credentialsList.map {
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
    }
}
