package com.movie.app.movie_api.services.api

import com.movie.app.movie_api.dto.ClientDTO
import com.movie.app.movie_api.dto.MovieDTO

interface ClientService {

    fun addClient(clientDTO: ClientDTO): ClientDTO
    fun getClientById(id: Int): ClientDTO
    fun getAllClients(): List<ClientDTO>
    fun updateClient(clientDTO: ClientDTO): ClientDTO
    fun deleteClient(id: Int)
    fun addFavoriteMovie(clientId: Int, movieId: Int)
    fun removeFavoriteMovie(clientId: Int, movieId: Int)
}