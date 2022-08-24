package com.thanosfisherman.grpcplayground.server


fun main() {
    val server = ServerApp()
    server.start()
    server.blockUntilShutdown()
}
