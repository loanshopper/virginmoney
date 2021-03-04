$(document).ready(function(){
	$('[data-bs-tooltip]').tooltip();
});

$.getJSON('/conference/rest/sessions', function(view) {
    $.get("assets/templates/session-row.mustache", function(template) {
      var sessions = {sessions: view};
      console.log(sessions);
      var speakers = Mustache.render(template, sessions);
      $("#session-rows").append(speakers);
    });
  });