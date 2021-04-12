
function sendToServer(form){
	if(!checkForm(form)) return;
	 event.preventDefault();
	    fetch("j_security_check", {
	        method: 'POST',
	        body: new URLSearchParams(new FormData(form)) 
	    }).then((resp) => {
	        return resp.text(); 
	    }).then((body) => {	  
		    if(body==""){
				window.location.replace("nav.html");
			}else{
				alert("Failed. Try agin ");
				
			}
	    }).catch((error) => {
		    alert("Server error");
		   
	    });
}
