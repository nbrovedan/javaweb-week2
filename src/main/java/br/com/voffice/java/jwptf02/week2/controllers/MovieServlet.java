package br.com.voffice.java.jwptf02.week2.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.voffice.java.jwptf02.week2.entities.Movie;
import br.com.voffice.java.jwptf02.week2.repositories.MovieRepository;
import br.com.voffice.java.jwptf02.week2.repositories.RepositoryFactory;

@WebServlet("/api/movies/*")
public class MovieServlet extends HttpServlet {

	private static final String ENDPOINT_MOVIES = "/api/movies";
	private static final long serialVersionUID = 1L;
	private static final MovieRepository MOVIE_REPOSITORY = RepositoryFactory.getMovieRepository();

	private final ObjectMapper json = new ObjectMapper() {

		private static final long serialVersionUID = 1L;

		{
			this.registerModule(new JavaTimeModule());
			this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		}

	};

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String uri = req.getRequestURI();
			if (uri.endsWith(ENDPOINT_MOVIES)) {
				boolean forceError = Boolean.parseBoolean(req.getParameter("forceError"));
				Collection<Movie> movies = findMovies(forceError);
				String output = json.writeValueAsString(movies);
				resp.setContentType("application/json");
				resp.getWriter().write(output);
			} else if (uri.contains("/api/movies/")) {
				Long id = getLongPathVariable(uri);
				Movie movie = findMovie(id);
				log("found movie with id " + id + ": " + movie);
				resp.setContentType("application/json");
				String output = json.writeValueAsString(movie);
				resp.getWriter().write(output);
			} else {
				throw new IllegalStateException(String.format("%s not supported", uri));
			}
		} catch (Exception e) {
			log("cannot get movies", e);
		}
	}

	/**
	 * curl -v localhost:8080/api/movies
	 */
	public Collection<Movie> findMovies(boolean forceError) {
		if (forceError)
			throw new IllegalArgumentException("error simulated by using parameter forceError");
		return MOVIE_REPOSITORY.findAll();
	}

	public static Long getLongPathVariable(String uri) {
		String[] sections = uri.split("/");
		return sections.length > 0 ? Long.parseLong(sections[sections.length - 1]) : 0L;
	}

	/**
	 * curl -v localhost:8080/api/movies/1
	 */
	public Movie findMovie(Long id) {
		return MOVIE_REPOSITORY.findById(id);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String uri = req.getRequestURI();
			if (uri.endsWith(ENDPOINT_MOVIES)) {
				Movie movie = getMovieFromBody(req);
				Long id = addMovie(movie);
				resp.setStatus(201);
				resp.addHeader("Location", id.toString());
			}
		} catch (Exception e) {
			log("cannot create movie", e);
		}
	}

	private Movie getMovieFromBody(HttpServletRequest req) throws IOException {
		String bodyAsString = IOUtils.toString(req.getInputStream(), "UTF-8");
		JsonNode node = json.readTree(bodyAsString);
		Iterator<String> fieldNames = node.fieldNames();
		String category = null;
		String poster = null;
		double budget = 0.0;
		int rating = 0;
		LocalDate releasedDate = null;
		boolean result = false;
		String title = null;
		while (fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			switch (fieldName) {
			case "category":
				category = node.get("category").asText(null);
				break;
			case "poster":
				poster = node.get("poster").asText(null);
				break;
			case "budget":
				budget = node.get("budget").asDouble(0.0);
				break;
			case "rating":
				rating = node.get("rating").asInt(0);
				break;
			case "releasedDate":
				String releasedDateAsString = node.get("releasedDate").asText(null);
				releasedDate = Objects.nonNull(releasedDateAsString)
						? LocalDate.parse(releasedDateAsString, DateTimeFormatter.ISO_DATE)
						: null;
				break;
			case "result":
				result = node.get("result").asBoolean(false);
				break;
			case "title":
				title = node.get("title").asText(null);
				break;
			default:
				break;
			}
		}
		Movie movie = new Movie();
		movie.setBudget(budget);
		movie.setCategory(category);
		movie.setPoster(poster);
		movie.setRating(rating);
		movie.setReleasedDate(releasedDate);
		movie.setResult(result);
		movie.setTitle(title);
		return movie;
	}

	/**
	 * curl -v -X POST localhost:8080/api/movies -d '{"title":"Movie title"}' -H
	 * 'Content-Type: application/json'
	 *
	 * @throws IOException
	 */
	public Long addMovie(Movie movie) {
		return MOVIE_REPOSITORY.create(movie);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String uri = req.getRequestURI();
			if (uri.contains("/api/movies/")) {
				Long id = getLongPathVariable(uri);
				try {
					removeMovie(id);
					resp.setStatus(204);
				} catch (NotFoundException e) {
					resp.setStatus(404);
				}
			}
		} catch (Exception e) {
			log("cannot remove movie", e);
		}
	}

	/**
	 * curl -v --request DELETE localhost:8080/api/movies/:id
	 */
	public void removeMovie(Long id) {
		if (MOVIE_REPOSITORY.contains(id)) {
			MOVIE_REPOSITORY.remove(id);
		} else {
			throw new NotFoundException(id);
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String uri = req.getRequestURI();
			if (uri.contains("/api/movies/")) {
				Long id = getLongPathVariable(uri);
				Movie movie = getMovieFromBody(req);
				movie.setId(id);
				try {
					editMovie(id, movie);
					resp.setStatus(204);
				} catch (NotFoundException e) {
					resp.setStatus(404);
				}
			}
		} catch (Exception e) {
			log("cannot update movie", e);
		}
	}

	/**
	 * curl -v --request PUT --data '{"title": "Blade Runner 2049", "releasedDate":
	 * "2017-10-06"}' --header 'Content-Type: application/json'
	 * localhost:8080/movies/1
	 */
	public void editMovie(Long id, Movie movie) {
		if (MOVIE_REPOSITORY.contains(id)) {
			movie.setId(id);
			MOVIE_REPOSITORY.update(movie);
		} else {
			throw new NotFoundException(id);
		}
	}

	@SuppressWarnings("serial")
	class NotFoundException extends IllegalArgumentException {
		NotFoundException(Long id) {
			super(String.format("404 Movie %s not found", id));
		}
	}

}
