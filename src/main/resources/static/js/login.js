// Set the link of the button to be the authorize URI
// retrieved from the back-end

var linkToLogin = $("#loginLink");
var isPlaylistUp = true;
var isAddEventUp = false;
var addEventDiv = $("#addEvent");
var playlistDiv = $("#playlist");
hideMainContent();

if (window.location.pathname === "/vibe") {
  // Retrieve the authorizeURI from the back-end
  $.get("/login", function(responseJSON) {
    var authorizeURI = JSON.parse(responseJSON);
     authorizeURI += "&show_dialog=true"; // necessary for other users to login
    linkToLogin.prop("href", authorizeURI);
  });  
}


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