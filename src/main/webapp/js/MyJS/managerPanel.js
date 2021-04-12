
var activeUser = {
		login: null,
		email: null,
		employee: {
			name: null,
			surname: null,
			position: null
		}
		
};



function activeUserToString(activeUser){
	return activeUser.login+" "+activeUser.email+" "+activeUser.employee.name
	     +" "+activeUser.employee.surname+" "+activeUser.employee.position;
};
  function allEmplyee(){

	 sendToServerAndReciveJSON("api/manage",$("#table"))
		.then(() => {
			$("#table tr").click(function() {
		    	    var tableData = $(this).children("td").map(function() {
		    	        return $(this).text();
		    	    }).get(); 
		    	    setActiveUser(tableData);    
		    	});
		    
		});

guestNotCheckedInOnTime();
    
	
}

function guestNotCheckedInOnTime(){

	sendToServerAndReciveJSON("api/booking/late",$("#table1"))
		.then(() => {
			$(" #table1 tr  ").click(function() {
		    	    var tableData = $(this).children("td:first");
		    	    $("#late").val(tableData.html());    
		    	})
		})

	
}

function fireEmployee(){
	if(!activeUser) return;
	sendDELETEToServerAndReciveText("api/manage/"+activeUser.login);
	
}


function setActiveUser(value){
	
	 var str = String(value)
	    var array = str.split(',');
		activeUser.login=array[0];
		activeUser.email=array[1];

	    var employe = String(array[2]).match(/[A-Z][a-z]+|[0-9]+/g).join(" ");
	    var employe2 = employe.split(' ');

	    activeUser.employee.name=employe2[0];
	    activeUser.employee.surname=employe2[1];
	    activeUser.employee.position=employe2[2];

		$("#log").val(array[0]);
	    $("#activeUser").html(activeUserToString(activeUser));
}

function changePassword(form){
	if(!checkForm(form)) return;
	sendFormToServerAndReciveText(form);

	
}


function addEmployee(form){
	if(!checkForm(form)) return;
	sendFormToServerAndReciveJSON(form);
}




function deleteBooking(){
	sendDELETEToServerAndReciveText("api/booking/"+$("#late").val());
	
}

