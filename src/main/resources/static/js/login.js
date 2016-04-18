// Set the link of the button to be the authorize URI
// retrieved from the back-end

var linkToLogin = $("#loginLink");
var authorizeURI;
var isPlaylistUp = true;
var isAddEventUp = false;
var addEventDiv = $("#addEvent");
var playlistDiv = $("#playlist");
hideMainContent();

// Retrieve the authorizeURI from the back-end
$.get("/login", function(responseJSON) {
  var authorizeURI = JSON.parse(responseJSON);
  authorizeURI += "&show_dialog=true"; // necessary for other users to login
  linkToLogin.prop("href", authorizeURI);
  console.log(linkToLogin.href);
});



// var clientID = "bfd53bc39d2c46f081fa7951a5a88ea8";


// var getParams = {client_id: clientID, response_type: "code", show_dialog: "true"};
// $.get("https://accounts.spotify.com:443/authorize", getParams, function(responseJSON) {
//   var object = JSON.parse(responseJSON);
//   console.log("NEW: " + object);
//   linkToLogin.href = object;
// });


/**
  * Hides a portion of the page (playlist or add event div)
  * and shows the other portion
  *
  */
function hideMainContent() {
  if (isPlaylistUp) {
    addEventDiv.hide();  
    playlistDiv.show();
    isAddEventUp = false;
  } 
  if (isAddEventUp) {
    playlistDiv.hide();
    addEventDiv.show();
    isPlaylistUp = false;
  }
}

