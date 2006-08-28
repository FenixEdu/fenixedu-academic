<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuation" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<style>
table.vtsbc {
margin-bottom: 1em;
border: 2px solid #aaa;
text-align: center;
border-collapse: collapse;
}
table.vtsbc th {
padding: 0.2em 0.2em;
border: 1px solid #bbb;
border-bottom: 1px solid #aaa;
background-color: #cacaca;
font-weight: bold;
}
table.vtsbc td {
background-color: #eaeaea;
border: none;
border: 1px solid #ccc;
padding: 0.25em 0.5em;
}
table.vtsbc td.courses {
background-color: #f4f4f8;
width: 300px;
padding: 0.25em 0.25em;
text-align: left;
}
table.vtsbc td.green {
background-color: #ccddcc;
}
table.vtsbc td.red {
background-color: #ffddcc;
}
table.vtsbc td.yellow {
background-color: #ffffdd;
}
.right td {
text-align: right;
}
.left td {
text-align: left;
}
.backwhite {
text-align: left;
background-color: #fff;
}
.backwhite ul {
margin: 0.3em 0;
}
.backwhite ul li {
padding: 0.2em 0.5em;
color: #458;
}
table.search {
background-color: #f5f5f5;
border-collapse: collapse;
}
table.search tr td {
border: 1px solid #fff;
padding: 0.3em;
}
.leftcolumn {
text-align: right;
}
</style>


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
<html:hidden property="method" value=""/>
<html:hidden property="teacherServiceDistribution"/>
<html:hidden property="viewType"/>
<html:hidden property="viewCurricularInformation"/>
<html:hidden property="viewStudentsEnrolments"/>
<html:hidden property="viewShiftHours"/>
<html:hidden property="viewStudentsEnrolmentsPerShift"/>

<table class='search'>
	<tr class='left'>
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
	<tr class='left'>
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
	<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='exportTeacherServiceDistributionValuationToExcel'; document.teacherServiceDistributionValuationForm.submit();">
		<bean:message key="label.teacherService.exportToExcel"/>
	</html:link>
</logic:notEmpty>
<br/>
<br/>
<br/>

<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='changeToViewCourses'; document.teacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.navigateByCourse"/>
</html:link> | 
<b> <bean:message key="label.teacherService.navigateByTeacher"/> </b> | 
<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='changeToViewTeacherAndCourses'; document.teacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.viewByCoursesAndTeachers"/>
</html:link> |
<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='changeToViewCharts'; document.teacherServiceDistributionValuationForm.submit();">
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
			<html:link page="<%= "/professorshipValuation.do?method=prepareLinkForProfessorshipValuationByTeacher&amp;teacherServiceDistribution=" + 
			teacherServiceDistributionId + "&amp;valuationTeacher=" + valuationTeacherId + "&amp;courseValuation=" +  courseValuationId %>">
				<bean:write name="professorshipValuationDTOEntry" property="courseValuationDTOEntry.courseValuation.name"/>
			</html:link>
			(<logic:iterate name="professorshipValuationDTOEntry" property="courseValuationDTOEntry.curricularCoursesInformation" id="curricularCourseInformation">
						<bean:write name="curricularCourseInformation" property="key"/>
						(<logic:iterate name="curricularCourseInformation" property="value" id="curricularYear">
							<bean:write name="curricularYear"/>&nbsp;
						</logic:iterate>ºano)
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