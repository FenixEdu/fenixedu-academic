<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoDegree"%>
<script TYPE="text/javascript">
	<!--
	
	//TODO: Insert bean instead of integer, to be easily changed from the application
	maximumNumberOfCoursesAllowedPerStudent = 8;
	
	function addToList(listField, newText, newValue) {
		if (listField.length > maximumNumberOfCoursesAllowedPerStudent){
			alert("Não se pode escolher mais de " + maximumNumberOfCoursesAllowedPerStudent + " disciplinas");
			return true;
		}
		if ( ( newValue == "" ) || ( newText == "" ) ) {
			alert("Por favor, escolha uma disciplina!");	} else {
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
			//Does not do nothing, for there is nothing to be removed and the user
			//doesn't want to know that!
			//alert("There are no values which can be removed!");
		} else {
			var selected = listField.selectedIndex;
			if (selected == -1) {
				alert("Por favor, selecione uma disciplina para ser removida antes de pressionar este botao!");
			} else {  // Build arrays with the text and values to remain
				var replaceTextArray = new Array(listField.length-1);
				var replaceValueArray = new Array(listField.length-1);
				for (var i = 0; i < listField.length; i++) {
					// Put everything except the selected one into the array
					if ( i < selected) { replaceTextArray[i] = listField.options[i].text; }
					if ( i < selected) { replaceValueArray[i] = listField.options[i].value; }
					if ( i > selected ) { replaceTextArray[i-1] = listField.options[i].text; }           
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
	
	function selectAll() {
		var i = 0;
		document.studentShiftEnrolmentForm.wantedCourse.multiple=true;
		while( i < document.studentShiftEnrolmentForm.wantedCourse.options.length ) {
			document.studentShiftEnrolmentForm.wantedCourse.options[i].selected = true;
			i+=1;
		} 
	}
	
	function selectFirst() {
		var i = 0;
		//  document.studentShiftEnrolmentForm.wantedCourse.multiple=true;
		while( i <  document.studentShiftEnrolmentForm.wantedCourse.options.length ) {
			if (i == 0)
				document.studentShiftEnrolmentForm.wantedCourse.options[i].selected = true;
			else 
				document.studentShiftEnrolmentForm.wantedCourse.options[i].selected = false;
			i++;
		} 
	}
	
	//-->
</script>
<center>
	<span class="error"><html:errors /></span>
	<html:form  action="studentShiftEnrolmentManager" method="POST">
		<html:hidden property="method" value="proceedToShiftEnrolment" />
		<strong>
			Escolha o curso das cadeiras que quer frequentar:
		</strong>
		<br/>
		<html:select property="degree" size="1"onchange="document.studentShiftEnrolmentForm.method.value='enrollCourses';selectAll();document.studentShiftEnrolmentForm.submit();" >
			<logic:iterate  id="executionDegree" name="degreeList">
				<bean:define id="deg" name="executionDegree" property="infoDegreeCurricularPlan.infoDegree"/>
				<option value="<%= ((InfoDegree)deg).getSigla() %>">
					<bean:write name="deg" property="nome" />
				</option>
			</logic:iterate>
		</html:select>
		<br/>
		<br/>
		<%--
<bean:define id="wantedCourseList" name="wantedCourseList" property="wantedCourseList" type="java.util.ArrayList"/>
--%>
<%--				<td>
					Actualiza a coluna
					<br />
					da direita para
					<br />
					reflectir as disciplinas
					<br />
					a que te queres
					<br />
					inscrever,
					<br />
					utilizando os
					<br />
					botões abaixo.
					<br />
					<br />	
					Lembra-te que podes
					<br />
					modificar a tua escolha
					<br />
					posteriormente!
				</td> --%>

		<table width="80%" align="center">
			<tr>
				<td align="left">
					<b>Disciplinas do curso selecionado:</b>
					<html:select property="course" size="12" style="width:100%">
						<html:options collection="courseList" labelProperty="nome" property="idInternal"/>
					</html:select>
					<input type="button" value="Adicionar Disciplina" onclick="addToList(wantedCourse,course[course.selectedIndex].text,course[course.selectedIndex].value);" style="width:100%"/>
				</td>
			</tr>
			<tr>
				<td align="left">
					<b>Disciplinas que vai frequentar:</b>
					<html:select property="wantedCourse" multiple="false" size="12" style="width:100%">
						<html:options  collection="wantedCourse"   labelProperty="nome"  property="idInternal"/>
					</html:select>
					<input type="button"  value="Remover Disciplina" onclick="removeFromList(wantedCourse);selectFirst();" size="12" style="width:100%"/>
				</td>
			<tr/>
		</table>
		<br />
		<%-- Can't put form.submit into onclick in a submit button --%>
		<html:submit value="Continuar inscrição" onclick="document.studentShiftEnrolmentForm.method.value='proceedToShiftEnrolment';selectAll();return true;"/>
	</html:form>
</center>
