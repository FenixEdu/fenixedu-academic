<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<script language="Javascript" type="text/javascript">
<!--

function selectAvailableCorrection(){
	var availableCorrection = document.distributedTestForm.availableCorrection;
	var testType = document.distributedTestForm.testType;
	if(availableCorrection[0].checked==false && testType[2].checked==true){
		testType[0].checked=true;
	}
}
function selectStudentFeedback(){
	var studentFeedback = document.distributedTestForm.studentFeedback;
	var testType = document.distributedTestForm.testType;
	if(studentFeedback[1].checked==false && testType[2].checked==true){
		testType[0].checked=true;
	}
}
function selectInquiry() { 
	var testType = document.distributedTestForm.testType;
	var availableCorrection = document.distributedTestForm.availableCorrection;
	var studentFeedback = document.distributedTestForm.studentFeedback;
	var disable=false;
	if(testType[2].checked==true){
		availableCorrection[0].checked=true;
		studentFeedback[1].checked=true;
		disable=true;
	}
	for (var i=0; i<document.distributedTestForm.availableCorrection.length; i++){
		var e = document.distributedTestForm.availableCorrection[i];
		if(disable == true) e.disabled=true; else e.disabled=false;
	}
	for (var i=0; i<document.distributedTestForm.studentFeedback.length; i++){
		var e = document.distributedTestForm.studentFeedback[i];
		if(disable == true) e.disabled=true; else e.disabled=false;
	}
}

// -->
</script>

<h2><bean:message key="title.distributeTest"/></h2>
<table>
	<tr>
		<td class="infoop"><bean:message key="message.distributeTest.information" /></td>
	</tr>
</table>
<br/>
<html:form action="/testDistribution">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="chooseDistributionFor"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
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
		<td><bean:message key="option.manager.false"/></td><td><html:radio property="studentFeedback" value="false" /></td>
	</tr>
</table>
<br/>
<br/>
<table>
	<tr>
		<td><b><bean:message key="label.distributeFor"/>:</b></td>
		<td><html:submit styleClass="inputbutton" property="shifts"><bean:message key="link.executionCourse.shifts"/></html:submit></td>
		<td><html:submit styleClass="inputbutton" property="students"><bean:message key="link.students"/></html:submit></td>
	</tr>
</table>
<br/>
<br/>
<table align="center">
	<tr>
		<td><html:reset styleClass="inputbutton"><bean:message key="label.clear"/></html:reset></td>
		</html:form>
		<html:form action="/testsManagement">
		<html:hidden property="page" value="0"/>
		<html:hidden property="method" value="showTests"/>
		<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
		<td><html:submit styleClass="inputbutton" property="action"><bean:message key="label.back"/></html:submit></td>
		</html:form>
	</tr>
</table>