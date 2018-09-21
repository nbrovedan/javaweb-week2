package br.com.voffice.java.jwptf02.week2.examples.practice06;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.voffice.java.jwptf02.week2.application.entities.Movie;

@WebServlet("/practice06-examples-movies-view")
public class MovieView extends HttpServlet {

	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Movie movie = (Movie) req.getAttribute("movie");
		writeAsHtml(resp, movie);
	}

	private void writeAsHtml(HttpServletResponse resp, Movie movie) throws IOException {
			resp.setContentType("text/html");
			PrintWriter writer = resp.getWriter();
			writer.write(getHTMLCard(movie));
	}

	public String getHTMLCard(Movie movie) {
		StringBuilder sb = new StringBuilder();
		sb.append("<link rel=\"stylesheet\"\n" +
				"	href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css\"\n" +
				"	integrity=\"sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO\"\n" +
				"	crossorigin=\"anonymous\">\n" +
				"");
		sb.append("<div class=container>");
		sb.append("<div class=card style=\"width: 18rem;\">");
		sb.append(String.format("<h5 class=\"card-title\">%s</h5>", movie.getTitle()));
		sb.append(String.format("<img src=\"%s\" class=\"card-img-top\">", movie.getPoster()));
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}

}
