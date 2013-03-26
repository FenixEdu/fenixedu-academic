<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.formbeans.teacherServiceDistribution.GlobalTSDProcessValuationForm" %>
<%@ page import="net.sourceforge.fenixedu.domain.ShiftType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.tsdProcessVisualization"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<html:link page="/globalTSDProcessValuation.do?method=prepareForGlobalTSDProcessValuation">
			<bean:message key="link.teacherServiceDistribution.tsdProcessVisualization"/>
		</html:link>
	</em>
</p>

<html:form action="/globalTSDProcessValuation">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.TSDProcess" property="tsdProcess"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>

<table class='tstyle5'>
	<tr>
		<td align="right">
			<bean:message key="label.teacherServiceDistribution.tsdProcessPhase"/>:
		</td>
		<td>
			<html:select property="tsdProcessPhase" onchange="this.form.method.value='viewGlobalTSDProcessValuation'; this.form.submit();">
				<html:options collection="tsdProcessPhaseList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td align="right">
			<bean:message key="label.teacherServiceDistribution.TeacherServiceDistribution"/>:
		</td>
		<td>
			<html:select property="tsd" onchange="this.form.method.value='viewGlobalTSDProcessValuation'; this.form.submit();">
				<html:options collection="tsdOptionEntryList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td align="right">
			<bean:message key="label.teacherServiceDistribution.semester"/>:
		</td>
		<td>
 			<html:select property="executionPeriod" onchange="this.form.method.value='viewGlobalTSDProcessValuation'; this.form.submit();">
				<html:option value="-1"><bean:message key="label.teacherServiceDistribution.both"/></html:option>
				<html:options collection="executionPeriodList" property="idInternal" labelProperty="semester"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td align="right">
			<bean:message key="label.teacherService.viewCourseInfo"/>:
		</td>
		<td>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.viewCurricularInformation" property="viewCurricularInformation"  onclick="this.form.method.value='viewGlobalTSDProcessValuation'; this.form.submit()"/>
		</td>
	</tr>
	<tr>
		<td align="right">
			<bean:message key="label.teacherService.viewStudentsEnrolments"/>:
		</td>
		<td>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.viewStudentsEnrolments" property="viewStudentsEnrolments"  onclick="this.form.method.value='viewGlobalTSDProcessValuation'; this.form.submit()"/>		
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
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.shiftTypeArray" property="shiftTypeArray" onclick="this.form.method.value='viewGlobalTSDProcessValuation'; this.form.submit()">
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

<p class="mtop15 mbottom1">
	<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='changeToViewTeachers'; document.forms[0].submit();">
		<bean:message key="label.teacherService.navigateByTeacher"/>
	</html:link> | 
	<b> <bean:message key="label.teacherService.navigateByCourse"/> </b> | 
	<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='changeToViewTeacherAndCourses'; document.forms[0].submit();">
		<bean:message key="label.teacherService.viewByCoursesAndTeachers"/>
	</html:link> 
</p>

<table class='tstyle4'>
	<tr>
		<th>
			<bean:message key="label.teacherService.course.name"/>
		</th>
<logic:equal name="viewCurricularInformation" value="true">
		<th>
			<bean:message key="label.teacherService.course.campus"/>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.courses"/>
		</th>
</logic:equal>
<logic:equal name="viewStudentsEnrolments" value="true">
		<th>
			<bean:message key="label.teacherService.course.firstTimeEnrolledStudentsNumber"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.secondTimeEnrolledStudentsNumber"/>
		</th>
</logic:equal>		
		<th>
			<bean:message key="label.teacherService.course.totalStudentsNumber"/>
		</th>
<logic:iterate name="selectedShiftTypes" id="type">
		<th>
			H. <bean:write name="type" property="fullNameTipoAula"/>
		</th>
</logic:iterate>
		<th>
			<bean:message key="label.teacherService.course.totalHours"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.availability"/>
		</th>
<%--<logic:iterate name="selectedShiftTypes" id="type">
		<th>
			<bean:message key="label.teacherServiceDistribution.studentsNumberPerShift"/> <bean:write name="type" property="fullNameTipoAula"/>
		</th>
</logic:iterate>--%>
	</tr>
