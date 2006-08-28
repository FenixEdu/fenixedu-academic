<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuationType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationValueType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuation" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ValuationTeacherDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ProfessorshipValuation" %>
<%@ page import="net.sourceforge.fenixedu.domain.CompetenceCourse" %>
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
width: 150px;
padding: 0.25em 0.25em;
text-align: left;
}

</style>


<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
		<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
		<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.professorshipValuationService"/>
</h3>

<html:form action="/professorshipValuation">
<html:hidden property="method" value=""/>
<html:hidden property="teacherServiceDistribution"/>
<html:hidden property="viewType"/>
<html:hidden property="professorshipValuation"/>
<html:hidden property="distributionViewAnchor"/>
<html:hidden property="page" value="0"/>

<b> <bean:message key="label.teacherService.navigateByCourse"/> </b> |
<html:link href="javascript:document.professorshipValuationForm.method.value='loadProfessorshipValuations'; document.professorshipValuationForm.viewType.value=0; document.professorshipValuationForm.submit()">
	<bean:message key="label.teacherService.navigateByTeacher"/> 
</html:link>
<br/>
<br/>

<b><bean:message key="label.teacherServiceDistribution.ValuationGrouping"/>:</b>
<html:select property="valuationGrouping" onchange="this.form.method.value='loadValuationGrouping'; this.form.submit();">
	<html:options collection="valuationGroupingOptionEntryList" property="idInternal" labelProperty="name"/>
</html:select>

<b><bean:message key="label.teacherServiceDistribution.semester"/>:</b>
<html:select property="executionPeriod" onchange="this.form.method.value='loadExecutionPeriod'; this.form.submit();">
	<html:options collection="executionPeriodList" property="idInternal" labelProperty="semester"/>
</html:select>
<br/>
<br/>

<logic:notPresent name="notAvailableCompetenceCourses">
<table border=0>
	<tr valign="top">
		<td>
			<b><bean:message key="label.teacherServiceDistribution.competenceCourse"/>:</b>
			<br/>
			<html:select property="valuationCompetenceCourse" onchange="this.form.method.value='loadValuationCompetenceCourse'; this.form.submit();">
				<html:options collection="valuationCompetenceCourseList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
		<td>
			&nbsp;
		</td>
		<td>
			<logic:equal name="selectedCourseValuationType" value="<%= CourseValuationType.CURRICULAR_COURSE_VALUATION.toString() %>">
			<b><bean:message key="label.teacherServiceDistribution.curricularCourse"/>:</b>
			<br/>
			<html:select property="valuationCurricularCourse" onchange="this.form.method.value='loadProfessorshipValuations'; this.form.submit();">
				<html:options collection="valuationCurricularCourseList" property="idInternal" labelProperty="degreeCurricularPlan.degree.name"/>
			</html:select>							
			</logic:equal>
			
			<logic:equal name="selectedCourseValuationType" value="<%= CourseValuationType.CURRICULAR_COURSE_VALUATION_GROUP.toString() %>">
			<b><bean:message key="label.teacherServiceDistribution.curricularCourseValuationGroup"/>:</b>
			<br/>
			<html:select property="curricularCourseValuationGroup" onchange="this.form.method.value='loadProfessorshipValuations'; this.form.submit();">
				<html:options collection="curricularCourseValuationGroupList" property="idInternal" labelProperty="groupName"/>
			</html:select>							
			</logic:equal>
		</td>
	</tr>
</table>

<br/>

<logic:notEqual name="selectedCourseValuationType" value="<%= CourseValuationType.NOT_DETERMINED.toString() %>">
<logic:notPresent name="courseValuationNotSelected">

<b><bean:message key="label.teacherServiceDistribution.associatedTeachers"/>:</b>
<br/>
<table class='vtsbc'>
	<tr class='center'>
		<th>
			<bean:message key="label.teacherServiceDistribution.name"/>
		</th>
		<logic:greaterThan name="selectedCourseValuation" property="theoreticalHoursPerShift" value="0.0">
			<th>
				<bean:message key="label.teacherServiceDistribution.theoreticalHoursLectured"/>
			</th>
		</logic:greaterThan>
		<logic:greaterThan name="selectedCourseValuation" property="praticalHoursPerShift" value="0.0">
			<th>
				<bean:message key="label.teacherServiceDistribution.praticalHoursLectured"/>
			</th>
		</logic:greaterThan>
		<logic:greaterThan name="selectedCourseValuation" property="theoPratHoursPerShift" value="0.0">
			<th>
				<bean:message key="label.teacherServiceDistribution.theoPratHoursLectured"/>
			</th>
		</logic:greaterThan>
		<logic:greaterThan name="selectedCourseValuation" property="laboratorialHoursPerShift" value="0.0">
			<th>
				<bean:message key="label.teacherServiceDistribution.laboratorialHoursLectured"/>
			</th>
		</logic:greaterThan>
		<th>
			&nbsp;
		</th>				
		<th>
			<bean:message key="label.teacherServiceDistribution.total"/>
		</th>
		<th>
		</th>
		<th>
		</th>					
	</tr>

