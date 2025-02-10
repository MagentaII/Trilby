package com.example.trilby.data.repositories.auth_repository

import com.example.trilby.data.sources.network.auth_network_source.NetworkUser

/**
 * Network to External
 */
fun NetworkUser.toExternal(): User {
    return User(
        uid = uid,
        name = name,
        email = email
    )
}

/**
 * External to Network
 */
fun User.toNetwork(): NetworkUser {
    return NetworkUser(
        uid = uid,
        name = name,
        email = email
    )
}