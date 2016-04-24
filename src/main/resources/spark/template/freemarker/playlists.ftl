<#assign content>

<form action="/code" method="POST" id="hiddenForm">

</form>


<div id="wrapper">
	<!-- Sidebar -->
	<div id="sidebar-wrapper">
	    <ul class="sidebar-nav" id = "calanderEvents">
	        <li class="sidebar-brand">
	            <a href="#">
	            
	            <script>
	            <p id="date"></p>
	            var d = new Date();
	            document.getElementById("date").innerHTML = d.toDateString();
	            var result = d.fontcolor("white");
				</script>
				 <font color="white"> d </font>
	            </a>
	        </li>
	        <li>
	            <a href="#"> 
	            <input type='button' name='settings' value= + class = "settings-button"/> 
	            &nbsp
	            7am | Running 
	            </a>
	            
	        </li>
	        <li>
	            <a href="#">
	            <input type='button' name='settings' value= + class = "settings-button"/> 
	            &nbsp
	            8am | Drive to Work
	            </a>
	        </li>
	        <li>
	        	<a href="#">
	        	<input type='button' name='settings' value= + class = "settings-button"/> 
	            &nbsp
	            9am | Work
	            </a>
	        </li>
	        <li>
	        	<a href="#">
	        	<input type='button' name='settings' value= + class = "settings-button"/> 
	            &nbsp
	            5pm | Drive Home
	            </a>
	        </li>
	        <li>
	        	<a href="#">
	        	<input type='button' name='settings' value= + class = "settings-button"/> 
	            &nbsp
	            6pm | Dinner with Friends
	            </a>
	        </li>
	        <li>
	        	<a href="#">
	        	<input type='button' name='settings' value= + class = "settings-button"/> 
	            &nbsp
	            7pm | Reading
	            </a>
	        </li>
	        <li>
	        	<a href="#">
	        	<input type='button' name='settings' value= + class = "settings-button"/> 
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
	            <input type='button' name='AddNewEvent' value='Add New Event' class = "form-button"/>
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
	                    <a href="http://localhost:4567/vibe"><i class="fa fa-fw fa-power-off"></i> Log Out</a>
	                </li>
	            </ul>
	        </li>
	    </ul>

	    <br>
	    <h2>TODO: Display Selected Event Here</h2>
	        <!-- <div class="table-responsive">
	            <table class="table table-hover">
	                <thead>
	                    <tr>
	                        <th>Song</th>
	                        <th>Artist</th>
	                        <th>Genre</th>
	                    </tr>
	                </thead>
	                <tbody>
	                    <tr>
	                        <td>Post To Be</td>
	                        <td>Omarion</td>
	                        <td>RB</td>
	                    </tr>
	                    <tr>
	                        <td>On the Regular</td>
	                        <td>Shamir</td>
	                        <td>Pop</td>
	                    </tr>
	                    <tr>
	                        <td>Warm Water</td>
	                        <td>Banks</td>
	                        <td>RB</td>
	                    </tr>
	                    <tr>
	                        <td>Oui</td>
	                        <td>Jermiah</td>
	                        <td>RB</td>
	                    </tr>
	                    <tr>
	                        <td>Formation</td>
	                        <td>Beyonce</td>
	                        <td>RB</td>
	                    </tr>
	                    <tr>
	                        <td>Pt.2</td>
	                        <td>Kanye</td>
	                        <td>Rap</td>
	                    </tr>
	                    <tr>
	                        <td>Work</td>
	                        <td>Rhianna</td>
	                        <td>Dancehall</td>
	                    </tr>
	                </tbody>
	            </table>
	        </div> -->
	         <a href="#menu-toggle" class="btn btn-default" id="menu-toggle">Expand Playlist View</a>
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

console.log(window.location.href);
console.log("Split: " + window.location.href.split("code=(.*?)&"));
</script>



</#assign>
<#include "main.ftl">
