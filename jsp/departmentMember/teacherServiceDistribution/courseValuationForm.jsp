<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuationType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationValueType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.CurricularCourseValuation" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

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
	<bean:message key="link.teacherServiceDistribution.courseValuationService"/>
</h3>

<html:form action="/courseValuation">
<html:hidden property="method" value=""/>
<html:hidden property="teacherServiceDistribution"/>
<html:hidden property="curricularCourseValuation"/>
<html:hidden property="courseValuationViewLink"/>
<html:hidden property="page" value="0"/>

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

<logic:notPresent name="notAvailableValuationCompetenceCourses">
<b><bean:message key="label.teacherServiceDistribution.competenceCourse"/>:</b>
<br/>
<html:select property="valuationCompetenceCourse" onchange="this.form.method.value='loadValuationCompetenceCourse'; this.form.submit();">
	<html:options collection="valuationCompetenceCourseList" property="idInternal" labelProperty="name"/>
</html:select>							
<br/>
<br/>

<b><bean:message key="label.teacherServiceDistribution.valuate"/>:</b>
<br/>
<table class='vtsbc'>
	<tr class='acenter'>
		<td>
			<html:radio property="courseValuationType" value="<%= CourseValuationType.COMPETENCE_COURSE_VALUATION.toString() %>" onclick="this.form.method.value='setCourseValuationType'; this.form.submit();"><bean:message key="label.teacherServiceDistribution.competenceCourseValuationType"/></html:radio>
		</td>
		<td>
			<html:radio property="courseValuationType" value="<%= CourseValuationType.CURRICULAR_COURSE_VALUATION.toString() %>" onclick="this.form.method.value='setCourseValuationType'; this.form.submit();"><bean:message key="label.teacherServiceDistribution.curricularCourseValuationType"/></html:radio>
		</td>
		<td>
			<html:radio property="courseValuationType" value="<%= CourseValuationType.CURRICULAR_COURSE_VALUATION_GROUP.toString() %>" onclick="this.form.method.value='setCourseValuationType'; this.form.submit();"><bean:message key="label.teacherServiceDistribution.curricularCourseValuationGroupType"/></html:radio>
		</td>
	</tr>
</table>
<br/>

<logic:equal name="selectedCourseValuationType" value="<%= CourseValuationType.CURRICULAR_COURSE_VALUATION.toString() %>">
<b><bean:message key="label.teacherServiceDistribution.curricularCourse"/>:</b>
<br/>
<html:select property="valuationCurricularCourse" onchange="this.form.method.value='loadCourseValuations'; this.form.submit();">
	<html:options collection="valuationCurricularCourseList" property="idInternal" labelProperty="degreeCurricularPlan.degree.name"/>
</html:select>							
<br/>
<br/>
</logic:equal>

<logic:equal name="selectedCourseValuationType" value="<%= CourseValuationType.CURRICULAR_COURSE_VALUATION_GROUP.toString() %>">
<logic:notEmpty name="curricularCourseValuationGroupList">
	<b><bean:message key="label.teacherServiceDistribution.curricularCourseValuationGroup"/>:</b>
	<br/>
	<html:select property="curricularCourseValuationGroup" onchange="this.form.method.value='loadCourseValuations'; this.form.submit();">
		<html:options collection="curricularCourseValuationGroupList" property="idInternal" labelProperty="groupName"/>
	</html:select>
	<html:button property="" onclick="this.form.method.value='deleteCurricularCourseValuationGroup'; this.form.submit();"><bean:message key="label.teacherServiceDistribution.deleteCurricularCourseValuationGroup"/></html:button>
</logic:notEmpty>
<html:button property="" onclick='<%= "this.form.method.value='prepareForCurricularCourseValuationGroupCreation'; this.form.submit();" %>'>
	<bean:message key="label.teacherServiceDistribution.createCourseValuationGroup"/>
</html:button>
<br/>
<br/>
</logic:equal>


<logic:equal name="selectedCourseValuationType" value="<%= CourseValuationType.NOT_DETERMINED.toString() %>">
	<html:hidden property="suppressRedundantHoursTypes"/>
</logic:equal>


<logic:notEqual name="selectedCourseValuationType" value="<%= CourseValuationType.NOT_DETERMINED.toString() %>">
<logic:notPresent name="courseValuationNotSelected">

