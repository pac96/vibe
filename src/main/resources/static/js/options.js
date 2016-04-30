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

/**
 * Displays the playlist for a given event.
 */
function displayCustomizePlaylist(eventID){
    document.getElementById('view-playlist-panel').style.display = "block";   
}

/**
 * Displays options for a users Spotify Playlist.
 */
function displaySpotifyPlaylist(eventID){
    document.getElementById('customize-playlist-panel').style.display = "block";   

}

/**
 * Allows a user to edit an event.
 */
function editEvent(eventID, toChange){
	if (toChange) {
        var currentEvent = document.getElementById('eventID');
        if (currentEvent) {
            var newHTML = 
            	"<li id='" + event.id + "' class='eventClick' onclick='createDropdown()'> " +
    			"<a href='#'>" +
    			"<a href='javascript:;' data-toggle='collapse' data-target='#demo'>" +
    				"<i class='fa fa-fw fa-arrows-v'></i> " +
    				event.start.hour + timePeriod + " | " + event.name + " " +
    				"<i class='fa fa-fw fa-caret-down'></i></a>" +
    				"<ul id= 'demo'" + "class='collapse'>" +
    					"<li>" +
    						"<a href='#'>View Playlist" + event.name + "</a>" +
    					"</li>" +
    					"<li>" +
    						"<a href='#'>Customize Playlist </a>" +
    					"</li>" +
    					"<li>" +
    						"<a href='#'>Use Spotify Playlist </a>" +
    					"</li>" +
    					"<li>" +
    						"<a href='#'>Edit Event </a>" +
    					"</li>" +
    					"<li>" +
    						"<a href='#'>Delete Event </a>" +
    					"</li>" +
    				"</ul>" +
    			"</a>" +
    		"</li>";
            elt.innerHTML = newHTML;
        }
    }
    document.getElementById('use-spotify-playlist-panel').style.display = "block";   

}

/**
 * Allows a user to delete an event.
 */
function deleteEvent(eventID){
    document.getElementById('edit-event').style.display = "block";   

}
