var name = "";
var eventsArray = [];
var currentEvent;
var eventComparator = function(eventA, eventB) {
	if (eventA == null || eventB == null) {
		return -1;
	}

	var eventAStartTime = eventA.start;
	var eventBStartTime = eventB.start;
	
	//Case where eventA and eventB are both in the AM or both PM
	if (eventAStartTime.isAM && eventBStartTime.isAM 
			|| (!eventAStartTime.isAM && !eventBStartTime.isAM)) {	
		if (eventAStartTime.hour < eventBStartTime.hour) {
			return -1;
		} else if (eventAStartTime.hour > eventBStartTime.hour) {
			return 1;
		} else {
			if (eventAStartTime.minute <= eventBStartTime.minute) {
				return -1;
			} else if (eventAStartTime.minute > eventBStartTime.minute) {
				return 1;
			}			
		} 
	
	} else if (eventAStartTime.isAM && !eventBStartTime.isAM) {
		//eventA comes before eventB
		return -1;	
	//Case where eventA is in the PM and eventA is in the AM	
	} else {
		//eventA comes after eventB
		return 1;
	}
}


if (window.location.pathname === "/playlists") {
	$("#editEventForm").hide();
	// First, set the logout link 
	$("#logoutLink").attr('href', "http://localhost:" + window.location.port + "/vibe");

	// Next, retrieve the user's code to send to the back-end from the URL	
	var uri = new URI(window.location.href);
	var urlParams = uri.search(true);
	var codeFromURL = urlParams.code;
	var postParams = {code: JSON.stringify(codeFromURL)};

	$.post("/code", postParams, function(responseJSON) {
	  // Back-end sends back the user's display name
	  var backendParams = responseJSON.split(",");

	  if (backendParams === "") {
	  	alert("Error loading user information. Please logout and login again!");
	  }

	  var username = backendParams[0].slice(1, backendParams[0].length).trim(); // remove the [
	  var playlistURI = "https://embed.spotify.com/?uri=" 
	  + backendParams[1].slice(0, backendParams[1].length - 1).trim(); // remove the ]

	  document.cookie = "name=" + username; // Store the username in a cookie

	  console.log("user: " + username);
	  console.log("playlist: " + playlistURI);

	  if (backendParams != null) {
	    name = username;
		$("#displayname").html(name);
	  }

	  // $("#playlist").attr('src', playlistURI);
	
	}); // end access token code post

	
	/* Handles adding an event */
	$("#AddNewEvent").click(function() {
		console.log("Adding new event...");
		addEvent();
    }); // end add new click handler

	$(".anEvent").click(function() {
		console.log("Clicked on an event");
		var eventID = this.id;
		console.log("Current event: " + currentEvent + "[id: " + eventID + "]");
		createDropdown(eventID);
	}); // end click on event handler

}


//Object to mirror EventTime from the backend
function EventTime(eventTime) {
	this.hour = Number(eventTime.hour);
	this.minute = Number(eventTime.minute);
	this.isAM = Boolean(eventTime.isAM);
}

//Object to mirror CalendarEvent from the backend
function CalendarEvent(event) {
	this.start = new EventTime(event.start);
	this.end = new EventTime(event.end);
	this.name = event.name;
	this.id = event.id;
	this.playlistId = event.playlistID;
}

//Creates a new event on the sidebar
function renderCalander(event){
	var timePeriod = "";
	if(event.start.isAM){
		timePeriod = "am";
	} else {
		timePeriod = "pm";
	}
	
	var eventTimeline = $('#calanderEvents');
	var eventTimelineItems = eventTimeline.children();
	var eventHTMLString = 
		"<li id='" + event.id + "' class='anEvent'> " +
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

	eventTimeline.append(eventHTMLString);
	
	for(var i = 0; i < eventTimelineItems.length; i++){
		if (eventComparator(event, eventsArray[i]) == -1){
			continue;
		} else {
			var priorEvent = eventsArray[i];
			eventTimeline.insertAfter(eventsArray[i], eventHTMLString);
		}
	}
	
}

/* When the user clicks on the button, 
toggle dropdown content using show and hide  */
function createDropdown(id) {
    document.getElementById(id).classList.toggle("show");
}




// <div id='view-playlist-panel'> ... </div>
//	    <div id='customize-playlist-panel'> ... </div>
//	    <div id='use-spotify-playlist-panel'> ... </div>
//	    <div id='edit-event'> ... </div>



