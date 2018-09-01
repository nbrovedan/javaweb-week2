<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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

<title>Searching movies by year</title>
</head>
<body>
	<div class="container">
		<h1 class="pagetitle">Finding movies by year</h1>
		<form action="practice08-examples-movies" method="post">
			<fieldset class="form-group">
				<label for="movie.field.year">Year: </label> <input type="number" name="movie.field.year" class="form-control" min="0" max="2018" /> <span
					id="movie.help.year"></span>
			</fieldset>
			<fieldset class="form-group">
				<input id="btnSearch" class="btn btn-primary" type="submit"
					value="Search" /> <input id="btnClear" class="btn btn-secondary"
					type="reset" value="Clear" />
			</fieldset>
		</form>
	</div>
</body>
</html>