var name = "";
var eventsArray = [];
var occurrenceArray = [];
var currentEvent;
var currentEventID;
var otherContent = $("div.otherContent");
var editEventForm;
var customizePlaylistForm;
var eventModal;
var eventToTimeout = new Map();

//setInterval(function() {
//	var timeNow = new Date().getTime();
//	console.log(timeNow);
//	for (var i = 0; i < eventsArray.length; i++) {
//		if (d === eventsArray[i].startDate) {
//			alert("It's time for " + eventsArray[i].name);
//		}
//	}
//
// }, 1000);

function alertUserForEvent(calendarEvent) {
	// console.log("It's time for " + calendarEvent.name);
	var m = moment();
	var m1Format = m.format("h:mm a");
	var calendarMFormat = calendarEvent.moment.format("h:mm a");

	console.log(m1Format);
	console.log(calendarMFormat);

	if (m.isSame(calendarEvent.moment)) {
		console.log("IT IS TIME FOR " + calendarEvent.name);
	}

}



function compareEvents(eventA, eventB) {
	if (eventA == null || eventB == null) {
		return -1;
	}

	if (eventA.startDate < eventB.startDate) {
		return -1;
	} else {
		return 1;
	}
}

var eventComparator = function(eventA, eventB) {

	if (eventA == null || eventB == null) {
		return -1;
	}

	var eventAMoment = eventA.moment;
	var eventBMoment = eventB.moment;

	// If event A is in the AM and event B is in the PM, event A comes before event B
	// if (eventA.isAM && !eventB.isAM) {
	// 	return -1;
	// } 

	// // If event G is in the AM and event A is in the PM, event B comes before event A
	// if (eventB.isAM && !eventA.isPM) {
	// 	return 1;
	// }

	if (eventBMoment.isAfter(eventA.moment)) {
		return -1;
	} else {
		return 1;
	}

	// var eventAStartTime = eventA.start;
	// var eventBStartTime = eventB.start;
	
	// //Case where eventA and eventB are both in the AM or both PM
	// if (eventAStartTime.isAM && eventBStartTime.isAM 
	// 		|| (!eventAStartTime.isAM && !eventBStartTime.isAM)) {	
	// 	if (eventAStartTime.hour < eventBStartTime.hour) {
	// 		return -1;
	// 	} else if (eventAStartTime.hour > eventBStartTime.hour) {
	// 		return 1;
	// 	} else {
	// 		if (eventAStartTime.minute <= eventBStartTime.minute) {
	// 			return -1;
	// 		} else if (eventAStartTime.minute > eventBStartTime.minute) {
	// 			return 1;
	// 		}			
	// 	} 
	
	// } else if (eventAStartTime.isAM && !eventBStartTime.isAM) {
	// 	//eventA comes before eventB
	// 	return -1;	
	// //Case where eventA is in the PM and eventA is in the AM	
	// } else {
	// 	//eventA comes after eventB
	// 	return 1;
	// }
}


if (window.location.pathname === "/playlists") {
	var d = new Date();
	document.getElementById("date").innerHTML = d.toDateString();

	otherContent.hide();
	editEventForm = $("#editEventForm");
	editEventForm.hide();
	customizePlaylistForm = $("#customizePlaylistForm");
	customizePlaylistForm.hide();

	// First, set the logout link 
	$("#logoutLink").attr('href', 
		"http://localhost:" + window.location.port + "/vibe");

	// If the cookie doesn't have the code already
	// Next, retrieve the user's code to send to the back-end from the URL	
	var uri = new URI(window.location.href);
	var urlParams = uri.search(true);
	var codeFromURL = urlParams.code;
	var postParams = {code: JSON.stringify(codeFromURL)};

	// Retrieve the username from the back-end
	$.post("/code", postParams, function(username) {
	  // Back-end sends back the user's display name
//	  var backendParams = responseJSON.split(",");
//
//	  if (backendParams === "") {
//	  	alert("Error loading user information. Please logout and login again!");
//	  }
//
//	  var username = backendParams[0].slice(1, backendParams[0].length).trim(); // remove the [
//	  var playlistURI = "https://embed.spotify.com/?uri=" 
//	  + backendParams[1].slice(0, backendParams[1].length - 1).trim(); // remove the ]
//
//	  document.cookie = "name=" + username; // Store the username in a cookie
//
//	  console.log("user: " + username);
//	  console.log("playlist: " + playlistURI);
//
//	  if (backendParams != null) {
//	    name = username;
//		$("#displayname").html(name);
//	  }
//
//	  $("#playlist").attr('src', playlistURI);
		
	  console.log("Username: " + username);
	  $("#displayname").html(username);
	
	}); // end access token code post
}

/* Handles adding an event */
$("#AddNewEvent").click(function() {
	console.log("Adding new event...");
	addEvent();
}); // end add new click handler


$(document).on('click', '.anEvent', function() {
	currentEventID = this.id;
	createDropdown(currentEventID);
}); // end click on event handler


/////////////////////////////////////
// Constructor Declarations
////////////////////////////////////

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
	var m = moment();
	var startD = new Date();

	if (!this.start.isAM) {
		if (this.start.hour == 12) {
			m.hour(this.start.hour);
			startD.setHours(this.start.hour);
		} else {
			m.hour(this.start.hour + 12);
			startD.setHours(this.start.hour + 12);
		}
	} else {
		if (this.start.hour == 12) {
			startD.setHours(0);
			m.hour(0);
		} else {
			m.hour(this.start.hour);
			startD.setHours(this.start.hour);
		}
	}

	m.minute(this.start.minute);
	m.second(0);
