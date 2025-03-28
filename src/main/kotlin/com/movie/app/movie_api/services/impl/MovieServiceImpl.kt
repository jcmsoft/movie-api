package com.movie.app.movie_api.services.impl

import com.movie.app.movie_api.dto.MovieDTO
import com.movie.app.movie_api.services.api.MovieService
import org.springframework.stereotype.Service

@Service
class MovieServiceImpl (private val protoService: MovieProtoService): MovieService {
    override fun addMovie(movieDTO: MovieDTO): MovieDTO {
        return protoService.addMovie(movieDTO)?: throw RuntimeException("Movie with id ${movieDTO.id} already exists")
    }

    override fun updateMovie(movieDTO: MovieDTO): MovieDTO {
        return protoService.updateMovie(movieDTO) ?: throw RuntimeException("Movie with id ${movieDTO.id} not found")
    }

    override fun deleteMovie(id: Int) {
        protoService.deleteMovie(id)
    }

    override fun getMovieById(id: Int): MovieDTO {
        return protoService.getMovieById(id) ?: throw RuntimeException("Movie with id $id not found")
    }

    override fun getAllMovies(): List<MovieDTO> {
        return protoService.getAllMovies()
    }

    override fun getMovieByTitle(title: String): MovieDTO {
        return protoService.getMovieByTitle(title) ?: throw RuntimeException("Movie with title $title not found")
    }
}