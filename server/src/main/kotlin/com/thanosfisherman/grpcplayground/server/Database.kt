package com.thanosfisherman.grpcplayground.server

import com.thanosfisherman.grpcplayground.protos.Credentials
import com.thanosfisherman.grpcplayground.protos.copy
import java.util.*

class Database {
    private val map = mutableMapOf<UUID, MutableList<Credentials.Credential>>()
    private var nextId = 1
    private val lock = Any()

    fun add(userId: UUID, credential: Credentials.Credential) {
        synchronized(lock) {
            val newData = credential.copy { id = nextId }
            val list = map.getOrElse(userId) { mutableListOf() }.apply { add(newData) }
            nextId++
            map[userId] = list
            println("MAP " + map[userId])
        }
    }

    fun count(userid: UUID): Int {
        return map.getOrElse(userid) { emptyList() }.size
    }

    fun get(userId: UUID, limit: Int, after: Int): List<Credentials.Credential> {
        return map.getOrElse(userId) { emptyList() }.filter { it.id > after }.sortedBy { it.id }
            .take(limit)
    }
}