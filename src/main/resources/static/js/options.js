// "View Playlist" option
$("#viewPlaylist").click(function(event) {
	// TODO: retrieve the playlist for a certain event
	// var playlistURI = "";
	// showPlaylist(playlistURI);
});

// "Customize Playlist" option
$("#customizePlaylist").click(function(event) {
	console.log("Customize playlist clicked!");
});

// "Edit Event" option
$("#editEvent").click(function(event) {
	console.log("Edit event clicked!");
	
});

// "Delete Event" option
$("#deleteEvent").click(function(event) {
	console.log("Delete event clicked!");

});


///////////////////////////////////////////
// Function Declarations
///////////////////////////////////////////
/**
 * Given the URI of a playlist, this function makes the playlist
 * show up on the screen
 * @param {String} playlistURI - the link to the Spotify playlist
 */
function showPlaylist(playlistURI) {
	var playlist = $("#playlist");
	// Set the source of the playlist to be the input URI
	playlist.attr('src', playlistURI);
	playlist.show();
}

/**
 * Retrieves the URI of the playlist associated with a specific event ID
 * @param  {String} eventID - the ID of the event that was clicked
 * @return {String} the URI of the playlist
 */
function getPlaylistURI(eventID) {
	var uri = "";
	// TODO: retrieve the URI of a playlist given an event ID
	return uri;
}