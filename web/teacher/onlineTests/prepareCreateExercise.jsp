<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<script language="Javascript" type="text/javascript">
<!--
var select = false;

function invertSelect(){
	if ( select == false ) { 
		select = true; 
	} else { 
		select = false;
	}
	for (var i=0; i<document.forms[0].shuffle.length; i++){
		var e = document.forms[0].shuffle[i];
		if (select == true) { e.checked = true; } else { e.checked = false; }
	}
}

function checkResponseTextBox(){
		var e = document.forms[0].response[0];
		if (e.checked == true) {
			document.forms[0].maxChar.disabled=true;
			document.forms[0].rows.disabled=false;
		} else { 
			document.forms[0].rows.disabled=true;
			document.forms[0].maxChar.disabled=false;
		}
}

function changeResponseTextBox(input){
	if (input == document.forms[0].rows){
		document.forms[0].response[0].checked=true;
		document.forms[0].maxChar.disabled=true;
		document.forms[0].rows.disabled=false;
	}else{
		document.forms[0].response[1].checked=true;
		document.forms[0].rows.disabled=true;
		document.forms[0].maxChar.disabled=false;
	} 
}

function changeFocus(input) { 
	if( input.value.length == input.maxLength) { 
		next=getIndex(input)+1;
		if (next<document.forms[0].elements.length){
			document.forms[0].elements[next].focus();
		}
	} 
} 

function getIndex(input){
	var index = -1, i = 0; 
	while ( i < input.form.length && index == -1 ) 
	if ( input.form[i] == input ) { 
		index = i; 
	} else { 
		i++; 
	} 
	return index; 
}
function add(){
	document.forms[0].method.value="addNewCondition";
	document.forms[0].page.value="1";
	document.forms[0].submit();
}

function remove(i){
	document.forms[0].method.value="removeCondition";
	document.forms[0].page.value="1";
	document.forms[0].conditionId.value=i;
	document.forms[0].submit();
}

// -->
</script>

<h2><bean:message key="title.createExercise"/></h2>

<html:form action="/exerciseCreation">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createExercise"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.questionType" property="questionType"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.optionNumber" property="optionNumber"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.cardinalityType" property="cardinalityType"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.conditionId" property="conditionId"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.evaluationQuestion" property="evaluationQuestion"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.exerciseCode" property="exerciseCode"/>
<span class="error"><!-- Error messages go here --><html:errors /></span>


<logic:empty name="exerciseCreationForm" property="exerciseCode">
<br/>
<table>
	<tr><td class="infoop"><bean:message key="message.createMetadata.information" /></td></tr>
</table>
<br/>
<table>
<tr><td><b><bean:message key="message.tests.author"/></b></td><td><html:text bundle="HTMLALT_RESOURCES" altKey="text.author" size="25" property="author"/></td></tr>
<tr><td><b><bean:message key="label.description"/>:</b></td><td><html:text bundle="HTMLALT_RESOURCES" altKey="text.description" size="25" property="description"/></td></tr>
<tr><td><b><bean:message key="label.test.difficulty"/>:</b></td>
<td>
<html:select bundle="HTMLALT_RESOURCES" altKey="select.difficulty" property="difficulty">
<logic:iterate id="questionDifficulty" name="questionDifficultyList" type="org.apache.struts.util.LabelValueBean">
	<html:option value="<%=questionDifficulty.getValue()%>"><bean:write name="questionDifficulty" property="label"/></html:option>
</logic:iterate>
</html:select>
</td></tr>
<tr><td><b><bean:message key="label.test.materiaPrincipal"/>:</b></td><td><html:text bundle="HTMLALT_RESOURCES" altKey="text.mainSubject" size="25" property="mainSubject"/></td></tr>
<tr><td><b><bean:message key="label.test.materiaSecundaria"/>:</b></td><td><html:text bundle="HTMLALT_RESOURCES" altKey="text.secondarySubject" size="25" property="secondarySubject"/></td></tr>
<tr><td><b><bean:message key="label.test.learningTime"/>:</b><bean:message key="message.hourFormat"/></td>
<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.learningHour" size="2" maxlength="2" property="learningHour" onkeyup="changeFocus(this)"/>:<html:text bundle="HTMLALT_RESOURCES" altKey="text.learningMinute" size="2" maxlength="2" property="learningMinute" onkeyup="changeFocus(this)"/></td></tr>
<tr><td><b><bean:message key="label.exam.enrollment.year"/>:</b></td><td><html:text bundle="HTMLALT_RESOURCES" altKey="text.level" size="2" maxlength="2" property="level"/></td></tr>
</table>
</logic:empty>
<bean:define id="questionType" name="exerciseCreationForm" property="questionType"/>
<bean:define id="optionNumber" name="exerciseCreationForm" property="optionNumber"/>
<bean:define id="cardinalityType" name="exerciseCreationForm" property="cardinalityType"/>
<br/>

