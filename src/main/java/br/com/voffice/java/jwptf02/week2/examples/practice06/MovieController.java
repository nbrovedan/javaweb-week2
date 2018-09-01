package br.com.voffice.java.jwptf02.week2.examples.practice06;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.voffice.java.jwptf02.week2.entities.Movie;

@WebServlet("/practice06-examples-movies")
public class MovieController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String CATEGORY_BEST_PICTURE = "Best Picture";
	private static final Movie movie = new Movie(null, "Shape of Water", LocalDate.of(2017, 12, 22), 19_400_000d, "https://drraa3ej68s2c.cloudfront.net/wp-content/uploads/2017/12/12163133/87954c22e6e3783117f13feadf7e9681f463b7011a91c7af2ebd1a962d20aa53-195x195.jpg", 74, CATEGORY_BEST_PICTURE, true);


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		writeAsHtml(resp);
	}

	private void writeAsHtml(HttpServletResponse resp) throws IOException {
			resp.setContentType("text/html");
			PrintWriter writer = resp.getWriter();
			writer.write(getHTMLCard());
	}

	public String getHTMLCard() {
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
