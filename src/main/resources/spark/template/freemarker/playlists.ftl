<#assign content>

<div id="wrapper">
	<!-- Sidebar -->
	<div id="sidebar-wrapper">
	    <ul class="sidebar-nav">
	        <li class="sidebar-brand" id="date">
	            <a href="#"><p id="date"></p></a>
	        </li>
	        
	        <hr>
	        <form id='eventForm' method="POST" action="/newEvent">
	            <label for='name' >Event Name: </label>
	            <input type='text' name='name' id='name' maxlength="30" class="form-textbox"/>
	            <br/><br/>

	            <label for='startTime' >Start Time:</label> 
	            <input type='text' id='startTime' name='startTime' maxlength="5" class="form-textbox-time-start"/> 
	            &nbsp <input type="radio" id='startAM' name="time-selection-start" value="am-start">am
	            &nbsp <input type="radio" id='startPM' name="time-selection-start" value="pm-start" checked>pm
	            <br/><br/>

	            <label for='endTime' >End Time:</label> 
	            <input type='text' id='endTime' name='endTime' maxlength="5" class="form-textbox-time-end"/>
	            &nbsp <input type="radio" id='endAM' name="time-selection-end" value="am-end">am
	            &nbsp <input type="radio" id='endPM' name="time-selection-end" value="pm-end" checked>pm
	            <br/><br/>

	            &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp 
	            <input type='button' name='AddNewEvent' value='Add New Event' id="AddNewEvent" class="btn btn-success"/>
   		       <!-- <button name='AddNewEvent' value='Add New Event' id="AddNewEvent" class="btn btn-success">Add New Event</button> -->

	        </form>
	        <hr>

	        <ul id="calendarEvents"></ul>
	    </ul>
	</div>
	<!-- /#sidebar-wrapper -->


	<!-- Event Modal -->
	<div class="modal fade" id="vibe-modal" role="dialog">
		<!-- Modal dialog -->
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="modal-event-name"> is happening now!</h4>
				</div>

				<div class="modal-body">
					<p id="modal-msg"></p>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">OK</button>
				</div>
			</div>
			<!-- end modal content -->
		</div>
		<!-- end modal dialog -->
	</div>
	<!-- end modal -->


	<!-- Displays helpful/error messages -->
    <div class="otherContent"></div>


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
		    

		    <div class="row">
				<div id='view-playlist-panel'> 
					<!-- Displays the name of the event --> 
				</div>
		    </div>
				    
				    
			<div class="row">
				<div class="hiddenDiv" id="customizeDiv">    
					<!-- Customize Form -->
					<form id='customizePlaylistForm' form method="POST" action="/customizePlaylist">
					
						<!-- Customization Panel Title -->
						<div id='customizeTitle' class ='scustomizeTitle'> 
							<text color = "white"> 
								Customize Your Playlist Settings 
							</text> 
						</div>
					
						<!-- Choosing a Playlist from Existing Spotify Playlist for Event -->
						<!-- Horizontal Line -->
						<hr color='white' size='2'>
						
						
						<!-- Event Tags -->
						<div id="eTagWrapper" class="row section-wrapper">
							<h2 class="sub-desc">Select an Event Tag</h2>
							<div>
								<input type="radio" name="eTradio" id="eTradio1" class="radio" value="Eat/Social" checked/>
								<label for="eTradio1">Eat/Social</label>
							</div>

							<div>
								<input type="radio" name="eTradio" id="eTradio2" class="radio" value="Work/Study"/>
								<label for="eTradio2">Work/Study</label>
							</div>
							  
							<div>	
								<input type="radio" name="eTradio" id="eTradio3" class="radio" value="Exercise"/>
								<label for="eTradio3">Exercise</label>
							</div>

							<div>	
								<input type="radio" name="eTradio" id="eTradio4" class="radio" value="Party"/>
								<label for="eTradio4">Party</label>
							</div>

							<div>	
								<input type="radio" name="eTradio" id="eTradio5" class="radio" value="Restful"/>
								<label for="eTradio5">Restful</label>
							</div>
						</div>
						
						<!-- Horizontal Line -->
						<!-- <hr color='white' size='2' width='100%'> -->
						
						<!-- Mood Selection -->
						<div id="mTagWrapper" class="row section-wrapper">
							<h2 class="sub-desc">Select an Event Mood</h2>

							<div>
								<input type="radio" name="mSradio" id="mSradio1" class="radio" value="Happy" checked/>
								<label for="mSradio1">Happy</label>
							</div>

							<div>
								<input type="radio" name="mSradio" id="mSradio2" class="radio" value="Excited" />
								<label for="mSradio2">Excited</label>
							</div>
							  
							<div>	
								<input type="radio" name="mSradio" id="mSradio3" class="radio" value="Sad" />
								<label for="mSradio3">Sad</label>
							</div>

							<div>	
								<input type="radio" name="mSradio" id="mSradio4" class="radio" value="Relaxing" />
								<label for="mSradio4">Relaxing</label>
							</div>
						</div>
						
						<!-- Horizontal Line -->
						<!-- <hr color='white' size='2' width='100%'> -->

						<!-- Genre Selection -->
						<div class="row">
							<dl id='genre-selector' class="gDropdown"> 
								<dt>
									<yy href="#">
									  <span class="hida">Select Preferred Genre(s)</span>    
									  <p class="multiSelection"></p>  
									</yy>
								</dt>

								<dd>
								    <div class="mutliSelect" id="msGenre">
								        <ul id="ulGenre">
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
								                <input type="checkbox" value="Pop" />Pop
								            </li>
								            <li>
								                <input type="checkbox" value="RandB" />R&amp;B
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
							</dl>
						</div>
								
						<!-- Range sliders for popularity and energy bars -->
						<div class="row">
							<div class="col-xs-6">
								<label for="popularitySlider" class='rangeSliderTitle'>Popularity</label>
					  			<div class="range range-success">
					    		<input type="range" name="range" min="0" max="10" value="5" onchange="popularitySlider.value=value">
					    		<output id="popularitySlider">5</output>
					  			</div>
					  		</div>
					  		<div class="col-xs-6">
					  			<label for="energySlider" class='rangeSliderTitle'>Energy</label>
					  			<div class="range range-success">
					    			<input type="range" name="range" min="0" max="10" value="5" onchange="energySlider.value=value">
					    			<output id="energySlider">5</output>
					  			</div>
							</div>
						</div>
						
						<div id='customize-playlist-submit' class="row">
							<!-- <input type="button" class="btn btn-primary cbutton" value="Use your Own Playlist" id="useOwnPlaylist"> -->
						
							<!-- <dl id='select-your-playlist' class="plDropdown btn btn-primary cbutton"> 
								<dt>
									<yy href="#">
									  <span class="hida">Use Your Own Playlist</span>    
									  <p class="plmultiSelection"></p>  
									</yy>
								</dt>

								<dd>
								    <div class="plmutliSelect">
								        <ul>
								            add in li elements for each playlist you have -->
							<!-- 	        </ul>
								    </div>
								</dd>
							</dl> -->
						
							<input type="button" class="btn btn-success cbutton" value="Generate Custom Playlist" id="generateCustom">
						</div>
					</form>
				</div>

				<!-- Edit Event Form -->
				
				<div class="hiddenDiv" id="editDiv">    
					<form id='editEventForm' form method="POST" action="/editEvent" class='smart-green'>
			  		   <h7> Edit Event </h7>
				       <label for='name' >Event Name: </label>
				       <input type='text' id='editEventName' name='name' maxlength="30" class="form-textbox"/>
				       <br/><br/>

				       <label for='start-time' >Start Time:</label> 
				       <input type='text' id='editStartTime' name='startTime' maxlength="5" class="form-textbox-time-start"/> 
				       &nbsp <input type="radio" id='editStartAM' name="time-selection-start" value="am-start">am
				       &nbsp <input type="radio" id='editStartPM' name="time-selection-start" value="pm-start" checked>pm
				       <br/><br/>

				       <label for='end-time' >End Time:</label> 
				       <input type='text' id='editEndTime' name='endTime' maxlength="5" class="form-textbox-time-end"/>
				       &nbsp <input type="radio" id='editEndAM' name="time-selection-end" value="am-end">am
				       &nbsp <input type="radio" id='editEndPM' name="time-selection-end" value="pm-end" checked>pm
				       <br/><br/>

				       &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp 
				       <!-- <input type='button' name='AddNewEvent' value='Submit Changes' id="EditAddNewEvent" class="button"/> -->
				       <button name="EditEvent" value="Submit Changes" id="EditAddNewEvent" class="btn btn-success"></button>
					</form>
				</div>

				<div>
					<iframe id="playlist" frameborder="0" allowtransparency="true"></iframe>
			    </div>
			</div>
<!-- begining of conflict -->			
			<div id='customize-playlist-submit'>
			<input type="button" class="cbutton" value="Generate Custom Playlist" id="generateCustom">
			</div>
		</form>
		</div>

<!-- end of conflict -->
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