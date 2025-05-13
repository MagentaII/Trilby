package com.example.trilby.data.repositories.auth_repository.model

import com.example.trilby.data.data_sources.firebase.model.FbUser

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
)

fun User.toFirebaseModel(): FbUser {
    return FbUser(
        uid = uid,
        name = name,
        email = email
    )
}