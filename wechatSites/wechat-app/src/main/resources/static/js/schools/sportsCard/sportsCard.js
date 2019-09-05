function search(){
	var activity = $("#activity").val();
	var grade =$("#grade").val();
	
	if(activity == ""){
		alert("请选择活动");
		return;
	}
	var clazz =$("#clazz").val();
	$("#grade").val("");
	if(activity!=""&&grade!=""){
	window.location.href="sportsCard?activity="+activity+"&clazzId="+clazz;
}
	
}


function searchGrade(){
	var activity = $("#activity").val();
	if(activity == ""){
		alert("请选择活动");
		return;
	}
	$("#clazz").val("");
	var grade =$("#grade").val();
	if(activity!=""&&grade!=""){
	window.location.href="sportsCard?activity="+activity+"&grade="+grade;
		
	}
	
	
	
}
	
	

function searchVal(){
	var grade =$("#grade").val();
	var clazz =$("#clazz").val();
	if(clazz!=""){
		search();
	}
	if(grade!=""){
		searchGrade();
	}
	
}


function toExport(){
	var activity = $("#activity").val();
	var grade =$("#grade").val();
	var clazz =$("#clazz").val();
	
	if(activity == ""){
		alert("请选择活动");
		return;
	}
	window.location.href="sportsCardStudents/export?activity="+activity+"&clazzId="+clazz+"&grade="+grade;
	
}









