package com.movie.app.movie_api.rest

import com.movie.app.movie_api.dto.MovieDTO
import com.movie.app.movie_api.services.api.MovieService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController("/movies")
class MovieResource (
    private val movieService: MovieService
) {
    @PostMapping("movies/add")
    fun addMovie(@RequestBody movieDTO: MovieDTO): ResponseEntity<MovieDTO> {

        return try {
            ResponseEntity(movieService.addMovie(movieDTO), HttpStatus.CREATED)
        }catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }

    }

    @GetMapping("/movies/{id}")
    fun getMovieById(@PathVariable id: Int): ResponseEntity<MovieDTO> {
        return try {
            ResponseEntity(movieService.getMovieById(id), HttpStatus.OK)
        }catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/movies")
    fun getAllMovies(): ResponseEntity<List<MovieDTO>> {
        return try {
            ResponseEntity(movieService.getAllMovies(), HttpStatus.OK)
        }catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/movies")
    fun updateMovie(@RequestBody movieDTO: MovieDTO): ResponseEntity<MovieDTO> {
        return try {
            ResponseEntity(movieService.updateMovie(movieDTO.id, movieDTO), HttpStatus.OK)
        }catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("movies/{id}")
    fun deleteMovie(@PathVariable id: Int): ResponseEntity<Unit> {
        return try {
            movieService.deleteMovie(id)
            ResponseEntity(HttpStatus.OK)
        }catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }
}