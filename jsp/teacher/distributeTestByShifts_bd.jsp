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
	for (var i=0; i<document.forms[0].selected.length; i++){
		var e = document.forms[0].selected[i];
		if (select == true) { e.checked = true; } else { e.checked = false; }
	}
}

function cleanSelect() { 
	select = false; 
	document.forms[0].selected[0].checked = false; 
}
// -->
</script>

<bean:size id="shiftsSize" name="shifts"/>
<logic:notEqual name="shiftsSize" value="0">

<html:form action="/testDistributionByShifts">
<html:hidden property="page" value="2"/>
<html:hidden property="method" value="distributeTest"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>

<html:hidden property="testInformation"/>
<html:hidden property="beginDateFormatted"/>
<html:hidden property="beginHourFormatted"/>
<html:hidden property="endDateFormatted"/>
<html:hidden property="endHourFormatted"/>
<html:hidden property="testType"/>
<html:hidden property="availableCorrection"/>
<html:hidden property="studentFeedback"/>
<html:hidden property="insertByShifts" value="true"/>

<table>
	<tr>
		<td><b><bean:message key="message.selectShifts"/></b></td><td/><td/><td/><td><span class="error"><html:errors property="selected"/></span></td>
	</tr>
	<tr><td></td>
		<td><b><bean:message key="label.allShifts"/></b></td>
		<td>
		<html:multibox property="selected" onclick="invertSelect()">
		    <bean:message key="label.allShifts"/>
		</html:multibox> 
		</td>
	</tr>
	<tr><td></td>
		<td class="listClasses-header"><bean:message key="link.executionCourse.shifts"/></td>
		<td class="listClasses-header"><bean:message key="label.curricularCourseType"/></td>
		<td class="listClasses-header"></td>
	</tr>
	<logic:iterate id="shiftList" name="shifts" indexId="index">
		<logic:equal name="index" value="0">
			<bean:define id="tipoBefore" name="shiftList" property="tipo.fullNameTipoAula"/>
		</logic:equal>
		<logic:notEqual name="index" value="0">
			<logic:notEqual name="shiftList" property="tipo.fullNameTipoAula" value="<%tipoBefore%>">
				<tr height=20><td></td></tr>
				<tr><td></td>
					<td class="listClasses-header"><bean:message key="link.executionCourse.shifts"/></td>
					<td class="listClasses-header"><bean:message key="label.curricularCourseType"/></td>
					<td class="listClasses-header"></td>
				</tr>		
			</logic:notEqual>
		</logic:notEqual>
		<tr><td></td>
			<td class="listClasses"><bean:write name="shiftList" property="nome"/></td>
			<td class="listClasses"><bean:write name="shiftList" property="tipo.fullNameTipoAula"/></td>
			<td class="listClasses">
				<html:multibox property="selected" onclick="cleanSelect()">
			    <bean:write name="shiftList" property="idInternal"/>
				</html:multibox> 
			</td>
		</tr>
		<bean:define id="tipoBefore" name="shiftList" property="tipo.fullNameTipoAula"/>
	</logic:iterate>
</table>
<table><tr>
<td><html:submit styleClass="inputbutton" property="distribute"><bean:message key="link.student.room.distribution"/></html:submit></td></html:form>
<html:form action="/testsManagement">
<html:hidden property="page" value="0"/>
<html:hidden property="method" value="showTests"/>
<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<td><html:submit styleClass="inputbutton"><bean:message key="button.cancel"/></html:submit></td>
</html:form>
</tr></table>
</logic:notEqual>

<logic:equal name="shiftsSize" value="0">
	<html:form action="/testsManagement">
	<html:hidden property="page" value="0"/>
	<html:hidden property="method" value="testsFirstPage"/>
	<html:hidden property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
		<table>
		<tr><td><span class="error"><bean:message key="error.shifts.class.not.available"/></span></tr></td>
		<tr><td><html:submit styleClass="inputbutton"><bean:message key="button.cancel"/></html:submit></tr></td>
		</table>
	</html:form>
</logic:equal>