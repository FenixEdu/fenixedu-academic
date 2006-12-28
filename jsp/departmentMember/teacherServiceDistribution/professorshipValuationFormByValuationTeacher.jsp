<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuationType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationValueType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuation" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ValuationTeacherDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ProfessorshipValuation" %>
<%@ page import="net.sourceforge.fenixedu.domain.CompetenceCourse" %>
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
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
		<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
<%--		(<bean:write name="teacherServiceDistribution" property="executionPeriod.semester"/>�<bean:message key="label.common.courseSemester"/> --%>
		<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.professorshipValuationService"/>
</h3>

<html:form action="/professorshipValuation">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherServiceDistribution" property="teacherServiceDistribution"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.professorshipValuation" property="professorshipValuation"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributionViewAnchor" property="distributionViewAnchor"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>

<html:link href="javascript:document.professorshipValuationForm.method.value='loadProfessorshipValuations'; document.professorshipValuationForm.viewType.value=1; document.professorshipValuationForm.submit()">
	<bean:message key="label.teacherService.navigateByCourse"/>
</html:link> |
<b> <bean:message key="label.teacherService.navigateByTeacher"/> </b>
<br/>
<br/>

<b><bean:message key="label.teacherServiceDistribution.ValuationGrouping"/>:</b>
<html:select property="valuationGrouping" onchange="this.form.method.value='loadValuationGrouping'; this.form.submit();">
	<html:options collection="valuationGroupingOptionEntryList" property="idInternal" labelProperty="name"/>
</html:select>
<br/>
<br/>

<logic:notPresent name="notAvailableValuationTeachers">
<b><bean:message key="label.teacherServiceDistribution.valuationTeacher"/>:</b>
<br/>
<html:select property="valuationTeacher" onchange="this.form.method.value='loadProfessorshipValuations'; this.form.submit();">
	<html:options collection="valuationTeacherList" property="idInternal" labelProperty="name"/>
</html:select>							
<br/>
<br/>

<b><bean:message key="label.teacherServiceDistribution.associatedCourses"/>:</b>
<br/>
<table class='vtsbc'>
	<tr class='acenter'>
		<th>
			<bean:message key="label.teacherServiceDistribution.name"/>
		</th>
		<logic:greaterThan name="selectedValuationTeacher" property="totalTheoreticalHoursLectured" value="0.0">
			<th>
				<bean:message key="label.teacherServiceDistribution.theoreticalHoursLectured"/>
			</th>
		</logic:greaterThan>
		<logic:greaterThan name="selectedValuationTeacher" property="totalPraticalHoursLectured" value="0.0">
			<th>
				<bean:message key="label.teacherServiceDistribution.praticalHoursLectured"/>
			</th>
		</logic:greaterThan>
		<logic:greaterThan name="selectedValuationTeacher" property="totalTheoPratHoursLectured" value="0.0">
			<th>
				<bean:message key="label.teacherServiceDistribution.theoPratHoursLectured"/>
			</th>
		</logic:greaterThan>
		<logic:greaterThan name="selectedValuationTeacher" property="totalLaboratorialHoursLectured" value="0.0">
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

<logic:iterate name="selectedValuationTeacher" property="professorshipValuationDTOEntries" id="professorshipValuationDTOEntry">
	<bean:define id="professorshipValuation" name="professorshipValuationDTOEntry" property="professorshipValuation" type="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ProfessorshipValuation"/>
	<tr class='tdright'>
		<td class='courses' style="width: 300px;">
			<bean:write name="professorshipValuation" property="courseValuation.name"/>
		</td>
		<logic:greaterThan name="selectedValuationTeacher" property="totalTheoreticalHoursLectured" value="0.0">
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="professorshipValuation" property="theoreticalHours"/>
			</fmt:formatNumber>
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedValuationTeacher" property="totalPraticalHoursLectured" value="0.0">
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="professorshipValuation" property="praticalHours"/>
			</fmt:formatNumber>
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedValuationTeacher" property="totalTheoPratHoursLectured" value="0.0">
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="professorshipValuation" property="theoPratHours"/>
			</fmt:formatNumber>
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedValuationTeacher" property="totalLaboratorialHoursLectured" value="0.0">
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
<logic:equal name="selectedValuationTeacher" property="usingExtraCredits" value="true">
	<tr class='tdright'>
		<td class='courses'>
			<bean:write name="selectedValuationTeacher" property="extraCreditsName"/>
		</td>
		<logic:greaterThan name="selectedValuationTeacher" property="totalTheoreticalHoursLectured" value="0.0">
		<td>
			&nbsp;
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedValuationTeacher" property="totalPraticalHoursLectured" value="0.0">
		<td>
			&nbsp;
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedValuationTeacher" property="totalTheoPratHoursLectured" value="0.0">
		<td>
			&nbsp;
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedValuationTeacher" property="totalLaboratorialHoursLectured" value="0.0">
		<td>
			&nbsp;
		</td>
		</logic:greaterThan>
		<td>
			&nbsp;
		</td>				
		<td>
			<bean:write name="selectedValuationTeacher" property="extraCreditsValue"/>
		</td>
		<td>
		</td>
		<td>
			&nbsp;
		</td>
	</tr>
</logic:equal>
	<tr class='tdright'>
		<td class='courses'>
			&nbsp;
		</td>
		<logic:greaterThan name="selectedValuationTeacher" property="totalTheoreticalHoursLectured" value="0.0">
		<td>
			&nbsp;
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedValuationTeacher" property="totalPraticalHoursLectured" value="0.0">
		<td>
			&nbsp;
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedValuationTeacher" property="totalTheoPratHoursLectured" value="0.0">
		<td>
			&nbsp;
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedValuationTeacher" property="totalLaboratorialHoursLectured" value="0.0">
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
	<tr class='tdright'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.total"/>
		</td>
		<logic:greaterThan name="selectedValuationTeacher" property="totalTheoreticalHoursLectured" value="0.0">
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="selectedValuationTeacher" property="totalTheoreticalHoursLectured"/>
			</fmt:formatNumber>
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedValuationTeacher" property="totalPraticalHoursLectured" value="0.0">
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="selectedValuationTeacher" property="totalPraticalHoursLectured"/>
			</fmt:formatNumber>
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedValuationTeacher" property="totalTheoPratHoursLectured" value="0.0">
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="selectedValuationTeacher" property="totalTheoPratHoursLectured"/>
			</fmt:formatNumber>
		</td>
		</logic:greaterThan>
		<logic:greaterThan name="selectedValuationTeacher" property="totalLaboratorialHoursLectured" value="0.0">
		<td>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="selectedValuationTeacher" property="totalLaboratorialHoursLectured"/>
			</fmt:formatNumber>
		</td>
		</logic:greaterThan>
		<td>
			&nbsp;
		</td>				
		<td><b>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="selectedValuationTeacher" property="totalHoursLecturedPlusExtraCredits"/>
			</fmt:formatNumber>
			/ 
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="selectedValuationTeacher" property="requiredHours"/>
			</fmt:formatNumber></b>
		</td>
		<td>
		</td>
		<td>
		</td>	
	</tr>	
</table>
<br/>

<b><bean:message key="label.teacherServiceDistribution.extraCredits"/>:</b>
<br/>
<table class='vtsbc'>
	<tr class='acenter'>
		<th>
			<bean:message key="label.teacherServiceDistribution.extraCreditsName"/>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.value"/>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.creditsInUse"/>
		</th>
		<th/>
	</tr>
	<tr>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.extraCreditsName" property="extraCreditsName" size="28" maxlength="240"/>
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.extraCreditsValue" property="extraCreditsValue" size="3" maxlength="4"/>
		</td>
		<td>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.usingExtraCredits" property="usingExtraCredits"/>
		</td>
		<td>
			<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='setExtraCredits'; this.form.page.value='3'; this.form.submit();">
				<bean:message key="label.teacherServiceDistribution.valuate"/>
			</html:button>
		</td>
	</tr>
</table>

<br/>
<br/>


<table class='search'>
	<tr>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.semester"/>:</b>
			&nbsp;&nbsp;
			<html:select property="executionPeriod" onchange="this.form.method.value='loadExecutionPeriod'; this.form.submit();">
				<html:options collection="executionPeriodList" property="idInternal" labelProperty="semester"/>
			</html:select>
			&nbsp;
		</td>
	<logic:notPresent name="notAvailableCompetenceCourses">
		<td>
			<b><bean:message key="label.teacherServiceDistribution.competenceCourse"/>:</b>
		</td>
		<td>
			<html:select property="valuationCompetenceCourse" onchange="this.form.method.value='loadValuationCompetenceCourse'; this.form.submit();">
				<html:options collection="valuationCompetenceCourseList" property="idInternal" labelProperty="name"/>
			</html:select>									
		</td>
	</tr>
	<tr>
		<td/>
		<td>
		<logic:equal name="selectedCourseValuationType" value="<%= CourseValuationType.CURRICULAR_COURSE_VALUATION.toString() %>">
			
				<b><bean:message key="label.teacherServiceDistribution.curricularCourse"/>:</b>
			</td>
			<td>
				<html:select property="valuationCurricularCourse" onchange="this.form.method.value='loadProfessorshipValuations'; this.form.submit();">
					<html:options collection="valuationCurricularCourseList" property="idInternal" labelProperty="degreeCurricularPlan.degree.name"/>
				</html:select>							
			
		</logic:equal>
		<logic:equal name="selectedCourseValuationType" value="<%= CourseValuationType.CURRICULAR_COURSE_VALUATION_GROUP.toString() %>">
			
				<b><bean:message key="label.teacherServiceDistribution.curricularCourseValuationGroup"/>:</b>
			</td>
			<td>
				<html:select property="curricularCourseValuationGroup" onchange="this.form.method.value='loadProfessorshipValuations'; this.form.submit();">
					<html:options collection="curricularCourseValuationGroupList" property="idInternal" labelProperty="groupName"/>
				</html:select>							
			
		</logic:equal>
		</td>
	</logic:notPresent>
	</tr>
</table>

<logic:notPresent name="notAvailableCompetenceCourses">

<br/>

<logic:notEqual name="selectedCourseValuationType" value="<%= CourseValuationType.NOT_DETERMINED.toString() %>">
<logic:notPresent name="courseValuationNotSelected">

<b><bean:message key="label.teacherServiceDistribution.associateTeacherToCourse"/>:</b>
<br/>
<table class='vtsbc'>
	<tr class='acenter'>
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
	<tr class='tdright'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.theoreticalHours"/>
		</td>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.theoreticalHoursType" property="theoreticalHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>">
			</html:radio>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoreticalHoursManual" property="theoreticalHoursManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.theoreticalHoursType" property="theoreticalHoursType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((ValuationTeacherDTOEntry) request.getAttribute("selectedValuationTeacher")).getValuationTeachers().get(0).getRealTheoreticalHours(((CourseValuation) request.getAttribute("selectedCourseValuation")).getAssociatedExecutionCoursesLastYear()) %>
				</fmt:formatNumber>
			</html:radio>
		</td>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.theoreticalHoursType" property="theoreticalHoursType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((ValuationTeacherDTOEntry) request.getAttribute("selectedValuationTeacher")).getValuationTeachers().get(0).getRealTheoreticalHours(((CourseValuation) request.getAttribute("selectedCourseValuation")).getAssociatedExecutionCourses()) %>
				</fmt:formatNumber>
			</html:radio>
		</td>
	</tr>
</logic:greaterThan>
<logic:lessEqual name="selectedCourseValuation" property="theoreticalHoursPerShift" value="0.0">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.theoreticalHoursType" property="theoreticalHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.theoreticalHoursManual" property="theoreticalHoursManual" value="0.0"/>	
</logic:lessEqual>

	
<logic:greaterThan name="selectedCourseValuation" property="praticalHoursPerShift" value="0.0">			
	<tr class='tdright'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.praticalHours"/>
		</td>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.praticalHoursType" property="praticalHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.praticalHoursManual" property="praticalHoursManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.praticalHoursType" property="praticalHoursType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((ValuationTeacherDTOEntry) request.getAttribute("selectedValuationTeacher")).getValuationTeachers().get(0).getRealPraticalHours(((CourseValuation) request.getAttribute("selectedCourseValuation")).getAssociatedExecutionCoursesLastYear()) %>
				</fmt:formatNumber>
			</html:radio>
		</td>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.praticalHoursType" property="praticalHoursType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((ValuationTeacherDTOEntry) request.getAttribute("selectedValuationTeacher")).getValuationTeachers().get(0).getRealPraticalHours(((CourseValuation) request.getAttribute("selectedCourseValuation")).getAssociatedExecutionCourses()) %>
				</fmt:formatNumber>
			</html:radio>
		</td>
	</tr>
</logic:greaterThan>
<logic:lessEqual name="selectedCourseValuation" property="praticalHoursPerShift" value="0.0">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.praticalHoursType" property="praticalHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.praticalHoursManual" property="praticalHoursManual" value="0.0"/>	
</logic:lessEqual>

	
<logic:greaterThan name="selectedCourseValuation" property="theoPratHoursPerShift" value="0.0">				
	<tr class='tdright'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.theoPratHours"/>
		</td>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.theoPratHoursType" property="theoPratHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoPratHoursManual" property="theoPratHoursManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.theoPratHoursType" property="theoPratHoursType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((ValuationTeacherDTOEntry) request.getAttribute("selectedValuationTeacher")).getValuationTeachers().get(0).getRealTheoPratHours(((CourseValuation) request.getAttribute("selectedCourseValuation")).getAssociatedExecutionCoursesLastYear()) %>
				</fmt:formatNumber>
			</html:radio>
		</td>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.theoPratHoursType" property="theoPratHoursType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((ValuationTeacherDTOEntry) request.getAttribute("selectedValuationTeacher")).getValuationTeachers().get(0).getRealTheoPratHours(((CourseValuation) request.getAttribute("selectedCourseValuation")).getAssociatedExecutionCourses()) %>
				</fmt:formatNumber>
			</html:radio>
		</td>
	</tr>
</logic:greaterThan>
<logic:lessEqual name="selectedCourseValuation" property="theoPratHoursPerShift" value="0.0">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.theoPratHoursType" property="theoPratHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.theoPratHoursManual" property="theoPratHoursManual" value="0.0"/>	
</logic:lessEqual>

	
<logic:greaterThan name="selectedCourseValuation" property="laboratorialHoursPerShift" value="0.0">					
	<tr class='tdright'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.laboratorialHours"/>
		</td>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.laboratorialHoursType" property="laboratorialHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.laboratorialHoursManual" property="laboratorialHoursManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.laboratorialHoursType" property="laboratorialHoursType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((ValuationTeacherDTOEntry) request.getAttribute("selectedValuationTeacher")).getValuationTeachers().get(0).getRealLaboratorialHours(((CourseValuation) request.getAttribute("selectedCourseValuation")).getAssociatedExecutionCoursesLastYear()) %>
				</fmt:formatNumber>
			</html:radio>
		</td>
		<td>
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.laboratorialHoursType" property="laboratorialHoursType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((ValuationTeacherDTOEntry) request.getAttribute("selectedValuationTeacher")).getValuationTeachers().get(0).getRealLaboratorialHours(((CourseValuation) request.getAttribute("selectedCourseValuation")).getAssociatedExecutionCourses()) %>
				</fmt:formatNumber>
			</html:radio>
		</td>
	</tr>
</logic:greaterThan>
<logic:lessEqual name="selectedCourseValuation" property="laboratorialHoursPerShift" value="0.0">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.laboratorialHoursType" property="laboratorialHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.laboratorialHoursManual" property="laboratorialHoursManual" value="0.0"/>	
</logic:lessEqual>
</table>

<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='setProfessorshipValuation'; this.form.page.value='1'; this.form.submit();">
	<bean:message key="label.teacherServiceDistribution.assign"/>
</html:button>
<br/>
<br/>

</logic:notPresent>
</logic:notEqual>
<logic:equal name="selectedCourseValuationType" value="<%= CourseValuationType.NOT_DETERMINED.toString() %>">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.theoPratHoursManual" property="theoPratHoursManual" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.praticalHoursManual" property="praticalHoursManual" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.theoreticalHoursManual" property="theoreticalHoursManual" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.laboratorialHoursManual" property="laboratorialHoursManual" value="0"/>
	<span class="error">
		<bean:message key="label.teacherServiceDistribution.nonValuatedCourse"/>
	</span>
</logic:equal>

</logic:notPresent>
<logic:present name="notAvailableCompetenceCourses">
	<br/>
	<span class="error">
		<bean:message key="label.teacherServiceDistribution.noCompetenceCoursesForExecutionPeriod"/>
	</span>
	<br/>
	<br/>
</logic:present>

</logic:notPresent>
<logic:present name="notAvailableValuationTeachers">
	<br/>
	<span class="error">
		<bean:message key="label.teacherServiceDistribution.notExistsValuationTeachers"/>
	</span>
	<br/>
	<br/>
</logic:present>

<span class="error">
	<html:errors/>
</span>
<br/>


<br/>
<logic:notEqual name="professorshipValuationForm" property="distributionViewAnchor" value="0">
	<bean:define id="valuationTeacherID" name="professorshipValuationForm" property="distributionViewAnchor"/>
	<html:link page='<%= "/teacherServiceDistributionValuation.do?method=prepareForTeacherServiceDistributionValuationByTeachers&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() 
	+ "#" + valuationTeacherID %>'>
		<bean:message key="link.teacherServiceDistribution.backToteacherServiceDistributionVisualization"/>
	</html:link>
</logic:notEqual>
<logic:equal name="professorshipValuationForm" property="distributionViewAnchor" value="0">
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
		<bean:message key="link.back"/>
	</html:link>
</logic:equal>

</html:form>


