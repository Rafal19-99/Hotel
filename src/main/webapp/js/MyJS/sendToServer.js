 function sendToServerAndReciveJSON(path, appendTo){
	
		event.preventDefault();
	return fetch(path, {
	        method: 'GET',
	    }).then((resp) => {
	        return resp.json(); 
	    }).then((body) => {
			addTable(body, appendTo);
	    }).catch((error) => {
			alert("Acess denied ");
			window.location.replace("nav.html");
	    });		
}

function sendToServerAndReciveText(path){
		event.preventDefault();
		fetch(path, {
	        method: 'GET',
	    }).then((resp) => {
	        return resp.text(); 
	    }).then((body) => {
			if(body=="success"){
   				alert("sucess");
   				location.reload();
        	}else{
				alert(body);
        	}
	    }).catch((error) => {
			alert("Access to server failed ");
	    });		
}


function sendDELETEToServerAndReciveText(path){
	event.preventDefault();
		fetch(path, {
	        method: 'DELETE'
	    }).then((resp) => {
	        return resp.text(); 
	    }).then((body) => {
			if(body=="success"){
   				alert("succesfly removed");
   				location.reload();
        	}else{
				alert(body);
        	}
	    }).catch((error) => {
			alert("Access to server failed ");
	    });		
}

function sendFormToServerAndReciveJSON(form){
	event.preventDefault();
	fetch(form.action, {
	        method: 'POST',
	        body: new URLSearchParams(new FormData(form)) 
	    }).then((resp) => {
	        return resp.json(); 
	    }).then((body) => {
		    if(body){
				alert("success");
			    form.reset();
				location.reload();
			}else{
				alert(body);  
			}
	    }).catch((error) => {
	    	alert("Access to server failed ");
	    });
	
}


function sendFormToServerAndReciveText(form){
	event.preventDefault();
	fetch(form.action, {
	        method: 'POST',
	        body: new URLSearchParams(new FormData(form)) 
	    }).then((resp) => {
	        return resp.text(); 
	    }).then((body) => {
		    if(body=="success"){
				alert("success");
			    form.reset();
				location.reload();
			}else{
				alert(body);  
			}
	    }).catch((error) => {
	    	alert("Access to server failed ");
	    });
	
}