

 function requestConfirmation(formId,messageKey,titleKey) {
	 jQuery.alerts.overlayOpacity= 0.4;
	 jQuery.alerts.overlayColor= '#333';
	 jConfirm(messageKey, titleKey,function(userInput) {
		  if(userInput) {
			  $("#" + formId).submit(); 
           }
        });
}
 
 function requestConfirmationForJQueryForm(form,messageKey,titleKey) {
	 jQuery.alerts.overlayOpacity= 0.4;
	 jQuery.alerts.overlayColor= '#333';
	 jConfirm(messageKey, titleKey,function(userInput) {
		  if(userInput) {
			 form.submit(); 
           }
        });
} 
 
function linkConfirmationHookForm(formId, messageKey, titleKey) {
	 var submitButton = jQuery("#" + formId + " :submit");
	    
	    submitButton.attr('onclick','jConfirm('+messageKey+', '+titleKey+',function(userInput) { if (userInput) {$(#'+formId+').submit(); } return false; });return false; ');
	    jQuery("<div class=\"dinline forminline\"><form id='" +  formId + "form' action='" + 'teste' + "' method=\"post\"'></form></div>").insertBefore("#" + formId);    

}
function linkConfirmationHook(linkId, messageKey, titleKey) {
  var href = jQuery("#" + linkId ).attr('href');
  jQuery("#" + linkId).click(function() {
	  requestConfirmation(linkId + "form",messageKey,titleKey);
        });
  jQuery("#" + linkId).attr('href',"#");
  jQuery("<div class=\"dinline forminline\"><form id='" +  linkId + "form' action='" + href + "' method=\"post\"'></form></div>").insertBefore("#" + linkId);
}
