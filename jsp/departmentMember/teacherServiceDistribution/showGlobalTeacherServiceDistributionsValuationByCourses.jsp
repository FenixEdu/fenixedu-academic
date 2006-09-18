<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.formbeans.teacherServiceDistribution.GlobalTeacherServiceDistributionValuationForm" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<jsp:useBean id="globalTeacherServiceDistributionValuationForm" scope="request" class="net.sourceforge.fenixedu.presentationTier.formbeans.teacherServiceDistribution.GlobalTeacherServiceDistributionValuationForm" />

<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionVisualization"/>
</h3>

<html:form action="/globalTeacherServiceDistributionValuation">
<html:hidden property="method" value=""/>
<html:hidden property="viewType"/>

<logic:iterate name="globalTeacherServiceDistributionValuationForm" property="selectedTeacherServiceDistributions" id="teacherServiceDistribution">
	<bean:define name="teacherServiceDistribution" id="teacherServiceDistribution" type="String"/>
	<% 
		String teacherServiceDistributionProperty = "teacherServiceDistribution(" + (String) teacherServiceDistribution + ")";
		String valuationPhaseProperty = "valuationPhase(" + (String) teacherServiceDistribution + ")";
		String valuationGroupingProperty = "valuationGrouping(" + (String) teacherServiceDistribution + ")"; 
	%>
	
	<html:hidden property="<%= teacherServiceDistributionProperty %>"/>
	<html:hidden property="<%= valuationPhaseProperty %>"/>
	<html:hidden property="<%= valuationGroupingProperty %>"/>		
</logic:iterate>

<table class='search'>
	<tr class='left'>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.semester"/>:</b>
		</td>
		<td>
 			<html:select property="executionPeriod" onchange="this.form.method.value='viewGlobalTeacherServiceDistributionValuation'; this.form.submit();">
				<html:option value="-1"><bean:message key="label.teacherServiceDistribution.both"/></html:option>
				<html:options collection="executionPeriodList" property="idInternal" labelProperty="semester"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td colspan=2>
			<table>
				<tr>
					<td align=left>
						<html:checkbox property="viewCurricularInformation"  onclick="this.form.method.value='viewGlobalTeacherServiceDistributionValuation'; this.form.submit()"/>
						<bean:message key="label.teacherService.viewCourseInfo"/>
					</td>
				</tr>
				<tr>
					<td align=left>
						<html:checkbox property="viewStudentsEnrolments"  onclick="this.form.method.value='viewGlobalTeacherServiceDistributionValuation'; this.form.submit()"/>		
						<bean:message key="label.teacherService.viewStudentsEnrolments"/>
					</td>
				</tr>
				<tr>
					<td align=left>
						<html:checkbox property="viewShiftHours"  onclick="this.form.method.value='viewGlobalTeacherServiceDistributionValuation'; this.form.submit()"/>				
						<bean:message key="label.teacherService.viewHoursPerShift"/>
					</td>
				</tr>
				<tr>
					<td align=left>
						<html:checkbox property="viewStudentsEnrolmentsPerShift"  onclick="this.form.method.value='viewGlobalTeacherServiceDistributionValuation'; this.form.submit()"/>				
						<bean:message key="label.teacherService.viewStudentsPerShift"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<br/>

<html:link href="javascript:document.globalTeacherServiceDistributionValuationForm.method.value='changeToViewTeachers'; document.globalTeacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.navigateByTeacher"/>
</html:link> | 
<b> <bean:message key="label.teacherService.navigateByCourse"/> </b> | 
<html:link href="javascript:document.globalTeacherServiceDistributionValuationForm.method.value='changeToViewTeacherAndCourses'; document.globalTeacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.viewByCoursesAndTeachers"/>
</html:link> 
<br/>

<table class='vtsbc'>
	<tr>
		<th>
			<bean:message key="label.teacherService.course.name"/>
		</th>
