package com.movie.app.movie_api.services.api

import com.movie.app.movie_api.dto.MovieDTO

interface MovieService {
    fun addMovie(movieDTO: MovieDTO): MovieDTO
    fun getMovieById(id: Int): MovieDTO
    fun getAllMovies(): List<MovieDTO>
    fun updateMovie(id: Int, movieDTO: MovieDTO): MovieDTO
    fun deleteMovie(id: Int)
}