<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<script language="Javascript" type="text/javascript">
<!--

function selectAvailableCorrection(){
	var availableCorrection = document.forms[0].availableCorrection;
	var testType = document.forms[0].testType;
	if(availableCorrection[0].checked==false && testType[2].checked==true){
		testType[0].checked=true;
		changeInformation(document.forms[0].notInquiryInformation.value);
	}
}
function selectImsFeedback(){
	var imsFeedback = document.forms[0].imsFeedback;
	var testType = document.forms[0].testType;
	if(imsFeedback[1].checked==false && testType[2].checked==true){
		testType[0].checked=true;
		changeInformation(document.forms[0].notInquiryInformation.value);
	}
}
function selectInquiry() { 
	var testType = document.forms[0].testType;
	var availableCorrection = document.forms[0].availableCorrection;
	var imsFeedback = document.forms[0].imsFeedback;
	var disable=false;
	if(testType[2].checked==true){
		availableCorrection[0].checked=true;
		imsFeedback[1].checked=true;
		disable=true;
		changeInformation(document.forms[0].inquiryInformation.value);
	}else{
		changeInformation(document.forms[0].notInquiryInformation.value);
	}

	for (var i=0; i<document.forms[0].availableCorrection.length; i++){
		var e = document.forms[0].availableCorrection[i];
		if(disable == true) e.disabled=true; else e.disabled=false;
	}
	for (var i=0; i<document.forms[0].imsFeedback.length; i++){
		var e = document.forms[0].imsFeedback[i];
		if(disable == true) e.disabled=true; else e.disabled=false;
	}
}