<logic:iterate name="selectedCourseValuation" property="professorshipValuations" id="professorshipValuation">
	<bean:define id="professorshipValuation" name="professorshipValuation" type="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ProfessorshipValuation"/>
	<tr class='right'>
		<td class='courses' style="width: 300px;">
			<bean:write name="professorshipValuation" property="valuationTeacher.name"/>
		</td>
		<logic:greaterThan name="selectedCourseValuation" property="theoreticalHoursPerShift" value="0.0">
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="professorshipValuation" property="theoreticalHours"/>
			</fmt:formatNumber>
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedCourseValuation" property="praticalHoursPerShift" value="0.0">
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="professorshipValuation" property="praticalHours"/>
			</fmt:formatNumber>
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedCourseValuation" property="theoPratHoursPerShift" value="0.0">
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="professorshipValuation" property="theoPratHours"/>
			</fmt:formatNumber>
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedCourseValuation" property="laboratorialHoursPerShift" value="0.0">
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="professorshipValuation" property="laboratorialHours"/>
			</fmt:formatNumber>
		</td>
		</logic:greaterThan>
		<td>
			&nbsp;
		</td>					
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="professorshipValuation" property="totalHours"/>
			</fmt:formatNumber>
		</td>
		<td>
		</td>
		<td>
			<html:link href='<%= "javascript:document.professorshipValuationForm.method.value='removeProfessorshipValuation'; document.professorshipValuationForm.professorshipValuation.value=" + ((ProfessorshipValuation) professorshipValuation).getIdInternal().toString() + ";document.professorshipValuationForm.submit();" %>' >
				<bean:message key="label.teacherServiceDistribution.delete"/>
			</html:link>
		</td>
	</tr>
</logic:iterate>
	<tr class='right'>
		<td class='courses'>
			&nbsp;
		</td>
		<logic:greaterThan name="selectedCourseValuation" property="theoreticalHoursPerShift" value="0.0">
		<td>
			&nbsp;
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedCourseValuation" property="praticalHoursPerShift" value="0.0">
		<td>
			&nbsp;
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedCourseValuation" property="theoPratHoursPerShift" value="0.0">
		<td>
			&nbsp;
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedCourseValuation" property="laboratorialHoursPerShift" value="0.0">
		<td>
			&nbsp;
		</td>
		</logic:greaterThan>
		<td>
			&nbsp;
		</td>				
		<td>
			&nbsp;
		</td>
		<td>
			&nbsp;
		</td>
		<td>
			&nbsp;
		</td>
	</tr>	
	<tr class='right'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.total"/>
		</td>
		<logic:greaterThan name="selectedCourseValuation" property="theoreticalHoursPerShift" value="0.0">
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="selectedCourseValuation" property="totalTheoreticalHoursLectured"/>
			</fmt:formatNumber>
			/
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"> 
			<bean:write name="selectedCourseValuation" property="theoreticalHours"/>
			</fmt:formatNumber>
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedCourseValuation" property="praticalHoursPerShift" value="0.0">
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="selectedCourseValuation" property="totalPraticalHoursLectured"/>
			</fmt:formatNumber>
			/ 
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="selectedCourseValuation" property="praticalHours"/>
			</fmt:formatNumber>
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedCourseValuation" property="theoPratHoursPerShift" value="0.0">
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="selectedCourseValuation" property="totalTheoPratHoursLectured"/>
			</fmt:formatNumber>
			/
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			 <bean:write name="selectedCourseValuation" property="theoPratHours"/>
			 </fmt:formatNumber>
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedCourseValuation" property="laboratorialHoursPerShift" value="0.0">
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="selectedCourseValuation" property="totalLaboratorialHoursLectured"/>
			</fmt:formatNumber>
			/ 
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="selectedCourseValuation" property="laboratorialHours"/>
			</fmt:formatNumber>
		</td>
		</logic:greaterThan>
		<td>
			&nbsp;
		</td>				
		<td><b>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="selectedCourseValuation" property="totalHoursLectured"/>
			</fmt:formatNumber>
			/
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"> 
			<bean:write name="selectedCourseValuation" property="totalHours"/>
			</fmt:formatNumber>
		</td>
		<td>
		</td>		
		<td>
		</td>	
	</tr>	
