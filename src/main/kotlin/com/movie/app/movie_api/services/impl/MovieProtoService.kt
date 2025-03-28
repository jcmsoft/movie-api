package com.movie.app.movie_api.services.impl

import com.movie.app.movie_api.dto.MovieDTO
import io.grpc.ManagedChannel
import io.grpc.StatusException
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import services.MovieProtoServiceGrpcKt.MovieProtoServiceCoroutineStub
import java.io.Closeable
import java.util.concurrent.TimeUnit

@Service
class MovieProtoService (
    private val channel: ManagedChannel,
    private val movieProtoServiceCoroutineStub: MovieProtoServiceCoroutineStub,
    private val grpcTimeout: Long
) : Closeable {
    private val logger = LoggerFactory.getLogger(ClientProtoService::class.java)

    fun addMovie(movieDTO: MovieDTO): MovieDTO? = runBlocking {
        val movieRequest = services.Services.MovieRequest.newBuilder()
            .setId(movieDTO.id)
            .setTitle(movieDTO.title)
            .setPrice(movieDTO.price.toFloat())
            .build()

        try {
            val result = movieProtoServiceCoroutineStub.withDeadlineAfter(grpcTimeout, TimeUnit.SECONDS).addMovie(movieRequest)
            return@runBlocking MovieDTO(result.id, result.title, result.price.toDouble())
        } catch (e: StatusException){
            when (e.status.code) {
                io.grpc.Status.Code.UNAVAILABLE -> logger.error("gRPC service unavailable", e)
                io.grpc.Status.Code.PERMISSION_DENIED -> logger.error("Permission denied to add client", e)
                else -> logger.error("RPC failed: {}", e.status, e)
            }
            return@runBlocking null
        }
    }

    fun updateMovie(movieDTO: MovieDTO): MovieDTO? = runBlocking {
        val movieRequest = services.Services.MovieRequest.newBuilder()
            .setId(movieDTO.id)
            .setTitle(movieDTO.title)
            .setPrice(movieDTO.price.toFloat())
            .build()

        try {
            val result = movieProtoServiceCoroutineStub.withDeadlineAfter(grpcTimeout, TimeUnit.SECONDS).updateMovie(movieRequest)
            return@runBlocking MovieDTO(result.id, result.title, result.price.toDouble())
        } catch (e: StatusException){
            when (e.status.code) {
                io.grpc.Status.Code.UNAVAILABLE -> logger.error("gRPC service unavailable", e)
                io.grpc.Status.Code.PERMISSION_DENIED -> logger.error("Permission denied to add client", e)
                else -> logger.error("RPC failed: {}", e.status, e)
            }
            return@runBlocking null
        }
    }

    fun deleteMovie(id: Int) = runBlocking {
        val idRequest = services.Services.IdRequest.newBuilder()
            .setId(id)
            .build()

        try {
            movieProtoServiceCoroutineStub.withDeadlineAfter(grpcTimeout, TimeUnit.SECONDS).deleteMovie(idRequest)
            logger.info("MovieProtoService deleted movie with id {}", id)
            return@runBlocking
        } catch (e: StatusException){
            when (e.status.code) {
                io.grpc.Status.Code.UNAVAILABLE -> logger.error("gRPC service unavailable", e)
                io.grpc.Status.Code.PERMISSION_DENIED -> logger.error("Permission denied to add client", e)
                else -> logger.error("RPC failed: {}", e.status, e)
            }
            return@runBlocking
        }
    }

    fun getMovieById(id: Int): MovieDTO? = runBlocking {
        val idRequest = services.Services.IdRequest.newBuilder()
            .setId(id)
            .build()

        try {
            val result = movieProtoServiceCoroutineStub.withDeadlineAfter(grpcTimeout, TimeUnit.SECONDS).getMovieById(idRequest)
            return@runBlocking MovieDTO(result.id, result.title, result.price.toDouble())
        } catch (e: StatusException){
            when (e.status.code) {
                io.grpc.Status.Code.UNAVAILABLE -> logger.error("gRPC service unavailable", e)
                io.grpc.Status.Code.PERMISSION_DENIED -> logger.error("Permission denied to add client", e)
                else -> logger.error("RPC failed: {}", e.status, e)
            }
            return@runBlocking null
        }
    }

    fun getMovieByTitle(title: String): MovieDTO? = runBlocking {
        val titleRequest = services.Services.TitleRequest.newBuilder()
            .setTitle(title)
            .build()

        try {
            val result = movieProtoServiceCoroutineStub.withDeadlineAfter(grpcTimeout, TimeUnit.SECONDS).getMovieByTitle(titleRequest)
            return@runBlocking MovieDTO(result.id, result.title, result.price.toDouble())
        } catch (e: StatusException){
            when (e.status.code) {
                io.grpc.Status.Code.UNAVAILABLE -> logger.error("gRPC service unavailable", e)
                io.grpc.Status.Code.PERMISSION_DENIED -> logger.error("Permission denied to add client", e)
                else -> logger.error("RPC failed: {}", e.status, e)
            }
            return@runBlocking null
        }
    }

    fun getAllMovies(): List<MovieDTO> = runBlocking {
        val request = services.Services.EmptyRequest.newBuilder().build()
        try {
            val response = movieProtoServiceCoroutineStub.withDeadlineAfter(grpcTimeout, TimeUnit.SECONDS).getAllMovies(request)
            logger.info("All movies returned {}", response)
            return@runBlocking response.map { MovieDTO(it.id, it.title, it.price.toDouble()) }.toList()
        } catch (e: StatusException){
            when (e.status.code) {
                io.grpc.Status.Code.UNAVAILABLE -> logger.error("gRPC service unavailable", e)
                io.grpc.Status.Code.PERMISSION_DENIED -> logger.error("Permission denied to add client", e)
                else -> logger.error("RPC failed: {}", e.status, e)
            }
            return@runBlocking emptyList()
        }
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}