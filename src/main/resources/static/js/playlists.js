var name = "";

if (window.location.pathname === "/playlists") {
	var uri = new URI(window.location.href);
	var urlParams = uri.search(true);
	console.log("User code: " + urlParams.code);

	var codeFromURL = urlParams.code;
	var postParams = {code: JSON.stringify(codeFromURL)};

	$.post("/code", postParams, function(responseJSON) {
	  // Back-end sends back the user's display name
	  var userDisplayName = responseJSON; // no need to parse it since it's a string
	  if (userDisplayName === "") {
	  	alert("Error loading user information. Please logout and login again!");
	  }

	  console.log("user: " + userDisplayName);
	  if (userDisplayName != null) {
	    name = userDisplayName;
		$("#displayname").html(name);
		console.log("Name: " + name);
	  }
	
	});
}