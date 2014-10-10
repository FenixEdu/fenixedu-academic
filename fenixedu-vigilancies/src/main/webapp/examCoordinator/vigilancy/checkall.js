function check(id,value) {
	var checkboxes = document.getElementById(id).getElementsByTagName("INPUT"); 
	for (var i=0; i<checkboxes.length; i++) { 
		checkboxes[i].checked = value; 
	}
}
function checkall(id) { 
	check(id,true);
}

function uncheckall(id) { 
	check(id,false);
}