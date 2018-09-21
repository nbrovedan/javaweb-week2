package br.com.voffice.java.jwptf02.week2.application.repositories;

public abstract class RepositoryFactory {

	private RepositoryFactory(){}
	private static MovieRepository movieRepository = new InMemoryMovieRepository();
	private static MediaFileRepository mediaFileRepository = new InMemoryMediaFileRepository();

	public static final MovieRepository getMovieRepository() {
		return movieRepository;
	}

	public static final MediaFileRepository getMediaFileRepository() {
		return mediaFileRepository;
	}

}
