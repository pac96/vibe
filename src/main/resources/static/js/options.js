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
	// Show the edit event form
    $("#editEventForm").show();
}));

/* Handles clicking on the submit changes button */
$("#EditAddNewEvent").click(function() {
	editEventPost(currentEventID);
});

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
 * Edits an event on the front-end by updating the calendar and appending
 * a new list item
 * @param  {CalendarEvent} editedEvent - the event object you want to add in
 */
function editEvent(editedEvent){
	var timePeriod = "";
	if(editedEvent.start.isAM){
		timePeriod = "am";
	} else {
		timePeriod = "pm";
	}

	if (editedEvent) {
		var dataTargetID = "dropdown-" + currentEventID;
		var newHTML = htmlDropdown(dataTargetID, timePeriod, editedEvent);
		
		var eventLI = $('#' + currentEventID);
		console.log(newHTML);
		console.log("inner html of event LI...");
		console.log(eventLI.html());

		// Replaces the html of the old event with the info from the new one
		eventLI.html(newHTML);
	    $("#editEventForm").hide();
	}	
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
 * Edits an event by sending a post request to the back-end 
 * and updates the calendar
 * @param  {String} eventID - the ID of the event
 */
function editEventPost(eventID) {
	// necessary for some browser problems (saw on jquery's website)
		$.valHooks.textarea = {
		  get: function( elem ) {
		    return elem.value.replace( /\r?\n/g, "\r\n" );
		  }
		};

		var editableEvent;
		var eventName = $('#editEventName').val();
		var startTime = $('#editStartTime').val();
		var endTime = $('#editEndTime').val();
		var startAP;
		var endAP;

		// Check to see if start time is AM or PM
		if ($('#editStartAM').is(':checked')) {
			startAP = true;
		} else {
			startAP = false;
		}
		
		// Check to see if end time is AM or PM
		if ($('#editEndAM').is(':checked')) {
			endAP = true;
		} else {
			endAP = false;
		}

		// Set up the event format and time format
		eventFormat = /^[a-zA-Z]+$/;
		timeFormat = /^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/;
		
		if(eventName == null || startTime == null ||
		 endTime == null || startAP == null || endAP == null) {
			alert("One or more event fields are empty");
		} else if(eventName == '' && !eventName.match(eventFormat)) {
			alert("Invalid Event Name: " + eventName);
		} else if(startTime == '' && !startTime.match(timeFormat)) {
	    	alert("Invalid Start Time: " + startTime);
	    } else if(endTime == '' && !startTime.match(timeFormat)) {
	    	alert("Invalid End Time: " + endTime);
	    } else {
	    	var postParameters = {
				start : startTime ,
				end : endTime ,
				startAMPM : startAP ,
				endAMPM : endAP,
				name : eventName
	    	};
	    	
	    	$.post("/newEvent", postParameters, function(response) {
	    		// 1. send stuff to back end and store in responseObject
	    		var responseObject = JSON.parse(response);

	    		// 2. Get calendar event from the calendar array
	    		editableEvent = new CalendarEvent(responseObject);

	    		console.log("Editable: ");
	    		console.log(editableEvent);
	    		console.log(editableEvent.start);
	    		console.log(editableEvent.end);

	    		// 3. Remove the old event from the eventsArray
	    		for (var i = 0; i < eventsArray.length; i++) {
	    			if (eventsArray[i].id === eventID) {
	    				eventsArray.splice(i,1);
	    			}
	    		}
	    		
	    		// 4. Add new calendar event to user's list
	    		eventsArray.push(editableEvent);
	    		
	    		// 5. sort the list of events
	    		eventsArray.sort(eventComparator);
	    		
	    		editEvent(editableEvent);
	    	});
		}
}

function getEvent(eventID){
	console.log("get Event...")
	for (var i = 0; i < eventsArray.length; i++) {
		if(eventsArray[i].id === eventID){
			return eventsArray[i];
		}
	}
}