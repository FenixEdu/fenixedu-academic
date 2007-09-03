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

<p>
<b><bean:message key="label.teacherService.navigateByCourse"/> </b> |
<html:link href="javascript:document.forms[0].method.value='loadProfessorshipValuations'; document.forms[0].viewType.value=0; document.forms[0].submit()">
	<bean:message key="label.teacherService.navigateByTeacher"/> 
</html:link>
</p>

<table class="tstyle5 thlight thright thmiddle">
	<tr>
		<th style="width: 100px;">
			<bean:message key="label.teacherServiceDistribution.ValuationGrouping"/>:
		</th>
		<td>
			<html:select property="valuationGrouping" onchange="this.form.method.value='loadValuationGrouping'; this.form.submit();">
				<html:options collection="valuationGroupingOptionEntryList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
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
</table>

<logic:notPresent name="notAvailableCompetenceCourses">
<table class="tstyle5 thlight thright thmiddle">
	<tr>
		<th style="width: 100px;">
			<bean:message key="label.teacherServiceDistribution.competenceCourse"/>:
		</th>
		<td>
			<html:select property="valuationCompetenceCourse" onchange="this.form.method.value='loadValuationCompetenceCourse'; this.form.submit();">
				<html:options collection="valuationCompetenceCourseList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<logic:equal name="selectedCourseValuationType" value="<%= CourseValuationType.CURRICULAR_COURSE_VALUATION.toString() %>">
			<th>
				<bean:message key="label.teacherServiceDistribution.curricularCourse"/>:
			</th>
			<td>
				<html:select property="valuationCurricularCourse" onchange="this.form.method.value='loadProfessorshipValuations'; this.form.submit();">
					<html:options collection="valuationCurricularCourseList" property="idInternal" labelProperty="degreeCurricularPlan.degree.name"/>
				</html:select>							
			</td>
		</logic:equal>
	</tr>
	<tr>			
		<logic:equal name="selectedCourseValuationType" value="<%= CourseValuationType.CURRICULAR_COURSE_VALUATION_GROUP.toString() %>">
			<th>
				<bean:message key="label.teacherServiceDistribution.curricularCourseValuationGroup"/>:</th>
			</th>
			<td>
				<html:select property="curricularCourseValuationGroup" onchange="this.form.method.value='loadProfessorshipValuations'; this.form.submit();">
					<html:options collection="curricularCourseValuationGroupList" property="idInternal" labelProperty="groupName"/>
				</html:select>							
			</td>
		</logic:equal>
	</tr>
</table>



<logic:notEqual name="selectedCourseValuationType" value="<%= CourseValuationType.NOT_DETERMINED.toString() %>">
<logic:notPresent name="courseValuationNotSelected">

<p>
	<b><bean:message key="label.teacherServiceDistribution.associatedTeachers"/>:</b>
</p>

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
			<html:link href='<%= "javascript:document.forms[0].method.value='removeProfessorshipValuation'; document.forms[0].professorshipValuation.value=" + ((ProfessorshipValuation) professorshipValuation).getIdInternal().toString() + ";document.forms[0].submit();" %>' >
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

<logic:notPresent name="notAvailableValuationTeachers">

<p>
	<b><bean:message key="label.teacherServiceDistribution.valuationTeacher"/>:</b>
</p>

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

<p>
	<b><bean:message key="label.teacherServiceDistribution.associateTeacherToCourse"/>:</b>
</p>


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
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.theoreticalHoursType" property="theoreticalHoursType" value="<%= ValuationValueType.MANUAL_VALUE.toString() %>">
			</html:radio>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoreticalHoursManual" property="theoreticalHoursManual" size="3" maxlength="4" />
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
	<tr class='right'>
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
	<tr class='right'>
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
	<tr class='right'>
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

<p>
	<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='setProfessorshipValuation'; this.form.page.value='1'; this.form.submit();">
		<bean:message key="label.teacherServiceDistribution.assign"/>
	</html:button>
</p>

<p>
	<span class="error0">
		<html:errors/>
	</span>
</p>

</logic:notPresent>


<logic:present name="notAvailableValuationTeachers">
	<p>
		<span class="error0">
			<bean:message key="label.teacherServiceDistribution.notExistsValuationTeachers"/>
		</span>
	</p>
</logic:present>


</logic:notPresent>
</logic:notEqual>
<logic:equal name="selectedCourseValuationType" value="<%= CourseValuationType.NOT_DETERMINED.toString() %>">
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


<logic:notEqual name="professorshipValuationForm" property="distributionViewAnchor" value="0">
	<bean:define id="courseValuationID" name="professorshipValuationForm" property="distributionViewAnchor"/>
	<html:link page='<%= "/teacherServiceDistributionValuation.do?method=prepareForTeacherServiceDistributionValuation&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() 
	+ "#" + courseValuationID %>'>
		<bean:message key="link.teacherServiceDistribution.backToteacherServiceDistributionVisualization"/>
	</html:link>
</logic:notEqual>

<logic:equal name="professorshipValuationForm" property="distributionViewAnchor" value="0">
	<ul class="mtop15">
		<li>
			<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
				<bean:message key="link.back"/>
			</html:link>
		</li>
	</ul>
</logic:equal>


</html:form>