<bean:message key="label.teacherServiceDistribution.supressRedundantHoursTypes"/>:
<html:checkbox property="suppressRedundantHoursTypes" onchange="this.form.method.value='loadCourseValuations'; this.form.submit();"/>
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
			<bean:message key="label.teacherServiceDistribution.omissionValue"/>
		</th>
		<th>
			<bean:write name="selectedCourseValuation" property="executionPeriod.executionYear.year"/>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.calculatedValue"/>
		</th>
	</tr>

	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.firstTimeEnrolledStudents"/>
		</td>
		<td>
			<html:radio property="firstTimeEnrolledStudentsType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>">
			</html:radio>
			<html:text property="firstTimeEnrolledStudentsManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio property="firstTimeEnrolledStudentsType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="realFirstTimeEnrolledStudentsNumberLastYear"/>
			</html:radio>
		</td>
		<td>
		
		</td>
		<td>
			<html:radio property="firstTimeEnrolledStudentsType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="realFirstTimeEnrolledStudentsNumber"/>
			</html:radio>
		</td>
		<td>
		</td>
	</tr>

	
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.secondTimeEnrolledStudents"/>
		</td>
		<td>
			<html:radio property="secondTimeEnrolledStudentsType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>">
			</html:radio>
			<html:text property="secondTimeEnrolledStudentsManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio property="secondTimeEnrolledStudentsType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="realSecondTimeEnrolledStudentsNumberLastYear"/>
			</html:radio>
		</td>
		<td>
		
		</td>
		<td>
			<html:radio property="secondTimeEnrolledStudentsType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="realSecondTimeEnrolledStudentsNumber"/>
			</html:radio>
		</td>
		<td>
		</td>		
	</tr>

<c:if test="${selectedCourseValuation.theoreticalHoursPerShift > 0.0 || !courseValuationForm.map.suppressRedundantHoursTypes}">
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.studentsPerTheoreticalShift"/>
		</td>
		<td>
			<html:radio property="studentsPerTheoreticalShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>">
			</html:radio>
			<html:text property="studentsPerTheoreticalShiftManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio property="studentsPerTheoreticalShiftType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="realStudentsPerTheoreticalShiftLastYear"/>
			</html:radio>
		</td>
		<td>
			<html:radio property="studentsPerTheoreticalShiftType" value="<%= ValuationValueType.OMISSION_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="valuationPhase.studentsPerTheoreticalShift"/>
			</html:radio>			
		</td>
		<td>
			<html:radio property="studentsPerTheoreticalShiftType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="realStudentsPerTheoreticalShift"/>
			</html:radio>
		</td>
		<td>
			<html:radio property="studentsPerTheoreticalShiftType" value="<%= ValuationValueType.CALCULATED_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="studentsPerTheoreticalShiftCalculated"/>
			</html:radio>
		</td>
	</tr>
</c:if>
<c:if test="${selectedCourseValuation.theoreticalHoursPerShift <= 0.0 && courseValuationForm.map.suppressRedundantHoursTypes}">
	<html:hidden property="studentsPerTheoreticalShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="studentsPerTheoreticalShiftManual" value="0"/>	
</c:if>

<c:if test="${selectedCourseValuation.praticalHoursPerShift > 0.0 || !courseValuationForm.map.suppressRedundantHoursTypes}">		
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.studentsPerPraticalShift"/>
		</td>
		<td>
			<html:radio property="studentsPerPraticalShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>">
			</html:radio>
			<html:text property="studentsPerPraticalShiftManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio property="studentsPerPraticalShiftType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="realStudentsPerPraticalShiftLastYear"/>
			</html:radio>
		</td>
		<td>
			<html:radio property="studentsPerPraticalShiftType" value="<%= ValuationValueType.OMISSION_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="valuationPhase.studentsPerPraticalShift"/>
			</html:radio>			
		</td>
		<td>
			<html:radio property="studentsPerPraticalShiftType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="realStudentsPerPraticalShift"/>
			</html:radio>
		</td>
		<td>
			<html:radio property="studentsPerPraticalShiftType" value="<%= ValuationValueType.CALCULATED_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="studentsPerPraticalShiftCalculated"/>
			</html:radio>
		</td>
	</tr>
</c:if>
<c:if test="${selectedCourseValuation.praticalHoursPerShift <= 0.0 && courseValuationForm.map.suppressRedundantHoursTypes}">
	<html:hidden property="studentsPerPraticalShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="studentsPerPraticalShiftManual" value="0"/>	
</c:if>

