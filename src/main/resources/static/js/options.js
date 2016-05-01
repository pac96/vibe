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

}));

/* Handles editing an event */
$("#EditAddNewEvent").click(function() {
	console.log("Editing new event...");
	editEventPost(currentEventID);
}); // end add new click handler


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
function editEvent(editedEvent){
	//var elt =  document.getElementById(eventID);

	console.log("edit event: event details...");
//	console.log(editedEvent);
//
//	console.log("Edited Event Name =" + editedEvent.name);
//	console.log("Edited Event Start =" + editedEvent.start);

	var timePeriod = "";
	if(editedEvent.start.isAM){
		timePeriod = "am";
	} else {
		timePeriod = "pm";
	}

	if (editedEvent) {
		var newHTML = 
//			"<li id='" + editedEvent.id + "' class='anEvent'> " +
//			"<a href='#'>" +
			"<a href='javascript:;' data-toggle='collapse' data-target='#demo'>" +
			"<i class='fa fa-fw fa-arrows-v'></i> " +
			editedEvent.start.hour + timePeriod + " | " + editedEvent.name + " " +
			"<i class='fa fa-fw fa-caret-down'></i></a>" +
			"<ul id= 'demo'" + "class='collapse'>" +
			"<li id='viewPlaylist'>" +
			"<a href='#'>View Playlist" + editedEvent.start.hour + "</a>" +
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
			"</a>"
//			"</li>";
		
		var eventLI = document.getElementById(editedEvent.id);
		console.log(newHTML);
		eventLI.innerHTML = newHTML;      
//		console.log("After");
//		console.log(eventLI.html());
//		console.log("Selection id: " + eventLI.attr("id"));
//		console.log("Text: " + eventLI.text());

	}
    
    //document.getElementById('use-spotify-playlist-panel').style.display = "block";   
	
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
 * Edits an event and changes it on the 
 * list of events on the front-end
 */
function editEventPost(eventID) {
	console.log("edit Event...");
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

	    		
//	    		editableEvent.start = startTime;
//	    		editableEvent.end = endTime;
//	    		editableEvent.name = eventName;
//	    		editableEvent.start.hour = 
//	    		editableEvent.id = currentEventID;
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
	    		
	    		// 3. Add new calendar event to user's list
	    		eventsArray.push(editableEvent);
	    		
	    		// 4. sort the list 
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