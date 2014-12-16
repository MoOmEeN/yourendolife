window.com_moomeen_views_workouts_details_timelapse_JsTimeLapse = function() {
  var e = this.getElement();
	
  this.onStateChange = function() {
	  if (e.hasChildNodes()){
		  return;
	  }
    var fakeResult = {};
    
	var points = [];
	var routes = [];
	var route = { overview_path: points};
	fakeResult.routes = routes;
	routes[0] = route;

	for (var i = 0; i < this.getState().points.length; i++){
		var point = this.getState().points[i];
		points[i] = new google.maps.LatLng(point[0], point[1]);
	};
    
	//-- Route
    var routeSequence = StreetviewSequence(e, {
        route: fakeResult,
        duration: 30000,
        key: 'AIzaSyDnDtkT3abR20ajxVlbYhI6uYKTXMgNJG4',
        loop: false,
        width: "1000",
        height: "500"
    });
 //   var $routeProgressContainer = $("#route-progress-container");
 //   var $routeProgressBar = $routeProgressContainer.find('.progress-bar');
 //   routeSequence.progress(function (p) {
 //       $routeProgressBar.css({width: (p * 100) + '%'});
 //   });
   routeSequence.done(function(player) {
 //       $routeProgressContainer.hide();
       player.play();
   });
  }
}