var eventComparator = function(eventA, eventB) {
	if (eventA == null || eventB == null) {
		return -1;
	}

	var eventAStartTime = eventA.start;
	var eventBStartTime = eventB.start;
	
	//Case where eventA and eventB are both in the AM or both PM
	if (eventAStartTime.isAM && eventBStartTime.isAM 
			|| (!eventAStartTime.isAM && !eventBStartTime.isAM)) {	
		if (eventAStartTime.hour < eventBStartTime.hour) {
			return -1;
		} else if (eventAStartTime.hour > eventBStartTime.hour) {
			return 1;
		} else {
			if (eventAStartTime.minute <= eventBStartTime.minute) {
				return -1;
			} else if (eventAStartTime.minute > eventBStartTime.minute) {
				return 1;
			}			
		} 
	
	} else if (eventAStartTime.isAM && !eventBStartTime.isAM) {
		//eventA comes before eventB
		return -1;	
	//Case where eventA is in the PM and eventA is in the AM	
	} else {
		//eventA comes after eventB
		return 1;
	}
}

if (window.location.pathname === "/playlists") {
	// First, set the logout link 
	$("#logoutLink").attr('href', "http://localhost:" + window.location.port + "/vibe");

	// Next, retrieve the user's code to send to the back-end from the URL	
	var uri = new URI(window.location.href);
	var urlParams = uri.search(true);
	var codeFromURL = urlParams.code;
	var postParams = {code: JSON.stringify(codeFromURL)};

	$.post("/code", postParams, function(responseJSON) {
	  // Back-end sends back the user's display name
	  var backendParams = responseJSON.split(",");

	  if (backendParams === "") {
	  	alert("Error loading user information. Please logout and login again!");
	  }

	  var username = backendParams[0].slice(1, backendParams[0].length).trim(); // remove the [
	  var playlistURI = "https://embed.spotify.com/?uri=" 
	  + backendParams[1].slice(0, backendParams[1].length - 1).trim(); // remove the ]

	  document.cookie = "name=" + username; // Store the username in a cookie

	  console.log("user: " + username);
	  console.log("playlist: " + playlistURI);

	  if (backendParams != null) {
	    name = username;
		$("#displayname").html(name);
	  }

	$("#playlist").attr('src', playlistURI);
	
	}); // end code post

	// Handles clicking on each event and generating spotify playlist
	// $(".eventClick").on('click', function() {
	// 	console.log("Clicked id = " + this.id);
	// 	var eventID = this.id;
	// 	var eventClickParams = {eventID: JSON.stringify(eventID)};
	// 	$.post("/eventClick", eventClickParams, function(responseJSON) {
	// 		var eventClickResponse = responseJSON;

	// 		console.log("Event click response : " + eventClickResponse);

	// 	 	var uri = "https://embed.spotify.com/?uri=" + eventClickResponse; // remove the ]

	// 	  	console.log("Event id: " +  eventID + " | playlist: " + playlistURI);
	// 	});
	// })
	
	/* Handle clicking an event */
	
	$("#AddNewEvent").click(function() {
		console.log("Adding new event...");

		// necessary for some browser problems (saw on jquery's website)

    if (!event.target.matches('collapse')) {

	    var dropdowns = document.getElementsByClassName('collapse');
	    var i;
	    for (i = 0; i < dropdowns.length; i++) {
	      var openDropdown = dropdowns[i];
	      if (openDropdown.classList.contains('show')) {
	        openDropdown.classList.remove('show');
	      }
	    }
	}
}

/**
 * Adds an event to the list of events on the front-end
 */
function addEvent() {
	// necessary for some browser problems (saw on jquery's website)
		$.valHooks.textarea = {
		  get: function( elem ) {
		    return elem.value.replace( /\r?\n/g, "\r\n" );
		  }
		};

		var eventName = $('#name').val();
		var startTime = $('#startTime').val();
		var endTime = $('#endTime').val();
		var startAP;
		var endAP;

		// Check to see if start time is AM or PM
		if ($('#startAM').is(':checked')) {
			startAP = true;
		} else {
			startAP = false;
		}
		
		// Check to see if end time is AM or PM
		if ($('#endAM').is(':checked')) {
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

	    		// 2. Make calendar event object from responseObject
	    		var newEvent = new CalendarEvent(responseObject);
	    		currentEvent = newEvent;
	    		
	    		// 3. Add new calendar event to user's list
	    		eventsArray.push(newEvent);
	    		
	    		// 4. sort the list 
	    		eventsArray.sort(eventComparator);
	    		
	    		//5. Render calendar
	    		renderCalander(currentEvent);
	    	});
		}
}