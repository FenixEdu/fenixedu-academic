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
	}
}
function selectStudentFeedback(){
	var studentFeedback = document.forms[0].studentFeedback;
	var testType = document.forms[0].testType;
	if(studentFeedback[1].checked==false && testType[2].checked==true){
		testType[0].checked=true;
	}
}
function selectInquiry() { 
	var testType = document.forms[0].testType;
	var availableCorrection = document.forms[0].availableCorrection;
	var studentFeedback = document.forms[0].studentFeedback;
	var disable=false;
	if(testType[2].checked==true){
		availableCorrection[0].checked=true;
		studentFeedback[1].checked=true;
		disable=true;
	}
	for (var i=0; i<document.forms[0].availableCorrection.length; i++){
		var e = document.forms[0].availableCorrection[i];
		if(disable == true) e.disabled=true; else e.disabled=false;
	}
	for (var i=0; i<document.forms[0].studentFeedback.length; i++){
		var e = document.forms[0].studentFeedback[i];
		if(disable == true) e.disabled=true; else e.disabled=false;
	}
}
function changeMethod(){
	document.forms[0].method.value="showDistributedTests";
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
<br/>
<table>
	<tr>
		<td class="infoop"><bean:message key="message.editDistributedTest.information" /></td>
	</tr>
</table>
<br/>
<span class="error"><html:errors property="InvalidTime"/></span>
<table>
	<tr>
		<td><bean:message key="label.test.information"/></td>
	</tr>
	<tr>
		<td><html:textarea rows="7" cols="45" property="testInformation"/></td>
	</tr>
	<tr>
		<td><bean:message key="message.testBeginDate"/><bean:message key="message.dateFormat"/></td>
	</tr>
	<tr>
		<td><html:text property="beginDateFormatted"/></td><td><span class="error"><html:errors property="beginDateFormatted"/></span></td>
	<tr/>
	<tr>
		<td><bean:message key="message.testBeginHour"/><bean:message key="message.hourFormat"/></td>
	</tr>
	<tr>
		<td><html:text property="beginHourFormatted"/></td><td><span class="error"><html:errors property="beginHourFormatted"/></span></td>
	<tr/>
	<tr>
		<td><bean:message key="message.testEndDate"/><bean:message key="message.dateFormat"/></td>
	</tr>
	<tr>
		<td><html:text property="endDateFormatted"/></td><td><span class="error"><html:errors property="endDateFormatted"/></span></td>
	<tr/>
	<tr>
		<td><bean:message key="message.testEndHour"/><bean:message key="message.hourFormat"/></td>
	</tr>
	<tr>
		<td><html:text property="endHourFormatted"/></td><td><span class="error"><html:errors property="endHourFormatted"/></span></td>
	<tr/>
</table>
<br/>
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
		<td><b><bean:message key="message.studentFeedback"/></b></td>
	</tr>
	<tr><td></td>
		<td><bean:message key="option.manager.true"/></td><td><html:radio property="studentFeedback" value="true" onclick="selectStudentFeedback()"/></td>
	</tr>
	<tr><td></td>
		<td><bean:message key="option.manager.false"/></td><td><html:radio property="studentFeedback" value="false"/></td>
	</tr>
</table>
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
	<td><html:submit styleClass="inputbutton" onclick="changeMethod()"><bean:message key="label.back"/></html:submit></td>
</tr>
</table>
</html:form>