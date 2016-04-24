var name = "";
var eventsArray = [];



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
}

//Creates a new event on the sidebar
function renderCalander(event){
	var timePeriod = "";
	if(event.start.isAM){
		timePeriod = "am";
	} else {
		timePeriod = "pm";
	}
	
	var eventTimeline = document.getElementById('#calanderEvents'); //.parentNode?
	var eventHTMLString = 
		"<li id='" + event.id + "' class='eventClick'> " +
		"<a href='#'>" +
		"<input type='button' name='settings' value='+' class='settings-button'/> " +
		"&nbsp " +
		event.start.hour + timePeriod + " | " + event.name +
		"</a> </li>";
	
	for(var i = 0; i < eventsArray.length; i++){
		if (eventComparator(event, eventsArray[i]) == -1){
			continue;
		} else {
			var priorEvent = eventsArray[i];
			eventTimeline.insertAfter(eventsArray[i], eventHTMLString);
		}
	}
}



var eventComparator = function (eventA , eventB) {
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
	var uri = new URI(window.location.href);
	var urlParams = uri.search(true);
	// console.log("User code: " + urlParams.code);

	var codeFromURL = urlParams.code;
	var postParams = {code: JSON.stringify(codeFromURL)};

	$.post("/code", postParams, function(responseJSON) {
	  // Back-end sends back the user's display name
	  var backendParams = responseJSON.split(",");
	  console.log(backendParams[0]);
	  console.log(backendParams[1]);


	  if (backendParams === "") {
	  	alert("Error loading user information. Please logout and login again!");
	  }

	  var username = backendParams[0].slice(1, backendParams[0].length); // remove the [
	  var playlistURI = "https://embed.spotify.com/?uri=" 
	  + backendParams[1].slice(0, backendParams[1].length - 1).trim(); // remove the ]

	  console.log("user: " + username);
	  console.log("playlist: " + playlistURI);

	  if (backendParams != null) {
	    name = username;
		$("#displayname").html(name);
	  }

	$("#playlist").attr('src', playlistURI);
	
	}); // end code post

	console.log("We got out the post");

	// Handles clicking on each event and generating spotify playlist
	$(".eventClick").on('click', function() {
		console.log("Clicked id = " + this.id);
		var eventID = this.id;
		var eventClickParams = {eventID: JSON.stringify(eventID)};
		$.post("/eventClick", eventClickParams, function(responseJSON) {
			var eventClickResponse = responseJSON;

			console.log("Event click response : " + eventClickResponse);

		 	var uri = "https://embed.spotify.com/?uri=" + eventClickResponse; // remove the ]

		  	console.log("Event id: " +  eventID + " | playlist: " + playlistURI);
		});
	})
	
	$("#AddNewEvent").on('click', function() {
		console.log("Adding new event...");

		// necessary for some browser problems (saw on jquery's website)
		$.valHooks.textarea = {
		  get: function( elem ) {
		    return elem.value.replace( /\r?\n/g, "\r\n" );
		  }
		};

		var eventName = $('#eventName').val();
		var startTime = $('#startTime').val();
		var endTime = $('#endTime').val();
		var startAP;
		var endAP;
		console.log(eventName);
		console.log(startTime);
		console.log(endTime);
		
		if ($('#startAP').val == "am-start"){
			startAP = true;
		} else {
			startAP = false;
		}
		
		if ($('#endAP').val == "am-end"){
			endAP = true;
		} else {
			endAP = false;
		}
		
		eventFormat = /^[a-zA-Z]+$/;
		timeFormat = /^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/;
		
		if(eventName == '' || startTime == '' || endTime == '' || startAP == '' || endAP == '') {
			alert("One or more event fields are empty");
		} else if(eventName != '' && !eventName.match(eventFormat)) {
			alert("Invalid Event Name: " + eventName.val);
		} else if(startTime != '' && !startTime.match(timeFormat)) {
	    	alert("Invalid Start Time: " + startTime.val);
	    } else if(endTime != '' && !startTime.match(timeFormat)) {
	    	alert("Invalid End Time: " + endTime.val);
	    } else {
	    	
	    	var postParameters = {
	    			start : startTime ,
	    			end : endTime ,
	    			startAMPM : startAP ,
	    			endAMPM : endAP,
	    			name : eventName
	    	};
	    	
	    	$.post("/newEvent", postParameters, function(response) {
	    		//1. send stuff to back end and store in responseObject
	    		var responseObject = JSON.parse(responseJSON);
	    		//2. Make calendar event object from responseObject
	    		var newEvent =  new CalendarEvent(responseObject);
	    		
	    		//3. Add new calendar event to user's list
	    		eventsArray.push(newEvent);
	    		
	    		//4. sort the list 
	    		eventsArray.sort(eventComparator);
	    		
	    		//5. Render calendar 
	    		renderCalendar(newEvent);
	    		
	    	});
	    }
	    
		});
	
	$.post("/code", postParams, function(responseJSON) {
		var eventRequest = $('#eventForm');
		
	});

}