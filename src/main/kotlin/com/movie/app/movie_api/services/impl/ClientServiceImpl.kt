package com.movie.app.movie_api.services.impl

import com.movie.app.movie_api.dto.ClientDTO
import com.movie.app.movie_api.services.api.ClientService
import org.springframework.stereotype.Service

@Service
class ClientServiceImpl (private val protoService: ClientProtoService) : ClientService {
    override fun addClient(clientDTO: ClientDTO): ClientDTO {
        return protoService.addClient(clientDTO)?: throw RuntimeException("Client not added")
    }

    override fun getClientById(id: Int): ClientDTO {
        return protoService.getClientById(id) ?: throw RuntimeException("Client with id $id not found")
    }

    override fun getAllClients(): List<ClientDTO> {
        return protoService.getAllClients()
    }

    override fun updateClient(clientDTO: ClientDTO): ClientDTO {
        return protoService.updateClient(clientDTO) ?: throw RuntimeException("Client with id ${clientDTO.id} not found")
    }

    override fun deleteClient(id: Int) {
        protoService.deleteClient(id)
    }

    override fun addFavoriteMovie(clientId: Int, movieId: Int) {
        protoService.addFavoriteMovie(clientId, movieId)
    }

    override fun removeFavoriteMovie(clientId: Int, movieId: Int) {
        protoService.removeFavoriteMovie(clientId, movieId)
    }
}