//	m.millisecond(0);
	startD.setMinutes(this.start.minute);
	startD.setSeconds(0);
	startD.setMilliseconds(0);

	this.moment = m;
	this.startDate = startD;
}



/////////////////////////////////////
// Function Declarations
////////////////////////////////////
/**
 * Renders the calendar so that we can 
 * @param  {CalendarEvent} event - the calendar event object 
 *                               we got from the backend
 */
function renderCalendar(event){
	var timePeriod = "";
	if(event.start.isAM){
		timePeriod = "am";
	} else {
		timePeriod = "pm";
	}
	
	var eventTimeline = $('#calendarEvents');

	var targetID = "dropdown-" + event.id;

	var htmlCode = htmlDropdown(targetID, timePeriod, event);

	var $eventHTML = $("<li>").attr({
		id: event.id,
		class: "anEvent"
	});

	$eventHTML.append(htmlCode);

	// Find all li tags and dynamically remove the bullet points
	$(".collapse").find("li").css('list-style-type', 'none');;

	// Fix cursor
	eventTimeline.find("li").css('cursor', 'pointer');
	var appended = false;

	for(var i = 0; i < eventsArray.length; i++){
		var currEvent = eventsArray[i];
		if (compareEvents(event, currEvent) == -1){
			$eventHTML.insertBefore(eventTimeline.find("#" + currEvent.id));
			appended = true;
			break;
		} else {
			continue;
		}
	}	

	if (!appended) {
		eventTimeline.append($eventHTML);
	}	
}


/* When the user clicks on the button, 
toggle dropdown content using show and hide  */
function createDropdown(id) {
    document.getElementById(id).classList.toggle("show");
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

	    		// 3. Add new calendar event to user's list
	    		eventsArray.push(newEvent);
	    		occurrenceArray.push(newEvent);
	    		
	    		// 4. sort the list 
	    		eventsArray.sort(compareEvents);
	    		occurrenceArray.push(compareEvents);

	    		//5. Render calendar
	    		renderCalendar(newEvent);

   				nextEventPopup();
	    	});
		}
}

/**
 * Creates a string of html code necessary for dropdown creation
 * @param  {String} dataTargetID - id needed for data-target
 * @param  {String} timePeriod   - "am" or "pm"
 * @param  {CalendarEvent} event  - the event you need to get info from
 * @return {String}              - the html code necessary for dropdown creation
 */
function htmlDropdown(dataTargetID, timePeriod, cEvent) {
	var htmlStr = 
	"<a href='javascript:;' data-toggle='collapse' data-target='#" + dataTargetID + "'>" +
		"<i class='fa fa-fw fa-arrows-v'></i> " +
		// event.start.hour + ":" +  event.start.minute 
		"<p class='eventDesc'>" + cEvent.moment.format("h:mm") + " " + timePeriod 
		+ " | " + cEvent.name + " </p>" +
		"<i class='fa fa-fw fa-caret-down'></i></a>" +
		"<ul id='" + dataTargetID + "' class='collapse'>" +
			"<li id='viewPlaylist'>" +
				"<a>View Playlist" + "</a>" +
			"</li>" +
			"<li id='customizePlaylist'>" +
				"<a>Customize Playlist</a>" +
			"</li>" +
			"<li id='usePlaylist'>" +
				"<a>Use Spotify Playlist</a>" +
			"</li>" +
			"<li id='editEvent'>" +
				"<a>Edit Event</a>" +
			"</li>" +
			"<li id='deleteEvent'>" +
				"<a>Delete Event</a>" +
			"</li>" +
		"</ul>" +
	"</a>";

	return htmlStr;
}


/**
 * Initiates the search for a next event 
 */
function nextEventPopup() {
	var foundNext = true;
	console.log("It's time to get the next important event");
	var nextEvent = findNextEvent();

	if (nextEvent == null) {
		foundNext = false;
	}

	if (foundNext) {
		console.log("Next event time: " + nextEvent.startDate.toTimeString());

		var nextTime = nextEvent.startDate.getTime();
		var timeNow = new Date().getTime();

		var millisecondOffset = nextTime - timeNow;
		console.log("Num ms till next event: " + millisecondOffset);
		// Remove this event from the occurrence array now that 
		occurrenceArray.splice(occurrenceArray.indexOf(nextEvent), 1);
		// Sort the array again to make sure everything is still in order
		occurrenceArray.sort(compareEvents);

		var timeout = setTimeout(function() {
			// 1. Write the text to display within the box
			$("#modal-event-name").text(nextEvent.name + " is happening now!");
			$("#modal-msg").html("REMINDER: <strong>" 
				+ nextEvent.name + "</strong> is happening right now!");

			// 2. Show the popup box
			$("#vibe-modal").modal('show');
		}, millisecondOffset);

		console.log("Timeout handler/id " + timeout);
		eventToTimeout.set(nextEvent.id, timeout);
	}
}

/**
 * Looks through the occurrence array to 
 * find the next event (timewise)
 */
function findNextEvent() {
	var nextEvent;

	for (var i = 0; i < occurrenceArray.length; i++) {
		var dateNow = new Date();
		if (occurrenceArray[i].startDate > dateNow) {
			nextEvent = occurrenceArray[i];
			break;
		}
	}

	return nextEvent;
}