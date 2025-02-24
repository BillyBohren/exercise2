
import io.grpc.stub.StreamObserver
import com.backend.exercise2.grpc.UserServiceGrpc
import com.backend.exercise2.grpc.UserResponse
import com.backend.exercise2.grpc.ListUsersRequest
import com.backend.exercise2.grpc.GetUserRequest
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.stereotype.Service

@GrpcService
class UserService : UserServiceGrpc.UserServiceImplBase() {
    // Implementación de la llamada normal GetUser
    override fun getUser(request: GetUserRequest, responseObserver: StreamObserver<UserResponse>) {
        val userResponse = UserResponse.newBuilder()
            .setUserId("123")
            .setName("Alice")
            .setEmail("alice@example.com")
            .build()

        responseObserver.onNext(userResponse)
        responseObserver.onCompleted()
    }

    // Implementación del Server Streaming: ListUsers
    override fun listUsers(request: ListUsersRequest, responseObserver: StreamObserver<UserResponse>) {
        val users = listOf(
            UserResponse.newBuilder().setUserId("1").setName("Alice").setEmail("alice@example.com").build(),
            UserResponse.newBuilder().setUserId("2").setName("Bob").setEmail("bob@example.com").build(),
            UserResponse.newBuilder().setUserId("3").setName("Charlie").setEmail("charlie@example.com").build()
        )

        // Simulamos un filtro (aunque en este ejemplo no estamos usando el filtro realmente)
        for (user in users) {
            responseObserver.onNext(user)
        }

        responseObserver.onCompleted() // Señaliza que hemos terminado de enviar datos
    }

}