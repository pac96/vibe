<#assign content>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>HomePage</title>
</head>

<body>
    
    <div id="wrapper">
        <!-- Sidebar -->
        <div id="sidebar-wrapper">
            <ul class="sidebar-nav">
                <li class="sidebar-brand">
                    <a href="#">
                        Display Date Here
                    </a>
                </li>
                <li>
                    <a href="#">7am | Running</a>
                </li>
                <li>
                    <a href="#">8am | Drive to Work</a>
                </li>
                <li>
                    <a href="#">9am | Work</a>
                </li>
                <li>
                    <a href="#">5pm | Drive Home</a>
                </li>
                <li>
                    <a href="#">6pm | Dinner with Friends</a>
                </li>
                <li>
                    <a href="#">7pm | Reading</a>
                </li>
                <li>
                    <a href="#">9pm | Nightly Stretch</a>
                </li>
                <hr>
                <form id='form' action='handle_new_event.php' method='POST' target='formresponse'>
                    <label for='name' >Event Name: </label>
                    <input type='text' name='name' id='name' maxlength="30" class = "form-textbox"/><br/>

                    <br>
                    <label for='start-time' >Start Time:</label> 
                    <input type='text' name='startTime' id='startTime' maxlength="5" class = "form-textbox-time-start"/> 
                    &nbsp <input type="radio" name="time-selection-start" value="am-start" checked> am
                    &nbsp <input type="radio" name="time-selection-start" value="pm-start"> pm

                    <br><br>
                    <label for='end-time' >End Time:</label> 
                    <input type='text' name='endTime' id='endTime' maxlength="5" class = "form-textbox-time-end"/>
                    &nbsp <input type="radio" name="time-selection-end" value="am-end" checked> am
                    &nbsp <input type="radio" name="time-selection-end" value="pm-end"> pm 
                    
                    <br><br>
                    &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp 
                    <input type='button' name='Add New Event' value='Add New Event' class = "form-button"/>
                </form>
            </ul>
        </div>
        <!-- /#sidebar-wrapper -->

        <!-- Page Content -->
        <div id="page-content-wrapper">
            <div class="container-fluid">
                     <!-- Top Menu Items -->
            <ul class="nav navbar-right top-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user"></i> Chelse Steele <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li>
                            <a href="#"><i class="fa fa-fw fa-power-off"></i> Log Out</a>
                        </li>
                    </ul>
                </li>
            </ul>

            <br>
            <h2>TODO: Display Selected Event Here</h2>
                <div class="table-responsive">
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
                </div>
                 <a href="#menu-toggle" class="btn btn-default" id="menu-toggle">Expand Playlist View</a>
            </div>
        </div>
        <!-- /#page-content-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>

    <!-- Menu Toggle Script -->
    <script>
    $("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#wrapper").toggleClass("toggled");
    });
    </script>

</body>

</html>

</#assign>
<#include "main.ftl">