function changeInformation(value) {
	var actualInfo = document.forms[0].testInformation.value;
	var inquiryInformation = document.forms[0].inquiryInformation.value;
	var notInquiryInformation = document.forms[0].notInquiryInformation.value;
	
	if(actualInfo == inquiryInformation || actualInfo == notInquiryInformation){
		document.forms[0].testInformation.value=value;
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

// -->
</script>

<logic:present name="infoDistributedTest" >
<bean:define id="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />

<html:form action="/distributedTestEdition">
<h2><bean:message key="title.editDistributedTest" />&nbsp;"<bean:write name="distributedTestForm" property="title" />"</h2>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseDistributionFor" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.inquiryInformation" property="inquiryInformation" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.notInquiryInformation" property="notInquiryInformation" />
	<br />
	<table>
		<tr>
			<td class="infoop"><bean:message key="message.editDistributedTest.information" /></td>
		</tr>
	</table>
	<br />
	<table>
		<tr>
			<td><b><bean:message key="message.testType" /></b></td>
		</tr>
		<logic:iterate id="testType" name="testTypeList" type="org.apache.struts.util.LabelValueBean">
			<tr>
				<td></td>
				<td><bean:write name="testType" property="label" /></td>
				<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.testType" property="testType" value="<%=testType.getValue()%>" onclick="selectInquiry()" /></td>
			</tr>
		</logic:iterate>
	</table>
	<br />
	<table>
		<tr>
			<td><b><bean:message key="message.availableCorrection" /></b></td>
		</tr>
		<logic:iterate id="correctionAvailability" name="correctionAvailabilityList" type="org.apache.struts.util.LabelValueBean">
			<tr>
				<td></td>
				<td><bean:write name="correctionAvailability" property="label" /></td>
				<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.availableCorrection" property="availableCorrection" value="<%=correctionAvailability.getValue()%>" onclick="selectAvailableCorrection()" /></td>
			</tr>
		</logic:iterate>
	</table>
	<br />
	<table>
		<tr>
			<td><b><bean:message key="message.imsFeedback" /></b></td>
		</tr>
		<tr>
			<td></td>
			<td><bean:message key="message.yes" /></td>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.imsFeedback" property="imsFeedback" value="true" onclick="selectImsFeedback()" /></td>
		</tr>
		<tr>
			<td></td>
			<td><bean:message key="message.no" /></td>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.imsFeedback" property="imsFeedback" value="false" /></td>
		</tr>
	</table>
	<br />
	<br />
	<table>
		<tr>
			<td><b><bean:message key="label.test.information" /></b></td>
		</tr>
		<tr>
			<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.testInformation" rows="7" cols="45" property="testInformation" /></td>
		</tr>
	</table>
	<br />
	<span class="error"><!-- Error messages go here --><html:errors property="InvalidTime" /></span>
	<table>
		<tr>
			<td colspan="5"><bean:message key="message.testBeginDate" /><bean:message key="message.dateFormat" /></td>
		</tr>
		<tr>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.beginDayFormatted" maxlength="2" size="2" property="beginDayFormatted" onkeyup="changeFocus(this)" /></td>
			<td>/</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.beginMonthFormatted" maxlength="2" size="2" property="beginMonthFormatted" onkeyup="changeFocus(this)" /></td>
			<td>/</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.beginYearFormatted" maxlength="4" size="4" property="beginYearFormatted" onkeyup="changeFocus(this)" /></td>
			<td><span class="error"><!-- Error messages go here --><html:errors property="beginDayFormatted" /><html:errors property="beginMonthFormatted" /><html:errors
				property="beginYearFormatted" /></span></td>
			<tr />
			<tr>
				<td colspan="5"><bean:message key="message.testBeginHour" /><bean:message key="message.hourFormat" /></td>
			</tr>
			<tr>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.beginHourFormatted" maxlength="2" size="2" property="beginHourFormatted" onkeyup="changeFocus(this)" /></td>
				<td>:</td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.beginMinuteFormatted" maxlength="2" size="2" property="beginMinuteFormatted" onkeyup="changeFocus(this)" /></td>
				<td></td>
				<td></td>
				<td><span class="error"><!-- Error messages go here --><html:errors property="beginHourFormatted" /><html:errors property="beginMinuteFormatted" /></span></td>
				<tr />
				<tr>
					<td colspan="5"><bean:message key="message.testEndDate" /><bean:message key="message.dateFormat" /></td>
				</tr>
				<tr>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.endDayFormatted" maxlength="2" size="2" property="endDayFormatted" onkeyup="changeFocus(this)" /></td>
					<td>/</td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.endMonthFormatted" maxlength="2" size="2" property="endMonthFormatted" onkeyup="changeFocus(this)" /></td>
					<td>/</td>
					<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.endYearFormatted" maxlength="4" size="4" property="endYearFormatted" onkeyup="changeFocus(this)" /></td>
					<td><span class="error"><!-- Error messages go here --><html:errors property="endDayFormatted" /><html:errors property="endMonthFormatted" /><html:errors
						property="endYearFormatted" /></span></td>
					<tr />
					<tr>
						<td colspan="5"><bean:message key="message.testEndHour" /><bean:message key="message.hourFormat" /></td>
					</tr>
					<tr>
						<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.endHourFormatted" maxlength="2" size="2" property="endHourFormatted" onkeyup="changeFocus(this)" /></td>
						<td>:</td>
						<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.endMinuteFormatted" maxlength="2" size="2" property="endMinuteFormatted" onkeyup="changeFocus(this)" /></td>
						<td></td>
						<td></td>
						<td><span class="error"><!-- Error messages go here --><html:errors property="endHourFormatted" /><html:errors property="endMinuteFormatted" /></span></td>
						<tr />
	</table>
	<br />
	<br />
	<table>
		<tr>
			<td><b><bean:message key="label.add" />:</b></td>
			<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.addStudents" styleClass="inputbutton" property="addStudents">
				<bean:message key="link.students" />
			</html:submit></td>
			<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.addShifts" styleClass="inputbutton" property="addShifts">
				<bean:message key="link.executionCourse.shifts" />
			</html:submit></td>
		</tr>
	</table>
	<br />
	<br />

	<table align="center">
		<tr>
			<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.save" styleClass="inputbutton" property="save">
				<bean:message key="button.save" />
			</html:submit></td>
			<td><html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
				<bean:message key="label.clear" />
			</html:reset></td>
</html:form>
<html:form action="/testDistribution">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showDistributedTests" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>" />
	<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.back" />
	</html:submit></td>
</html:form>

</tr>
</table>
</logic:present>
