package br.com.voffice.java.jwptf02.week2.examples.practice05;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.voffice.java.jwptf02.week2.application.entities.Movie;
import br.com.voffice.java.jwptf02.week2.application.repositories.MovieRepository;
import br.com.voffice.java.jwptf02.week2.application.repositories.RepositoryFactory;

@WebServlet("/practice05-examples-movies")
public class MovieController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String CATEGORY_BEST_PICTURE = "Best Picture";
	private static final Movie phantom = new Movie(null, "Phantom Thread", LocalDate.of(2017, 12, 11), 35_000_000d,
			"https://image.tmdb.org/t/p/w1280/yZ8j8xKk2xQ1d88hB8YG6LZfRQj.jpg", 73, CATEGORY_BEST_PICTURE, false);
	private static final MovieRepository movieRepository = RepositoryFactory.getMovieRepository();

	private static final ObjectMapper JSON = new ObjectMapper() {

		private static final long serialVersionUID = 1L;

		{
			this.registerModule(new JavaTimeModule());
			this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		}

	};

	private static final XmlMapper XML = new XmlMapper() {

		private static final long serialVersionUID = 1L;

		{
			this.registerModule(new JavaTimeModule());
			this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		}

	};

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String format = req.getParameter("format");
		format = Optional.ofNullable(format).filter(f -> Arrays.asList("json", "xml").contains(f)).orElse("json");
		Movie movie = getMovie(req);
		try {
			writeAsJson(resp, format, movie);
			writeAsXml(resp, format, movie);
		} catch (Exception e) {
			log("erro ao obter filmes", e);
		}
	}

	private Movie getMovie(HttpServletRequest req) {
		String parameter = req.getParameter("movie");
		Movie movie = phantom;
		if (Objects.nonNull(parameter)) {
			 Movie found = movieRepository.findById(Long.parseLong(parameter));
			 if (Objects.nonNull(found)) {
				 movie = found;
			 }
		}
		return movie;
	}

	private void writeAsXml(HttpServletResponse resp, String format, Movie movie) throws IOException {
		if ("xml".equals(format)) {
			resp.setContentType("text/xml");
			resp.getWriter().write(XML.writeValueAsString(movie));

		}
	}

	private void writeAsJson(HttpServletResponse resp, String format, Movie movie) throws IOException {
		if ("json".equals(format)) {
			resp.setContentType("application/json");
			resp.getWriter().write(JSON.writeValueAsString(movie));
		}
	}

}
