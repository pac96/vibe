<#assign content>

<h1 align = "center"> Bacon </h1>
<body>
	<div align = "center">
		<p> Find the shortest path between actors!
  		<p> <button id="showButton" align = "center" class = "button"></button> </p>
  		<p> <input type="text" class = "textField" id="textField1" placeholder="First actor"></input></p>
  		<div class="dropdown">
  			<div id="myDropdown" class="dropdown-content1">
				<p> Suggestions!</p>
    		</div>
		</div> 	
  		<p><input type="text" class = "textField" id="textField2" placeholder="Second actor"></input></p>
		<div class="dropdown">
  			<div id="myDropdown" class="dropdown-content2">
				 <p> Suggestions! </p>
    		</div>
		</div>
		<p> <button id="submitButton" class = "button"></button> </p>
		<div class="baconResults"> (Warning, this may take a moment) </div>
	</div>
</body>

</#assign>
<#include "main.ftl">
