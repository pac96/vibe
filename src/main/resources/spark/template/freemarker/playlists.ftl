<#assign content>

<div class="container-fluid">
	<div class="row">
		<!-- Calendar -->
		<div class="col-md-3" id="calendar">
			Calendar goes here
			<button class="btn btn-primary">Sync with Google Calendar</button>
		</div>

		<!-- Spotify Playlist -->
		<div class="col-md-8" id="playlist">
			Playlist goes here
			<iframe src="https://embed.spotify.com/?uri=spotify:user:spotify:playlist:3rgsDhGHZxZ9sB9DQWQfuf" frameborder="0" allowtransparency="true"></iframe>
		</div>

		<!-- Adding an event -->
		<div class="col-md-8" id="addEvent">
			Add event button goes here
			<button class="btn btn-success">Add Event</button>
		</div>
	</div>
</div>

</#assign>
<#include "main.ftl">