<logic:equal name="exerciseCreationForm" property="evaluationQuestion" value="true">
	<logic:equal name="questionType" value="1"> <%-- QuestiobType.LID --%>
	<table>
		<tr><td class="infoop"><bean:message key="message.createLIDExercise.information" /></td></tr>
	</table>
	</logic:equal>
	<logic:equal name="questionType" value="2"> <%-- QuestiobType.STR --%>
	<table>
		<tr><td class="infoop"><bean:message key="message.createSTRExercise.information" /></td></tr>
	</table>
	</logic:equal>
	<logic:equal name="questionType" value="3"> <%-- QuestiobType.NUM --%>
	<table>
		<tr><td class="infoop"><bean:message key="message.createNUMExercise.information" /></td></tr>
	</table>
	</logic:equal>
	<br/>
	<table><tr>
	<td><bean:message key="message.tests.questionValue"/></td>
	<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.questionValue" size="2" maxlength="2" property="questionValue"/></td>
	</tr></table>
</logic:equal>
<logic:notEqual name="exerciseCreationForm" property="evaluationQuestion" value="true">
	<logic:equal name="questionType" value="1"> <%-- QuestiobType.LID --%>
	<table>
		<tr><td class="infoop"><bean:message key="message.createLIDInquiryExercise.information" /></td></tr>
	</table>
	</logic:equal>
	<logic:equal name="questionType" value="2"> <%-- QuestiobType.STR --%>
	<table>
		<tr><td class="infoop"><bean:message key="message.createSTRInquiryExercise.information" /></td></tr>
	</table>
	</logic:equal>
	<logic:equal name="questionType" value="3"> <%-- QuestiobType.NUM --%>
	<table>
		<tr><td class="infoop"><bean:message key="message.createNUMInquiryExercise.information" /></td></tr>
	</table>
	</logic:equal>
</logic:notEqual>
<br/>
<table>
<tr><td><b><bean:message key="label.questionText"/></b></td></tr>
<tr><td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.questionText" rows="5" cols="85" property="questionText"/></td></tr>
</table>

<logic:equal name="questionType" value="1"> <%-- QuestiobType.LID --%>
	<table class="infoop" >
	<logic:equal  name="exerciseCreationForm" property="evaluationQuestion" value="true">
		<tr><td align='center'><b><bean:message key="label.correctResponse"/></b></td>
	</logic:equal>
	<td align='center'><b><bean:message key="label.options"/></b></td>
	<logic:equal  name="exerciseCreationForm" property="evaluationQuestion" value="true">
		<td align='center'>
		<html:link href="javascript:invertSelect()">
		<b><bean:message key="label.shuffle"/></b>
		</html:link>
		</td>
	</logic:equal>
	<td></td>
	</tr>
	<% for(int i=1; i<=new Integer(optionNumber.toString()).intValue() ;i++){%>
		
		<logic:equal  name="exerciseCreationForm" property="evaluationQuestion" value="true">
			<logic:equal name="cardinalityType" value="1"> <%-- CardinalityType.SINGLE --%>
				<tr><td align='center'><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.correctOptions" property="correctOptions" value="<%=new Integer(i).toString() %>"/></td>
			</logic:equal>
			<logic:equal name="cardinalityType" value="2"> <%-- CardinalityType.MULTIPLE --%>
				<tr><td align='center'>
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.correctOptions" property="correctOptions">
						<bean:define id="correctOptions" value="<%=new Integer(i).toString()%>"/>
						<bean:write name="correctOptions"/>
					</html:multibox>
				</td>
			</logic:equal>
		</logic:equal>
		
		<bean:define id="value" value=""/>
		<logic:notEmpty name="exerciseCreationForm" property='<%="options["+new Integer(i-1)+"]"%>'>
			<bean:define id="value" name="exerciseCreationForm" property='<%="options["+new Integer(i-1)+"]"%>' type="java.lang.String"/>
		</logic:notEmpty>
		<td align='center'><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.options" rows="3" cols="70" property="options" value="<%=value.toString()%>"/></td>
		<logic:equal  name="exerciseCreationForm" property="evaluationQuestion" value="true">
			<td align='center'>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.shuffle" property="shuffle">
					<bean:define id="shuffleValue" value="<%=new Integer(i).toString()%>"/>
					<bean:write name="shuffleValue"/>
				</html:multibox>
			</td>
		</logic:equal>
		<logic:greaterThan name="optionNumber" value="1">
			<td align='center'><html:link href='<%="javascript:remove("+new Integer(i-1)+")"%>'>
					<img border='0' src="<%= request.getContextPath()%>/images/remove.gif" alt="<bean:message key="remove" bundle="IMAGE_RESOURCES" />"/></td></tr>
				</html:link></td>
		</logic:greaterThan>
		</tr>
	<%}%>
	<logic:lessThan name="optionNumber" value="99">
		<tr><td><html:link href="javascript:add();">
			<img border='0' src="<%= request.getContextPath()%>/images/add.gif" alt="<bean:message key="add" bundle="IMAGE_RESOURCES" />"/>
		</html:link></td></tr>
	</logic:lessThan>