<logic:equal name="globalTeacherServiceDistributionValuationForm" property="viewCurricularInformation" value="true">
		<th>
			<bean:message key="label.teacherService.course.campus"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.degrees"/>
		</th>
</logic:equal>
<logic:equal name="globalTeacherServiceDistributionValuationForm" property="viewStudentsEnrolments" value="true">
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
<logic:equal name="globalTeacherServiceDistributionValuationForm" property="viewShiftHours" value="true">		
		<th>
			<bean:message key="label.teacherService.course.theoreticalHours"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.praticalHours"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.theoPratHours"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.laboratorialHours"/>
		</th>
</logic:equal>		
		<th>
			<bean:message key="label.teacherService.course.totalHours"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.availability"/>
		</th>
<logic:equal name="globalTeacherServiceDistributionValuationForm" property="viewStudentsEnrolmentsPerShift" value="true">		
		<th>
			<bean:message key="label.teacherService.course.studentsNumberByTheoreticalShift"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.studentsNumberByPraticalShift"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.studentsNumberByTheoPraticalShift"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.studentsNumberByLaboratorialShift"/>
		</th>
</logic:equal>
	</tr>
<logic:iterate name="courseValuationDTOEntryList" id="courseValuationDTOEntry">
	<bean:define id="courseValuationId" name="courseValuationDTOEntry" property="courseValuation.idInternal"/>
	<tr class='acenter' id=<%= courseValuationId %>>
		<td class='courses'>
				<bean:write name="courseValuationDTOEntry" property="courseValuation.name"/>
		</td>
<logic:equal name="globalTeacherServiceDistributionValuationForm" property="viewCurricularInformation" value="true">					
		<td>
			<logic:iterate name="courseValuationDTOEntry" property="courseValuation.campus" id="campusEntry">
				<bean:write name="campusEntry"/>&nbsp;
			</logic:iterate>
		</td>
		<td>
			<table width='100%'>
			<logic:iterate name="courseValuationDTOEntry" property="curricularCoursesInformation" id="curricularCourseInformation">
				<tr>
					<td align='left'>
						<bean:write name="curricularCourseInformation" property="key"/>
					</td>
					<td width='5%' class="aright">
						<logic:iterate name="curricularCourseInformation" property="value" id="curricularYear">
							<bean:write name="curricularYear"/>º&nbsp;
						</logic:iterate>
					</td>
				</tr>				
			</logic:iterate>
			</table>
		</td>
</logic:equal>
<logic:equal name="globalTeacherServiceDistributionValuationForm" property="viewStudentsEnrolments" value="true">					
		<td class="aright">
			<bean:write name="courseValuationDTOEntry" property="courseValuation.firstTimeEnrolledStudents"/>
		</td>
		<td class="aright">
			<bean:write name="courseValuationDTOEntry" property="courseValuation.secondTimeEnrolledStudents"/>
		</td>
</logic:equal>		
		<td class="aright">
			<bean:write name="courseValuationDTOEntry" property="courseValuation.totalEnrolledStudents"/>		
		</td>
<logic:equal name="globalTeacherServiceDistributionValuationForm" property="viewShiftHours" value="true">								
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<bean:write name="courseValuationDTOEntry" property="courseValuation.theoreticalHours"/>
			</fmt:formatNumber>
		</td>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="courseValuationDTOEntry" property="courseValuation.praticalHours"/></fmt:formatNumber>
		</td>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="courseValuationDTOEntry" property="courseValuation.theoPratHours"/></fmt:formatNumber>
		</td>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="courseValuationDTOEntry" property="courseValuation.laboratorialHours"/></fmt:formatNumber>
		</td>
</logic:equal>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="courseValuationDTOEntry" property="courseValuation.totalHours"/></fmt:formatNumber>
		</td>
		<logic:greaterThan name="courseValuationDTOEntry" property="courseValuation.totalHoursNotLectured" value="0.0"> 
		<td class="aright" class="yellow">
		</logic:greaterThan>
		<logic:lessThan  name="courseValuationDTOEntry" property="courseValuation.totalHoursNotLectured" value="0.0"> 
		<td class="aright" class="red">
		</logic:lessThan>
		<logic:equal name="courseValuationDTOEntry" property="courseValuation.totalHoursNotLectured" value="0.0"> 
		<td class="aright" class="green">
		</logic:equal>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="courseValuationDTOEntry" property="courseValuation.totalHoursNotLectured"/></fmt:formatNumber>
		</td>
