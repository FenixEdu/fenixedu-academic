function autoCompleteUpdate(autoCompleteField,selectedElement)
{
	document.getElementById(autoCompleteField.id + '_AutoComplete').value = selectedElement.id;
}
  	  	
function autoCompleteClearValueFieldIfTextIsEmpty(autoCompleteFieldId)
{   	  		  	  		
	if (document.getElementById(autoCompleteFieldId).value == '') 
	{ 
		document.getElementById(autoCompleteFieldId + '_AutoComplete').value = ''; 
	}
}