$(document).ready(function(){
	$('[data-bs-tooltip]').tooltip();
});

$.getJSON('/conference/rest/sessions', function(data) {
    $.get("assets/templates/session-row.mustache", function(template) {
      var sessions = {sessions: data};
      var speakers = Mustache.render(template, sessions);
      $("#session-rows").append(speakers);
    });
  });
  
 $('#reload').click(() => {
    var speakerName = $("#speakerName").val();
    var sessionDate = $("#sessionDate").val();
    var data = { speakerName: speakerName, sessionDate: sessionDate };
    
    $.getJSON('/conference/rest/sessions', data, function(response) {
	    $.get("assets/templates/session-row.mustache", function(template) {
	      var sessions = {sessions: response};
	      console.log(sessions);
	      var speakers = Mustache.render(template, sessions);
	      $("#session-rows").empty();
	      $("#session-rows").append(speakers);
	    });
    });
    
    return false;

}); 