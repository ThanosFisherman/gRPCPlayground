package com.thanosfisherman.grpcplayground.domain.repos

import com.thanosfisherman.grpcplayground.domain.models.DUserProfileModel
import kotlinx.coroutines.flow.Flow

interface PrefsRepo {

    suspend fun storeUserProfile(userProfile: DUserProfileModel)
    suspend fun deleteUserProfile()
    fun getUserProfile(): Flow<DUserProfileModel>
}
