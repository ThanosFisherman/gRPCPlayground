package com.thanosfisherman.grpcplayground.server

import io.grpc.Server
import io.grpc.ServerBuilder

class ServerApp {
    private var server: Server? = null
    private val port = 50051
    private val database = Database()
    private val credentials = CredentialsServiceImpl(database)

    fun start() {
        println("Starting server")
        server =
            ServerBuilder.forPort(port).addService(credentials).build()
                .start()
        println("Server started on port $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this@ServerApp.stop()
                println("*** server shut down")
            }
        )
    }

    fun stop() {
        server?.shutdown()

    }

    fun blockUntilShutdown() {
        server?.awaitTermination()

    }
}