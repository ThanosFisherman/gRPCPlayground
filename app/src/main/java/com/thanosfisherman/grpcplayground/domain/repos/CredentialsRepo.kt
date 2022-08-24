package com.thanosfisherman.grpcplayground.domain.repos

import com.thanosfisherman.grpcplayground.domain.models.DCredential

interface CredentialsRepo {

    suspend fun getCredentials(onlyFromCache: Boolean = false): List<DCredential>
    suspend fun deleteCredentials()
}