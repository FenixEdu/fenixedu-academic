<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionVisualization"/>
</h3>

<html:form action="/globalTeacherServiceDistributionValuation">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewCurricularInformation" property="viewCurricularInformation"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewStudentsEnrolments" property="viewStudentsEnrolments"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewShiftHours" property="viewShiftHours"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewStudentsEnrolmentsPerShift" property="viewStudentsEnrolmentsPerShift"/>



<logic:iterate name="globalTeacherServiceDistributionValuationForm" property="selectedTeacherServiceDistributions" id="teacherServiceDistribution">
	<bean:define name="teacherServiceDistribution" id="teacherServiceDistribution" type="String"/>
	<% 
		String teacherServiceDistributionProperty = "teacherServiceDistribution(" + (String) teacherServiceDistribution + ")";
		String valuationPhaseProperty = "valuationPhase(" + (String) teacherServiceDistribution + ")";
		String valuationGroupingProperty = "valuationGrouping(" + (String) teacherServiceDistribution + ")"; 
	%>
	
	<html:hidden alt="<%= teacherServiceDistributionProperty %>" property="<%= teacherServiceDistributionProperty %>"/>
	<html:hidden alt="<%= valuationPhaseProperty %>" property="<%= valuationPhaseProperty %>"/>
	<html:hidden alt="<%= valuationGroupingProperty %>" property="<%= valuationGroupingProperty %>"/>		
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
</table>
<br/>


<b> <bean:message key="label.teacherService.navigateByTeacher"/> </b> | 
<html:link href="javascript:document.globalTeacherServiceDistributionValuationForm.method.value='changeToViewCourses'; document.globalTeacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.navigateByCourse"/>
</html:link> | 
<html:link href="javascript:document.globalTeacherServiceDistributionValuationForm.method.value='changeToViewTeacherAndCourses'; document.globalTeacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.viewByCoursesAndTeachers"/>
</html:link> 
<br/>

<table class='vtsbc'>
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
		<th>
			<bean:message key="label.teacherServiceDistribution.theoreticalHoursLectured"/>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.praticalHoursLectured"/>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.theoPratHoursLectured"/>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.laboratorialHoursLectured"/>
		</th>		
		<th>
			<bean:message key="label.teacherService.teacher.totalLecturedHours"/>
		</th>
		<th>
			<bean:message key="label.teacherService.teacher.hours"/>
		</th>
		<th>
			<bean:message key="label.teacherService.course.availability"/>
		</th>
	</tr>
<logic:iterate name="valuationTeacherDTOEntryList" id="valuationTeacherDTOEntry">
	<tr class='center'>
		<td class="courses">
			<bean:write name="valuationTeacherDTOEntry" property="name"/>
		</td>
		<td>
			<logic:equal name="valuationTeacherDTOEntry" property="isRealTeacher" value="true">
				<bean:write name="valuationTeacherDTOEntry" property="teacherNumber"/>
			</logic:equal>
		</td>
		<td>
			<bean:write name="valuationTeacherDTOEntry" property="category.code"/>
		</td>
		<td align='right'>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="valuationTeacherDTOEntry" property="totalTheoreticalHoursLectured"/></fmt:formatNumber>
		</td>	
		<td align='right'>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="valuationTeacherDTOEntry" property="totalPraticalHoursLectured"/></fmt:formatNumber>		
		</td>		
		<td align='right'>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="valuationTeacherDTOEntry" property="totalTheoPratHoursLectured"/></fmt:formatNumber>
		</td>
		<td align='right'>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="valuationTeacherDTOEntry" property="totalLaboratorialHoursLectured"/></fmt:formatNumber>
		</td>
		<td align='right'>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="valuationTeacherDTOEntry" property="totalHoursLectured"/></fmt:formatNumber>
		</td>
		<td align='right'>	
			<bean:write name="valuationTeacherDTOEntry" property="requiredHours"/>
		</td>
		<logic:greaterThan name="valuationTeacherDTOEntry" property="availability" value="0.0"> 
		<td align='right' class="yellow">
		</logic:greaterThan>
		<logic:lessThan  name="valuationTeacherDTOEntry" property="availability" value="0.0"> 
		<td align='right' class="red">
		</logic:lessThan>
		<logic:equal name="valuationTeacherDTOEntry" property="availability" value="0.0"> 
		<td align='right' class="green">
		</logic:equal>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="valuationTeacherDTOEntry" property="availability"/></fmt:formatNumber>
		</td>
	</tr>
	<tr>
		<td colspan="10" class='backwhite' style='background-color: #fff;'>
	<ul>
	<logic:iterate name="valuationTeacherDTOEntry" property="professorshipValuationDTOEntries" id="professorshipValuationDTOEntry">
		<li>
			<bean:define id="valuationTeacherId" name="professorshipValuationDTOEntry" property="professorshipValuation.valuationTeacher.idInternal"/>
			<bean:define id="courseValuationId" name="professorshipValuationDTOEntry" property="courseValuationDTOEntry.courseValuation.idInternal"/>
				<bean:write name="professorshipValuationDTOEntry" property="courseValuationDTOEntry.courseValuation.name"/>
			(<logic:iterate name="professorshipValuationDTOEntry" property="courseValuationDTOEntry.curricularCoursesInformation" id="curricularCourseInformation">
						<bean:write name="curricularCourseInformation" property="key"/>
						(<logic:iterate name="curricularCourseInformation" property="value" id="curricularYear">
							<bean:write name="curricularYear"/>&nbsp;
						</logic:iterate>�ano)
			</logic:iterate>)
			- <fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="professorshipValuationDTOEntry" property="professorshipValuation.totalHours"/></fmt:formatNumber>
			<bean:message key="label.teacherService.hours"/>
		</li>
	</logic:iterate>
	<logic:equal name="valuationTeacherDTOEntry" property="usingExtraCredits" value="true">
		<li>
			<bean:write name="valuationTeacherDTOEntry" property="extraCreditsName"/>(
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="valuationTeacherDTOEntry" property="extraCreditsValue"/></fmt:formatNumber>)
		</li>
	</logic:equal>
	</ul>
		</td>
	</tr>
</logic:iterate>	
</table>
<br/>

<html:link page="/globalTeacherServiceDistributionValuation.do?method=prepareForGlobalTeacherServiceDistributionValuation">
	<bean:message key="link.back"/>
</html:link>	
</html:form>