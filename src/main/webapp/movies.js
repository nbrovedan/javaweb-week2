
	var repository = [];
	var $$ = (id) => document.getElementById(id);
	var $$value = (id) => $$(id).value;
	var $$selected = (id) => $$(id).selected;
	var $$checkedValue = (ids) => {
		var checkeds = ids.filter((_id) => $$(_id).checked);
		return checkeds.length == 1?$$(checkeds[0]).value:"";
	};

	var getMoviesSK = () => {
		var xhr = new XMLHttpRequest();
		var url = uriPrefix+"/api/movies?forceError=false";
		xhr.open("GET", url, true);
		xhr.onload = () => {console.log(JSON.parse(xhr.responseText));}
		xhr.send();
	}
	var addMovieSK = () => {
		var xhr = new XMLHttpRequest();
		var url = uriPrefix+"/api/movies";
		var data = {title: "Avengers: Infinity War", releasedDate:"2018-04-17", poster: "https://image.tmdb.org/t/p/w1280/rkHe0BfOo1f5N2q6rxgdYac7Zf6.jpg", rating: 83, budget: 300000000, result: false};
		xhr.open("POST", url, true);
		xhr.setRequestHeader("Content-Type","application/json");
		xhr.onload = () => {console.log(JSON.parse(xhr.responseText));}
		xhr.send(JSON.stringify(data));
	}
	var removeMovieSK = (id) => {
		if (!id) return;
		var xhr = new XMLHttpRequest();
		var url = uriPrefix+"/api/movies/"+id;
		xhr.open("DELETE", url, true);
		xhr.onload = () => {console.log(xhr.status);}
		xhr.send();
	}
	var getMovies = () => {
		var xhr = new XMLHttpRequest();
		var url = uriPrefix+"/api/movies?forceError=false";
		xhr.open("GET", url, true);
		xhr.onload = () => {var loaded = JSON.parse(xhr.responseText); repository = loaded; showMovies();}
		xhr.send();
	}
	var addMovie = (movie) => {
		var xhr = new XMLHttpRequest();
		var url = uriPrefix+"/api/movies";
		xhr.open("POST", url, true);
		xhr.setRequestHeader("Content-Type","application/json");
		xhr.onload = () => {console.log(xhr.responseText); getMovies();}
		xhr.send(JSON.stringify(movie));
	}
	var removeMovie = (id) => {
		if (!id) return;
		var xhr = new XMLHttpRequest();
		var url = uriPrefix+"/api/movies/"+id;
		xhr.open("DELETE", url, true);
		xhr.onload = () => {console.log(xhr.status); getMovies();}
		xhr.send();
	}
	var updateMovie = (movie) => {
		if (!movie.id) return;
		var xhr = new XMLHttpRequest();
		var url = uriPrefix+"/api/movies/"+movie.id;
		xhr.open("PUT", url, true);
		xhr.setRequestHeader("Content-Type","application/json");
		xhr.onload = () => {console.log(xhr.status); getMovies();}
		xhr.send(JSON.stringify(movie));
	}
	var findMovieById = (id) => {
		if (!id) return;
		var xhr = new XMLHttpRequest();
		var url = uriPrefix+"/api/movies/"+id;
		xhr.open("GET", url, true);
		xhr.onload = () => {var loaded = JSON.parse(xhr.responseText); console.log(loaded);movieToForm(loaded);}
		xhr.send();
	}
	var movieFromForm = () =>{
		var id = $$value("movie.field.id");
		var title = $$value("movie.field.title");
		var releasedDate = $$value("movie.field.releasedDate");
		var budget = $$value("movie.field.budget");
		var poster = $$value("movie.field.poster");
		var rating = $$value("movie.field.rating");
		var category = $$value("movie.field.category");
		category = document.getElementById("movie.field.category").value;
		var result = $$checkedValue(["movie.field.result.winner","movie.field.result.nominee"]);
		result = $$("movie.field.result.winner").value == result;
		return {id: id, title: title, releasedDate: releasedDate, budget: budget, poster: poster, rating: rating, category: category, result: result};
	};
	var clearForm = () =>{
		$$("movie.field.id").value=null;
		$$("movie.field.title").value=null;
		$$("movie.field.releasedDate").value=null;
		$$("movie.field.budget").value=null;
		$$("movie.field.poster").value=null;
		$$("movie.field.rating").value=null;
		$$("movie.field.category").value=null;
		$$("movie.field.result.nominee").checked=null;
		$$("movie.field.result.winner").checked=null;
	};
	var movieToForm = (movie) =>{
		$$("movie.field.id").value=movie.id;
		$$("movie.field.title").value=movie.title;
		$$("movie.field.releasedDate").value=movie.releasedDate;
		$$("movie.field.budget").value=movie.budget;
		$$("movie.field.poster").value=movie.poster;
		$$("movie.field.rating").value=movie.rating;
		$$("movie.field.category").value=movie.category;
		document.getElementById("movie.field.category").value=movie.category;
		$$("movie.field.result.nominee").checked= !movie.result;
		$$("movie.field.result.winner").checked= movie.result;
	};
	var addRowToTable = (movie) => {
		var row = "";
		row += "<tr>";
		row += "<td>"+movie.title+"</td>";
		row += "<td>"+movie.releasedDate+"</td>";
		row += "<td>"+(movie.budget || "")+"</td>";
		row += "<td><img";
		row += "	src=\""+(movie.poster || "")+"\" ";
		row += "	width=\""+"30px"+"\"></td>";
		row += "<td>"+(movie.rating || "")+"</td>";
		row += "<td>"+(movie.category || "")+"</td>";
		row += "<td>"+(movie.result)+"</td>";
		row += "<td><button id=\"btnDelete"+movie.id+"\" data-movie-id=\""+movie.id+"\">"+"delete"+"</button></td>";
		row += "<td><button id=\"btnEdit"+movie.id+"\" data-movie-id=\""+movie.id+"\">"+"edit"+"</button></td>";
		row += "</tr>";
		return row;
	};
	var fieldValueIsRequired = (field, help, label, value) => {
		var result = true;
		if (!value){
			$$(field).style.borderColor = "red";
			$$(help).style.color = "red";
	 		$$(help).innerHTML = label+" is required";
			result = false;
		} else {
			$$(field).style.borderColor = "";
			$$(help).style.color = "";
	 		$$(help).innerHTML = "";
		}
		return result;
	};
	var movieIsValid = (movie) => {
		var result = true;
		result &= fieldValueIsRequired("movie.field.title","movie.help.title","title", movie.title);
		result &= fieldValueIsRequired("movie.field.releasedDate","movie.help.releasedDate","released date", movie.releasedDate);
		return result;
	};
	var saveMovieLs = (movie) => {
		if (!movie.id){
			movie.id = new Date().getTime();
		}
		repository.push(movie);
		localStorage.setItem("movies",JSON.stringify(repository));
	};
	var saveMovie = (movie) => {
		if (movie.id) updateMovie(movie); else addMovie(movie);
	}
	var onclickBtnSave = (e) => {
		var movie = movieFromForm();
		if (movieIsValid(movie)){
			saveMovie(movie);
			clearForm();
			loadAndShowMovies();
		}
	};
	var onclickBtnClear = (e) => clearForm();
	var deleteMovieLs = (id) => {
		var index = repository.findIndex(_m => _m.id == id);
		repository.splice(index,1);
		localStorage.setItem("movies",JSON.stringify(repository));
	};
	var deleteMovie = (id) => {
		removeMovie(id);
	};
	var onclickBtnDelete = (e) => {
		deleteMovie(e.target.dataset.movieId);
		loadAndShowMovies();
	};
	var onclickBtnEdit = (e) => {
		findMovieById(e.target.dataset.movieId);
	};
	var addRows = () => {
		repository.forEach(_movie => {
			$$("tblBody").innerHTML += (addRowToTable(_movie));
		});
	};
	var registerRowEvents = () => {
		repository.forEach(_movie => {
			$$("btnDelete"+_movie.id).onclick = onclickBtnDelete;
		});
		repository.forEach(_movie => {
			$$("btnEdit"+_movie.id).onclick = onclickBtnEdit;
		});
	};
	var showMovies = () => {
		$$("tblBody").innerHTML = "";
		$$("tblcaption").innerText = ""+repository.length+" movies";
		addRows();
		registerRowEvents();
	}
	var loadMoviesLs = () => {
		repository = JSON.parse(localStorage.getItem("movies")) || [];
	};
	var loadMovies = () => {
		getMovies();
	};
	var loadAndShowMovies = () => {
		loadMovies();
		showMovies();
	};
	var init = () => {
		// localStorage.removeItem("movies");
		var title = "servlets 4";
		document.getElementsByTagName("title").item(0).innerText = title;
		document.getElementsByClassName("pagetitle").item(0).innerText = title;
		$$("btnSave").onclick = onclickBtnSave;
		$$("btnClear").onclick = onclickBtnClear;
		loadAndShowMovies();
	};
	window.onload = init;