</table>
<br/>
<logic:notPresent name="notAvailableValuationTeachers">

<b><bean:message key="label.teacherServiceDistribution.valuationTeacher"/>:</b>
<br/>
<html:select property="valuationTeacher" onchange="this.form.method.value='loadProfessorshipValuations'; this.form.submit();">
	<html:options collection="valuationTeacherList" property="idInternal" labelProperty="name"/>
</html:select>
&nbsp;&nbsp;
<b><bean:message key="label.teacherServiceDistribution.availability"/>:</b>
<logic:greaterThan name="selectedValuationTeacher" property="availability" value="0.0">
<span style="color: green;">
</logic:greaterThan>
<logic:lessThan name="selectedValuationTeacher" property="availability" value="0.0">
<span style="color: red;">
</logic:lessThan>
<logic:equal name="selectedValuationTeacher" property="availability" value="0.0">
<span style="color: #aaaaaa;">
</logic:equal>
	<b>
	<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
		<bean:write name="selectedValuationTeacher" property="availability"/>
	</fmt:formatNumber>
	</b>
</span>
<bean:message key="label.teacherService.hours"/>
<br/>
<br/>

<b><bean:message key="label.teacherServiceDistribution.associateTeacherToCourse"/>:</b>
<br/>
<table class='vtsbc'>
	<tr class='center'>
		<th>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.manualValue"/>
		</th>
		<th>
			<bean:write name="selectedCourseValuation" property="lastYearExecutionPeriod.executionYear.year"/>
		</th>
		<th>
			<bean:write name="selectedCourseValuation" property="executionPeriod.executionYear.year"/>
		</th>
	</tr>

<logic:greaterThan name="selectedCourseValuation" property="theoreticalHoursPerShift" value="0.0">	
	<tr class='right'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.theoreticalHours"/>
		</td>
		<td>
			<html:radio property="theoreticalHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>">
			</html:radio>
			<html:text property="theoreticalHoursManual" size="3" maxlength="4" />
		</td>
		<td>
			<html:radio property="theoreticalHoursType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((ValuationTeacherDTOEntry) request.getAttribute("selectedValuationTeacher")).getValuationTeachers().get(0).getRealTheoreticalHours(((CourseValuation) request.getAttribute("selectedCourseValuation")).getAssociatedExecutionCoursesLastYear()) %>
				</fmt:formatNumber>
			</html:radio>
		</td>
		<td>
			<html:radio property="theoreticalHoursType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((ValuationTeacherDTOEntry) request.getAttribute("selectedValuationTeacher")).getValuationTeachers().get(0).getRealTheoreticalHours(((CourseValuation) request.getAttribute("selectedCourseValuation")).getAssociatedExecutionCourses()) %>
				</fmt:formatNumber>
			</html:radio>
		</td>
	</tr>
</logic:greaterThan>
<logic:lessEqual name="selectedCourseValuation" property="theoreticalHoursPerShift" value="0.0">
	<html:hidden property="theoreticalHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="theoreticalHoursManual" value="0.0"/>	
</logic:lessEqual>

	
<logic:greaterThan name="selectedCourseValuation" property="praticalHoursPerShift" value="0.0">			
	<tr class='right'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.praticalHours"/>
		</td>
		<td>
			<html:radio property="praticalHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text property="praticalHoursManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio property="praticalHoursType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((ValuationTeacherDTOEntry) request.getAttribute("selectedValuationTeacher")).getValuationTeachers().get(0).getRealPraticalHours(((CourseValuation) request.getAttribute("selectedCourseValuation")).getAssociatedExecutionCoursesLastYear()) %>
				</fmt:formatNumber>
			</html:radio>
		</td>
		<td>
			<html:radio property="praticalHoursType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((ValuationTeacherDTOEntry) request.getAttribute("selectedValuationTeacher")).getValuationTeachers().get(0).getRealPraticalHours(((CourseValuation) request.getAttribute("selectedCourseValuation")).getAssociatedExecutionCourses()) %>
				</fmt:formatNumber>
			</html:radio>
		</td>
	</tr>
</logic:greaterThan>
<logic:lessEqual name="selectedCourseValuation" property="praticalHoursPerShift" value="0.0">
	<html:hidden property="praticalHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="praticalHoursManual" value="0.0"/>	