<c:if test="${selectedCourseValuation.theoPratHoursPerShift > 0.0 || !courseValuationForm.map.suppressRedundantHoursTypes}">
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.studentsPerTheoPratShift"/>
		</td>
		<td>
			<html:radio property="studentsPerTheoPratShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>">
			</html:radio>
			<html:text property="studentsPerTheoPratShiftManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio property="studentsPerTheoPratShiftType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="realStudentsPerTheoPratShiftLastYear"/>
			</html:radio>
		</td>
		<td>
			<html:radio property="studentsPerTheoPratShiftType" value="<%= ValuationValueType.OMISSION_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="valuationPhase.studentsPerTheoPratShift"/>
			</html:radio>			
		</td>
		<td>
			<html:radio property="studentsPerTheoPratShiftType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="realStudentsPerTheoPratShift"/>
			</html:radio>
		</td>
		<td>
			<html:radio property="studentsPerTheoPratShiftType" value="<%= ValuationValueType.CALCULATED_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="studentsPerTheoPratShiftCalculated"/>
			</html:radio>
		</td>
	</tr>
</c:if>	
<c:if test="${selectedCourseValuation.theoPratHoursPerShift <= 0.0 && courseValuationForm.map.suppressRedundantHoursTypes}">
	<html:hidden property="studentsPerTheoPratShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="studentsPerTheoPratShiftManual" value="0"/>	
</c:if>

<c:if test="${selectedCourseValuation.laboratorialHoursPerShift > 0.0 || !courseValuationForm.map.suppressRedundantHoursTypes}">				
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.studentsPerLaboratorialShift"/>
		</td>
		<td>
			<html:radio property="studentsPerLaboratorialShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text property="studentsPerLaboratorialShiftManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio property="studentsPerLaboratorialShiftType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="realStudentsPerLaboratorialShiftLastYear"/>
			</html:radio>
		</td>
		<td>
			<html:radio property="studentsPerLaboratorialShiftType" value="<%= ValuationValueType.OMISSION_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="valuationPhase.studentsPerLaboratorialShift"/>
			</html:radio>			
		</td>
		<td>
			<html:radio property="studentsPerLaboratorialShiftType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="realStudentsPerLaboratorialShift"/>
			</html:radio>
		</td>
		<td>
			<html:radio property="studentsPerLaboratorialShiftType" value="<%= ValuationValueType.CALCULATED_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="studentsPerLaboratorialShiftCalculated"/>
			</html:radio>
		</td>
	</tr>
</c:if>
<c:if test="${selectedCourseValuation.laboratorialHoursPerShift <= 0.0 && courseValuationForm.map.suppressRedundantHoursTypes}">
	<html:hidden property="studentsPerLaboratorialShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="studentsPerLaboratorialShiftManual" value="0"/>	
</c:if>


<c:if test="${selectedCourseValuation.theoreticalHoursPerShift > 0.0 || !courseValuationForm.map.suppressRedundantHoursTypes}">	
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.weightFirstTimeEnrolledStudentsPerTheoShift"/>
		</td>
		<td>
			<html:radio property="weightFirstTimeEnrolledStudentsPerTheoShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text property="weightFirstTimeEnrolledStudentsPerTheoShiftManual" size="3" maxlength="3"/>
		</td>
		<td>

		</td>
		<td>
			<html:radio property="weightFirstTimeEnrolledStudentsPerTheoShiftType" value="<%= ValuationValueType.OMISSION_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="valuationPhase.weightFirstTimeEnrolledStudentsPerTheoShift"/>
			</html:radio>
		</td>
		<td>

		</td>
		<td>

		</td>
	</tr>
</c:if>
<c:if test="${selectedCourseValuation.theoreticalHoursPerShift <= 0.0 && courseValuationForm.map.suppressRedundantHoursTypes}">
	<html:hidden property="weightFirstTimeEnrolledStudentsPerTheoShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="weightFirstTimeEnrolledStudentsPerTheoShiftManual" value="0.0"/>	
</c:if>


<c:if test="${selectedCourseValuation.praticalHoursPerShift > 0.0 || !courseValuationForm.map.suppressRedundantHoursTypes}">			
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.weightFirstTimeEnrolledStudentsPerPratShift"/>
		</td>
		<td>
			<html:radio property="weightFirstTimeEnrolledStudentsPerPratShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text property="weightFirstTimeEnrolledStudentsPerPratShiftManual" size="3" maxlength="3"/>
		</td>
		<td>

		</td>
		<td>
			<html:radio property="weightFirstTimeEnrolledStudentsPerPratShiftType" value="<%= ValuationValueType.OMISSION_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="valuationPhase.weightFirstTimeEnrolledStudentsPerPratShift"/>
			</html:radio>
		</td>
		<td>

		</td>
		<td>

		</td>
	</tr>
