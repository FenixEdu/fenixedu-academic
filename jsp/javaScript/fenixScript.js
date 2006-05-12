function autoCompleteUpdate(autoCompleteField, selectedElement)
{
	autoCompleteField.value = selectedElement.name;
	document.getElementById(autoCompleteField.id + '_AutoComplete').value = selectedElement.id;
}

function autoCompleteClearValueFieldIfTextIsEmpty(autoCompleteFieldId)
{   	  		  	  		
	if (document.getElementById(autoCompleteFieldId).value == '') 
	{ 
		document.getElementById(autoCompleteFieldId + '_AutoComplete').value = ''; 
	}
}

function autoCompleteUpdateValueField(textFieldId, typingValue)
{   	  		  	  		
	if (document.getElementById(textFieldId).value == '') 
	{
		document.getElementById(textFieldId + '_AutoComplete').value = ''; 
	}
	else {
		document.getElementById(textFieldId + '_AutoComplete').value = typingValue; 
	}
}
