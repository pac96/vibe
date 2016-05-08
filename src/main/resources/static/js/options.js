// "View Playlist" option
$(document).on('click', '#viewPlaylist', (function() {
    // Retrieve the playlist URI from the backend and show it
	showPlaylist(currentEventID);
	
	// show name of event at top of page 
	var eventName = $('#name').val();
	var startTime = $('#startTime').val();
	var endTime = $('#endTime').val();
	var eventDIV = document.getElementById('view-playlist-panel');
	eventDIV.innerHTML = eventName + ' | ' + startTime + ' - ' + endTime;
	console.log(eventName);
	$('#view-playlist-panel').show();
	
	//hide unneded divs
	$("#customizePlaylistForm").hide();
	$("#editEventForm").hide();
}));


// "Customize Playlist" option
$(document).on('click', '#customizePlaylist', (function() {
	console.log("Customize playlist clicked!");
	$("#customizePlaylistForm").show();
	customize(currentEventID);
	
	//hide unneeded divs
	$('#view-playlist-panel').hide();
	$("#editEventForm").hide();
}));


// "Edit Event" option
$(document).on('click', '#editEvent', (function() {
	// Show the edit event form
    $("#editEventForm").show();
    
    // hide unneeded divs
    $("#customizePlaylistForm").hide();
    $('#view-playlist-panel').hide();
}));

/* Handles clicking on the submit changes button */
$("#EditAddNewEvent").click(function() {
	requestEdit(currentEventID);
	var $msg = $("<p>", {
		class: "contentMsg", 
		id: "successMsg"
	});

	$msg.append("Event edit successful!");
	otherContent.html($msg);
    otherContent.fadeIn('slow');
    setTimeout(function() {
	    otherContent.fadeOut('slow');
    }, 2000);
});



// "Delete Event" option
$(document).on('click', '#deleteEvent', (function() {
    deleteEvent(currentEventID);


    // hide unneeded divs
    $("#editEventForm").hide();
    $("#customizePlaylistForm").hide();
    $('#view-playlist-panel').hide();
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
  if (!$clicked.parents().hasClass("gDropdown")) {
	  $(".gDropdown dd ul").hide();
  }
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


// if (eventsArray.length > 0) {
// 	console.log("It's time to get the next important event");
// 	var nextEvent = calculateNextImportantEvent();
// 	var nextTime = nextEvent.startDate.getTime();
// 	var timeNow = new Date().getTime();

// 	var millisecondOffset = timeNow - nextTime;
// 	console.log("Millisecond offset: " + millisecondOffset);

// 	setTimeout(function() {
// 		console.log("--------------------------");
// 		console.log("--------------------------");
// 		console.log("IT'S TIME FOR THIS EVENT");
// 		console.log("--------------------------");
// 		console.log("--------------------------");
// 	}, millisecondOffset);
// }



///////////////////////////////////////////
// Function Declarations
///////////////////////////////////////////
/**
 * Given the URI of a playlist, this function makes the playlist
 * show up on the screen
 * @param {String} playlistURI - the link to the Spotify playlist
 */
function showPlaylist(eventID) {
	// 1. Set up a post request to the backend and get the event ID
    var uri = "";
	var name;

	for (var i = 0; i < eventsArray.length; i++) {
		if (eventID === eventsArray[i].id) {
			name = eventsArray[i].name;
		}
	}    

    var postParams = {
    	eventID: eventID, 
    	eventName: name
    };

    $.post("/getPlaylist", postParams, function(link) {
        console.log("Playlist @ " + link);
        uri = link;
        var playlist = $("#playlist");
		// Set the source of the playlist to be the input URI
		playlist.attr('src', "https://embed.spotify.com/?uri=" + uri);
		playlist.show();
    }); 
}

/**
 * Retrieves the URI of the playlist associated with a specific event ID
 * @param  {String} eventID - the ID of the event that was clicked
 * @return {String} the URI of the playlist
 */
function getPlaylistURI(eventID) {
	// 1. Set up a post request to the backend and get the event ID
    var uri = "";
	
	var name;

	for (var i = 0; i < eventsArray.length; i++) {
		if (eventID === eventsArray[i].id) {
			name = eventsArray[i].name;
		}
	}    

    var postParams = {
    	eventID: eventID, 
    	eventName: name
    };

    $.post("/getPlaylist", postParams, function(link) {
        console.log("Playlist @ " + link);
        uri = link;

        return uri;
    }); 
}


/**
 * Edits an event on the front-end by updating the calendar and appending
 * a new list item
 * @param  {CalendarEvent} editedEvent - the event object you want to add in
 */
function editCalendarEvent(editedEvent){
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
			var $msg = $("<p>", {
				class: "contentMsg", 
				id: "successMsg"
			});

			$msg.append("Deletion successful!");
			otherContent.html($msg);
		    otherContent.fadeIn('slow');
		    setTimeout(function() {
			    otherContent.fadeOut('slow');
		    }, 2000);

		    // Remove the event from both the events and occurrences arrays
		    for (var i = 0; i < eventsArray.length; i++) {
    			if (eventsArray[i].id === id) {
    				eventsArray.splice(i,1);
    			}
    		}

    		for (var j = 0; j < occurrenceArray.length; j++) {
    			if (occurrenceArray[j].id === id) {
    				occurrenceArray.splice(j,1);
    			}
    		}

    		var toClear = eventToTimeout.get(id);
    		console.log("Timeout to clear: " + toClear);
    		clearTimeout(toClear);
    		// TODO: CLEAR TIMEOUT RELATED TO EVENT THAT YOU DELETED
    		// SO THE POPUP DOES NOT APPEAR

			nextEventPopup();
		}
	});


}


