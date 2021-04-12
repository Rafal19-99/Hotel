
window.onload = function(){
	$("#checkIn").attr("min", new Date().toISOString().split("T")[0]);
	$("#checkOut").attr("min", new Date().toISOString().split("T")[0]) ;
}

function setMinDateForCheckOut(){

	$("#checkOut").attr("min",$("#checkIn").val() ) 
}



function sendToServer(form){
	if(!checkForm(form)) return;
	event.preventDefault();
	    fetch("api/booking", {
	        method: 'POST',
	        body: new URLSearchParams(new FormData(form)) 
	    }).then((resp) => {
	        return resp.text(); 
	    }).then((body) => {
		    switch(parseInt(body)){
		    case 0:
			    alert("Booked");
			    form.reset();
			     break;
		    case 1:
		    	alert("No free room in "+form.checkIn.value+" - "+form.checkOut.value+" for "+form.type.value+" person");
		    	break;
		    case 2:
			    alert("Date of checkin is after date of checkout");
			    break;
		    default:
			    alert(body);
				break;
		    }

	    }).catch((error) => {
		    alert(error);
	    	alert("Server error");
	    });
}