</logic:lessEqual>

	
<logic:greaterThan name="selectedCourseValuation" property="theoPratHoursPerShift" value="0.0">				
	<tr class='right'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.theoPratHours"/>
		</td>
		<td>
			<html:radio property="theoPratHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text property="theoPratHoursManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio property="theoPratHoursType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((ValuationTeacherDTOEntry) request.getAttribute("selectedValuationTeacher")).getValuationTeachers().get(0).getRealTheoPratHours(((CourseValuation) request.getAttribute("selectedCourseValuation")).getAssociatedExecutionCoursesLastYear()) %>
				</fmt:formatNumber>
			</html:radio>
		</td>
		<td>
			<html:radio property="theoPratHoursType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((ValuationTeacherDTOEntry) request.getAttribute("selectedValuationTeacher")).getValuationTeachers().get(0).getRealTheoPratHours(((CourseValuation) request.getAttribute("selectedCourseValuation")).getAssociatedExecutionCourses()) %>
				</fmt:formatNumber>
			</html:radio>
		</td>
	</tr>
</logic:greaterThan>
<logic:lessEqual name="selectedCourseValuation" property="theoPratHoursPerShift" value="0.0">
	<html:hidden property="theoPratHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="theoPratHoursManual" value="0.0"/>	
</logic:lessEqual>

	
<logic:greaterThan name="selectedCourseValuation" property="laboratorialHoursPerShift" value="0.0">					
	<tr class='right'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.laboratorialHours"/>
		</td>
		<td>
			<html:radio property="laboratorialHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text property="laboratorialHoursManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio property="laboratorialHoursType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((ValuationTeacherDTOEntry) request.getAttribute("selectedValuationTeacher")).getValuationTeachers().get(0).getRealLaboratorialHours(((CourseValuation) request.getAttribute("selectedCourseValuation")).getAssociatedExecutionCoursesLastYear()) %>
				</fmt:formatNumber>
			</html:radio>
		</td>
		<td>
			<html:radio property="laboratorialHoursType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((ValuationTeacherDTOEntry) request.getAttribute("selectedValuationTeacher")).getValuationTeachers().get(0).getRealLaboratorialHours(((CourseValuation) request.getAttribute("selectedCourseValuation")).getAssociatedExecutionCourses()) %>
				</fmt:formatNumber>
			</html:radio>
		</td>
	</tr>
</logic:greaterThan>
<logic:lessEqual name="selectedCourseValuation" property="laboratorialHoursPerShift" value="0.0">
	<html:hidden property="laboratorialHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="laboratorialHoursManual" value="0.0"/>	
</logic:lessEqual>
</table>

<html:button property="" onclick="this.form.method.value='setProfessorshipValuation'; this.form.page.value='1'; this.form.submit();">
	<bean:message key="label.teacherServiceDistribution.assign"/>
</html:button>
<br/>
<br/>

<span class="error">
	<html:errors/>
</span>
<br/>


</logic:notPresent>

<logic:present name="notAvailableValuationTeachers">
	<span class="error">
		<bean:message key="label.teacherServiceDistribution.notExistsValuationTeachers"/>
	</span>
	<br/><br/>
</logic:present>

</logic:notPresent>
</logic:notEqual>
<logic:equal name="selectedCourseValuationType" value="<%= CourseValuationType.NOT_DETERMINED.toString() %>">
	<span class="error">
		<b><bean:message key="label.teacherServiceDistribution.nonValuatedCourse"/></b>
	</span>
	<br/><br/>
</logic:equal>
</logic:notPresent>
<br/>

<logic:present name="notAvailableCompetenceCourses">
	<span class="error">
		<bean:message key="label.teacherServiceDistribution.noCompetenceCoursesForExecutionPeriod"/>
	</span>
	<br/><br/>
</logic:present>


<logic:notEqual name="professorshipValuationForm" property="distributionViewAnchor" value="0">
	<bean:define id="courseValuationID" name="professorshipValuationForm" property="distributionViewAnchor"/>
	<html:link page='<%= "/teacherServiceDistributionValuation.do?method=prepareForTeacherServiceDistributionValuation&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() 
	+ "#" + courseValuationID %>'>
		<bean:message key="link.teacherServiceDistribution.backToteacherServiceDistributionVisualization"/>
	</html:link>
</logic:notEqual>
<logic:equal name="professorshipValuationForm" property="distributionViewAnchor" value="0">
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
		<bean:message key="link.back"/>
	</html:link>
</logic:equal>


</html:form>
