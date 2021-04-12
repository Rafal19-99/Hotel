
function allRoomToClean(){

	sendToServerAndReciveJSON("api/clean", $("#table1"))
	.then(() => {
    		$(" #table1 tr  ").click(function() {
        	    var tableData = $(this).children("td:first");
        	    $("#clean").val(tableData.html());    
        	})  
    	})
	
	

   curretlyCleanedRoomByMe();
	
}


function curretlyCleanedRoomByMe(){
	sendToServerAndReciveJSON("api/clean/active", $("#table"))
	.then(() => {
		$("#table tr").click(function() {
    		$(" #table tr  ").click(function() {
        	    var tableData = $(this).children("td:first");
        	    $("#active").val(tableData.html());    
        	})  
    	})
	
	})

}
function finish(){
	var id = $("#active").val()
	if(id<0){
		alert("Wrong number")
		return ;
	}
    sendToServerAndReciveText("api/clean/finish/"+id);
	
	
}


function update(){
	var id = $("#clean").val()
	if(id<0){
		alert("Wrong number")
		return ;
	}
    sendToServerAndReciveText("api/clean/update/"+id);
	
}
	
	