</c:if>
<c:if test="${selectedCourseValuation.praticalHoursPerShift <= 0.0 && courseValuationForm.map.suppressRedundantHoursTypes}">
	<html:hidden property="weightFirstTimeEnrolledStudentsPerPratShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="weightFirstTimeEnrolledStudentsPerPratShiftManual" value="0.0"/>	
</c:if>


<c:if test="${selectedCourseValuation.theoPratHoursPerShift > 0.0 || !courseValuationForm.map.suppressRedundantHoursTypes}">					
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.weightFirstTimeEnrolledStudentsPerTheoPratShift"/>
		</td>
		<td>
			<html:radio property="weightFirstTimeEnrolledStudentsPerTheoPratShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text property="weightFirstTimeEnrolledStudentsPerTheoPratShiftManual" size="3" maxlength="3"/>
		</td>
		<td>

		</td>
		<td>
			<html:radio property="weightFirstTimeEnrolledStudentsPerTheoPratShiftType" value="<%= ValuationValueType.OMISSION_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="valuationPhase.weightFirstTimeEnrolledStudentsPerTheoPratShift"/>
			</html:radio>
		</td>
		<td>

		</td>
		<td>

		</td>
	</tr>
</c:if>
<c:if test="${selectedCourseValuation.theoPratHoursPerShift <= 0.0 && courseValuationForm.map.suppressRedundantHoursTypes}">
	<html:hidden property="weightFirstTimeEnrolledStudentsPerTheoPratShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="weightFirstTimeEnrolledStudentsPerTheoPratShiftManual" value="0.0"/>	
</c:if>


<c:if test="${selectedCourseValuation.laboratorialHoursPerShift > 0.0 || !courseValuationForm.map.suppressRedundantHoursTypes}">							
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.weightFirstTimeEnrolledStudentsPerLabShift"/>
		</td>
		<td>
			<html:radio property="weightFirstTimeEnrolledStudentsPerLabShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text property="weightFirstTimeEnrolledStudentsPerLabShiftManual" size="3" maxlength="3"/>
		</td>
		<td>

		</td>
		<td>
			<html:radio property="weightFirstTimeEnrolledStudentsPerLabShiftType" value="<%= ValuationValueType.OMISSION_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="valuationPhase.weightFirstTimeEnrolledStudentsPerLabShift"/>
			</html:radio>
		</td>
		<td>

		</td>
		<td>

		</td>
	</tr>
</c:if>
<c:if test="${selectedCourseValuation.laboratorialHoursPerShift <= 0.0 && courseValuationForm.map.suppressRedundantHoursTypes}">
	<html:hidden property="weightFirstTimeEnrolledStudentsPerLabShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="weightFirstTimeEnrolledStudentsPerLabShiftManual" value="0.0"/>	
</c:if>


<c:if test="${selectedCourseValuation.theoreticalHoursPerShift > 0.0 || !courseValuationForm.map.suppressRedundantHoursTypes}">			
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.weightSecondTimeEnrolledStudentsPerTheoShift"/>
		</td>
		<td>
			<html:radio property="weightSecondTimeEnrolledStudentsPerTheoShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text property="weightSecondTimeEnrolledStudentsPerTheoShiftManual" size="3" maxlength="3"/>
		</td>
		<td>

		</td>
		<td>
			<html:radio property="weightSecondTimeEnrolledStudentsPerTheoShiftType" value="<%= ValuationValueType.OMISSION_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="valuationPhase.weightSecondTimeEnrolledStudentsPerTheoShift"/>
			</html:radio>
		</td>
		<td>

		</td>
		<td>

		</td>
	</tr>
</c:if>
<c:if test="${selectedCourseValuation.theoreticalHoursPerShift <= 0.0 && courseValuationForm.map.suppressRedundantHoursTypes}">
	<html:hidden property="weightSecondTimeEnrolledStudentsPerTheoShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="weightSecondTimeEnrolledStudentsPerTheoShiftManual" value="0.0"/>	
</c:if>