<logic:iterate name="tsdCourseDTOEntryList" id="tsdCourseDTOEntry">
	<bean:define id="tsdCourse" name="tsdCourseDTOEntry" property="TSDCourse"/>
	<bean:define id="tsdCourseId" name="tsdCourseDTOEntry" property="TSDCourse.idInternal"/>
	<tr class='acenter' id=<%= tsdCourseId %>>
		<td class='highlight7'>
			<bean:write name="tsdCourseDTOEntry" property="TSDCourse.name"/>
		</td>	
<logic:equal name="viewCurricularInformation" value="true">
		<td>
			<logic:iterate name="tsdCourseDTOEntry" property="TSDCourse.campus" id="campusEntry">
				<bean:write name="campusEntry"/>&nbsp;
			</logic:iterate>
		</td>
		<td>
			<table width='100%'>
			<logic:iterate name="tsdCourseDTOEntry" property="curricularCoursesInformation" id="curricularCourseInformation">
				<tr>
					<td align='left'>
						<bean:write name="curricularCourseInformation" property="key"/>
					</td>
					<td width='5%' class="aright">
						<logic:iterate name="curricularCourseInformation" property="value" id="curricularYear">
							<bean:write name="curricularYear"/>&#186;&nbsp;
						</logic:iterate>
					</td>
				</tr>				
			</logic:iterate>
			</table>
		</td>
</logic:equal>
<logic:equal name="viewStudentsEnrolments" value="true">
		<td class="aright">
			<bean:write name="tsdCourseDTOEntry" property="TSDCourse.firstTimeEnrolledStudents"/>
		</td>
		<td class="aright">
			<bean:write name="tsdCourseDTOEntry" property="TSDCourse.secondTimeEnrolledStudents"/>
		</td>
</logic:equal>		
		<td class="aright">
			<bean:write name="tsdCourseDTOEntry" property="TSDCourse.totalEnrolledStudents"/>		
		</td>
<logic:iterate name="selectedShiftTypes" id="type">
		<bean:define name="tsdCourseDTOEntry" property="TSDCourse" id="tsdCourse"/>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((TSDCourse)tsdCourse).getHours((ShiftType)type) %>
			</fmt:formatNumber>
		</td>
</logic:iterate>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="tsdCourseDTOEntry" property="TSDCourse.totalHours"/></fmt:formatNumber>
		</td>
		<logic:greaterThan name="tsdCourseDTOEntry" property="TSDCourse.totalHoursNotLectured" value="0.0"> 
		<td class="aright" class="yellow">
		</logic:greaterThan>
		<logic:lessThan  name="tsdCourseDTOEntry" property="TSDCourse.totalHoursNotLectured" value="0.0"> 
		<td class="aright" class="red">
		</logic:lessThan>
		<logic:equal name="tsdCourseDTOEntry" property="TSDCourse.totalHoursNotLectured" value="0.0"> 
		<td class="aright" class="green">
		</logic:equal>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="tsdCourseDTOEntry" property="TSDCourse.totalHoursNotLectured"/></fmt:formatNumber>
		</td>
<%--<logic:iterate name="selectedShiftTypes" id="type">
		<bean:define name="tsdCourseDTOEntry" property="TSDCourse" id="tsdCourse"/>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((TSDCourse)tsdCourse).getStudentsPerShift((ShiftType)type) %>
			</fmt:formatNumber>
		</td>
</logic:iterate>--%>
	</tr>
	<tr>
		<td colspan="40" class='backwhite' style="background-color: #fdfdfd;">
			<logic:iterate name="tsdCourseDTOEntry" property="TSDProfessorshipDTOEntries" id="tsdProfessorshipDTOEntry">
				<ul>
					<li>
						<bean:write name="tsdProfessorshipDTOEntry" property="TSDTeacherDTOEntry.name"/>
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
								h. <bean:write name="professorship" property="type.fullNameTipoAula"/>
							</logic:iterate>
							)
						</logic:notEqual>
					</li>					
				</ul>
			</logic:iterate>
		</td>	
	</tr>
</logic:iterate>
</table>
<br/>

<html:link page="/globalTSDProcessValuation.do?method=prepareForGlobalTSDProcessValuation">
	<bean:message key="link.back"/>
</html:link>	
</html:form>