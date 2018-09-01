package br.com.voffice.java.jwptf02.week2.repositories;

import java.util.Collection;

import br.com.voffice.java.jwptf02.week2.entities.MediaFile;

public interface MediaFileRepository {

	Collection<MediaFile> findAll();

	Long create(MediaFile file);

	MediaFile findById(Long id);

	void update(MediaFile file);

	void remove(Long id);

	boolean contains(Long id);

}
