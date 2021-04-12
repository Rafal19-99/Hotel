
function checkOut(){
	sendToServerAndReciveJSON("api/booking/checkOut",$("#table"))
	.then(() => {
			$("#table tr").click(function() {
	    		var tableData = $(this).children("td:first");
	    		$("#checkOut").val(tableData.html());      	      
	    	})
	  })
}

function updateCheckOut(){

	var id = $("#checkOut").val();
	if(id<0){
		alert("Wybierz poprawna rezerwacje ")
		return ;
	}

	sendToServerAndReciveText("api/booking/checkOut/"+id);

	
}
