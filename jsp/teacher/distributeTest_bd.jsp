<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.distributeTest"/></h2>

<script language="Javascript" type="text/javascript">
<!--
var select = false;

function invertSelect(){
	if ( select == false ) { 
		select = true; 
	} else { 
		select = false;
	}
	for (var i=0;i<document.testDistributionForm.selectedShifts.length;i++){
		var e = document.testDistributionForm.selectedShifts[i];
		if ( select == true ) { e.checked = true; } else { e.checked = false; }
	}
}

function cleanSelect() { 
	select = false; 
	document.testDistributionForm.selectedShifts[0].checked = false; 
}

// -->
</script>

<html:form action="/testDistribution">
<html:hidden property="page" value="1"/>
<html:hidden property="method" value="distributeTest"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>

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
		<td><html:text property="testBeginDate"/></td><td><span class="error"><html:errors property="testBeginDate"/></span></td>
	<tr/>
	<tr>
		<td><bean:message key="message.testBeginHour"/><bean:message key="message.hourFormat"/></td>
	</tr>
	<tr>
		<td><html:text property="testBeginHour"/></td><td><span class="error"><html:errors property="testBeginHour"/></span></td>
	<tr/>
	<tr>
		<td><bean:message key="message.testEndDate"/><bean:message key="message.dateFormat"/></td>
	</tr>
	<tr>
		<td><html:text property="testEndDate"/></td><td><span class="error"><html:errors property="testEndDate"/></span></td>
	<tr/>
	<tr>
		<td><bean:message key="message.testEndHour"/><bean:message key="message.hourFormat"/></td>
	</tr>
	<tr>
		<td><html:text property="testEndHour"/></td><td><span class="error"><html:errors property="testEndHour"/></span></td>
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
<br/>
<table>
	<tr>
		<td><b><bean:message key="message.selectShifts"/></b></td><td/><td/><td/><td><span class="error"><html:errors property="selectedShifts"/></span></td>
	</tr>
	<tr><td></td>
		<td><b><bean:message key="label.allStudents"/></b></td>
		<td>
		<html:multibox property="selectedShifts" onclick="invertSelect()">
		    <bean:message key="label.allStudents"/>
		</html:multibox> 
		</td>	
	<tr><td></td>
		<td class="listClasses-header"><bean:message key="link.executionCourse.shifts"/></td>
		<td class="listClasses-header"><bean:message key="label.curricularCourseType"/></td>
		<td class="listClasses-header"></td>
	</tr>
	<logic:iterate id="shiftType" name="shiftTypes">
		<bean:define id="iterateType" name="shiftType" property="value"/>
		<logic:iterate id="shiftList" name="shifts">
			<bean:define id="thisShiftType" name="shiftList" property="tipo"/>
			<% if((thisShiftType.toString()).equals(iterateType.toString())){ %>
				<tr><td></td>
				<td class="listClasses"><bean:write name="shiftList" property="nome"/></td>
				<td class="listClasses"><bean:write name="shiftType" property="label"/></td>
				<td class="listClasses">
					<html:multibox property="selectedShifts" onclick="cleanSelect()">
				    <bean:write name="shiftList" property="idInternal"/>
					</html:multibox> 
				</td>
				</tr>
			<%}%>
		</logic:iterate>
	</logic:iterate>
</table>
<br/>
<br/>
<html:submit styleClass="inputbutton"><bean:message key="button.save"/></html:submit> 
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>  
</html:form>
