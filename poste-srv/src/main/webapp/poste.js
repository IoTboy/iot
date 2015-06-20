
function onDocReady(){
	console.log("Ready!")
	var wsock = new WebSocket("ws://"+location.hostname+":8080/poste-srv/postewsk");
	wsock.onmessage = function (event) {
		  //console.log(event.data);
		  $("#poste").html(event.data);
		  var data = parseInt(event.data);
		  if(data < 20){
			  $("#poste").addClass("aceso");
		  }else{
			  $("#poste").removeClass("aceso");
		  }
	}

}
$(document).ready(onDocReady);
