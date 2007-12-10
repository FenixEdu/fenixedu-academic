<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDTeacherDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDProfessorshipDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.domain.ShiftType" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

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
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewCurricularInformation" property="viewCurricularInformation"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewStudentsEnrolments" property="viewStudentsEnrolments"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewShiftHours" property="viewShiftHours"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewStudentsEnrolmentsPerShift" property="viewStudentsEnrolmentsPerShift"/>

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
			<bean:message key="label.teacherServiceDistribution.viewHoursPerShift"/>:
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
<br/>

<logic:notEmpty name="tsdTeacherDTOEntryList">
	<ul>
		<li>
			<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='exportTSDProcessValuationToExcel'; document.forms[0].submit();">
				<bean:message key="label.teacherService.exportToExcel"/>
			</html:link>
		</li>
	</ul>
</logic:notEmpty>
<br/>

<p class="mtop15 mbottom1">
	<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='changeToViewCourses'; document.forms[0].submit();">
		<bean:message key="label.teacherService.navigateByCourse"/>
	</html:link> | 
	<b> <bean:message key="label.teacherService.navigateByTeacher"/> </b> | 
	<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='changeToViewTeacherAndCourses'; document.forms[0].submit();">
		<bean:message key="label.teacherService.viewByCoursesAndTeachers"/>
	</html:link> |
	<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='changeToViewCharts'; document.forms[0].submit();">
		<bean:message key="label.teacherServiceDistribution.viewByCharts"/>
	</html:link>
</p>

<logic:empty name="tsdTeacherDTOEntryList">
	<br/>
	<em>
		<bean:message key="label.teacherServiceDistribution.notExistsTSDTeachers"/>
	</em>
	<br/>
</logic:empty>
<logic:notEmpty name="tsdTeacherDTOEntryList">
<table class='tstyle4'>
	<tr>
		<th>
			<bean:message key="label.teacherService.teacher.name"/>
		</th>
		<th>
			<bean:message key="label.teacherService.teacher.number"/>
		</th>
		<th>
			<bean:message key="label.teacherService.teacher.category"/>
		</th>
<logic:iterate name="selectedShiftTypes" id="type">
		<th>
			H. <bean:write name="type" property="fullNameTipoAula"/>
		</th>
</logic:iterate>
		<th>
			<bean:message key="label.teacherService.teacher.totalLecturedHours"/>
		</th>
		<th>
			<bean:message key="label.teacherService.teacher.hours"/>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.availableHours"/>
		</th>
	</tr>
<logic:iterate name="tsdTeacherDTOEntryList" id="tsdTeacherDTOEntry">
	<tr class='center'>
		<td class="highlight7">
			<bean:write name="tsdTeacherDTOEntry" property="name"/>
		</td>
		<td>
			<logic:equal name="tsdTeacherDTOEntry" property="isRealTeacher" value="true">
				<bean:write name="tsdTeacherDTOEntry" property="teacherNumber"/>
			</logic:equal>
		</td>
		<td>
			<bean:write name="tsdTeacherDTOEntry" property="category.code"/>
		</td>
	<logic:iterate name="selectedShiftTypes" id="type">
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((TSDTeacherDTOEntry)tsdTeacherDTOEntry).getTotalHoursLecturedByShiftType((ShiftType)type) %>
			</fmt:formatNumber>
		</td>
	</logic:iterate>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<bean:write name="tsdTeacherDTOEntry" property="totalHoursLectured"/>
			</fmt:formatNumber>
		</td>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<bean:write name="tsdTeacherDTOEntry" property="requiredHours"/>
			</fmt:formatNumber>
		</td>
		<logic:greaterThan name="tsdTeacherDTOEntry" property="availability" value="0.0"> 
		<td class="aright" style="background-color: #ffddcc;"><%-- red --%>
		</logic:greaterThan>
		<logic:lessThan  name="tsdTeacherDTOEntry" property="availability" value="0.0"> 
		<td class="aright" style="background-color: #ffffdd;"> <%-- yellow --%>
		</logic:lessThan>
		<logic:equal name="tsdTeacherDTOEntry" property="availability" value="0.0"> 
		<td class="aright" style="background-color: #ccddcc;"><%--green --%>
		</logic:equal>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<bean:write name="tsdTeacherDTOEntry" property="availability"/>
			</fmt:formatNumber>
		</td>
	</tr>
	<tr>
		<td colspan="10" class='backwhite' style='background-color: #fff;'>
	<ul>
	<logic:iterate name="tsdTeacherDTOEntry" property="TSDProfessorshipDTOEntries" id="tsdProfessorshipDTOEntry">
		<li>
			<% Integer tsdTeacherId = ((TSDProfessorshipDTOEntry) tsdProfessorshipDTOEntry).getTSDTeacherDTOEntry().getTSDTeachers().get(0).getIdInternal(); %>
			<bean:define id="tsdCourseId" name="tsdProfessorshipDTOEntry" property="TSDCourseDTOEntry.TSDCourse.idInternal"/>
			<html:link page="<%= "/tsdProfessorship.do?method=prepareLinkForTSDProfessorshipByTeacher&amp;tsdProcess=" + 
			tsdProcessId + "&amp;tsdTeacher=" + tsdTeacherId + "&amp;tsdCourse=" +  tsdCourseId %>">
				<bean:write name="tsdProfessorshipDTOEntry" property="TSDCourseDTOEntry.TSDCourse.name"/>
			</html:link>
			&nbsp;-&nbsp;
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="tsdProfessorshipDTOEntry" property="totalHours"/></fmt:formatNumber>
			<bean:message key="label.teacherService.hours"/>
			<logic:notEqual name="tsdProfessorshipDTOEntry" property="totalHours" value="0.0">
				&nbsp;(
				<% boolean firstSymbol = true; %>
				<logic:iterate name="tsdProfessorshipDTOEntry" property="TSDProfessorships" id="professorship">
					<% if(firstSymbol){ firstSymbol = false; } else { out.print("+");	}%>
					<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
						<bean:write name="professorship" property="hours"/>
					</fmt:formatNumber>
					h. <bean:write name="professorship" property="type.siglaTipoAula"/>
				</logic:iterate>
				)
			</logic:notEqual>
		</li>
	</logic:iterate>
	<logic:equal name="tsdTeacherDTOEntry" property="usingExtraCredits" value="true">
		<li>
			<bean:write name="tsdTeacherDTOEntry" property="extraCreditsName"/>(
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="tsdTeacherDTOEntry" property="extraCreditsValue"/></fmt:formatNumber>)
		</li>
	</logic:equal>
	</ul>
		</td>
	</tr>
</logic:iterate>	
</table>
</logic:notEmpty>
<br/>

<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + tsdProcessId %>'>
	<bean:message key="link.back"/>
</html:link>

</html:form>