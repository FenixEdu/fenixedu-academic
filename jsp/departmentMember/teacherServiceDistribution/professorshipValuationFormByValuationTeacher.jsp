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

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.professorshipValuationService"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
			<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
		<%--	(<bean:write name="teacherServiceDistribution" property="executionPeriod.semester"/>�<bean:message key="label.common.courseSemester"/> --%>
			<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.professorshipValuationService"/>
	</em>
</p>

<html:form action="/professorshipValuation">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherServiceDistribution" property="teacherServiceDistribution"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.professorshipValuation" property="professorshipValuation"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributionViewAnchor" property="distributionViewAnchor"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>

<p class="mbottom15">
	<html:link href="javascript:document.forms[0].method.value='loadProfessorshipValuations'; document.forms[0].viewType.value=1; document.forms[0].submit()">
		<bean:message key="label.teacherService.navigateByCourse"/>
	</html:link> | 
	<b><bean:message key="label.teacherService.navigateByTeacher"/></b>
</p>

<p>
	<bean:message key="label.teacherServiceDistribution.ValuationGrouping"/>:<br/>
	<html:select property="valuationGrouping" onchange="this.form.method.value='loadValuationGrouping'; this.form.submit();">
		<html:options collection="valuationGroupingOptionEntryList" property="idInternal" labelProperty="name"/>
	</html:select>
</p>


<logic:notPresent name="notAvailableValuationTeachers">
<p>
	<bean:message key="label.teacherServiceDistribution.valuationTeacher"/>:<br/>
	<html:select property="valuationTeacher" onchange="this.form.method.value='loadProfessorshipValuations'; this.form.submit();">
		<html:options collection="valuationTeacherList" property="idInternal" labelProperty="name"/>
	</html:select>
</p>


<p class="mtop2 mbottom05">
	<b><bean:message key="label.teacherServiceDistribution.associatedCourses"/>:</b>
</p>

<table class='tstyle4 mtop05'>
	<tr>
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
	<tr>
		<td style="width: 300px;">
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
			<html:link href='<%= "javascript:document.forms[0].method.value='removeProfessorshipValuation'; document.forms[0].professorshipValuation.value=" + ((ProfessorshipValuation) professorshipValuation).getIdInternal().toString() + ";document.forms[0].submit();" %>' >
				<bean:message key="label.teacherServiceDistribution.delete"/>
			</html:link>
		</td>
	</tr>
</logic:iterate>
<logic:equal name="selectedValuationTeacher" property="usingExtraCredits" value="true">
	<tr>
		<td>
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
	<tr>
		<td>
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
	<tr>
		<td>
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

<p class="mbottom05">
	<b><bean:message key="label.teacherServiceDistribution.extraCredits"/>:</b>
</p>

<table class='tstyle5 thlight tdcenter mtop05'>
	<tr>
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


<table class='tstyle5 thlight thright'>
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.semester"/>:
		</th>
		<td>
			<html:select property="executionPeriod" onchange="this.form.method.value='loadExecutionPeriod'; this.form.submit();">
				<html:options collection="executionPeriodList" property="idInternal" labelProperty="semester"/>
			</html:select>
		</td>
	</tr>
	<logic:notPresent name="notAvailableCompetenceCourses">
		<tr>
			<th>
				<bean:message key="label.teacherServiceDistribution.competenceCourse"/>:
			</th>
			<td>
				<html:select property="valuationCompetenceCourse" onchange="this.form.method.value='loadValuationCompetenceCourse'; this.form.submit();">
					<html:options collection="valuationCompetenceCourseList" property="idInternal" labelProperty="name"/>
				</html:select>									
			</td>
		</tr>

		<logic:equal name="selectedCourseValuationType" value="<%= CourseValuationType.CURRICULAR_COURSE_VALUATION.toString() %>">
			<tr>
				<th>
					<bean:message key="label.teacherServiceDistribution.curricularCourse"/>:
				</th>
				<td>
					<html:select property="valuationCurricularCourse" onchange="this.form.method.value='loadProfessorshipValuations'; this.form.submit();">
						<html:options collection="valuationCurricularCourseList" property="idInternal" labelProperty="degreeCurricularPlan.degree.name"/>
					</html:select>							
				</td>
			</tr>
		</logic:equal>
		<logic:equal name="selectedCourseValuationType" value="<%= CourseValuationType.CURRICULAR_COURSE_VALUATION_GROUP.toString() %>">
			<th>
				<bean:message key="label.teacherServiceDistribution.curricularCourseValuationGroup"/>:
			</th>
			<td>
				<html:select property="curricularCourseValuationGroup" onchange="this.form.method.value='loadProfessorshipValuations'; this.form.submit();">
					<html:options collection="curricularCourseValuationGroupList" property="idInternal" labelProperty="groupName"/>
				</html:select>							
			</td>
		</logic:equal>
	</logic:notPresent>
	</tr>
</table>

<logic:notPresent name="notAvailableCompetenceCourses">



<logic:notEqual name="selectedCourseValuationType" value="<%= CourseValuationType.NOT_DETERMINED.toString() %>">
<logic:notPresent name="courseValuationNotSelected">

<p class="mbottom05">
	<b><bean:message key="label.teacherServiceDistribution.associateTeacherToCourse"/>:</b>
</p>

<table class='tstyle5 thlight thright mtop05'>
	<tr>
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
	<tr>
		<td>
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
	<tr>
		<td>
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
	<tr>
		<td>
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
	<tr>
		<td>
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

<p>
	<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='setProfessorshipValuation'; this.form.page.value='1'; this.form.submit();">
		<bean:message key="label.teacherServiceDistribution.assign"/>
	</html:button>
</p>

</logic:notPresent>
</logic:notEqual>
<logic:equal name="selectedCourseValuationType" value="<%= CourseValuationType.NOT_DETERMINED.toString() %>">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.theoPratHoursManual" property="theoPratHoursManual" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.praticalHoursManual" property="praticalHoursManual" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.theoreticalHoursManual" property="theoreticalHoursManual" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.laboratorialHoursManual" property="laboratorialHoursManual" value="0"/>
	<p>
		<span class="error0">
			<bean:message key="label.teacherServiceDistribution.nonValuatedCourse"/>
		</span>
	</p>
</logic:equal>

</logic:notPresent>
<logic:present name="notAvailableCompetenceCourses">
	<p>
		<span class="error0">
			<bean:message key="label.teacherServiceDistribution.noCompetenceCoursesForExecutionPeriod"/>
		</span>
	</p>
</logic:present>

</logic:notPresent>
<logic:present name="notAvailableValuationTeachers">
	<p>
		<span class="error">
			<bean:message key="label.teacherServiceDistribution.notExistsValuationTeachers"/>
		</span>
	</p>
</logic:present>

<p>
<span class="error0">
	<html:errors/>
</span>
</p>

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