<logic:equal name="globalTeacherServiceDistributionValuationForm" property="viewStudentsEnrolmentsPerShift" value="true">		
		<td class="aright">
			<bean:write name="courseValuationDTOEntry" property="courseValuation.studentsPerTheoreticalShift"/>
		</td>
		<td class="aright">
			<bean:write name="courseValuationDTOEntry" property="courseValuation.studentsPerPraticalShift"/>
		</td>
		<td class="aright">
			<bean:write name="courseValuationDTOEntry" property="courseValuation.studentsPerTheoPratShift"/>
		</td>
		<td class="aright">
			<bean:write name="courseValuationDTOEntry" property="courseValuation.studentsPerLaboratorialShift"/>
		</td>
</logic:equal>
	</tr>
	<tr>
		<td colspan="20" class='backwhite' style="background-color: #fdfdfd;">
			<logic:iterate name="courseValuationDTOEntry" property="professorshipValuationDTOEntries" id="professorshipValuationDTOEntry">
				<ul>
					<li>
						<bean:define id="valuationTeacherId" name="professorshipValuationDTOEntry" property="professorshipValuation.valuationTeacher.idInternal"/>
							<bean:write name="professorshipValuationDTOEntry" property="professorshipValuation.valuationTeacher.name"/>
						&nbsp;-&nbsp;
						<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="professorshipValuationDTOEntry" property="professorshipValuation.totalHours"/></fmt:formatNumber>
						<bean:message key="label.teacherService.hours"/>
						<logic:notEqual name="professorshipValuationDTOEntry" property="professorshipValuation.totalHours" value="0.0">
							&nbsp;(
							<% boolean firstSymbol = true; %>
							<logic:notEqual name="professorshipValuationDTOEntry" property="professorshipValuation.theoreticalHours" value="0.0">
								<% if(firstSymbol){ firstSymbol = false; } %>
								<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="professorshipValuationDTOEntry" property="professorshipValuation.theoreticalHours"/></fmt:formatNumber>
								<bean:message key="label.teacherService.theoretical"/>
							</logic:notEqual>
							<logic:notEqual name="professorshipValuationDTOEntry" property="professorshipValuation.praticalHours" value="0.0">
								<% if(firstSymbol){ 
									firstSymbol = false; 
								} else { 
									out.print("+");
								}%>
								<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="professorshipValuationDTOEntry" property="professorshipValuation.praticalHours"/></fmt:formatNumber>
								<bean:message key="label.teacherService.pratical"/>
							</logic:notEqual>
							<logic:notEqual name="professorshipValuationDTOEntry" property="professorshipValuation.theoPratHours" value="0.0">
								<% if(firstSymbol){ 
									firstSymbol = false; 
								} else { 
									out.print("+");
								}%>
								<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="professorshipValuationDTOEntry" property="professorshipValuation.theoPratHours"/></fmt:formatNumber>
								<bean:message key="label.teacherService.theopratical"/>
							</logic:notEqual>
							<logic:notEqual name="professorshipValuationDTOEntry" property="professorshipValuation.laboratorialHours" value="0.0">
								<% if(!firstSymbol) out.print("+");  %>
								<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="professorshipValuationDTOEntry" property="professorshipValuation.laboratorialHours"/></fmt:formatNumber>
								<bean:message key="label.teacherService.laboratorial"/>
							</logic:notEqual>
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

<html:link page="/globalTeacherServiceDistributionValuation.do?method=prepareForGlobalTeacherServiceDistributionValuation">
	<bean:message key="link.back"/>
</html:link>	
</html:form>