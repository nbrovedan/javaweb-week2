package br.com.voffice.java.jwptf02.week2.examples.practice06;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.voffice.java.jwptf02.week2.application.entities.Movie;
import br.com.voffice.java.jwptf02.week2.application.repositories.MovieRepository;
import br.com.voffice.java.jwptf02.week2.application.repositories.RepositoryFactory;

@WebServlet("/practice06-examples-movies")
public class MovieController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String CATEGORY_BEST_PICTURE = "Best Picture";
	private static final Movie shape = new Movie(null, "Shape of Water", LocalDate.of(2017, 12, 22), 19_400_000d, "https://drraa3ej68s2c.cloudfront.net/wp-content/uploads/2017/12/12163133/87954c22e6e3783117f13feadf7e9681f463b7011a91c7af2ebd1a962d20aa53-195x195.jpg", 74, CATEGORY_BEST_PICTURE, true);
	private static final MovieRepository movieRepository = RepositoryFactory.getMovieRepository();


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Movie movie = getMovie(req);
		req.setAttribute("movie", movie);
		req.getRequestDispatcher("/practice06-examples-movies-view").forward(req, resp);
	}


	private Movie getMovie(HttpServletRequest req) {
		String parameter = req.getParameter("movie");
		Movie movie = shape;
		if (Objects.nonNull(parameter)) {
			 Movie found = movieRepository.findById(Long.parseLong(parameter));
			 if (Objects.nonNull(found)) {
				 movie = found;
			 }
		}
		return movie;
	}

}