<c:if test="${selectedCourseValuation.praticalHoursPerShift > 0.0 || !courseValuationForm.map.suppressRedundantHoursTypes}">						
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.weightSecondTimeEnrolledStudentsPerPratShift"/>
		</td>
		<td>
			<html:radio property="weightSecondTimeEnrolledStudentsPerPratShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text property="weightSecondTimeEnrolledStudentsPerPratShiftManual" size="3" maxlength="3"/>
		</td>
		<td>

		</td>
		<td>
			<html:radio property="weightSecondTimeEnrolledStudentsPerPratShiftType" value="<%= ValuationValueType.OMISSION_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="valuationPhase.weightSecondTimeEnrolledStudentsPerPratShift"/>
			</html:radio>
		</td>
		<td>

		</td>
		<td>

		</td>
	</tr>
</c:if>
<c:if test="${selectedCourseValuation.praticalHoursPerShift <= 0.0 && courseValuationForm.map.suppressRedundantHoursTypes}">
	<html:hidden property="weightSecondTimeEnrolledStudentsPerPratShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="weightSecondTimeEnrolledStudentsPerPratShiftManual" value="0.0"/>	
</c:if>


<c:if test="${selectedCourseValuation.theoPratHoursPerShift > 0.0 || !courseValuationForm.map.suppressRedundantHoursTypes}">							
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.weightSecondTimeEnrolledStudentsPerTheoPratShift"/>
		</td>
		<td>
			<html:radio property="weightSecondTimeEnrolledStudentsPerTheoPratShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text property="weightSecondTimeEnrolledStudentsPerTheoPratShiftManual" size="3" maxlength="3"/>
		</td>
		<td>

		</td>
		<td>
			<html:radio property="weightSecondTimeEnrolledStudentsPerTheoPratShiftType" value="<%= ValuationValueType.OMISSION_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="valuationPhase.weightSecondTimeEnrolledStudentsPerTheoPratShift"/>
			</html:radio>
		</td>
		<td>

		</td>
		<td>

		</td>
	</tr>
</c:if>
<c:if test="${selectedCourseValuation.theoPratHoursPerShift <= 0.0 && courseValuationForm.map.suppressRedundantHoursTypes}">
	<html:hidden property="weightSecondTimeEnrolledStudentsPerTheoPratShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="weightSecondTimeEnrolledStudentsPerTheoPratShiftManual" value="0.0"/>	
</c:if>


<c:if test="${selectedCourseValuation.laboratorialHoursPerShift > 0.0 || !courseValuationForm.map.suppressRedundantHoursTypes}">									
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.weightSecondTimeEnrolledStudentsPerLabShift"/>
		</td>
		<td>
			<html:radio property="weightSecondTimeEnrolledStudentsPerLabShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text property="weightSecondTimeEnrolledStudentsPerLabShiftManual" size="3" maxlength="3"/>
		</td>
		<td>

		</td>
		<td>
			<html:radio property="weightSecondTimeEnrolledStudentsPerLabShiftType" value="<%= ValuationValueType.OMISSION_VALUE.toString() %>">
				<bean:write name="selectedCourseValuation" property="valuationPhase.weightSecondTimeEnrolledStudentsPerLabShift"/>
			</html:radio>
		</td>
		<td>

		</td>
		<td>

		</td>
	</tr>
</c:if>
<c:if test="${selectedCourseValuation.laboratorialHoursPerShift <= 0.0 && courseValuationForm.map.suppressRedundantHoursTypes}">
	<html:hidden property="weightSecondTimeEnrolledStudentsPerLabShiftType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="weightSecondTimeEnrolledStudentsPerLabShiftManual" value="0.0"/>	
</c:if>


<c:if test="${selectedCourseValuation.theoreticalHoursPerShift > 0.0 || !courseValuationForm.map.suppressRedundantHoursTypes}">							
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.theoreticalHours"/>
			&nbsp;(<bean:write name="selectedCourseValuation" property="theoreticalHoursPerShift"/>h
			<bean:message key="label.teacherServiceDistribution.byShift"/>)
		</td>
		<td>
			<html:radio property="theoreticalHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>">
			</html:radio>
			<html:text property="theoreticalHoursManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio property="theoreticalHoursType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="selectedCourseValuation" property="realTheoreticalHoursLastYear"/></fmt:formatNumber>
			</html:radio>
		</td>
		<td>

		</td>
		<td>
			<html:radio property="theoreticalHoursType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="selectedCourseValuation" property="realTheoreticalHours"/></fmt:formatNumber>
			</html:radio>
		</td>
		<td>
			<html:radio property="theoreticalHoursType" value="<%= ValuationValueType.CALCULATED_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="selectedCourseValuation" property="theoreticalHoursCalculated"/></fmt:formatNumber>
			</html:radio>
		</td>
	</tr>