</table>
</logic:equal>
<logic:notEqual name="questionType" value="1"> <%-- QuestionType.STR and QuestionType.NUM --%>
<table>
	<tr>
		<td><b><bean:message key="label.breakLineBeforeResponseTextBox"/></b></td>
		<td><html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.breakLineBeforeResponseBox" property="breakLineBeforeResponseBox" value="true"/></td>
	</tr>
</table>
<br/><br/>
<table>
	<tr><td><b><bean:message key="label.responseTextBox"/></b></td></tr>
	<tr><td/><td/><td><bean:message key="label.numberOfColumns"/></td><td><html:text bundle="HTMLALT_RESOURCES" altKey="text.cols" size="2" maxlength="2" property="cols"/></td></tr>
	<bean:define id="disable" value="false"/>
	<logic:equal name="exerciseCreationForm" property="rows" value="0">
		<bean:define id="disable" value="true"/>
	</logic:equal>
	<tr><td/><td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.response" property="response" value="true" onclick="checkResponseTextBox()"/></td><td><bean:message key="label.numberOfRows"/></td><td><html:text bundle="HTMLALT_RESOURCES" altKey="text.rows" size="2" maxlength="2" property="rows" disabled="<%=new Boolean(disable).booleanValue()%>" onkeyup="changeResponseTextBox(this)"/></td></tr>
	<bean:define id="disable" value="false"/>
	<logic:equal name="exerciseCreationForm" property="maxChar" value="0">
		<bean:define id="disable" value="true"/>
	</logic:equal>
	<tr><td/><td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.response" property="response" value="false" onclick="checkResponseTextBox()"/></td><td><bean:message key="label.maxNumberOfChars"/></td><td><html:text bundle="HTMLALT_RESOURCES" altKey="text.maxChar" size="4" maxlength="4" property="maxChar" disabled="<%=new Boolean(disable).booleanValue()%>" onkeyup="changeResponseTextBox(this)"/></td></tr>
</table>
<logic:equal name="questionType" value="3"> <%-- QuestionType.NUM --%>
<br/>
<table>
		<tr><td><b><bean:message key="message.tests.questionCardinality"/></b></td></tr>
		<tr><td/>
			<td><bean:message key="label.integer"/></td>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.integerType" property="integerType" value="true"/></td>
		</tr>
		<tr><td/>
			<td><bean:message key="label.decimal"/></td>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.integerType" property="integerType" value="false"/></td>
		</tr>
