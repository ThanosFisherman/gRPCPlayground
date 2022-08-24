package com.thanosfisherman.grpcplayground.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DCredential(
    val userId: String,
    val id: Int,
    val issuedOn: Long,
    val subject: String,
    val issuer: String,
    val title: String
) : Parcelable