</c:if>
<c:if test="${selectedCourseValuation.theoreticalHoursPerShift <= 0.0 && courseValuationForm.map.suppressRedundantHoursTypes}">
	<html:hidden property="theoreticalHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="theoreticalHoursManual" value="0.0"/>	
</c:if>


<c:if test="${selectedCourseValuation.praticalHoursPerShift > 0.0 || !courseValuationForm.map.suppressRedundantHoursTypes}">									
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.praticalHours"/>
			&nbsp;(<bean:write name="selectedCourseValuation" property="praticalHoursPerShift"/>h
			<bean:message key="label.teacherServiceDistribution.byShift"/>)
		</td>
		<td>
			<html:radio property="praticalHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text property="praticalHoursManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio property="praticalHoursType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="selectedCourseValuation" property="realPraticalHoursLastYear"/></fmt:formatNumber>
			</html:radio>
		</td>
		<td>

		</td>
		<td>
			<html:radio property="praticalHoursType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="selectedCourseValuation" property="realPraticalHours"/></fmt:formatNumber>
			</html:radio>
		</td>
		<td>
			<html:radio property="praticalHoursType" value="<%= ValuationValueType.CALCULATED_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="selectedCourseValuation" property="praticalHoursCalculated"/></fmt:formatNumber>
			</html:radio>
		</td>
	</tr>
</c:if>
<c:if test="${selectedCourseValuation.praticalHoursPerShift <= 0.0 && courseValuationForm.map.suppressRedundantHoursTypes}">
	<html:hidden property="praticalHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="praticalHoursManual" value="0.0"/>	
</c:if>


<c:if test="${selectedCourseValuation.theoPratHoursPerShift > 0.0 || !courseValuationForm.map.suppressRedundantHoursTypes}">				
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.theoPratHours"/>
			&nbsp;(<bean:write name="selectedCourseValuation" property="theoPratHoursPerShift"/>h
			<bean:message key="label.teacherServiceDistribution.byShift"/>)
		</td>
		<td>
			<html:radio property="theoPratHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text property="theoPratHoursManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio property="theoPratHoursType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="selectedCourseValuation" property="realTheoPratHoursLastYear"/></fmt:formatNumber>
			</html:radio>
		</td>
		<td>

		</td>
		<td>
			<html:radio property="theoPratHoursType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="selectedCourseValuation" property="realTheoPratHours"/></fmt:formatNumber>
			</html:radio>
		</td>
		<td>
			<html:radio property="theoPratHoursType" value="<%= ValuationValueType.CALCULATED_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="selectedCourseValuation" property="theoPratHoursCalculated"/></fmt:formatNumber>
			</html:radio>
		</td>
	</tr>
</c:if>
<c:if test="${selectedCourseValuation.theoPratHoursPerShift <= 0.0 && courseValuationForm.map.suppressRedundantHoursTypes}">
	<html:hidden property="theoPratHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="theoPratHoursManual" value="0.0"/>	
</c:if>


<c:if test="${selectedCourseValuation.laboratorialHoursPerShift > 0.0 || !courseValuationForm.map.suppressRedundantHoursTypes}">	
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.laboratorialHours"/>
			&nbsp;(<bean:write name="selectedCourseValuation" property="laboratorialHoursPerShift"/>h
			<bean:message key="label.teacherServiceDistribution.byShift"/>)
		</td>
		<td>
			<html:radio property="laboratorialHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"></html:radio>
			<html:text property="laboratorialHoursManual" size="3" maxlength="4"/>
		</td>
		<td>
			<html:radio property="laboratorialHoursType" value="<%= ValuationValueType.LAST_YEAR_REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="selectedCourseValuation" property="realLaboratorialHoursLastYear"/></fmt:formatNumber>
			</html:radio>
		</td>
		<td>

		</td>
		<td>
			<html:radio property="laboratorialHoursType" value="<%= ValuationValueType.REAL_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="selectedCourseValuation" property="realLaboratorialHours"/></fmt:formatNumber>
			</html:radio>
		</td>
		<td>
			<html:radio property="laboratorialHoursType" value="<%= ValuationValueType.CALCULATED_VALUE.toString() %>">
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"><bean:write name="selectedCourseValuation" property="laboratorialHoursCalculated"/></fmt:formatNumber>
			</html:radio>
		</td>
	</tr>