/**
 * Sends a post request to the backend to edit the event
 * and updates the calendar accordingly
 * @param  {String} eventID - the ID of the event
 */
function requestEdit(eventID) {
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

	    		// 3. Remove the old event from the eventsArray
	    		for (var i = 0; i < eventsArray.length; i++) {
	    			if (eventsArray[i].id === eventID) {
	    				eventsArray.splice(i,1);
	    			}
	    		}

	    		// 3a. Remove the old event from the occurrenceArray
	    		for (var j = 0; j < occurrenceArray.length; j++) {
	    			if (occurrenceArray[j].id === eventID) {
	    				occurrenceArray.splice(j,1);
	    			}
	    		}
	    		
	    		// 4. Add new calendar event to user's list
	    		eventsArray.push(editableEvent);
	    		occurrenceArray.push(newEvent);

	    		
	    		// 5. sort the list of events
	    		eventsArray.sort(compareEvents);
	    		occurrenceArray.sort(compareEvents);
	    		
	    		// 6. Find out when the next event is and set the popup
	    		editCalendarEvent(editableEvent);
	    		
		    	nextEventPopup();
	    	});
		}
}


/** Object to mirror Customization from the backend **/
function Customization(preferences) {
	this.tag = preferences.event;
	this.mood = preferences.mood;
	this.popularity = preferences.popularity;
	this.energy = preferences.energy;
	this.genre = prefereces.genre;
	this.playlist = preferences.playlist;

}

/**
 * Customizes an event's music settings and returns
 * a new playlist based on those customizations
 */
/**function customizePlaylist() {
	// necessary for some browser problems (saw on jquery's website)
		$.valHooks.textarea = {
		  get: function( elem ) {
		    return elem.value.replace( /\r?\n/g, "\r\n" );
		  }
		};

		var eventTag = $('input[name=eTradio]:checked', '#myForm').val()
		var eventMood = $('input[name=mSradio]:checked', '#myForm').val()
		var popularityPref = $('#popularitySlider').data('slider').getValue();
		var energyPref = $('#energySlider').data('slider').getValue();
		var genreSelection = $('#genre-selector').val();
		var playlistSelection = $('#select-your-playlist').val();
		
		var customize = (eventTag != null  || eventMood != null  ||)
						popularityPref != null || energyPref != null ||
						genreSelection != null) ;
						

		
		if(playlistSelection != null && customize) {
			alert("You must either customize or select Spotify playlists");
		} else {
	    	var postParameters = {
				event : eventTag ,
				mood : eventMood ,
				popularity : popularityPref ,
				energy : energyPref,
				genre : genreSelection,
				playlist : playlistSelection
				
	    	};
	    	
	    	$.post("/customizePlaylist", postParameters, function(response) {
	    		// 1. Send information to the back end, store in responseObject
	    		var responseObject = JSON.parse(response);

	    		// 2. Make customization object from responseObject
	    		var custom = new Customization(responseObject);

	    	});
		}
}
**/