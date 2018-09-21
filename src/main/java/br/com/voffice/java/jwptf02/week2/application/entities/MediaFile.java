package br.com.voffice.java.jwptf02.week2.application.entities;

import java.util.Base64;

public class MediaFile {

	private Long id;
	private final String name;
	private final String filename;
	private final long size;
	private final byte[] contents;

	public MediaFile(String name, String filename, long size, byte[] contents) {
		super();
		this.name = name;
		this.filename = filename;
		this.size = size;
		this.contents = contents;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getFilename() {
		return filename;
	}

	public long getSize() {
		return size;
	}

	public byte[] getContents() {
		return contents;
	}

	public String getDataUrl() {
		return String.format("data:image/png;base64,%s",Base64.getEncoder().encodeToString(this.getContents()));
	}
}
