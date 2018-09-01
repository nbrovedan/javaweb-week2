package br.com.voffice.java.jwptf02.week2.repositories;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.voffice.java.jwptf02.week2.entities.Movie;


public class InMemoryMovieRepository implements MovieRepository {

	private final Map<Long, Movie> database = new HashMap<>();

	@Override
	public Collection<Movie> findAll() {
		return database.values();
	}

	@Override
	public Long create(Movie movie) {
		long id = System.currentTimeMillis();
		movie.setId(id);
		database.put(id, movie);
		return id;
	}

	@Override
	public Movie findById(Long id) {
		return database.get(id);
	}

	@Override
	public void update(Movie movie) {
		database.put(movie.getId(), movie);
	}

	@Override
	public void remove(Long id) {
		if (database.containsKey(id)) {
			database.remove(id);
		} else {
			throw new IllegalArgumentException("movie with id "+id+" not found");
		}
	}

	@Override
	public boolean contains(Long id) {
		return findById(id) != null;
	}

	@Override
	public Collection<Movie> findByYear(int year) {
		LocalDate start = LocalDate.of(year, 1, 1);
		LocalDate end = LocalDate.of(year, 12, 31);
		return findAll().stream().filter( m -> {
			return m.getReleasedDate() != null && m.getReleasedDate().isAfter(start) && m.getReleasedDate().isBefore(end);
		}).collect(Collectors.toList());
	}

}
