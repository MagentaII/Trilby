package com.example.trilby.data.repositories.auth_repository.util

import com.example.trilby.data.data_sources.firebase.model.FbUser
import com.example.trilby.data.repositories.auth_repository.model.User

/**
 * Network to External
 */
fun FbUser.toExternal(): User {
    return User(
        uid = uid,
        name = name,
        email = email
    )
}

/**
 * External to Network
 */
fun User.toNetwork(): FbUser {
    return FbUser(
        uid = uid,
        name = name,
        email = email
    )
}