<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoDegree"%>

<script language=javascript>
<!--

function addToList(listField, newText, newValue) {
if (listField.length > 2){
alert("Não se pode escolher mais de 2 disciplinas");
return true;
}
   if ( ( newValue == "" ) || ( newText == "" ) ) {
      alert("You cannot add blank values!");
   } else {
     for (i = 0; i < listField.length; i++){
       if (listField[i].value == newValue){
         alert("Já seleccionou essa disciplina!");
	 return true;
	 }
     }  
      var len = listField.length++; // Increase the size of list and return the size
      listField.options[len].value = newValue;
      listField.options[len].text = newText;
      listField.selectedIndex = len; // Highlight the one just entered (shows the user that it was entered)
   } // Ends the check to see if the value entered on the form is empty
}

function removeFromList(listField) {
   if ( listField.length == -1) {  // If the list is empty
      alert("There are no values which can be removed!");
   } else {
      var selected = listField.selectedIndex;
      if (selected == -1) {
         alert("You must select an entry to be removed!");
      } else {  // Build arrays with the text and values to remain
         var replaceTextArray = new Array(listField.length-1);
         var replaceValueArray = new Array(listField.length-1);
         for (var i = 0; i < listField.length; i++) {
            // Put everything except the selected one into the array
            if ( i < selected) { replaceTextArray[i] = listField.options[i].text; }
            if ( i > selected ) { replaceTextArray[i-1] = listField.options[i].text; }
            if ( i < selected) { replaceValueArray[i] = listField.options[i].value; }
            if ( i > selected ) { replaceValueArray[i-1] = listField.options[i].value; }
         }
         listField.length = replaceTextArray.length;  // Shorten the input list
         for (i = 0; i < replaceTextArray.length; i++) { // Put the array back into the list
            listField.options[i].value = replaceValueArray[i];
            listField.options[i].text = replaceTextArray[i];
         }
      } // Ends the check to make sure something was selected
   } // Ends the check for there being none in the list
}

//-->
</script>

<center>

<html:form  action="studentShiftEnrolmentManager" method="POST">
 <html:hidden property="method" value="proceedToShiftEnrolment" />


 <html:select property="degree" 
	      size="1"
	      onchange="document.studentShiftEnrolmentForm.method.value='enrollCourses';document.studentShiftEnrolmentForm.submit();">
	      <logic:iterate  id="executionDegree" name="degreeList">
	      <bean:define id="deg" name="executionDegree" property="infoDegreeCurricularPlan.infoDegree"/>
	      <option value="<%= ((InfoDegree)deg).getSigla() %>"> <bean:write name="deg" property="nome" /> </option> 
	      </logic:iterate>
 </html:select>
 <br/>
 <br/>

 <html:select property="course" size="12"> 
   <html:options collection="courseList" 
	         labelProperty="nome"
		 property="idInternal"/>
 </html:select>

 <html:select property="wantedCourse" size="12"> 
   <html:options collection="wantedCourse" 
	         labelProperty="nome"
		 property="idInternal"/>
 </html:select>

 <br/>
 <br/>

 <input type=button 
        value="add" 
	onclick="addToList(wantedCourse,course[course.selectedIndex].text,course[course.selectedIndex].value);"
	/>
 <input type=button 
        value="remove" 
	onclick="removeFromList(wantedCourse);"
        />
 <br>
 <html:submit value="Continuar inscrição"/>

</html:form>

</center>
