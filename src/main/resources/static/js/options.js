// "Customize Playlist" option
$(document).on('click', '#customizePlaylist', (function() {
	var dropdownID = $(this).parent().attr('id');
	currentEventID = dropdownID.split("_")[1];	//hide unneeded divs
	
	$('#view-playlist-panel').hide();
	editDiv.hide();
	hidePlaylist();

	// Show the div and buttons
	$("#customizeDiv").show();
	$("#selectPlaylistBtn").show();
	$("#generateCustomBtn").show();

	// Hide the forms
	$("#selectPlaylistForm").hide();
	$("#customizePlaylistForm").hide();
}));

// Load customization options
$(document).on('click', '#generateCustomBtn', function() {
	// Hide the select playlist form and buttons
	$("#selectPlaylistForm").hide();
	$("#selectPlaylistBtn").hide();

	// Bring in the customize playlist form and make the button appear active
	$("#generateCustomBtn").addClass("active");
	$("#customizePlaylistForm").fadeIn("fast");
});

// Submit customization options 
$(document).on('click', '#customizeSubmit', function() {
	customizePlaylist(currentEventID);
});



$(document).on('click', '#selectPlaylistBtn', function() {
	$("#customizePlaylistForm").hide();
	$("#generateCustomBtn").hide();

	$("#selectPlaylistBtn").addClass("active");
	$("#selectPlaylistForm").fadeIn("fast");

	$(document).on('click', "#existingSubmit", function() {
		selectExistingPlaylist(currentEventID);
	});
});




// "Edit Event" option
$(document).on('click', '#editEvent', (function() {
	    // hide unneeded divs
    $("#customizeDiv").hide();
    hidePlaylist();

	var dropdownID = $(this).parent().attr('id');
	currentEventID = dropdownID.split("_")[1];
	// Show the edit event form
    editDiv.show();
    
    // hide unneeded divs
    $('#view-playlist-panel').hide();
}));

/* Handles clicking on the submit changes button */
$("#EditAddNewEvent").click(function() {
	requestEdit(currentEventID);
});



