
package com.movie.app.movie_api.services.impl

import com.movie.app.movie_api.dto.ClientDTO
import io.grpc.ManagedChannel
import io.grpc.StatusException
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import services.ClientProtoServiceGrpcKt.ClientProtoServiceCoroutineStub
import services.Services.IdRequest
import java.io.Closeable
import java.util.concurrent.TimeUnit

@Service
class ClientProtoService(
    private val clientChannel: ManagedChannel,
    private val clientStub: ClientProtoServiceCoroutineStub,
    private val grpcTimeout: Long
) : Closeable {

    private val logger = LoggerFactory.getLogger(ClientProtoService::class.java)

    fun getClientById(id: Int): ClientDTO? = runBlocking {
        val request = IdRequest.newBuilder().setId(id).build()
        try {
            val response = clientStub.withDeadlineAfter(grpcTimeout, TimeUnit.SECONDS)
                .getClientById(request)
            val foundClient = ClientDTO(response.id, response.firstName, response.lastName, response.email)
            logger.info("ClientProtoService received: {}", foundClient)
            return@runBlocking foundClient
        } catch (e: StatusException) {
            when (e.status.code) {
                io.grpc.Status.Code.NOT_FOUND -> logger.warn("Client with id {} not found", id)
                io.grpc.Status.Code.UNAVAILABLE -> logger.error("gRPC service unavailable", e)
                io.grpc.Status.Code.PERMISSION_DENIED -> logger.error("Permission denied to access client data", e)
                else -> logger.error("RPC failed: {}", e.status, e)
            }
            return@runBlocking null
        }
    }

    override fun close() {
        clientChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}
