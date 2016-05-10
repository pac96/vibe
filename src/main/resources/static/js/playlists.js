var name = "";
var eventsArray = [];
var occurrenceArray = [];
var currentEvent;
var currentEventID;
var otherContent = $("div.otherContent");
var editDiv;
var customizeDiv;
var panel;
var eventModal;
var playlistDiv;
var eventToTimeout = new Map();
var playlist = $("#playlist");
var port = window.location.port;
// var ProgressBar = require('progressbar.js');




if (window.location.pathname === "/playlists") {
	var home = "http://localhost:" + port + "/vibe";
	var uri = new URI(window.location.href);
	var urlParams = uri.search(true);
	editDiv = $("#editDiv");
	customizeDiv = $("#customizePlaylistForm");
	panel = $("#view-playlist-panel");
	playlistDiv = $(".playlistDiv");

	if (urlParams.error === "access_denied") {
		// Send the user back to the login page
		window.location.href = home;
	}

	// var d = new Date();
	// document.getElementById("date").innerHTML = d.toDateString();
	document.getElementById("date").innerHTML = moment().format("dddd, MMMM Do YYYY");


	otherContent.hide();

	// First, set the logout link 
	$("#logoutLink").attr('href', home);

	var userCode;

	userCode = urlParams.code;

	var postParams = {code: userCode};

	// Retrieve the username from the back-end
	$.post("/code", postParams, function(infoMap) {
	  // Back-end sends back the user's display name
	  var backendInfo = JSON.parse(infoMap);
	  var username = backendInfo.username;

	  console.log("Username: " + username);
	  $("#displayname").append(username);

	  loadCachedEvents(backendInfo.cachedEvents);

	  // Populates the dropdown selection for user playlists
	  populateUserPlaylists();	  
	}); // end access token code post
}	

/* Handles adding an event */
$("#AddNewEvent").click(function() {
	addEvent();
}); // end add new click handler


/*
 * When the user clicks on an event, initiate the dropdown
 * and display the name of the event in the main content panel section
 */

$(document).on('click', '.anEvent', function() {
	// $(".loading").attr('id', "loadingBar");
	// // Loading bar
	// var bar = new ProgressBar.Line("#loadingBar", {
	//   strokeWidth: 5,
	//   easing: 'easeInOut',
	//   duration: 1000,
	//   color: 'green',
	//   trailColor: '#eee',
	//   trailWidth: 10,
	//   svgStyle: {width: '80%', height: '100%'},
	//   text: {
	//     style: {
	//       // Text color.
	//       // Default: same as stroke color (options.color)
	//       color: '#999',
	//       position: 'absolute',
	//       right: '0',
	//       top: '30px',
	//       padding: 0,
	//       margin: 0,
	//       transform: null
	//     },
	//     autoStyleContainer: false
	//   },
	//   from: {color: '#FFEA82'},
	//   to: {color: '#ED6A5A'},
	//   step: (state, bar) => {
	//     bar.setText(Math.round(bar.value() * 100) + ' %');
	//   }
	// });

	// bar.animate(1.0);

	$("div.bar").removeClass("hiddenDiv");
	currentEventID = this.id;
	createDropdown(currentEventID);
	var eventObject = getEvent(currentEventID);
	console.log("Current event: " + eventObject.name);


	if (eventObject.playlistURI == null) {
		// Retrieve the playlist URI from the backend and show it
		showPlaylist(currentEventID);			
	} else {
		// bar.animate(1.0); // start loading bar
		// playlist.attr('src', "https://embed.spotify.com/?uri=" + eventObject.playlistURI);
		$("div.bar").addClass("hiddenDiv");
		playlist.fadeIn("slow");
		$("#hidePlaylist").fadeIn("slow");
	}
}); // end click on event handler


$(document).on('click', '#hidePlaylist', function() {
	hidePlaylist();
});


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
	this.playlistURI = event.playlistURI;
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



/////////////////////////////////////////////////////////////////////////
// FUNCTION DECLARATIONS
////////////////////////////////////////////////////////////////////////

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

	var targetID = "dropdown_" + event.id;

	var htmlCode = htmlDropdown(targetID, timePeriod, event);
	// $("ul.collapse").css('list-style-type', 'none');

	var $eventHTML = $("<li>").attr({
		id: event.id,
		class: "anEvent"
	});

	$eventHTML.append(htmlCode);

	// Find all li tags and dynamically remove the bullet points
	// $("ul.collapse").css('list-style-type', 'none');
	// $("ul.collapse li").css('cursor', 'pointer');


	// Fix cursor
	// eventTimeline.find("li").css('cursor', 'pointer');
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
		var eventFormat = /^[a-zA-Z]+$/;
		var timeFormat = /^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/;
		
		// if ()

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
	    		if (responseObject.success === true) {
	    			// 2. Make calendar event object from responseObject
		    		var newEvent = new CalendarEvent(responseObject.event);

		    		// 3. Add new calendar event to user's list
		    		eventsArray.push(newEvent);
		    		// 4. sort the list 
		    		eventsArray.sort(compareEvents);
		    		// 5. Render calendar
		    		renderCalendar(newEvent);

		    		occurrenceArray.push(newEvent);
		    		occurrenceArray.sort(compareEvents);

	   				nextEventPopup();	
	    		} else {
	    			var $msg = $("<p>", {
						class: "contentMsg", 
						id: "errorMsg"
					});

					$msg.append("Add event failed, check that the time is formatted properly");
					otherContent.html($msg);
				    otherContent.fadeIn('slow');

				    setTimeout(function() {
					    otherContent.fadeOut('slow');
				    }, 2000);
	    		}
	    		
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
		"<span class='eventDesc'>" + cEvent.moment.format("h:mm") + " " + timePeriod 
		+ " | " + cEvent.name + " </span>" +
		"<i class='fa fa-fw fa-caret-down'></i></a>" +
		"<ul id='" + dataTargetID + "' class='collapse no-list-style'>" +
			// "<li id='viewPlaylist'>" +
			// 	"<a>View Playlist" + "</a>" +
			// "</li>" +
			"<li id='customizePlaylist'>" +
				"<a>Customize Playlist</a>" +
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



function loadCachedEvents(cachedEvents) {

	for (var i = 0; i < cachedEvents.length; i++) {
		var cachedEvent = new CalendarEvent(cachedEvents[i]);
		eventsArray.push(cachedEvent);
		occurrenceArray.push(cachedEvent);
		
		eventsArray.sort(compareEvents);
		occurrenceArray.push(compareEvents);

		renderCalendar(cachedEvent);

		nextEventPopup();	
	}
}



function populateUserPlaylists() {
	$.post("/getAllPlaylists", function(jarray) {
		// returns a jarray with an object at each index having access to a name and URI
		var playlists = JSON.parse(jarray);
		var names = [];
		var $dropdown = $("#playlistDropdown");

		for (var i = 0; i < playlists.length; i++) {
			var currentPlaylist = playlists[i];
			var $plElt = $("<option>").attr({
				id: currentPlaylist.uri,
				class: "plName",
				value: currentPlaylist.name
			}).append(currentPlaylist.name);
			$plElt.css('overflow-y', 'scroll');
			$dropdown.append($plElt);
		}
	});
}


function hidePlaylist() {
	playlist.removeClass("loading");
	playlist.fadeOut("fast");	
    $("#hidePlaylist").fadeOut("fast");
}