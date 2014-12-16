window.com_moomeen_views_workouts_details_timelapse_JsStreetView = function() {
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
        width: 640,
        height: 640
    });
   var $routeProgressContainer = $("#street-view-progress");
    var $routeProgressBar = $routeProgressContainer.find('.v-progressbar-indicator');
   routeSequence.progress(function (p) {
       $routeProgressBar.css({width: (p * 100) + '%'});
   });
   routeSequence.done(function(player) {
	   $routeProgressContainer.parent().hide();
	   $routeProgressContainer.parent().parent().parent().addClass("street-view-panel"); // adding class that removing padding, in other words: hack
       player.play();
       
       var $odv = $("<div class='street-view-control-pause'></div>");
       $odv.click(function() {
    	   if ($( this ).hasClass("street-view-control-pause")){
    		   player.pause();
    		   $( this ).removeClass("street-view-control-pause");
    		   $( this ).addClass("street-view-control-play");
    	   } else {
    		   player.play();
    		   $( this ).removeClass("street-view-control-play");
    		   $( this ).addClass("street-view-control-pause");
    	   }
       });
       $odv.appendTo(e);
   });
  }
}