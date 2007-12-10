<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourseType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDValueType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ page import="net.sourceforge.fenixedu.domain.ShiftType" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<bean:define id="tsdProcessId" name="tsdProcess" property="idInternal"/>
<bean:define id="tsdCourseId" name="selectedTSDCourse" property="idInternal"/>
<bean:define id="tsdId" name="selectedTSD" property="idInternal"/>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.valuateHours"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + tsdProcessId %>'>
			<bean:write name="tsdProcess" property="name"/>&nbsp;
			<bean:write name="tsdProcess" property="executionYear.year"/>
		</html:link>
		>
		<html:link page='<%= "/tsdCourse.do?method=prepareLinkForTSDCourse&amp;tsd=" + tsdId + "&amp;tsdProcess=" + tsdProcessId + "&amp;tsdCourse=" + tsdCourseId + "&amp;notTSDCourseViewLink=true" %>'>
			<bean:message key="link.teacherServiceDistribution.tsdCourseService"/>
		</html:link>
		> 
		<bean:message key="label.teacherServiceDistribution.valuateHours"/>
	</em>
</p>

<html:form action="/tsdCourseValuation">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="setTSDCourseHours"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdCourse" property="tsdCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="4"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.forward" property="forward" value="courseValuationHours"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.firstTimeEnrolledStudentsManual" property="firstTimeEnrolledStudentsManual"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.secondTimeEnrolledStudentsManual" property="secondTimeEnrolledStudentsManual"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.weightFirstTimeEnrolledStudentsPerShiftManual" property="weightFirstTimeEnrolledStudentsPerShiftManual"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.weightSecondTimeEnrolledStudentsPerShiftManual" property="weightSecondTimeEnrolledStudentsPerShiftManual"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.hidden" property="studentsPerShiftType" value="<%= TSDValueType.MANUAL_VALUE.toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.hidden" property="hoursType" value="<%= TSDValueType.MANUAL_VALUE.toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.hidden" property="tsd"/>


<table class="tstyle1 thright thlight thmiddle">
	<tr>
		<th>
			<bean:message key="label.teacherService.course.name"/>:
		</th>
		<td>
			<bean:write name="selectedTSDCourse" property="name" />
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.teacherService.course.semester"/>:
		</th>
		<td>
			<bean:write name="selectedTSDCourse" property="executionPeriod.name" />
		</td>
	</tr>
</table>

<br/>

<b><bean:message key="label.teacherServiceDistribution.currentValues"/>:</b>
<br/>

<logic:empty name="selectedTSDLoad">
	<p class="mtop15">
		<em>
			<bean:message key="label.teacherServiceDistribution.shiftTypeNotAvailableForTSDCourse"/>
		</em>
	</p>
</logic:empty>

<logic:notEmpty name="selectedTSDLoad">

<%  TSDCourse selectedTSDCourse = (TSDCourse)request.getAttribute("selectedTSDCourse"); 
	ShiftType shiftType = (ShiftType)request.getAttribute("shiftType");
%>

<table class='tstyle4 mtop05 thlight'>
	<tr>
		<th>
			<b><bean:message key="label.teacherServiceDistribution.valuateStudents"/></b>
		</th>
		<th>
		</th>
	</tr>
	<tr>
		<td class='highlight7'>
			<bean:message key="label.teacherServiceDistribution.firstTimeEnrolledStudents"/>
			<bean:message key="label.teacherServiceDistribution.firstTimeEnrolledStudents.symbol"/>
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDCourse" property="firstTimeEnrolledStudents" />
		</td>
	</tr>
	<tr>
		<td class='highlight7'>
			<bean:message key="label.teacherServiceDistribution.secondTimeEnrolledStudents"/>
			<bean:message key="label.teacherServiceDistribution.secondTimeEnrolledStudents.symbol"/>
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDCourse" property="secondTimeEnrolledStudents" />
		</td>
	</tr>
	<tr>
		<th>
			<b><bean:message name="selectedTSDLoad" property="type.name"/></b>
		</th>
		<th>
		</th>
	</tr>
	<tr>
		<td class='highlight7'>
			<bean:message key="label.teacherServiceDistribution.weightFirstTimeEnrolledStudentsPerShift"/>
			<bean:message key="label.teacherServiceDistribution.weightFirstTimeEnrolledStudentsPerShift.symbol"/>
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDLoad" property="weightFirstTimeEnrolledStudentsPerShift" />
		</td>
	</tr>
	<tr>
		<td class='highlight7'>
			<bean:message key="label.teacherServiceDistribution.weightSecondTimeEnrolledStudentsPerShift"/> 
			<bean:message key="label.teacherServiceDistribution.weightSecondTimeEnrolledStudentsPerShift.symbol"/> 
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDLoad" property="weightSecondTimeEnrolledStudentsPerShift" />
		</td>
	</tr>
	<tr>
		<td class='highlight7'>
			<bean:message key="label.teacherServiceDistribution.studentsTotal"/>
			<bean:message key="label.teacherServiceDistribution.studentsTotal.formula"/>
		</td>
		<td class='aright'>
			<%= selectedTSDCourse.getTotalNumberOfStudents(shiftType) %>
		</td>
	</tr>
	<tr>
		<td class='highlight7'>
			<bean:message key="label.teacherServiceDistribution.studentsPerShift"/>
			<bean:message key="label.teacherServiceDistribution.studentsPerShift.symbol"/>
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDLoad" property="studentsPerShift" />
		</td>
	</tr>
	<tr>
		<td class='highlight7'>
			<bean:message key="label.teacherServiceDistribution.shiftFrequency"/>
			<bean:message key="label.teacherServiceDistribution.shiftFrequency.symbol"/>
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDLoad" property="frequency" />
		</td>
	</tr>
	<tr>
		<td class='highlight7'>
			<bean:message key="label.teacherServiceDistribution.shiftDuration"/>
			<bean:message key="label.teacherServiceDistribution.shiftDuration.symbol"/>
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDLoad" property="hoursPerShift" /> 
		</td>
	</tr>
	<%-- <tr>
		<td class='highlight7'>
			<bean:message key="label.teacherServiceDistribution.timeTableSlots"/>
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDLoad" property="timeTableSlots" />
		</td>
	</tr>--%>
	<tr>
		<td class='highlight7'>
			<b><bean:message key="label.teacherServiceDistribution.weeklyHours"/></b>
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDLoad" property="hours" />
		</td>
	</tr>
