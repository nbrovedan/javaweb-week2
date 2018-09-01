package br.com.voffice.java.jwptf02.week2.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

@WebServlet("/files")
@MultipartConfig
public class MediaFileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final MediaFileRepository repository = RepositoryFactory.getMediaFileRepository();

	private final ObjectMapper json = new ObjectMapper();


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
				receiveMediaFile(req, resp, part);
			}
		} catch (Exception e) {
			log("cannot receive media file", e);
		}
	}

	private void receiveMediaFile(HttpServletRequest req, HttpServletResponse resp, Part part)
			throws IOException {
		String title = req.getParameter("title");
		String filename = part.getSubmittedFileName();
		MediaFile mediaFile = new MediaFile(title, filename, part.getSize(), IOUtils.toByteArray(part.getInputStream()));
		repository.create(mediaFile);
		if (Objects.nonNull(filename)) {
			writeAsJson(req, resp, mediaFile);
			writeAsHtml(req, resp, part, title, filename, mediaFile);
			//download(req, resp, mediaFile.getFilename(), new ByteArrayInputStream(mediaFile.getContents()));
		}

	}

	private void writeAsHtml(HttpServletRequest req, HttpServletResponse resp, Part part,
			String name, String filename, MediaFile mediaFile) throws IOException {
		if (!"true".equals(req.getParameter("xhr"))) {
			resp.setContentType("text/html");
			resp.getWriter().write("<h1>Posters</h1>");
			resp.getWriter().format("<h2>%s</h2>", name);
			resp.getWriter().format("<img src=\"%s\" alt=\"%s\" >",
					mediaFile.getDataUrl(), filename);
		}
	}

	private void writeAsJson(HttpServletRequest req, HttpServletResponse resp, MediaFile mediaFile)
			throws IOException {
		if ("true".equals(req.getParameter("xhr"))) {
			resp.setContentType("application/json");
			resp.getWriter().format("%s", new ObjectMapper().writeValueAsString(mediaFile));
		}
	}

	private void download(HttpServletRequest req, HttpServletResponse resp, String filename, InputStream in) {
		resp.setHeader("Content-disposition", "attachment; filename="+filename);

        try(
          OutputStream out = resp.getOutputStream()) {

            byte[] buffer = new byte[2048];

            int numBytesRead;
            while ((numBytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, numBytesRead);
            }
        } catch (IOException e) {
			log("cannot download file",e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void download(HttpServletRequest req, HttpServletResponse resp, String filename, String downloadFolder) {
		InputStream in = req.getServletContext().getResourceAsStream(downloadFolder+"/"+filename);
        download(req, resp, filename, in);
	}

}
