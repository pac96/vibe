<#assign content>


<div id="wrapper">
	<!-- Sidebar -->
	<div id="sidebar-wrapper">
	    <ul class="sidebar-nav" id="calanderEvents">
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
	        <form id='eventForm' form method="POST" action="/newEvent">
	            <label for='name' >Event Name: </label>
	            <input type='text' name='name' id='name' maxlength="30" class="form-textbox"/>
	            <br/><br/>

	            <label for='startTime' >Start Time:</label> 
	            <input type='text' id='startTime' name='startTime' maxlength="5" class="form-textbox-time-start"/> 
	            &nbsp <input type="radio" id='startAM' name="time-selection-start" value="am-start" checked>am
	            &nbsp <input type="radio" id='startPM' name="time-selection-start" value="pm-start">pm
	            <br/><br/>

	            <label for='endTime' >End Time:</label> 
	            <input type='text' id='endTime' name='endTime' maxlength="5" class="form-textbox-time-end"/>
	            &nbsp <input type="radio" id='endAM' name="time-selection-end" value="am-end" checked>am
	            &nbsp <input type="radio" id='endPM' name="time-selection-end" value="pm-end">pm
	            <br/><br/>

	            &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp 
	            <input type='button' name='AddNewEvent' value='Add New Event' id="AddNewEvent" class="form-button"/>
	            <ul id="calendarEvents">
	            </ul>
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
	    
 	    <div id='view-playlist-panel'> ... </div>
 	    
	    <div id='customize-playlist-panel'> 
	    	
	    	<!-- Choosing a Playlist from Exisiting Spotify Playlist for Event -->
	    	<div id='SpotifyPlaylistSelection' name='SpotifyPlaylistSelection' class='dropdown'>
	    		<select id='SpotifyPlaylistSelector' name='SpotifyPlaylistSelector' size='1' class='dropbtn'>
	    			 <div id='AvailablePlaylists' name='AvailablePlaylists' class="dropdown-content">
  					<!-- using jquery to fill the option list 
  						 with the user's playlists
  						 <a href="#">Link 1</a>-->
  					</div>
				</select>
			</div>
			
			<!-- Customize Form -->
			<form id='customizePlaylist' form method="POST" action="/customPlaylist">
			
			<!-- Event Tags -->
			<div id="eTagWrapper">
			<div>
				<input type="radio" name="radio" id="radio1" class="radio" checked/>
				<label for="radio1">Eat/Social</label>
			</div>
			
			<div>
				<input type="radio" name="radio" id="radio2" class="radio"/>
				<label for="radio2">Work/Study</label>
			</div>
			  
			<div>	
				<input type="radio" name="radio" id="radio3" class="radio"/>
				<label for="radio3">Exercise</label>
			</div>
			
			<div>	
				<input type="radio" name="radio" id="radio4" class="radio"/>
				<label for="radio4">Party</label>
			</div>
			
			<div>	
				<input type="radio" name="radio" id="radio5" class="radio"/>
				<label for="radio5">Restful</label>
			</div>
		</div>
			
			
			<!-- Genre Selection -->
			<dl class="gDropdown"> 
  
			    <dt>
			    <yy href="#">
			      <span class="hida">Select</span>    
			      <p class="multiSelection"></p>  
			    </yy>
			    </dt>
			  
			    <dd>
			        <div class="mutliSelect">
			            <ul>
			                <li>
			                    <input type="checkbox" value="Alternative" />Alternative
			                </li>
			                <li>
			                    <input type="checkbox" value="Blues" />Blues
			                </li>
			                <li>
			                    <input type="checkbox" value="Classical" />Classical
			                </li>
			                <li>
			                    <input type="checkbox" value="Country" />Country
			                </li>
			                <li>
			                    <input type="checkbox" value="Dancehall" />Dancehall
			                </li>
			                <li>
			                    <input type="checkbox" value="Electronic" />Electronic
			                </li>
			                <li>
			                    <input type="checkbox" value="Folk" />Folk
			                </li>
			                <li>
			                    <input type="checkbox" value="Gospel" />Gospel
			                </li>
			                <li>
			                    <input type="checkbox" value="Hip-Hop" />Hip-Hop
			                </li>
			                <li>
			                    <input type="checkbox" value="Indie" />Indie
			                </li>
			                <li>
			                    <input type="checkbox" value="Jazz" />Jazz
			                </li>
			                <li>
			                    <input type="checkbox" value="K-Pop" />K-Pop
			                </li>
			                <li>
			                    <input type="checkbox" value="K-Pop" />K-Pop
			                </li>
			                <li>
			                    <input type="checkbox" value="Pop" />Pop
			                </li>
			                <li>
			                    <input type="checkbox" value="RandB" />RandB
			                </li>
			                <li>
			                    <input type="checkbox" value="Reggae" />Reggae
			                </li>
			                <li>
			                    <input type="checkbox" value="Reggaeton" />Reggaeton
			                </li>
			                <li>
			                    <input type="checkbox" value="Rock" />Rock
			                </li>
			                <li>
			                    <input type="checkbox" value="Soul" />Soul
			                </li>
			                <li>
			                    <input type="checkbox" value="Spanish" />Spanish
			                </li>
			            </ul>
			        </div>
			    </dd>
			  <button>Filter</button>
			</dl>
			
		<input id="ex9" type="text"/>
	    
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
	           <input type='button' name='AddNewEvent' value='Submit Changes' id="EditAddNewEvent" class="form-button"/>
	    </form>

	    <br>

	    <!-- <h2>Display Selected Event Here</h2>
	    <a href="#menu-toggle" class="btn btn-default" id="menu-toggle">Expand Playlist View</a> -->
	    
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