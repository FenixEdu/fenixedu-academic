<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

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

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="infoDistributedTest" name="component" property="infoDistributedTest"/>
<bean:define id="executionCourse" name="component" property="executionCourse"/>
<bean:define id="objectCode" name="executionCourse" property="idInternal"/>

<h2><bean:message key="title.editDistributedTest"/>&nbsp;"<bean:write name="infoDistributedTest" property="title"/>"</h2>
<html:form action="/distributedTestEdition">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="chooseDistributionFor"/>
<html:hidden property="distributedTestCode" value="<%=(pageContext.findAttribute("distributedTestCode")).toString()%>"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden property="inquiryInformation"/>
<html:hidden property="notInquiryInformation"/>
<br/>
<table>
	<tr>
		<td class="infoop"><bean:message key="message.editDistributedTest.information" /></td>
	</tr>
</table>
<br/>
<table>
	<tr>
		<td><b><bean:message key="message.testType"/></b></td>
	</tr>
	<logic:iterate id="testType" name="testTypeList" type="org.apache.struts.util.LabelValueBean">
		<tr><td></td>
			<td><bean:write name="testType" property="label"/></td>
			<td><html:radio property="testType" value="<%=testType.getValue()%>" onclick="selectInquiry()"/></td>
		</tr>
	</logic:iterate>
</table>
<br/>
<table>
	<tr>
		<td><b><bean:message key="message.availableCorrection"/></b></td>
	</tr>
	<logic:iterate id="correctionAvailability" name="correctionAvailabilityList" type="org.apache.struts.util.LabelValueBean">
		<tr><td></td>
			<td><bean:write name="correctionAvailability" property="label"/></td>
			<td><html:radio property="availableCorrection" value="<%=correctionAvailability.getValue()%>" onclick="selectAvailableCorrection()"/></td>
		</tr>
	</logic:iterate>
</table>
<br/>
<table>
	<tr>
		<td><b><bean:message key="message.imsFeedback"/></b></td>
	</tr>
	<tr>
		<td></td><td><bean:message key="option.manager.true"/></td><td><html:radio property="imsFeedback" value="true" onclick="selectImsFeedback()"/></td>
	</tr>
	<tr>
		<td></td><td><bean:message key="option.manager.false"/></td><td><html:radio property="imsFeedback" value="false" /></td>
	</tr>
</table>
<br/>
<br/>
<table>
	<tr>
		<td><b><bean:message key="label.test.information"/></b></td>
	</tr>
	<tr>
		<td><html:textarea rows="7" cols="45" property="testInformation"/></td>
	</tr>
</table>
<br/>
<span class="error"><html:errors property="InvalidTime"/></span>
<table>
	<tr><td colspan="5"><bean:message key="message.testBeginDate"/><bean:message key="message.dateFormat"/></td></tr>
	<tr>
		<td><html:text maxlength="2" size="2" property="beginDayFormatted" onkeyup="changeFocus(this)"/></td>
		<td>/</td>
		<td><html:text maxlength="2" size="2" property="beginMonthFormatted" onkeyup="changeFocus(this)"/></td>
		<td>/</td>
		<td><html:text maxlength="4" size="4" property="beginYearFormatted" onkeyup="changeFocus(this)"/></td>
		<td><span class="error"><html:errors property="beginDayFormatted"/><html:errors property="beginMonthFormatted"/><html:errors property="beginYearFormatted"/></span></td>
	<tr/>
	<tr><td colspan="5"><bean:message key="message.testBeginHour"/><bean:message key="message.hourFormat"/></td></tr>
	<tr>
		<td><html:text maxlength="2" size="2" property="beginHourFormatted" onkeyup="changeFocus(this)"/></td>
		<td>:</td>
		<td><html:text maxlength="2" size="2" property="beginMinuteFormatted" onkeyup="changeFocus(this)"/></td>
		<td></td><td></td>
		<td><span class="error"><html:errors property="beginHourFormatted"/><html:errors property="beginMinuteFormatted"/></span></td>
	<tr/>
	<tr><td colspan="5"><bean:message key="message.testEndDate"/><bean:message key="message.dateFormat"/></td></tr>
	<tr>
		<td><html:text maxlength="2" size="2" property="endDayFormatted" onkeyup="changeFocus(this)"/></td>
		<td>/</td>
		<td><html:text maxlength="2" size="2" property="endMonthFormatted" onkeyup="changeFocus(this)"/></td>
		<td>/</td>
		<td><html:text maxlength="4" size="4" property="endYearFormatted" onkeyup="changeFocus(this)"/></td>
		<td><span class="error"><html:errors property="endDayFormatted"/><html:errors property="endMonthFormatted"/><html:errors property="endYearFormatted"/></span></td>
	<tr/>
	<tr><td colspan="5"><bean:message key="message.testEndHour"/><bean:message key="message.hourFormat"/></td></tr>
	<tr>
		<td><html:text maxlength="2" size="2" property="endHourFormatted" onkeyup="changeFocus(this)"/></td>
		<td>:</td>
		<td><html:text maxlength="2" size="2" property="endMinuteFormatted" onkeyup="changeFocus(this)"/></td>
		<td></td><td></td>
		<td><span class="error"><html:errors property="endHourFormatted"/><html:errors property="endMinuteFormatted"/></span></td>
	<tr/>
</table>
<br/>
<br/>
<table>
	<tr>
		<td><b><bean:message key="label.add"/>:</b></td>
		<td><html:submit styleClass="inputbutton" property="addShifts"><bean:message key="link.executionCourse.shifts"/></html:submit></td>
		<td><html:submit styleClass="inputbutton" property="addStudents"><bean:message key="link.students"/></html:submit></td>
	</tr>
</table>
<br/>
<br/>

<table align="center">
<tr>
	<td><html:submit styleClass="inputbutton" property="save"><bean:message key="button.save"/></html:submit></td>
	<td><html:reset styleClass="inputbutton"><bean:message key="label.clear"/></html:reset></td>
	</html:form>
	<html:form action="/testDistribution">
	<html:hidden property="page" value="0"/>
	<html:hidden property="method" value="showDistributedTests"/>
	<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
		<td><html:submit styleClass="inputbutton"><bean:message key="label.back"/></html:submit></td>
	</html:form>
	
</tr>
</table>
