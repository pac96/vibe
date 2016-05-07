var name = "";
var eventsArray = [];
var currentEvent;
var currentEventID;
var otherContent = $("div.other-content");

// setInterval(function() {
// 	var m = moment();
// 	var isSameMin = m.isSame(moment("") , "minute");

// }), 1000;

function compareEvents(eventA, eventB) {
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

	console.log("EA " + eventA.startDate);
	console.log("EB " + eventB.startDate);

	if (eventA.startDate < eventB.startDate) {
		// console.log(eventA.name  + " is before " + eventB.name);
		return -1;
	} else {
		// console.log(eventA.name  + " is after " + eventB.name);
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
	$("#editEventForm").hide();
	// First, set the logout link 
	$("#logoutLink").attr('href', 
		"http://localhost:" + window.location.port + "/vibe");

	// If the cookie doesn't have the code already
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


	$(document).on('click', '.anEvent', function() {
		currentEventID = this.id;
		console.log("Current event id: " + currentEventID);
		createDropdown(currentEventID);
	}); // end click on event handler
}


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
	startD.setMinutes(this.start.minute);
	startD.setSeconds(0);

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
	// var numEventItems = eventTimeline.children().length;

	var targetID = "dropdown-" + event.id;

	var htmlCode = htmlDropdown(targetID, timePeriod, event);

	var eventHTMLString = "<li id='" + event.id + "' class='anEvent'>"
	 + htmlCode + "</li>";

	var $eventHTML = $("<li>").attr({
		id: event.id,
		class: "anEvent"
	});

	$eventHTML.append(htmlCode);
	var eltAdded = false;

	for(var i = 0; i < eventsArray.length; i++){
		var currEvent = eventsArray[i];
		if (compareEvents(event, currEvent) == -1){
			$eventHTML.insertBefore(eventTimeline.find("#" + currEvent.id));
			eltAdded = true;
			break;
		} else {
			continue;
		}
	}	

	if (!eltAdded) {
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
	    		
	    		// 4. sort the list 
	    		eventsArray.sort(compareEvents);
	    		for (var i = 0; i < eventsArray.length; i++) {
	    			console.log(i+1 + ". " + eventsArray[i].start.hour + ":" + eventsArray[i].start.minute);
	    		}

	    		//5. Render calendar
	    		renderCalendar(newEvent);
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
		cEvent.moment.format("h:mm") /*eventMoment.format("h:mm")*/ + " " + timePeriod 
		+ " | " + cEvent.name + " " +
		"<i class='fa fa-fw fa-caret-down'></i></a>" +
		"<ul id='" + dataTargetID + "' class='collapse'>" +
			"<li id='viewPlaylist'>" +
				"<a href='#'>View Playlist" + "</a>" +
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
	"</a>";

	return htmlStr;
}