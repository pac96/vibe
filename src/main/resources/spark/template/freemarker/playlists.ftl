<#assign content>

<form action="/code" method="POST" id="hiddenForm">

</form>


<div id="wrapper">
	<!-- Sidebar -->
	<div id="sidebar-wrapper">
	    <ul class="sidebar-nav" id = "calanderEvents">
	        <li class="sidebar-brand" id="date">
	            <a href="#">
	    	    <script>
		            var d = new Date();
		            document.getElementById("date").innerHTML = d.toDateString();
				</script>
	            <p id="date"></p>
	            </a>
	        </li>
	        <li class="eventClick">
	            <a href="#"> 
	            <input type='button' name='settings' value="+" class = "settings-button"/> 
	            &nbsp
	            7am | Running 
	            </a>
	            
	        </li>
	        <li class="eventClick">
	            <a href="#">
	            <input type='button' name='settings' value="+" class = "settings-button"/> 
	            &nbsp
	            8am | Drive to Work
	            </a>
	        </li>
	        <li>
	        	<a href="#">
	        	<input type='button' name='settings' value="+" class = "settings-button"/> 
	            &nbsp
	            9am | Work
	            </a>
	        </li>
	        <li>
	        	<a href="#">
	        	<input type='button' name='settings' value="+" class = "settings-button"/> 
	            &nbsp
	            5pm | Drive Home
	            </a>
	        </li>
	        <li>
	        	<a href="#">
	        	<input type='button' name='settings' value="+" class = "settings-button"/> 
	            &nbsp
	            6pm | Dinner with Friends
	            </a>
	        </li>
	        <li>
	        	<a href="#">
	        	<input type='button' name='settings' value="+" class = "settings-button"/> 
	            &nbsp
	            7pm | Reading
	            </a>
	        </li>
	        <li>
	        	<a href="#">
	        	<input type='button' name='settings' value="+" class = "settings-button"/> 
	            &nbsp
	            9pm | Nightly Stretch
	            </a>
	        </li>
	        <hr>
	        <form id='eventForm' form method="POST" action="/newEvent">
	            <label for='name' > <font color="white"> Event Name: </font> </label>
	            <input type='text' id='eventName' name='name' id='name' maxlength="30" class = "form-textbox"/><br/>

	            <br>
	            <label for='start-time' ><font color="white">Start Time:</font></label> 
	            <input type='text' id='startTime' name='startTime' maxlength="5" class = "form-textbox-time-start"/> 
	            &nbsp <input type="radio" id='startAP' name="time-selection-start" value="am-start" checked> <font color="white"> am </font>
	            &nbsp <input type="radio" id='startAP' name="time-selection-start" value="pm-start"> <font color="white"> pm </font>

	            <br><br>
	            <label for='end-time' ><font color="white">End Time:</font></label> 
	            <input type='text' id='endTime' name='endTime' maxlength="5" class = "form-textbox-time-end"/>
	            &nbsp <input type="radio" id='endAP' name="time-selection-end" value="am-end" checked> <font color="white"> am </font>
	            &nbsp <input type="radio" id='endAP' name="time-selection-end" value="pm-end"> <font color="white"> pm </font>
	            
	            <br><br>
	            &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp 
	            <input type='button' name='AddNewEvent' value='Add New Event' id="AddNewEvent" class = "form-button"/>
	        </form>
	    </ul>
	</div>
	<!-- /#sidebar-wrapper -->

	<!-- Page Content -->
	<div id="page-content-wrapper">
	    <div class="container-fluid">
	    <ul class="nav navbar-right top-nav">
	        <li class="dropdown">
	            <a href="#" class="dropdown-toggle" id="displayname" data-toggle="dropdown"><i class="fa fa-user"></i> Username <b class="caret"></b></a>
	            <ul class="dropdown-menu">
	                <li>
	                    <a href="http://localhost:5555/vibe"><i class="fa fa-fw fa-power-off"></i> Log Out</a>
	                </li>
	            </ul>
	        </li>
	    </ul>

	    <br>

	    <h2>Display Selected Event Here</h2>
	    <a href="#menu-toggle" class="btn btn-default" id="menu-toggle">Expand Playlist View</a>
	    
		<iframe id="playlist" frameborder="0" allowtransparency="true"></iframe>
	    </div>
	</div>
	<!-- /#page-content-wrapper -->
</div>
<!-- /#wrapper -->

<!-- Menu Toggle Script -->
<script>
$("#menu-toggle").click(function(e) {
    e.preventDefault();
    $("#wrapper").toggleClass("toggled");
});

</script>



</#assign>
<#include "main.ftl">
