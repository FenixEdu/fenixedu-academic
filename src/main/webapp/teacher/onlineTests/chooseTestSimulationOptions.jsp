<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
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

// -->
</script>

<h2><bean:message key="title.distributeTest"/></h2>
<table>
	<tr>
		<td class="infoop"><bean:message key="message.distributeTest.information" /></td>
	</tr>
</table>
<br/>
<html:form action="/studentTestManagement">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareSimulateTest"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.inquiryInformation" property="inquiryInformation"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.notInquiryInformation" property="notInquiryInformation"/>

<table>
	<tr>
		<td><b><bean:message key="message.testType"/></b></td>
	</tr>
	<logic:iterate id="testType" name="testTypeList" type="org.apache.struts.util.LabelValueBean">
		<tr><td></td>
			<td><bean:write name="testType" property="label"/></td>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.testType" property="testType" value="<%=testType.getValue()%>" onclick="selectInquiry()"/></td>
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
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.availableCorrection" property="availableCorrection" value="<%=correctionAvailability.getValue()%>" onclick="selectAvailableCorrection()"/></td>
		</tr>
	</logic:iterate>
</table>
<br/>
<table>
	<tr>
		<td><b><bean:message key="message.imsFeedback"/></b></td>
	</tr>
	<tr>
		<td></td><td><bean:message key="message.yes"/></td><td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.imsFeedback" property="imsFeedback" value="true" onclick="selectImsFeedback()"/></td>
	</tr>
	<tr>
		<td></td><td><bean:message key="message.no"/></td><td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.imsFeedback" property="imsFeedback" value="false" /></td>
	</tr>
</table>
<br/>
<br/>
<table>
	<tr>
		<td><b><bean:message key="label.test.information"/></b></td>
	</tr>
	<tr>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.testInformation" rows="7" cols="45" property="testInformation"/></td>
	</tr>
</table>
<br/>
<br/>
<br/>
<table>
	<tr>
		<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.Simulate"/></html:submit></td>
	</tr>
</table>
</html:form>
<br/>
<br/>