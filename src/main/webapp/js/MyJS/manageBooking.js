var lastBookingIdClicked;


function setMinDateForCheckOut(){

	$("#newCheckOut").attr("min",$("#newCheckIn").val() ) 
}


 function allBooking(){
	$("#newCheckIn").attr("min", new Date().toISOString().split("T")[0]);
	$("#newCheckOut").attr("min", new Date().toISOString().split("T")[0]) ;
	
	sendToServerAndReciveJSON("api/booking/all",$("#table"))
	.then(() => {
			$("#table tr").click(function() {
		    	    var tableData = $(this).children("td").map(function() {
		    	        return $(this).text();
		    	    }).get(); 
					lastBookingIdClicked=tableData[0];
		    	    $("#book").html("Booking id :"+tableData[0]+"<br\> Room number:"+tableData[1][0]+"<br\> Room floor:"
									+tableData[1][1]+"<br\> Room type:"+tableData[1][2]+"<br/> Guest :"+tableData[2].split(/(?=[A-Z])/).join(" ")
									+" </br> Date of checkin: "+tableData[3]+" </br> Date of checkout: "+tableData[4]+"<br/>"); 
					$("#id").val(tableData[0]);
					$("#newCheckIn").val(tableData[3]);
					$("#newCheckOut").val(tableData[4]);
		    	});
		    
		});
		
}

function changeBookingDate(form){
	if(!checkForm(form)) return;
	sendFormToServerAndReciveText(form);
}


function cancel(){
	if(lastBookingIdClicked<=0){
		alert("Choose valid booking");
		return;
	}
	
	sendDELETEToServerAndReciveText("api/booking/"+lastBookingIdClicked);
	
}
