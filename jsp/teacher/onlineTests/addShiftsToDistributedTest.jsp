<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
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

<html:form action="/distributedTestEdition">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%=(pageContext.findAttribute("distributedTestCode")).toString()%>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testInformation" property="testInformation"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.beginDayFormatted" property="beginDayFormatted"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.beginMonthFormatted" property="beginMonthFormatted"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.beginYearFormatted" property="beginYearFormatted"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.beginHourFormatted" property="beginHourFormatted"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.beginMinuteFormatted" property="beginMinuteFormatted"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.endDayFormatted" property="endDayFormatted"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.endMonthFormatted" property="endMonthFormatted"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.endYearFormatted" property="endYearFormatted"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.endHourFormatted" property="endHourFormatted"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.endMinuteFormatted" property="endMinuteFormatted"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testType" property="testType"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.availableCorrection" property="availableCorrection"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.imsFeedback" property="imsFeedback"/>

<bean:size id="shiftsSize" name="shifts"/>
<logic:equal name="shiftsSize" value="0">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareEditDistributedTest"/>
	<table>
		<tr><td><span class="error"><bean:message key="error.shifts.class.not.available"/></span></tr></td>
		<tr><td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.back"/></html:submit></tr></td>
	</table>
</logic:equal>

<logic:notEqual name="shiftsSize" value="0">

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editDistributedTest"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.insertByShifts" property="insertByShifts" value="true"/>
<table>
	<tr>
		<td><b><bean:message key="message.selectShifts"/></b></td><td/><td/><td/><td><span class="error"><!-- Error messages go here --><html:errors property="selected"/></span></td>
	</tr>
	<tr><td></td>
		<td><b><bean:message key="label.allShifts"/></b></td>
		<td>
		<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selected" property="selected" onclick="invertSelect()">
		    <bean:message key="label.allShifts"/>
		</html:multibox> 
		</td>
	</tr>
	<tr><td></td>
		<th class="listClasses-header"><bean:message key="link.executionCourse.shifts"/></th>
		<th class="listClasses-header"><bean:message key="label.curricularCourseType"/></th>
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
					<th class="listClasses-header"><bean:message key="link.executionCourse.shifts"/></th>
					<th class="listClasses-header"><bean:message key="label.curricularCourseType"/></th>
					<td class="listClasses-header"></td>
				</tr>		
			</logic:notEqual>
		</logic:notEqual>
		<tr><td></td>
			<td class="listClasses"><bean:write name="shiftList" property="nome"/></td>
			<td class="listClasses"><bean:write name="shiftList" property="tipo.fullNameTipoAula"/></td>
			<td class="listClasses">
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selected" property="selected" onclick="cleanSelect()">
			    <bean:write name="shiftList" property="idInternal"/>
				</html:multibox> 
			</td>
		</tr>
		<bean:define id="tipoBefore" name="shiftList" property="tipo.fullNameTipoAula"/>
	</logic:iterate>
</table>
<table><tr>
<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="link.student.room.distribution"/></html:submit></td>
<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="javascript:document.forms[0].method.value='prepareEditDistributedTest';this.form.page.value=0;"><bean:message key="label.back"/></html:submit></td>
</logic:notEqual>
</html:form>
</tr></table>