</table>

<br/>
<br/>

<b><bean:message key="label.teacherServiceDistribution.newValues"/>:</b>
<br/>
<table class="tstyle5 thlight thmiddle mtop05 mbottom05">
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.shiftType"/>:
		</th>
		<td>
			<html:select property="shiftType" onchange="this.form.method.value='loadTSDCurricularLoadForHours'; this.form.submit();">
				<html:options collection="shiftsList" property="name" labelProperty="fullNameTipoAula"/>
			</html:select>			
		</td>		
	</tr>
</table>

<table class='tstyle4 mtop0 mbottom05'>
	<tr>
		<th>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.value"/>
		</th>
		<th>
			<bean:write name="selectedTSDCourse" property="lastYearExecutionPeriod.executionYear.year"/>
		</th>
		<th>
			<bean:write name="selectedTSDCourse" property="executionPeriod.executionYear.year"/>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.calculatedValue"/>
		</th>
	</tr>
	<tr>
		<td>
			<bean:message key="label.teacherServiceDistribution.studentsPerShift"/>
			<bean:message key="label.teacherServiceDistribution.studentsPerShift.symbol"/>
		</td>
		<td class='aright'>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.studentsPerShiftManual" property="studentsPerShiftManual" size="3" maxlength="4" styleClass="aleft" />
		</td>
		<td class='aright'>
			<%= selectedTSDCourse.getRealStudentsPerShiftLastYear(shiftType) %>
		</td>
		<td class='aright'>
			<%= selectedTSDCourse.getRealStudentsPerShift(shiftType) %>
		</td>
		<td>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.teacherServiceDistribution.shiftFrequency"/>
			<bean:message key="label.teacherServiceDistribution.shiftFrequency.symbol"/>
		</td>
		<td class='aright'>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.shiftFrequency" property="shiftFrequency" size="3" maxlength="4" styleClass="aleft"/>
		</td>
		<td colspan="3">
		</td>		
	</tr>
	<tr>
		<td>
			<bean:message key="label.teacherServiceDistribution.shiftDuration"/>
			<bean:message key="label.teacherServiceDistribution.shiftDuration.symbol"/>
		</td>
		<td class='aright'>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.hoursPerShift" property="hoursPerShift" size="3" maxlength="4" styleClass="aleft"/>
		</td>
		<td colspan="3">
		</td>		
	</tr>
	
	<tr>
		<td>
			<bean:message key="label.teacherServiceDistribution.weeklyHours"/> 
		</td>
		<td class='aright'>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.hoursManual" property="hoursManual" size="3" maxlength="4" styleClass="aleft"/>
		</td>
		<td class='aright'>
			<%= selectedTSDCourse.getRealHoursLastYear(shiftType) %>
		</td>
		<td class='aright'>
			<%= selectedTSDCourse.getRealHours(shiftType) %>
		</td>
		<td class='aright'>
			<bean:message key="label.teacherServiceDistribution.weeklyHours.formula"/> 
			<b><%= selectedTSDCourse.getHoursCalculated(shiftType) %></b>
		</td>
	</tr>
	<%-- <tr>
		<td>
			<bean:message key="label.teacherServiceDistribution.timeTableSlots"/>
		</td>
		<td class='aright'>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.timeTableSlots" property="timeTableSlots" size="3" maxlength="4" styleClass="aleft"/>
		</td>
		<% if(shiftType == shiftType.TEORICA || shiftType == shiftType.TEORICO_PRATICA ||
				shiftType == shiftType.PRATICA || shiftType == shiftType.LABORATORIAL){ %>
		<td colspan="3">
		<% } else { %>
		<td colspan="2">
		<% } %>
		</td>		
	</tr>--%>
</table>

<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.submit();">
	<bean:message key="label.teacherServiceDistribution.assign"/>
</html:button>
<br/>
<br/>
<span class="error">
	<html:errors/>
</span>
<br/>

</logic:notEmpty>

</html:form>

<html:link page='<%= "/tsdCourse.do?method=prepareLinkForTSDCourse&amp;tsd=" + tsdId + "&amp;tsdProcess=" + tsdProcessId + "&amp;tsdCourse=" + tsdCourseId + "&amp;notTSDCourseViewLink=true" %>'>
	<bean:message key="link.back"/>
</html:link>


