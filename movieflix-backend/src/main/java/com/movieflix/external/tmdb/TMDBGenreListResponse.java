package com.movieflix.external.tmdb;

import java.util.List;

public class TMDBGenreListResponse {
    private List<TMDBGenre> genres;

    public List<TMDBGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<TMDBGenre> genres) {
        this.genres = genres;
    }
}
