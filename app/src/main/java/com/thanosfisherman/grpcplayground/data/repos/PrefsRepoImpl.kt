package com.thanosfisherman.grpcplayground.data.repos

import com.thanosfisherman.grpcplayground.data.sources.DataStoreSource
import com.thanosfisherman.grpcplayground.domain.models.DUserProfileModel
import com.thanosfisherman.grpcplayground.domain.repos.PrefsRepo
import kotlinx.coroutines.flow.Flow

class PrefsRepoImpl(private val dataStoreSource: DataStoreSource) : PrefsRepo {
    override suspend fun storeUserProfile(userProfile: DUserProfileModel) {
        dataStoreSource.storeUserProfileModel(userProfile)
    }

    override suspend fun deleteUserProfile() {
        dataStoreSource.removeCurrentUser()
    }

    override fun getUserProfile(): Flow<DUserProfileModel> {
        return dataStoreSource.getUserProfileModel()
    }
}
