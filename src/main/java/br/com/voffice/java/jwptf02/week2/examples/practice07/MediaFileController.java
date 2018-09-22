package br.com.voffice.java.jwptf02.week2.examples.practice07;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.voffice.java.jwptf02.week2.application.entities.MediaFile;
import br.com.voffice.java.jwptf02.week2.application.repositories.MediaFileRepository;
import br.com.voffice.java.jwptf02.week2.application.repositories.RepositoryFactory;

@WebServlet("/practice07-examples-posters")
@MultipartConfig
public class MediaFileController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final MediaFileRepository repository = RepositoryFactory.getMediaFileRepository();

	private final ObjectMapper json = new ObjectMapper();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			resp.setContentType("application/json");
			Collection<MediaFile> files = repository.findAll();
			resp.getWriter().write(json.writeValueAsString(files));
		} catch (Exception e) {
			log("cannot get media files", e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			for (Part part : req.getParts()) {
				receiveMediaFile(req, resp, part);
			}
		} catch (Exception e) {
			log("cannot receive media file", e);
		}
	}

	private void receiveMediaFile(HttpServletRequest req, HttpServletResponse resp, Part part) throws IOException {
		String title = req.getParameter("title");
		String filename = part.getSubmittedFileName();
		MediaFile mediaFile = new MediaFile(title, filename, part.getSize(),
				IOUtils.toByteArray(part.getInputStream()));
		repository.create(mediaFile);
		if (Objects.nonNull(filename)) {
			writeAsJson(req, resp, mediaFile);
			writeAsHtml(req, resp, part, title, filename, mediaFile);
		}

	}

	private void writeAsHtml(HttpServletRequest req, HttpServletResponse resp, Part part, String name, String filename,
			MediaFile mediaFile) throws IOException {
		if (!"true".equals(req.getParameter("xhr"))) {
			resp.setContentType("text/html");
			resp.getWriter().write("<h1>Posters</h1>");
			resp.getWriter().format("<h2>%s</h2>", name);
			resp.getWriter().format("<img src=\"%s\" alt=\"%s\" >", mediaFile.getDataUrl(), filename);
		}
	}

	private void writeAsJson(HttpServletRequest req, HttpServletResponse resp, MediaFile mediaFile) throws IOException {
		if ("true".equals(req.getParameter("xhr"))) {
			resp.setContentType("application/json");
			resp.getWriter().format("%s", json.writeValueAsString(mediaFile));
		}
	}

}
