document.querySelector('#btnuploadxhr').addEventListener('click', function(e) {
  e.preventDefault();
  var file = document.querySelector('#file').files[0];
  var fd = new FormData();
  fd.append("file", file);
  fd.append("title", document.querySelector('#title'));
  var xhr = new XMLHttpRequest();
  xhr.open('POST', 'files?xhr=true', true);

  xhr.upload.onprogress = function(e) {
    if (e.lengthComputable) {
      var percentComplete = (e.loaded / e.total) * 100;
      console.log(percentComplete + '% uploaded');
    }
  };
  xhr.onload = function() {
    if (this.status == 200) {
      var resp = JSON.parse(this.responseText);
      var h2 = document.createElement('h2');
      h2.innerText = resp.name + "(" + resp.filename+", "+ file.size+") ";
      var image = document.createElement('img');
      image.src = resp.dataUrl;
      document.body.appendChild(h2);
      document.body.appendChild(image);
    }
  };
  xhr.send(fd);
}, false);
var init = () => {
	var title = "Posters";
	document.getElementsByTagName("title").item(0).innerText = title;
	document.getElementsByClassName("pagetitle").item(0).innerText = title;
};
window.onload = init;
