
function checkIn(){
	sendToServerAndReciveJSON("api/booking/checkIn",$("#table"))
	.then(() => { 	
	    $("#table tr").click(function() {
	    		var tableData = $(this).children("td:first");
	    	    $("#checkIn").val(tableData.html());   
	    	      
	    	})
	})
	
}

function updateCheckIn(){

	var id = $("#checkIn").val();
	if(id<0){
		alert("Wrong number")
		return ;
	}
	sendToServerAndReciveText("api/booking/checkIn/"+id);
	
}