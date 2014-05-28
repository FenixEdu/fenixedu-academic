<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
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

<bean:size id="shiftsSize" name="shiftList"/>
<logic:notEqual name="shiftsSize" value="0">

<html:form action="/testDistributionByShifts">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="distributeTest"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value="<%=(pageContext.findAttribute("testCode")).toString()%>"/>

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
	<logic:iterate id="shift" name="shiftList" indexId="index">
		<logic:equal name="index" value="0">
			<bean:define id="tipoBefore" name="shift" property="shiftTypesPrettyPrint"/>
		</logic:equal>
		<logic:notEqual name="index" value="0">
			<logic:notEqual name="shift" property="shiftTypesPrettyPrint" value="<%tipoBefore%>">
				<tr height=20><td></td></tr>
				<tr><td></td>
					<th class="listClasses-header"><bean:message key="link.executionCourse.shifts"/></th>
					<th class="listClasses-header"><bean:message key="label.curricularCourseType"/></th>
					<td class="listClasses-header"></td>
				</tr>		
			</logic:notEqual>
		</logic:notEqual>
		<tr><td></td>
			<td class="listClasses"><bean:write name="shift" property="nome"/></td>
			<td class="listClasses"><bean:write name="shift" property="shiftTypesPrettyPrint"/></td>
			<td class="listClasses">
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selected" property="selected" onclick="cleanSelect()">
			    <bean:write name="shift" property="externalId"/>
				</html:multibox> 
			</td>
		</tr>
		<bean:define id="tipoBefore" name="shift" property="shiftTypesPrettyPrint"/>
	</logic:iterate>
</table>
<table><tr>
<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.distribute" styleClass="inputbutton" property="distribute"><bean:message key="link.student.room.distribution"/></html:submit></td></html:form>
<html:form action="/testsManagement">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showTests"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>"/>
<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.cancel"/></html:submit></td>
</html:form>
</tr></table>
</logic:notEqual>

<logic:equal name="shiftsSize" value="0">
	<html:form action="/testsManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="testsFirstPage"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%=(pageContext.findAttribute("executionCourseID")).toString()%>"/>
		<table>
		<tr><td><span class="error"><!-- Error messages go here --><bean:message key="error.shifts.class.not.available"/></span></tr></td>
		<tr><td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.cancel"/></html:submit></tr></td>
		</table>
	</html:form>
</logic:equal>