// "Delete Event" option
$(document).on('click', '#deleteEvent', (function() {
	var dropdownID = $(this).parent().attr('id');
	console.log("Dropdown ID: " + dropdownID);
	currentEventID = dropdownID.split("_")[1];

    deleteEvent(currentEventID);

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

// Exit Event Editor
$('#ExitEditEvent').on('click', function() {
	editDiv.hide();
});



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
	var theEvent;
	for (var i = 0; i < eventsArray.length; i++) {
		if (eventID === eventsArray[i].id) {
			theEvent = eventsArray[i];
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
        playlist = $("#playlist");
        var eventObject = getEvent(eventID);
		// Set the source of the playlist to be the input URI
		playlist.attr('src', "https://embed.spotify.com/?uri=" + uri);
		eventObject.playlistURI = uri;
		console.log("Backend uri");
		playlist.fadeIn("slow");

		$("#customizeDiv").hide();
		$("#selectPlaylistForm").hide();
		$("#selectPlaylistBtn").hide();
		$("#customizePlaylistForm").hide();
		$("#generateCustomBtn").hide();		

		$("div.bar").fadeOut("fast");
		$("#hidePlaylist").fadeIn("slow");
    }); 
}

function showPlaylistWithURI(uri, eventID) {
    var eventObject = getEvent(eventID);
	eventObject.playlistURI = uri;

	console.log('https://embed.spotify.com/?uri=' + uri);
	playlist.attr('src', 'https://embed.spotify.com/?uri=' + uri);
	playlist.fadeIn("slow");

	$("#customizeDiv").hide();
	$("#selectPlaylistForm").hide();
	$("#selectPlaylistBtn").hide();
	$("#customizePlaylistForm").hide();
	$("#generateCustomBtn").hide();		

	$("div.bar").fadeOut("fast");
	$("#hidePlaylist").fadeIn("slow");
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
		var dataTargetID = "dropdown_" + currentEventID;
		var newHTML = htmlDropdown(dataTargetID, timePeriod, editedEvent);
		
		var eventLI = $('#' + currentEventID);

		// Replaces the html of the old event with the info from the new one
		eventLI.html(newHTML);

	    editDiv.hide();

	}	
}

/**
 * Sends a post request to the back-end to delete an event.
 * @param  {String} id - id of the event you want to delete
 */
function deleteEvent(id) {
	console.log("Right before we send it back... " + id);
	var postParam = {eventID: id};
	
	// The backend responds with a string: 
	// "SUCCESS" or "FAILED: [error message]"
	$.post('/deleteEvent', postParam, function(response) {
		console.log("postdelete");
		otherContent.html(""); // Replace what's already in other content

		if (response != "SUCCESS") {
			// do some stuff
			alert(response);
			var $msg = $("<p>", {
				class: "contentMsg", 
				id: "error"
			});

			$msg.append("There was an error trying to delete your event.");
			otherContent.html($msg);
		    otherContent.fadeIn('slow');
		    setTimeout(function() {
			    otherContent.fadeOut('slow');
		    }, 3000);
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
    				if (playlist.src === eventsArray[i].playlistURI) {
    					playlist.attr('src', null);
    					hidePlaylist();
    				}
    				eventsArray.splice(i,1);
    				break;
    			}
    		}

    		for (var j = 0; j < occurrenceArray.length; j++) {
    			if (occurrenceArray[j].id === id) {
    				occurrenceArray.splice(j,1);
    				break;
    			}
    		}

    		var toClear = eventToTimeout.get(id);
    		console.log("Timeout to clear: " + toClear);
    		clearTimeout(toClear);
    		// TODO: CLEAR TIMEOUT RELATED TO EVENT THAT YOU DELETED
    		// SO THE POPUP DOES NOT APPEAR

			nextEventPopup();

			// hide unneeded divs
		    editDiv.hide();
		    $("#customizeDiv").hide();
		    hidePlaylist();
		    playlist.attr('src', null);
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
		var keepOldPlaylist = true;


		// Ask the user if they want their playlist to be associated differently
		$("#playlist-alert").text("MAJOR KEY");
		$("#modal-msg-playlist").html("By default, the playlist associated with your old event will be kept. "
			+ "Do you want us to generate a new playlist based on your event name?");
		// 2. Show the popup box
		$("#new-playlist-modal").modal('show');

		// If the user wants to auto generate a playlist based on the name, set
		// the flag to true
		$(document).on('click', '.modalbtn', function() {
			// $(document).on('click', '#yesBtn', function() {
			// keepOldPlaylist = false;
			// });	
			if ($(this).attr('id') == "yesBtn") {
				keepOldPlaylist = false;
			}

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
		    	console.log("New start: " + startTime);
		    	console.log("New end: " + endTime);
		    	console.log("New startAMPM: " + startAP);
		    	console.log("New endAMPM: " + endAP);
		    	console.log("New name: " + eventName);
		    	console.log("ID: " + eventID);
		    	console.log("Keep old playlist: " + keepOldPlaylist);

		    	
		    	var postParameters = {
					start : startTime ,
					end : endTime ,
					startAMPM : startAP ,
					endAMPM : endAP,
					name : eventName,
					id : eventID,
					keepOldPlaylist: keepOldPlaylist
		    	};
		    	
		    	$.post("/editEvent", postParameters, function(response) {
		    		// 1. send stuff to back end and store in responseObject
		    		var responseObject = JSON.parse(response);
		    		console.log(responseObject["success"]);
		    		
		    		if (responseObject["success"] === true) {
			    		// 2. Get calendar event from the calendar array
			    		editableEvent = new CalendarEvent(responseObject["event"]);
			    		console.log("response object");
			    		console.log(editableEvent);
			    		console.log(responseObject["event"]);
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
			    		occurrenceArray.push(editableEvent);

			    		
			    		// 5. sort the list of events
			    		eventsArray.sort(compareEvents);
			    		occurrenceArray.sort(compareEvents);
			    		
			    		// 6. Find out when the next event is and set the popup
			    		editCalendarEvent(editableEvent);
			    		
				    	nextEventPopup();			
		    			
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
		    		} else {
		    			var $msg = $("<p>", {
							class: "contentMsg", 
							id: "errorMsg"
						});

						$msg.append("Edit event failed, check that the time is formatted properly");
						otherContent.html($msg);
					    otherContent.fadeIn('slow');

					    setTimeout(function() {
						    otherContent.fadeOut('slow');
					    }, 3000);
		    		}
		    	}); // end post
			} // end else
		}); // end onclick
}

/**
 * Customizes an event's music settings and returns
 * a new playlist based on those customizations
 */
function customizePlaylist(eventID) {


	// necessary for some browser problems (saw on jquery's website)
	$.valHooks.textarea = {
	  get: function( elem ) {
	    return elem.value.replace( /\r?\n/g, "\r\n" );
	  }
	};
	
	var eventMoodNum;
	var eventTag = "";
	var eventMood = "";
	var popularityPref = document.getElementById('popularitySlider').value;
	var energyPref = document.getElementById('energySlider').value;

	var genreSelection = [];

	// Retrieve the value of each selected genre
	$('#ulGenre :checkbox:checked').each(function(i) {
		genreSelection[i] = $(this).val();
	});

	$('#eTagWrapper :radio:checked').each(function() {
		eventTag = this.value;
		console.log("just the event tag");
		console.log(eventTag);
	});

	$('#mTagWrapper :radio:checked').each(function() {
		eventMood = this.value;
	});
	
	
	// assign value to mood
	if (eventMood == "Happy") {
		eventMoodNum = .75;
		
	} else if (eventMood == "Excited") {
		eventMoodNum = 1;
		
	} else if (eventMood == "Sad") {
		eventMoodNum = 0;
		
	} else if (eventMood == "Relaxing") {
		eventMoodNum = .40;
		
	} else {
		eventMoodNum = null;
	}
	
	var customize = (eventTag != null  && eventMood != null  &&
			popularityPref != null && energyPref != null &&
			genreSelection != null) ;
	
	var atLeastOneCustomOption = (eventTag != null  || eventMood != null  ||
			popularityPref != null || energyPref != null ||
			genreSelection != null) ;
	
	console.log("Customize playlist? " + customize);
	console.log("Event tag: " + eventTag);
	console.log("Event mood: " + eventMood);
	console.log("Event mood number: " + eventMoodNum);
	console.log("Popularity: " + popularityPref);
	console.log("Energy: " + energyPref);
	console.log("Genres: " + genreSelection); 
	var playlistSelection = "";

	// none of the custom options are selected
	if(!customize){
		playlistSelection = document.getElementById('#select-your-playlist').value;
		console.log(playlistSelection);
	}
	
	if (customize) {
    	var postParameters = {
			tag : eventTag ,
			mood : eventMoodNum ,
			popularity : popularityPref ,
			energy : energyPref,
			genres : JSON.stringify(genreSelection),
			playlist : playlistSelection,
			eventID : eventID
    	};
    	
    	$.post("/customizePlaylist", postParameters, function(response) {
    		// 2. Hide Customization div and show playlist div
    		$('#customizeTitle').hide();
    		$("#customizePlaylistForm").hide();
    		$('#view-playlist-panel').show();
    		
    		// 1. Send information to the back end, store in responseObject
    		//    backend will generate a new playlist id for this event
    		//console.log(response);
    		var responseObject = JSON.parse(response);
    		if (responseObject.success == "false") {
    			console.log("Customization was not successful!");
				var $msg = $("<p>", {
					class: "contentMsg", 
					id: "errorMsg"
				});

				$msg.append(responseObject.error);
				otherContent.html($msg);
			    otherContent.fadeIn('slow');

			    setTimeout(function() {
				    otherContent.fadeOut('slow');
			    }, 3000);
    		} else {
    			var $msg = $("<p>", {
					class: "contentMsg", 
					id: "successMsg"
				});

				$msg.append("Playlist customized!");
				otherContent.html($msg);
			    otherContent.fadeIn('slow');

			    setTimeout(function() {
				    otherContent.fadeOut('slow');
			    }, 2000);

	   			showPlaylist(eventID);
    		}
    	});
    	// empty playlist selection and no custom options 
    	// or empty playlist selection and incomplete custom option selection
	}else if((playlistSelection === "" && !customize) || 
			(playlistSelection === "" && atLeastOneCustomOption)) {
		var $msg = $("<p>", {
			class: "contentMsg", 
			id: "errorMsg"
		});

		$msg.append("You must either customize or select Spotify playlists");
		otherContent.html($msg);
	    otherContent.fadeIn('slow');
	    setTimeout(function() {
		    otherContent.fadeOut('slow');
	    }, 2000);var $msg = $("<p>", {
			class: "contentMsg", 
			id: "errorMsg"
		});
	
		// incomplete custom option selection and empty playlist selection
		// or incomplete custom option selection and non-empty playlist selection
	} else if ((atLeastOneCustomOption && playlistSelection === "") || 
			(atLeastOneCustomOption && playlistSelection !== "")) {
		var $msg = $("<p>", {
			class: "contentMsg", 
			id: "errorMsg"
		});

		$msg.append("Fully complete customizations or select a playlist");
		otherContent.html($msg);
	    otherContent.fadeIn('slow');
	    setTimeout(function() {
		    otherContent.fadeOut('slow');
	    }, 3000);
	}
}





function selectExistingPlaylist(id) {
	var $selectedOption = $("#playlistDropdown option:selected");
	var uri = $selectedOption.attr('id');

	console.log("Selected uri: " + uri);
	var theEvent = getEvent(id);
	theEvent.playlistURI = uri;

	var postParams = {
		playlistURI: uri, 
		eventID: id
	};

	$.post("/selectExistingPlaylist", postParams, function(response) {
		if (response.success === "false") {
			var $msg = $("<p>", {
				class: "contentMsg", 
				id: "errorMsg"
			});

			$msg.append("There was an error handling your event.");
			otherContent.html($msg);
		    otherContent.fadeIn('slow');
		    setTimeout(function() {
			    otherContent.fadeOut('slow');
		    }, 3000);
		} else {
			var $msg = $("<p>", {
				class: "contentMsg", 
				id: "successMsg"
			});

			$msg.append("Using your playlist!");
			otherContent.html($msg);
		    otherContent.fadeIn('slow');

			var eventObject = getEvent(id);
			console.log("Response uri: " + response.playlistURI);

			eventObject.playlistURI = uri;
			showPlaylistWithURI(uri, id);
		}
	});	
}

function getEvent(eventID) {
	var theEvent;

	for (var i = 0; i < eventsArray.length; i++) {
		if (eventsArray[i].id == eventID) {
			theEvent = eventsArray[i];
			break;
		}
	}

	return theEvent;
}