// "View Playlist" option
$(document).on('click', '#viewPlaylist', (function() {
	console.log("View playlist clicked!");
    // Retrieve the playlist URI from the backend and show it
	// var playlistURI = getPlaylistURI(currentEventID);
	// showPlaylist(playlistURI);
}));

// "Customize Playlist" option
$(document).on('click', '#customizePlaylist', (function() {
	console.log("Customize playlist clicked!");
}));

// "Edit Event" option
$(document).on('click', '#editEvent', (function() {
	console.log("Edit event clicked!");
    $("#editEventForm").show();
	
}));

// "Delete Event" option
$(document).on('click', '#deleteEvent', (function() {
	console.log("Delete event clicked!");
    // deleteEvent(currentEventID);
}));


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
	// 1. Set up a post request to the backend and get the event ID
    var uri = "";

    $.post("/getPlaylist", {eventID: eventID}, function(link) {
        uri = link;
    }); 

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
        var currentEvent = document.getElementById(eventID);
        var newName;
        var newStart;
        var newEnd;
        var newStartIsAM;
        var newEndIsAM;
        if (currentEvent) {
            var newHTML = 
            	"<li id='" + event.id + "' class='anEvent'> " +
    			"<a href='#'>" +
    			"<a href='javascript:;' data-toggle='collapse' data-target='#demo'>" +
    				"<i class='fa fa-fw fa-arrows-v'></i> " +
    				event.start.hour + timePeriod + " | " + event.name + " " +
    				"<i class='fa fa-fw fa-caret-down'></i></a>" +
        			"<ul id= 'demo'" + "class='collapse'>" +
                        "<li id='viewPlaylist'>" +
                            "<a href='#'>View Playlist" + event.start.hour + "</a>" +
                        "</li>" +
                        "<li id='customizePlaylist'>" +
                            "<a href='#'>Customize Playlist</a>" +
                        "</li>" +
                        "<li id='usePlaylist'>" +
                            "<a href='#'>Use Spotify Playlist</a>" +
                        "</li>" +
                        "<li id='editEvent'>" +
                            "<a href='#'>Edit Event</a>" +
                        "</li>" +
                        "<li id='deleteEvent'>" +
                            "<a href='#'>Delete Event</a>" +
                        "</li>" +
                    "</ul>" +
    			"</a>" +
    		"</li>";
            elt.innerHTML = newHTML;
            
        }
    }
    //document.getElementById('use-spotify-playlist-panel').style.display = "block";   
	regenerateEvent(eventID);
}

/**
 * Allows the user to delete an event.
 * @param  {String} eventID - the ID of the event you clicked on
 */
function deleteEvent(eventID){
    var jqueryEventID = "#" + eventID;
    $("#" + eventID).remove();
    // TODO: Send deletion request to the back-end
}

/**
 * Allows a user's event edits to effect the events 
 * attributes
**/

function regenerateEvent(eventID){
	var currentEvent = document.getElementById(eventID);
	currentEvent
}