package com.movie.app.movie_api.config

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import services.ClientProtoServiceGrpcKt.ClientProtoServiceCoroutineStub

@Configuration
class GrpcClientConfig {

    @Value("\${grpc.client.host:localhost}")
    private lateinit var host: String

    @Value("\${grpc.client.port:50051}")
    private var port: Int = 50051
    
    @Value("\${grpc.client.timeout:10}")
    private var timeoutSeconds: Long = 10

    @Bean
    fun clientChannel(): ManagedChannel {
        return ManagedChannelBuilder.forAddress(host, port)
            .usePlaintext()
            .build()
    }
    
    @Bean
    fun clientProtoServiceStub(clientChannel: ManagedChannel): ClientProtoServiceCoroutineStub {
        return ClientProtoServiceCoroutineStub(clientChannel)
    }
    
    @Bean
    fun grpcTimeout(): Long {
        return timeoutSeconds
    }
}