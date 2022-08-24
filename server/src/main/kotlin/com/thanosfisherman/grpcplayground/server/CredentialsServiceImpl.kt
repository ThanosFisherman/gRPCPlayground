package com.thanosfisherman.grpcplayground.server

import com.thanosfisherman.grpcplayground.protos.Credentials
import com.thanosfisherman.grpcplayground.protos.CredentialsServiceGrpc
import com.thanosfisherman.grpcplayground.protos.credential
import io.grpc.stub.StreamObserver
import java.io.IOException
import java.net.InetAddress
import java.util.*
import kotlin.random.Random

class CredentialsServiceImpl(private val database: Database) :
    CredentialsServiceGrpc.CredentialsServiceImplBase() {

    private val serverName = determineHostname()

    override fun getCredentials(
        request: Credentials.GetCredentialsRequest?,
        responseObserver: StreamObserver<Credentials.GetCredentialsResponse>?
    ) {
        if (request == null) {
            responseObserver?.onCompleted()
            return
        }

        println("getCredentials request: $request Server hostname: $serverName")
        val userId = UUID.fromString(request.userId)
        fillDatabase(userId)
        val limit = if (request.limit <= 0) 3 else request.limit
        val result = database.get(userId, limit, request.after)
        val credsResponse =
            Credentials.GetCredentialsResponse.newBuilder().addAllCredentials(result).build()
        responseObserver?.onNext(credsResponse)
        responseObserver?.onCompleted()
    }

    private fun fillDatabase(userId: UUID) {
        val maxEntries = 5
        if (database.count(userId) < maxEntries) {
            val newData = generateCredential()
            database.add(userId, newData)
        }
    }

    private fun generateCredential(): Credentials.Credential {

        return credential {
            val randomInt = Random.nextInt(1000)
            issuedOn = System.currentTimeMillis()
            issuer = "Issuer $randomInt"
            subject = "Subject $randomInt ${determineHostname()}"
            title = "Title $randomInt"
        }
    }

    private fun determineHostname(): String {
        try {
            return InetAddress.getLocalHost().hostName
        } catch (ex: IOException) {
            println("Failed to determine hostname. Will generate one $ex")
        }
        // Strange. Well, let's make an identifier for ourselves.
        return "generated-" + Random.nextInt(1000)
    }
}