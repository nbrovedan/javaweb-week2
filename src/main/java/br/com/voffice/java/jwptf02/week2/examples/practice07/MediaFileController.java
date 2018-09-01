package br.com.voffice.java.jwptf02.week2.examples.practice07;

import java.io.File;
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

import br.com.voffice.java.jwptf02.week2.entities.MediaFile;
import br.com.voffice.java.jwptf02.week2.repositories.MediaFileRepository;
import br.com.voffice.java.jwptf02.week2.repositories.RepositoryFactory;

@WebServlet("/practice07-examples-posters")
@MultipartConfig
public class MediaFileController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_DIRECTORY = "servletimages";
	private static final MediaFileRepository repository = RepositoryFactory.getMediaFileRepository();

	private final ObjectMapper json = new ObjectMapper();

	public String getFolderUploadPath() {
		String folderUploadPath = System.getProperty("user.dir") + File.separator + UPLOAD_DIRECTORY;
		System.out.println(folderUploadPath);
		File uploadDir = new File(folderUploadPath);
		if (!uploadDir.exists())
			uploadDir.mkdir();
		return folderUploadPath;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		Collection<MediaFile> files = repository.findAll();
		resp.getWriter().write(json.writeValueAsString(files));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			for (Part part : req.getParts()) {
				receiveMediaFile(req, resp, getFolderUploadPath(), part);
			}
		} catch (Exception e) {
			log("cannot receive media file", e);
		}
	}

	private void receiveMediaFile(HttpServletRequest req, HttpServletResponse resp, String folderUploadPath, Part part)
			throws IOException {
		String title = req.getParameter("title");
		String filename = part.getSubmittedFileName();
		MediaFile mediaFile = new MediaFile(title, filename, part.getSize(), IOUtils.toByteArray(part.getInputStream()));
		repository.create(mediaFile);
		if (Objects.nonNull(filename)) {
			writeAsJson(req, resp, mediaFile);
			writeAsHtml(req, resp, folderUploadPath, part, title, filename, mediaFile);
		}

	}

	private void writeAsHtml(HttpServletRequest req, HttpServletResponse resp, String folderUploadPath, Part part,
			String name, String filename, MediaFile mediaFile) throws IOException {
		if (!"true".equals(req.getParameter("xhr"))) {
			resp.setContentType("text/html");
			resp.getWriter().write("<h1>Posters</h1>");
			String fileUploadPath = folderUploadPath + File.separator + filename;
			System.out.println(folderUploadPath);
			part.write(fileUploadPath);
			resp.getWriter().format("<h2>%s</h2>", name);
			resp.getWriter().format("<img src=\"%s\" alt=\"%s\" width=50 >",
					"/servletimages/" + filename, filename);
			resp.getWriter().format("<div><label>Filesystem:</label><code>%s</code></div>", fileUploadPath);
			resp.getWriter().format("<img src=\"%s\">", mediaFile.getDataUrl());
		}
	}

	private void writeAsJson(HttpServletRequest req, HttpServletResponse resp, MediaFile mediaFile)
			throws IOException {
		if ("true".equals(req.getParameter("xhr"))) {
			resp.setContentType("application/json");
			resp.getWriter().format("%s", json.writeValueAsString(mediaFile));
		}
	}

}