</table>
</logic:equal>
<br/>
<logic:equal  name="exerciseCreationForm" property="evaluationQuestion" value="true">
<table class="infoop">

	<tr>
		<td align='center'><b><bean:message key="label.condition"/></b></td>		
		<td align="center"><b><bean:message key="label.correctResponse"/></b></td>
	</tr>
	
	<% for(int i=1; i<=new Integer(optionNumber.toString()).intValue() ;i++){%>
	<tr>
		<td align='center'>
		
		<bean:define id="signalCode" name="exerciseCreationForm" property='<%="signal["+new Integer(i-1)+"]"%>'/>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.signal" name="exerciseCreationForm" property="signal" value="<%=signalCode.toString()%>">
		<logic:iterate id="signal2" name="signals" type="org.apache.struts.util.LabelValueBean">
			<html:option value="<%=signal2.getValue()%>"><bean:write name="signal2" property="label"/></html:option>
		</logic:iterate>
		</html:select></td>
		
		<bean:define id="value" value=""/>
		<logic:notEmpty name="exerciseCreationForm" property='<%="correctOptions["+new Integer(i-1)+"]"%>'>
			<bean:define id="value" name="exerciseCreationForm" property='<%="correctOptions["+new Integer(i-1)+"]"%>' type="java.lang.String"/>
		</logic:notEmpty>
		
		<logic:equal name="questionType" value="2"> <%-- QuestionType.STR --%>
			<td align='center'><html:text bundle="HTMLALT_RESOURCES" altKey="text.correctOptions" size="70" property="correctOptions" value="<%=value.toString()%>"/></td>
		</logic:equal>	
		<logic:equal name="questionType" value="3"> <%-- QuestionType.NUM --%>
			<td align='center'><html:text bundle="HTMLALT_RESOURCES" altKey="text.correctOptions" size="20" property="correctOptions" value="<%=value.toString()%>"/></td>
			<logic:greaterThan name="optionNumber" value="1">
			<td><html:link href='<%="javascript:remove("+new Integer(i-1)+")"%>'>
				<img border='0' src="<%= request.getContextPath()%>/images/remove.gif" alt="<bean:message key="remove" bundle="IMAGE_RESOURCES" />"/></td></tr>
			</html:link></td>
			</logic:greaterThan>
		</logic:equal>
	</tr>
	<logic:equal name="questionType" value="2"> <%-- QuestionType.STR --%>
		<tr>
			<td align='center' colspan="2"><b><bean:message key="label.caseSensitive"/></b>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.caseSensitive" property="caseSensitive" value="true"/></td>
		</tr>
	</logic:equal>
	<%}%>
	<logic:equal name="questionType" value="3"> <%-- QuestionType.NUM --%>
		<logic:lessThan name="optionNumber" value="99">
			<tr><td>
			<html:link href="javascript:add();">
				<img border='0' src="<%= request.getContextPath()%>/images/add.gif" alt="<bean:message key="add" bundle="IMAGE_RESOURCES" />"/></td></tr>
			</html:link>
			</td></tr>
		</logic:lessThan>
	</logic:equal>
</table>
<br/><br/>
</logic:equal>
<table>
	<tr>
		<td><b><bean:message key="label.breakLineAfterResponseTextBox"/></b><html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.breakLineAfterResponseBox" property="breakLineAfterResponseBox" value="true"/></td>
	</tr>
	<tr><td><b><bean:message key="label.secondQuestionText"/></b></td></tr>
	<tr><td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.secondQuestionText" rows="5" cols="85" property="secondQuestionText"/></td></tr>
</table>
</logic:notEqual>
<br/>
<logic:equal  name="exerciseCreationForm" property="evaluationQuestion" value="true">
<table>
<tr><td><b><bean:message key="label.correctFeedbackText"/></b></td></tr>
<tr><td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.correctFeedbackText" rows="3" cols="85" property="correctFeedbackText"/></td></tr>
<tr><td><b><bean:message key="label.wrongFeedbackText"/></b></td></tr>
<tr><td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.wrongFeedbackText" rows="3" cols="85" property="wrongFeedbackText"/></td></tr>
</table>
</logic:equal>
<br/>
<br/>
<table>
<tr>
<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="javascript:document.forms[0].method.value='createExercise';this.form.page.value=2;"><bean:message key="label.create"/></html:submit></td>
<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="javascript:document.forms[0].method.value='previewExercise';this.form.page.value=2;"><bean:message key="link.students.enrolled.exam"/></html:submit></td>
</html:form>

<html:form action="/testsManagement">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="testsFirstPage"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.cancel"/></html:submit></td>
</html:form>
</tr>
</table>