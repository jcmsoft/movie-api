package com.movie.app.movie_api.services.api

import com.movie.app.movie_api.dto.MovieDTO

interface MovieService {
    fun addMovie(movieDTO: MovieDTO): MovieDTO
    fun updateMovie(movieDTO: MovieDTO): MovieDTO
    fun deleteMovie(id: Int)
    fun getMovieById(id: Int): MovieDTO
    fun getMovieByTitle(title: String): MovieDTO
    fun getAllMovies(): List<MovieDTO>
}