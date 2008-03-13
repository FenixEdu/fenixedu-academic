<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.ShiftType"%>
<html:xhtml/>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.tsdProcessValuation"/></h2>
<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<bean:define id="tsdProcessId" name="tsdProcess" property="idInternal"/>
		<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + tsdProcessId %>'>
			<bean:write name="tsdProcess" property="name"/>&nbsp;
			<bean:write name="tsdProcess" property="executionYear.year"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.tsdProcessValuation"/>
	</em>
</p>

<html:form action="/tsdProcessValuation">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.TSDProcess" property="tsdProcess"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>


<table class='tstyle5'>
	<tr>
		<td align="right">
			<bean:message key="label.teacherServiceDistribution.tsdProcessPhase"/>:
		</td>
		<td>
			<html:select property="tsdProcessPhase" onchange="this.form.method.value='loadTSDProcessPhase'; this.form.submit();">
				<html:options collection="tsdProcessPhaseList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td align="right">
			<bean:message key="label.teacherServiceDistribution.TeacherServiceDistribution"/>:
		</td>
		<td>
			<html:select property="tsd" onchange="this.form.method.value='loadTSDProcess'; this.form.submit();">
				<html:options collection="tsdOptionEntryList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td align="right">
			<bean:message key="label.teacherServiceDistribution.semester"/>:
		</td>
		<td>
			<html:select property="executionPeriod" onchange="this.form.method.value='loadTSDProcess'; this.form.submit();">
				<html:option value="-1"><bean:message key="label.teacherServiceDistribution.both"/></html:option>
				<html:options collection="executionPeriodList" property="idInternal" labelProperty="semester"/>
			</html:select>
		</td>
	</tr>
	<tr>
			<td align="right">
			<bean:message key="label.teacherServiceDistribution.selectShifts"/>:
		</td>
	
			<td>
		<% int brPos = 6, counter = 0; %>
			<table cellpadding="0" cellspacing="0" style="margin: 0em; border-collapse: collapse" border="0">
			<tr>
			<logic:iterate name="shiftsList" id="shiftType">
				<td>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.shiftTypeArray" property="shiftTypeArray" onclick="this.form.method.value='loadTSDProcess'; this.form.submit()">
					<bean:write name="shiftType" property="name"/>
				</html:multibox>
				<bean:write name="shiftType" property="fullNameTipoAula"/>
				</td>
				<%= ++counter == brPos ? "</tr><tr>" : ""  %>
			</logic:iterate>
			</tr></table>
		</td>
	</tr>
</table>

<p class="mtop15 mbottom1">
	<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='changeToViewCourses'; document.forms[0].submit();">
		<bean:message key="label.teacherService.navigateByCourse"/> 
	</html:link>
	| 
	<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='changeToViewTeachers'; document.forms[0].submit();">
		<bean:message key="label.teacherService.navigateByTeacher"/>
	</html:link> | 
	<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='changeToViewTeacherAndCourses'; document.forms[0].submit();">
		<bean:message key="label.teacherService.viewByCoursesAndTeachers"/>
	</html:link> |
	<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='changeToViewCharts'; document.forms[0].submit();">
		<bean:message key="label.teacherServiceDistribution.viewByCharts"/>
	</html:link>
						|
	<b><bean:message key="label.teacherServiceDistribution.viewPlanning"/></b>
	
</p>

</html:form>



<table class="tstyle1">
<tr>
<th rowspan="2"><bean:message key="label.teacherService.course.name"/></th>
<th rowspan="2"><bean:message key="label.teacherServiceDistribution.acronym"/></th>
<logic:iterate id="shift" name="selectedShiftTypes">
	<th colspan="4"><fr:view name="shift" property="fullNameTipoAula"/></th>
</logic:iterate>
</tr>
<tr>
	<logic:iterate id="shift" name="selectedShiftTypes">
	<th><bean:message key="label.teacherServiceDistribution.numberOfSchoolClasses.acronym"/></th>
	<th><bean:message key="label.teacherServiceDistribution.shiftDuration.acronym"/></th>
	<th><bean:message key="label.teacherServiceDistribution.shiftFrequency.acronym"/></th>
	<th><bean:message key="label.teacherServiceDistribution.numberOfShifts.acronym"/></th>
	</logic:iterate>
</tr>
	<logic:iterate id="dto" name="tsdCourseDTOEntryList">
	<tr>
			<bean:define id="tsdCourse" name="dto" property="TSDCourse" type="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse"/>
			<td><fr:view name="tsdCourse" property="name"/></td>
			<td><fr:view name="tsdCourse" property="acronym"/></td>
			
			<logic:iterate id="shift" name="selectedShiftTypes" type="net.sourceforge.fenixedu.domain.ShiftType">
			<td><%= tsdCourse.getNumberOfSchoolClasses(shift)%></td>
			
			<td><%= tsdCourse.getHoursPerShift(shift) %></td>
			<td><%= tsdCourse.getShiftFrequency(shift)%></td>
			<td><%= tsdCourse.getNumberOfShifts(shift) %></td>
			</logic:iterate>
	</tr>
	</logic:iterate>
</table>


	<p><strong><bean:message key="label.teacherServiceDistribution.numberOfSchoolClasses.acronym"/></strong>: <em><bean:message key="label.teacherServiceDistribution.numberOfSchoolClasses"/></em></p>
	<p><strong><bean:message key="label.teacherServiceDistribution.shiftDuration.acronym"/></strong>: <em><bean:message key="label.teacherServiceDistribution.shiftDuration"/></em></p>
	<p><strong><bean:message key="label.teacherServiceDistribution.shiftFrequency.acronym"/></strong>: <em><bean:message key="label.teacherServiceDistribution.shiftFrequency"/></em></p>
	<p><strong><bean:message key="label.teacherServiceDistribution.numberOfShifts.acronym"/></strong>: <em><bean:message key="label.teacherServiceDistribution.numberOfShifts"/></em></p>