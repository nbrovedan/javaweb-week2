package br.com.voffice.java.jwptf02.week2.examples.practice08;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.voffice.java.jwptf02.week2.entities.Movie;
import br.com.voffice.java.jwptf02.week2.repositories.MovieRepository;
import br.com.voffice.java.jwptf02.week2.repositories.RepositoryFactory;

@WebServlet("/practice08-examples-movies")
public class MovieController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final MovieRepository movieRepository = RepositoryFactory.getMovieRepository();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/practices/practice08/examples/movies-search.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int year = Optional.ofNullable(req.getParameter("year")).map(Integer::parseInt).orElse(LocalDate.now().getYear());
		Collection<Movie> movies = movieRepository.findByYear(year);
		req.setAttribute("movies", movies);
		req.setAttribute("year", year);
		req.setAttribute("notFoundMovies", movies.isEmpty());
		req.getRequestDispatcher("/practices/practice08/examples/movies-results.jsp").forward(req, resp);
	}
}
