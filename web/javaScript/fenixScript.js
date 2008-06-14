function autoCompleteUpdate(autoCompleteField, selectedElement)
{
	autoCompleteField.value = selectedElement.getAttribute("name");
	document.getElementById(autoCompleteField.id + '_AutoComplete').value = selectedElement.id;
	
	// Update the old value field, generated only by the AutoCompleteInputRenderer
	var oldValueField = 	document.getElementById(autoCompleteField.id + '_OldValue');
	if (oldValueField) {
		oldValueField.value = autoCompleteField.value;
	}
}

function autoCompleteClearValueFieldIfTextIsEmpty(autoCompleteFieldId)
{   	  		  	  		
	if (document.getElementById(autoCompleteFieldId).value == '') 
	{ 
		document.getElementById(autoCompleteFieldId + '_AutoComplete').value = ''; 
	}
}

//
// Used in the AutoCompleteInputRenderer to detect when the user types 
// a custom value. The idea is to compare the text field value before
// and after the user presses a key. If the value is different then
// we update the hidden field to contain the custom value.
//
// The old value is stored in and hidden field to avoid a global javascript
// variable.
//
function autoCompleteKeyDownHandler(event, textFieldId) {
	var textField     = document.getElementById(textFieldId);
	var oldValueField = document.getElementById(textFieldId + '_OldValue');
	
	oldValueField.value = textField.value;
}

function autoCompleteKeyUpHandler(event, textFieldId, customValue) {
	var textField     = document.getElementById(textFieldId);
	var oldValueField = document.getElementById(textFieldId + '_OldValue');
	var hiddenField   = document.getElementById(textFieldId + '_AutoComplete');
	
	if (textField.value == '') 
	{
		hiddenField.value = ''; 
	}
	else {
		switch (event.keyCode) {
			case Event.KEY_TAB:
			case Event.KEY_RETURN:
				break;
			default:
				if (! (textField.value == oldValueField.value)) {
					hiddenField.value = customValue;
					oldValueField.value = textField.value;
					Element.hide(textFieldId + '_Error'); // hide error message
				}
				break;
		}
	}
}

function showAutoCompleteError(textFieldId) {
	Element.show(textFieldId + '_Error');
}