</c:if>
<c:if test="${selectedCourseValuation.laboratorialHoursPerShift <= 0.0 && courseValuationForm.map.suppressRedundantHoursTypes}">
	<html:hidden property="laboratorialHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>"/>
	<html:hidden property="laboratorialHoursManual" value="0.0"/>	
</c:if>

<logic:equal name="selectedCourseValuationType" value="<%= CourseValuationType.CURRICULAR_COURSE_VALUATION_GROUP.toString() %>">
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.usingCurricularCourseValuations"/>
		</td>
		<td colspan=4>
		</td>
		<td>
			<html:checkbox property="usingCurricularCourseValuations"/>
		</td>
	</tr>
	
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.associatedCurricularCourse"/>
		</td>
		<td colspan=5>
			<table>
			<tr>
			<logic:iterate name="selectedCurricularCourseValuationGroup" property="curricularCourseValuations" id="curricularCourseValuation">
				<bean:define id="curricularCourseValuation" name="curricularCourseValuation"/>
			<td>	
				<html:multibox property="curricularCourseValuationArray" onclick='<%= "this.form.method.value='removeCurricularCourseValuationFromValuationGroup'; this.form.curricularCourseValuation.value=" + ((CurricularCourseValuation) curricularCourseValuation).getIdInternal() + ";this.form.submit()" %>' >
					<bean:write name="curricularCourseValuation" property="idInternal"/>
				</html:multibox>
				<bean:write name="curricularCourseValuation" property="valuationCurricularCourse.degreeCurricularPlan.degree.sigla"/>
			</td>
			</logic:iterate>
			</tr>
			</table>
		</td>					
	</tr>
	<logic:notEmpty name="availableCurricularCourseValuationsToGroup">
	<tr class='tdleft'>
		<td class='courses'>
			<bean:message key="label.teacherServiceDistribution.availableCurricularCoursesToAssociate"/>
		</td>
		<td colspan=5>
			<table>
			<tr>
			<logic:iterate name="availableCurricularCourseValuationsToGroup" id="curricularCourseValuation">
				<bean:define id="curricularCourseValuation" name="curricularCourseValuation"/>
			<td>	
				<html:multibox property="nonAssociatedCurricularCourseValuationArray" onclick='<%= "this.form.method.value='addCurricularCourseValuationFromValuationGroup'; this.form.curricularCourseValuation.value=" + ((CurricularCourseValuation) curricularCourseValuation).getIdInternal() + ";this.form.submit()" %>'>
					<bean:write name="curricularCourseValuation" property="idInternal"/>
				</html:multibox>
				<bean:write name="curricularCourseValuation" property="valuationCurricularCourse.degreeCurricularPlan.degree.sigla"/>
			</td>
			</logic:iterate>
			</tr>
			</table>
		</td>					
	</tr>
	</logic:notEmpty>
</logic:equal>
	
</table>

<html:button property="" onclick="this.form.method.value='setCourseValuation'; this.form.page.value=1; this.form.submit();">
	<bean:message key="label.teacherServiceDistribution.valuate"/>
</html:button>
<br/>
<span class="error">
	<html:errors/>
</span>
<br/>
</logic:notPresent>
</logic:notEqual>
</logic:notPresent>

<logic:present name="notAvailableValuationCompetenceCourses">
	<span class="error">
		<bean:message key="label.teacherServiceDistribution.noCompetenceCoursesForExecutionPeriod"/>
	</span>
	<br/>
	<br/>
</logic:present>

<logic:notEmpty name="courseValuationForm" property="courseValuationViewLink">
	<bean:define id="courseValuationID" name="courseValuationForm" property="courseValuationViewLink"/>
	<html:link page='<%= "/teacherServiceDistributionValuation.do?method=prepareForTeacherServiceDistributionValuation&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() 
	+ "#" + courseValuationID %>'>
		<bean:message key="link.teacherServiceDistribution.backToteacherServiceDistributionVisualization"/>
	</html:link>
</logic:notEmpty>
<logic:empty name="courseValuationForm" property="courseValuationViewLink">
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
		<bean:message key="link.back"/>
	</html:link>
</logic:empty>

</html:form>
