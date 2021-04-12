
function activeUserToString(activeUser){
	return activeUser.login+" "+activeUser.email+" "+activeUser.employee.name
	     +" "+activeUser.employee.surname+" "+activeUser.employee.position;
};



function getUserInfo(){

	
	fetch("api/user", {
        method: 'GET',
    }).then((resp) => {
        return resp.json(); 
    }).then((body) => {
	    if(body){
			$("#userDetails").html(activeUserToString(body));
			
		}else{
			alert("There are no infomation about you, contact administrator");
		}

    }).catch((error) => {

    	alert("Singin ");
    	window.location.replace("login.html");
    });
	
};

function logout(){

	event.preventDefault();

	fetch("api/user/logout", {
        method: 'GET',
    }).then((resp) => {
        return resp.text(); 
    }).then((body) => {
	    alert("signed out");
	    window.location.replace("login.html");
    }).catch((error) => {

    	alert("Server error  ");
    });



	
}

function changeMyPassword(form){

	if(!checkForm(form) ) return;
	sendFormToServerAndReciveText(form)
	

	
}