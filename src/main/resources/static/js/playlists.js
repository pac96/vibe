var name = "";

if (window.location.pathname === "/playlists") {
	var uri = new URI(window.location.href);
	var urlParams = uri.search(true);
	console.log("User code: " + urlParams.code);

	var codeFromURL = urlParams.code;
	var postParams = {code: JSON.stringify(codeFromURL)};

	$.post("/code", postParams, function(responseJSON) {
	  // Back-end sends back the user's display name
	  var backendParams = responseJSON.split(",");
	  console.log(backendParams[0]);
	  console.log(backendParams[1]);


	  if (backendParams === "") {
	  	alert("Error loading user information. Please logout and login again!");
	  }

	  var username = backendParams[0].slice(1, backendParams[0].length); // remove the [
	  var playlistURI = "https://embed.spotify.com/?uri=" + backendParams[1].slice(backendParams[1].length - 1, backendParams[1].length); // remove the ]

	  console.log("user: " + username);
	  console.log("playlist: " + playlistURI);

	  if (backendParams != null) {
	    name = username;
		$("#displayname").html(name);
	  }

	$("#playlist").attr('src', playlistURI);
	
	});

}