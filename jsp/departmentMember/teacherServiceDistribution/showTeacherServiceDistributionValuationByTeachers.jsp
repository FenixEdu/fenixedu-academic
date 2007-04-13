<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuation" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>


<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<bean:define id="teacherServiceDistributionId" name="teacherServiceDistribution" property="idInternal"/>
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + teacherServiceDistributionId %>'>
		<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
		<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionValuation"/>
</h3>

<html:form action="/teacherServiceDistributionValuation">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherServiceDistribution" property="teacherServiceDistribution"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewCurricularInformation" property="viewCurricularInformation"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewStudentsEnrolments" property="viewStudentsEnrolments"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewShiftHours" property="viewShiftHours"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewStudentsEnrolmentsPerShift" property="viewStudentsEnrolmentsPerShift"/>

<table class='search'>
	<tr class='aleft'>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.valuationPhase"/>:</b>
		</td>
		<td>
			<html:select property="valuationPhase" onchange="this.form.method.value='loadValuationPhase'; this.form.submit();">
				<html:options collection="valuationPhaseList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.semester"/>:</b>
			&nbsp;
			<html:select property="executionPeriod" onchange="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit();">
				<html:option value="-1"><bean:message key="label.teacherServiceDistribution.both"/></html:option>
				<html:options collection="executionPeriodList" property="idInternal" labelProperty="semester"/>
			</html:select>
		</td>
	</tr>
	<tr class='aleft'>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.ValuationGrouping"/>:</b>
		</td>
		<td colspan="2">
			<html:select property="valuationGrouping" onchange="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit();">
				
				<html:options collection="valuationGroupingOptionEntryList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
</table>
<br/>
<logic:notEmpty name="valuationTeacherDTOEntryList">
	<b>&bull;</b>&nbsp;
	<html:link href="javascript:document.forms[0].method.value='exportTeacherServiceDistributionValuationToExcel'; document.forms[0].submit();">
		<bean:message key="label.teacherService.exportToExcel"/>
	</html:link>
</logic:notEmpty>
<br/>
<br/>
<br/>

<bean:message key="label.teacherService.navigateBy"/>:
<html:link href="javascript:document.forms[0].method.value='changeToViewCourses'; document.forms[0].submit();">
	<bean:message key="label.teacherService.navigateByCourse"/>
</html:link> | 
<b> <bean:message key="label.teacherService.navigateByTeacher"/> </b> | 
<html:link href="javascript:document.forms[0].method.value='changeToViewTeacherAndCourses'; document.forms[0].submit();">
	<bean:message key="label.teacherService.viewByCoursesAndTeachers"/>
</html:link> |
<html:link href="javascript:document.forms[0].method.value='changeToViewCharts'; document.forms[0].submit();">
	<bean:message key="label.teacherServiceDistribution.viewByCharts"/>
</html:link>
<br/>
<br/>

<logic:empty name="valuationTeacherDTOEntryList">
	<span class="error">
		<bean:message key="label.teacherServiceDistribution.notExistsValuationTeachers"/>
	</span>
	<br/>
</logic:empty>
<logic:notEmpty name="valuationTeacherDTOEntryList">
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
			<bean:message key="label.teacherServiceDistribution.availableHours"/>
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
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="valuationTeacherDTOEntry" property="totalTheoreticalHoursLectured"/></fmt:formatNumber>
		</td>	
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="valuationTeacherDTOEntry" property="totalPraticalHoursLectured"/></fmt:formatNumber>		
		</td>		
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="valuationTeacherDTOEntry" property="totalTheoPratHoursLectured"/></fmt:formatNumber>
		</td>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="valuationTeacherDTOEntry" property="totalLaboratorialHoursLectured"/></fmt:formatNumber>
		</td>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="valuationTeacherDTOEntry" property="totalHoursLectured"/></fmt:formatNumber>
		</td>
		<td class="aright">	
			<bean:write name="valuationTeacherDTOEntry" property="requiredHours"/>
		</td>
		<logic:greaterThan name="valuationTeacherDTOEntry" property="availability" value="0.0"> 
		<td class="aright" class="yellow">
		</logic:greaterThan>
		<logic:lessThan  name="valuationTeacherDTOEntry" property="availability" value="0.0"> 
		<td class="aright" class="red">
		</logic:lessThan>
		<logic:equal name="valuationTeacherDTOEntry" property="availability" value="0.0"> 
		<td class="aright" class="green">
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
			<html:link page="<%= "/professorshipValuation.do?method=prepareLinkForProfessorshipValuationByTeacher&amp;teacherServiceDistribution=" + 
			teacherServiceDistributionId + "&amp;valuationTeacher=" + valuationTeacherId + "&amp;courseValuation=" +  courseValuationId %>">
				<bean:write name="professorshipValuationDTOEntry" property="courseValuationDTOEntry.courseValuation.name"/>
			</html:link>
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
</logic:notEmpty>
<br/>

<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + teacherServiceDistributionId %>'>
	<bean:message key="link.back"/>
</html:link>

</html:form>