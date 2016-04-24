//Toggle suggestions button and submit button
$("#showButton").text("Toggle Suggestions");
$("#submitButton").text("Find shortest path!");

//React to typing:
$("#textField1").on('keyup', function() {
	var textSoFar = $("#textField1").val();
	var postParameters = {"text": textSoFar};
	
	//Send the typed text to the back end
	$.post("/suggestions", postParameters, function(responseJSON){
		//Display the returned results
		
		responseObject = JSON.parse(responseJSON);
		var s = responseObject.suggestions;
		var numSuggestions = responseObject.numSuggestions;
		//Show results
		var combo = "<p>" + s[0] + "</p>";
		var i;
		for (i = 1; i < numSuggestions; i++){
			combo += "<p>" + s[i] + "</p>";
		}
		$( "div.dropdown-content1" ).html( combo );
		
	 });
});

//React to typing:
$("#textField2").on('keyup', function() {
	var textSoFar = $("#textField2").val();
	var postParameters = {"text": textSoFar};
	
	//Send the typed text to the back end
	$.post("/suggestions", postParameters, function(responseJSON){
		//Display the returned results
		responseObject = JSON.parse(responseJSON);
		var s = responseObject.suggestions;
		var numSuggestions = responseObject.numSuggestions;
		//Show results
		var combo = "<p>" + s[0] + "</p>";
		var i;
		for (i = 1; i < numSuggestions; i++){
			combo += "<p>" + s[i] + "</p>";
		}
		$( "div.dropdown-content2" ).html( combo );
		
	 });
});

//Toggle suggestions
$("#showButton").on('click', function() {
	$( "div.dropdown-content1" ).toggle();
	$( "div.dropdown-content2" ).toggle();
});

//Get results
$("#submitButton").on('click', function() {
	//Get text
	var actor1 = $("#textField1").val();
	var actor2 = $("#textField2").val();
	var postParameters = {"actor1": actor1, "actor2" : actor2};
	//Send the typed text to the back end
	$.post("/results", postParameters, function(responseJSON){
		//Display the returned results
		responseObject = JSON.parse(responseJSON);
		var r = responseObject.results;
		var numResults = responseObject.numResults;
		//Show results
		var combo = "<p>" + r[0] + "</p>";
		var i;
		for (i = 1; i < numResults; i++){
			combo += "<p>" + r[i] + "</p>";
		}
		$( "div.baconResults" ).html( combo );
		
	 });
});