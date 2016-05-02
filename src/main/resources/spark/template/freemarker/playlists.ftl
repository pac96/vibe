<#assign content>

<div id="wrapper">
	<!-- Sidebar -->
	<div id="sidebar-wrapper">
	    <ul class="sidebar-nav" id="calendarEvents">
	        <li class="sidebar-brand" id="date">
	            <a href="#">
	    	    <script>
		            var d = new Date();
		            document.getElementById("date").innerHTML = d.toDateString();
				</script>
	            <p id="date"></p>
	            </a>
	        </li>
	        
	        <hr>
	        <form id='eventForm' method="POST" action="/newEvent">
	            <label for='name' >Event Name: </label>
	            <input type='text' name='name' id='name' maxlength="30" class="form-textbox"/>
	            <br/><br/>

	            <label for='start-time' >Start Time:</label> 
	            <input type='text' id='startTime' name='startTime' maxlength="5" class="form-textbox-time-start"/> 
	            &nbsp <input type="radio" id='startAM' name="time-selection-start" value="am-start" checked>am
	            &nbsp <input type="radio" id='startPM' name="time-selection-start" value="pm-start">pm
	            <br/><br/>

	            <label for='end-time' >End Time:</label> 
	            <input type='text' id='endTime' name='endTime' maxlength="5" class="form-textbox-time-end"/>
	            &nbsp <input type="radio" id='endAM' name="time-selection-end" value="am-end" checked>am
	            &nbsp <input type="radio" id='endPM' name="time-selection-end" value="pm-end">pm
	            <br/><br/>

	            &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp 
	            <input type='button' name='AddNewEvent' value='Add New Event' id="AddNewEvent" class="form-button"/>
	        </form>
	        <hr>
	    </ul>
	</div>
	<!-- /#sidebar-wrapper -->

	<!-- Page Content -->
	<div id="page-content-wrapper">
    <div class="container-fluid">
    <ul class="nav navbar-right top-nav">
        <li class="dropdown">
            <a href="#" class="dropdown-toggle" id="displayname" data-toggle="dropdown"><i class="fa fa-user"></i><span class="caret"></span></a>
            <ul class="dropdown-menu">
                <li>
                    <a href="" id="logoutLink"><i class="fa fa-fw fa-power-off"></i> Log Out</a>
                </li>
            </ul>
        </li>
    </ul>
    
<#-- 	    <div id='view-playlist-panel'> ... </div>
    <div id='customize-playlist-panel'> ... </div>
    <div id='use-spotify-playlist-panel'> ... </div> -->
    <form id='editEventForm' form method="POST" action="/editEvent">
           <label for='name' >Event Name: </label>
           <input type='text' id='editEventName' name='name' maxlength="30" class="form-textbox"/>
           <br/><br/>

           <label for='start-time' >Start Time:</label> 
           <input type='text' id='editStartTime' name='startTime' maxlength="5" class="form-textbox-time-start"/> 
           &nbsp <input type="radio" id='editStartAM' name="time-selection-start" value="am-start" checked>am
           &nbsp <input type="radio" id='editStartPM' name="time-selection-start" value="pm-start">pm
           <br/><br/>

           <label for='end-time' >End Time:</label> 
           <input type='text' id='editEndTime' name='endTime' maxlength="5" class="form-textbox-time-end"/>
           &nbsp <input type="radio" id='editEndAM' name="time-selection-end" value="am-end" checked>am
           &nbsp <input type="radio" id='editEndPM' name="time-selection-end" value="pm-end">pm
           <br/><br/>

           &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp 
           <input type='button' name='EditAddNewEvent' value='Submit Changes' id="EditAddNewEvent" class="form-button"/>
    </form>

    <h2 id="selectedEvent"></h2>
    
    <!-- This div could hold error messages, other forms, etc. -->
    <div class="other-content"></div>

    <a href="#menu-toggle" class="btn btn-default" id="menu-toggle">Expand Playlist View</a>
	<iframe id="playlist" frameborder="0" allowtransparency="true"></iframe>

    </div>
	<!-- end container-fluid -->

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