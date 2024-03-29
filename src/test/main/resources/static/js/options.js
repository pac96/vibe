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
	editRequest(currentEventID);
});



// "Delete Event" option
$(document).on('click', '#deleteEvent', (function() {
    deleteEvent(currentEventID);
}));


/* genre dropdown, multi selection */
$(".gDropdown dt yy").on('click', function() {
	$(".gDropdown dd ul").slideToggle('fast');
});

$(".gDropdown dd ul li yy").on('click', function() {
  $(".gDropdown dd ul").hide();
});

function getSelectedValue(id) {
  return $("#" + id).find("dt yy span.value").html();
}

$(document).bind('click', function(e) {
  var $clicked = $(e.target);
  if (!$clicked.parents().hasClass("gDropdown")) $(".gDropdown dd ul").hide();
});

$('.mutliSelect input[type="checkbox"]').on('click', function() {

  var title = $(this).closest('.mutliSelect').find('input[type="checkbox"]').val(),
  title = $(this).val() + ",";

  if ($(this).is(':checked')) {
    var html = '<span title="' + title + '">' + title + '</span>';
    $('.multiSelection').append(html);
    $(".hida").hide();
  } else {
    $('span[title="' + title + '"]').remove();
    var ret = $(".hida");
    $('.dropdown dt yy').append(ret);

  }
});

/** slider functions **/

$('#popularitySlider').data('slider').getValue();
$('#energySlider').data('slider').getValue();




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

		// Replaces the html of the old event with the info from the new one
		eventLI.html(newHTML);
	    $("#editEventForm").hide();
	}	
}

/**
 * Sends a post request to the back-end to delete an event.
 * @param  {String} id - id of the event you want to delete
 */
function deleteEvent(id) {
	var postParam = {eventID: id};
	
	// The backend responds with a string: 
	// "SUCCESS" or "FAILED: [error message]"
	$.post('/deleteEvent', postParam, function(response) {
		console.log("postdelete");
		otherContent.html(""); // Replace what's already in other content

		if (response != "SUCCESS") {
			// do some stuff
			alert(response);
		} else {
			$("#" + id).remove();
			// The same as <p class='contentMsg' id='successMsg'>Deletion successful!</p>
			var $msg = $("<p/>", {class: "contentMsg", id: "successMsg"});
			$msg.append("Deletion successful!");
			otherContent.html($msg);
		    otherContent.fadeIn('slow');
		    setTimeout(function() {
			    otherContent.fadeOut('slow');
		    }, 2000);
		}
	});


}

/**
 * Edits an event by sending a post request to the back-end 
 * and updates the calendar
 * @param  {String} eventID - the ID of the event
 */
function editRequest(eventID) {
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
				name : eventName,
				id : eventID
	    	};
	    	
	    	$.post("/editEvent", postParameters, function(response) {
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