package com.example.trilby.data.data_sources.firebase.model

import com.example.trilby.data.repositories.auth_repository.model.User

data class FbUser(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
)

fun FbUser.toExternalModel(): User {
    return User(
        uid = uid,
        name = name,
        email = email
    )
}