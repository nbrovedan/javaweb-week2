package br.com.voffice.java.jwptf02.week2.repositories;

import java.util.Collection;

import br.com.voffice.java.jwptf02.week2.entities.Movie;

public interface MovieRepository {

	Collection<Movie> findAll();

	Long create(Movie movie);

	Movie findById(Long id);

	void update(Movie movie);

	void remove(Long id);

	boolean contains(Long id);

	Collection<Movie>  findByYear(int year);

}
