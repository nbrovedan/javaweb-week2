<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<!doctype html>
<html lang="en">
<head>

<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
	integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
	crossorigin="anonymous">

<title>Movies - Year: ${year}</title>
</head>
<body>
	<div class="container">
		<h1 class="pagetitle">Movies - Year: ${year}</h1>
		<h2 style="color:red">Titulo 2</h2>
		<t:titulo2 text="Servlets e JSP"></t:titulo2>

		<c:if test="${notFoundMovies}">
			<div class="alert alert-danger">No movies found</div>
		</c:if>
		<c:forEach var="_movie" items="${movies}">
			<div class="card" style="width: 18rem;">
				<img class="card-img-top" src="${_movie.poster}"
					alt="${_movie.title}">
				<div class="card-body">
					<h5 class="card-title">${_movie.title}</h5>
					<p class="card-text">${_movie.poster}</p>
					<a href="${_movie.poster}" class="btn btn-primary">See poster</a>
				</div>
			</div>
		</c:forEach>
	</div>
</body>
</html>