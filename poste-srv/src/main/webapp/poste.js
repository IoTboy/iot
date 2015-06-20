
function onDocReady(){
	console.log("Ready!")
	var wsock = new WebSocket("ws://"+location.hostname+":8080/poste-srv/postewsk");
	wsock.onmessage = function (event) {
		  //console.log(event.data);
		  $("#poste").html(event.data);
	}

}
$(document).ready(onDocReady);
