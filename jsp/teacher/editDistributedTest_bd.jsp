<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<script language="Javascript" type="text/javascript">
<!--
var select = false;
var disable = false;

function invertSelect(){
	if ( select == false ) { 
		select = true; 
	} else { 
		select = false;
	}
	for (var i=1;i<document.distributedTestForm.selected.length;i++){
		var e = document.distributedTestForm.selected[i];
		if (select == true) { e.checked = true; } else { e.checked = false; }
	}
}

function disableSelects() { 
	if ( disable == false ) { 
		disable = true; 
	} else { 
		disable = false;
	}
	var e0 = document.distributedTestForm.selected[0];
	for (var i=1;i<document.distributedTestForm.selected.length;i++){
		var e = document.distributedTestForm.selected[i];
		if (disable == true) { e0.checked=true; e.disabled = true;} else { e.disabled = false; e0.checked=false; }
	}
}


function cleanSelect() { 
	select = false; 
	document.distributedTestForm.selected[1].checked = false; 
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
			<td><html:radio property="testType" value="<%=testType.getValue()%>"/></td>
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
			<td><html:radio property="availableCorrection" value="<%=correctionAvailability.getValue()%>"/></td>
		</tr>
	</logic:iterate>
</table>
<br/>
<table>
	<tr>
		<td><b><bean:message key="message.studentFeedback"/></b></td>
	</tr>
	<tr><td></td>
		<td><bean:message key="option.manager.true"/></td><td><html:radio property="studentFeedback" value="true"/></td>
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
	<td><html:reset styleClass="inputbutton"><bean:message key="label.clear"/></html:reset></td></html:form>
	<html:form action="/testDistribution">
	<html:hidden property="page" value="0"/>
	<html:hidden property="method" value="showDistributedTests"/>
	<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>			
	<td><html:submit styleClass="inputbutton"><bean:message key="label.back"/></html:submit></td></html:form>
</tr>
</table>