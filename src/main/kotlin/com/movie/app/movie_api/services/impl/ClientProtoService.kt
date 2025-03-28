
package com.movie.app.movie_api.services.impl

import com.movie.app.movie_api.dto.ClientDTO
import io.grpc.ManagedChannel
import io.grpc.StatusException
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import services.ClientProtoServiceGrpcKt.ClientProtoServiceCoroutineStub
import services.FavoriteMovieProtoServiceGrpcKt.FavoriteMovieProtoServiceCoroutineStub
import services.Services.IdRequest
import java.io.Closeable
import java.util.concurrent.TimeUnit

@Service
class ClientProtoService(
    private val clientChannel: ManagedChannel,
    private val clientStub: ClientProtoServiceCoroutineStub,
    private val favoriteMovieStub: FavoriteMovieProtoServiceCoroutineStub,
    private val grpcTimeout: Long
) : Closeable {

    private val logger = LoggerFactory.getLogger(ClientProtoService::class.java)

    fun addClient(clientDTO: ClientDTO): ClientDTO? = runBlocking {
        val request = services.Services.ClientRequest.newBuilder()
            .setFirstName(clientDTO.firstName)
            .setLastName(clientDTO.lastName)
            .setEmail(clientDTO.email)
            .build()

        try {
            val response = clientStub.withDeadlineAfter(grpcTimeout, TimeUnit.SECONDS).addClient(request)
            val addedClient = ClientDTO(response.id, response.firstName, response.lastName, response.email)
            logger.info("ClientProtoService added: {}", addedClient)
            return@runBlocking addedClient
        } catch (e: StatusException) {
            when (e.status.code) {
                io.grpc.Status.Code.UNAVAILABLE -> logger.error("gRPC service unavailable", e)
                io.grpc.Status.Code.PERMISSION_DENIED -> logger.error("Permission denied to add client", e)
                else -> logger.error("RPC failed: {}", e.status, e)
            }
            return@runBlocking null
        }
    }

    fun updateClient(clientDTO: ClientDTO): ClientDTO? = runBlocking {
        val request = services.Services.ClientRequest.newBuilder()
            .setId(clientDTO.id)
            .setFirstName(clientDTO.firstName)
            .setLastName(clientDTO.lastName)
            .setEmail(clientDTO.email)
            .build()

        try {
            val response = clientStub.withDeadlineAfter(grpcTimeout, TimeUnit.SECONDS).updateClient(request)
            val updatedClient = ClientDTO(response.id, response.firstName, response.lastName, response.email)
            logger.info("ClientProtoService updated: {}", updatedClient)
            return@runBlocking updatedClient
        } catch (e: StatusException) {
            when (e.status.code) {
                io.grpc.Status.Code.NOT_FOUND -> logger.warn("Client with id {} not found", clientDTO.id)
                io.grpc.Status.Code.UNAVAILABLE -> logger.error("gRPC service unavailable", e)
                io.grpc.Status.Code.PERMISSION_DENIED -> logger.error("Permission denied to update client", e)
                else -> logger.error("RPC failed: {}", e.status, e)
            }
            return@runBlocking null
        }
    }

    fun deleteClient(id: Int) = runBlocking {
        val request = IdRequest.newBuilder().setId(id).build()
        try {
            clientStub.withDeadlineAfter(grpcTimeout, TimeUnit.SECONDS).deleteClient(request)
            logger.info("ClientProtoService deleted client with id {}", id)
        } catch (e: StatusException) {
            when (e.status.code) {
                io.grpc.Status.Code.NOT_FOUND -> logger.warn("Client with id {} not found", id)
                io.grpc.Status.Code.UNAVAILABLE -> logger.error("gRPC service unavailable", e)
                io.grpc.Status.Code.PERMISSION_DENIED -> logger.error("Permission denied to delete client", e)
                else -> logger.error("RPC failed: {}", e.status, e)
            }
        }
    }

    fun getClientById(id: Int): ClientDTO? = runBlocking {
        val request = IdRequest.newBuilder().setId(id).build()
        try {
            val response = clientStub.withDeadlineAfter(grpcTimeout, TimeUnit.SECONDS).getClientById(request)
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

    fun getClientByEmail(email: String): ClientDTO? = runBlocking {
        val request = services.Services.EmailRequest.newBuilder().setEmail(email).build()
        try {
            val response = clientStub.withDeadlineAfter(grpcTimeout, TimeUnit.SECONDS).getClientByEmail(request)
            val foundClient = ClientDTO(response.id, response.firstName, response.lastName, response.email)
            logger.info("ClientProtoService getClientByEmail found this client : {}", foundClient)
            return@runBlocking foundClient
        } catch (e: StatusException) {
            when (e.status.code) {
                io.grpc.Status.Code.NOT_FOUND -> logger.warn("Client with email {} not found", email)
                io.grpc.Status.Code.UNAVAILABLE -> logger.error("gRPC service unavailable", e)
                io.grpc.Status.Code.PERMISSION_DENIED -> logger.error("Permission denied to access client data", e)
                else -> logger.error("RPC failed: {}", e.status, e)
            }
            return@runBlocking null
        }
    }

    fun getAllClients(): List<ClientDTO> = runBlocking {
        try {
            val response = clientStub.withDeadlineAfter(grpcTimeout, TimeUnit.SECONDS).getAllClients(services.Services.EmptyRequest.getDefaultInstance())
            val clients = response.map { ClientDTO(it.id, it.firstName, it.lastName, it.email) }.toList()

            logger.info("ClientProtoService list of all clients: {}", clients)
            return@runBlocking clients
        } catch (e: StatusException) {
            when (e.status.code) {
                io.grpc.Status.Code.UNAVAILABLE -> logger.error("gRPC service unavailable", e)
                io.grpc.Status.Code.PERMISSION_DENIED -> logger.error("Permission denied to access client data", e)
                else -> logger.error("RPC failed: {}", e.status, e)
            }
            return@runBlocking emptyList()
        }
    }

    fun addFavoriteMovie(clientId: Int, movieId: Int) = runBlocking {
        val request = services.Services.FavoriteMovieRequest.newBuilder()
            .setClientId(clientId)
            .setMovieId(movieId)
            .build()

        try {
            favoriteMovieStub.withDeadlineAfter(grpcTimeout, TimeUnit.SECONDS).addClientFavoriteMovie(request)
            logger.info("ClientProtoService added movie {} to favorites for client {}", movieId, clientId)
        } catch (e: StatusException) {
            when (e.status.code) {
                io.grpc.Status.Code.NOT_FOUND -> logger.warn("Client with id {} or Movie with id {} not found", clientId, movieId)
                io.grpc.Status.Code.UNAVAILABLE -> logger.error("gRPC service unavailable", e)
                io.grpc.Status.Code.PERMISSION_DENIED -> logger.error("Permission denied to add favorite movie", e)
                else -> logger.error("RPC failed: {}", e.status, e)
            }
        }
    }

    fun removeFavoriteMovie(clientId: Int, movieId: Int) = runBlocking {
        val request = services.Services.FavoriteMovieRequest.newBuilder()
            .setClientId(clientId)
            .setMovieId(movieId)
            .build()

        try {
            favoriteMovieStub.withDeadlineAfter(grpcTimeout, TimeUnit.SECONDS).removeClientFavoriteMovie(request)
            logger.info("ClientProtoService removed movie {} from favorites for client {}", movieId, clientId)
        } catch (e: StatusException) {
            when (e.status.code) {
                io.grpc.Status.Code.NOT_FOUND -> logger.warn("Client with id {} or Movie with id {} not found", clientId, movieId)
                io.grpc.Status.Code.UNAVAILABLE -> logger.error("gRPC service unavailable", e)
                io.grpc.Status.Code.PERMISSION_DENIED -> logger.error("Permission denied to remove favorite movie", e)
                else -> logger.error("RPC failed: {}", e.status, e)
            }
        }
    }

    override fun close() {
        clientChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}
