package com.movie.app.movie_api.rest

import com.movie.app.movie_api.dto.ClientDTO
import com.movie.app.movie_api.services.api.ClientService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("/clients")
class ClientResource (
    private val clientService: ClientService
) {
    @PostMapping("clients/add")
    fun  addClient(@RequestBody clientDTO: ClientDTO): ResponseEntity<ClientDTO> {
        return try {
            ResponseEntity(clientService.addClient(clientDTO), HttpStatus.CREATED)
        }catch (ex: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("clients/{id}")
    fun getClientById(@PathVariable id: Int): ResponseEntity<ClientDTO> {
        return try {
            ResponseEntity(clientService.getClientById(id), HttpStatus.OK)
        }catch (ex: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("clients")
    fun getAllClients(): ResponseEntity<List<ClientDTO>> {
        return try {
            ResponseEntity(clientService.getAllClients(), HttpStatus.OK)
        }catch (ex: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("clients")
    fun updateClient(@RequestBody clientDTO: ClientDTO): ResponseEntity<ClientDTO> {
        return try {
            ResponseEntity(clientService.updateClient(clientDTO), HttpStatus.OK)
        }catch (ex: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("clients/{id}")
    fun deleteClient(@PathVariable id: Int): ResponseEntity<Unit> {
        return try {
            clientService.deleteClient(id)
            ResponseEntity(HttpStatus.OK)
        }catch (ex: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/{id}/favorite-movie")
    fun addFavoriteMovie(@PathVariable id: Int, @RequestBody movieId: Int): ResponseEntity<Unit> {
        return try {
            clientService.addFavoriteMovie(id, movieId)
            ResponseEntity(HttpStatus.OK)
        }catch (ex: Exception) {
            ResponseEntity.notFound().build()
        }
    }
}