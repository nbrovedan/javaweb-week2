package br.com.voffice.java.jwptf02.week2.application.repositories;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import br.com.voffice.java.jwptf02.week2.application.entities.MediaFile;

public class InMemoryMediaFileRepository implements MediaFileRepository {
	private final Map<Long, MediaFile> database = new HashMap<>();

	@Override
	public Collection<MediaFile> findAll() {
		return database.values();
	}

	@Override
	public Long create(MediaFile file) {
		long id = System.currentTimeMillis();
		file.setId(id);
		database.put(id, file);
		return id;
	}

	@Override
	public MediaFile findById(Long id) {
		return database.get(id);
	}

	@Override
	public void update(MediaFile file) {
		database.put(file.getId(), file);
	}

	@Override
	public void remove(Long id) {
		if (database.containsKey(id)) {
			database.remove(id);
		} else {
			throw new IllegalArgumentException("media file with id "+id+" not found");
		}
	}

	@Override
	public boolean contains(Long id) {
		return findById(id) != null;
	}

}
