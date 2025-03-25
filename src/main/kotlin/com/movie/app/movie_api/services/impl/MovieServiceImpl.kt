package com.movie.app.movie_api.services.impl

import com.movie.app.movie_api.dto.MovieDTO
import com.movie.app.movie_api.services.api.MovieService
import org.springframework.stereotype.Service

@Service
class MovieServiceImpl : MovieService {
    override fun addMovie(movieDTO: MovieDTO): MovieDTO {
        return movieDTO
    }

    override fun getMovieById(id: Int): MovieDTO {
        return MovieDTO(id, "Movie $id", 12.0)
    }

    override fun getAllMovies(): List<MovieDTO> {
        return (1..10).map { MovieDTO(it, "Movie $it", 25.0) }
    }

    override fun updateMovie(id: Int, movieDTO: MovieDTO): MovieDTO {
        return movieDTO
    }

    override fun deleteMovie(id: Int) {
        println("Movie $id deleted")
    }
}