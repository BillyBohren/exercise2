package com.backend.exercise2.grpc

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcClient {

    private val logger = LoggerFactory.getLogger(GrpcClient::class.java)

    @Bean
    fun runGrpcClient(): ApplicationRunner {
        return ApplicationRunner {
            val channel: ManagedChannel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build()

            val stub = UserServiceGrpc.newStub(channel)

            logger.info("=== Ejecutando Cliente gRPC Streaming ===")

            val request = ListUsersRequest.newBuilder().setFilter("all").build()

            val responseObserver = object : StreamObserver<UserResponse> {
                override fun onNext(response: UserResponse) {
                    logger.info("Usuario recibido: ${response.userId} - ${response.name} - ${response.email}")
                }

                override fun onError(t: Throwable) {
                    logger.error("Error en el stream: ${t.message}")
                }

                override fun onCompleted() {
                    logger.info("=== Stream finalizado ===")
                    channel.shutdown()
                }
            }

            stub.listUsers(request, responseObserver)
        }
    }
}