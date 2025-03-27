package com.movie.app.movie_api.services.impl

import com.movie.app.movie_api.dto.ClientDTO
import com.movie.app.movie_api.services.api.ClientService
import org.springframework.stereotype.Service

@Service
class ClientServiceImpl (private val protoService: ClientProtoService) : ClientService {
    override fun addClient(clientDTO: ClientDTO): ClientDTO {
        return clientDTO
    }

    override fun getClientById(id: Int): ClientDTO {
        return protoService.getClientById(id) ?: throw RuntimeException("Client with id $id not found")
    }

    override fun getAllClients(): List<ClientDTO> {
        return (1..10).map { ClientDTO(it, "Client $it", "Last name $it", "email$it@test.com") }
    }

    override fun updateClient(clientDTO: ClientDTO): ClientDTO {
        return clientDTO
    }

    override fun deleteClient(id: Int) {
        println("Client $id deleted")
    }

    override fun addFavoriteMovie(clientId: Int, movieId: Int) {
        println("Movie $movieId added to favorites")
    }
}