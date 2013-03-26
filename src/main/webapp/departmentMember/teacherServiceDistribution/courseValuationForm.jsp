<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourseType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDValueType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourse" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.tsdCourseService"/></h2>

<bean:define id="tsdProcessId" value="<%= ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>"/>

<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + tsdProcessId %>'>
			<bean:write name="tsdProcess" property="name"/>&nbsp;
			<bean:write name="tsdProcess" property="executionYear.year"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.tsdCourseService"/>	
	</em>
</p>

<html:form action="/tsdCourse">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdProcess" property="tsdProcess"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdCourseViewLink" property="tsdCourseViewLink"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>


<table class="tstyle5 thlight thright thmiddle">
<tr>
	<th style="width: 100px;">
		<bean:message key="label.teacherServiceDistribution.TeacherServiceDistribution"/>:
	</th>
	<td>
		<html:select property="tsd" onchange="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit();">
			<html:options collection="tsdOptionEntryList" property="idInternal" labelProperty="name"/>
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

<logic:notPresent name="notAvailableCompetenceCourses">

<tr>
	<th style="width: 100px;"><bean:message key="label.teacherServiceDistribution.competenceCourse"/>:</th>
	<td>
		<html:select property="competenceCourse" onchange="this.form.method.value='loadCompetenceCourse'; this.form.submit();">
			<html:options collection="competenceCourseList" property="idInternal" labelProperty="name"/>
		</html:select>							

	</td>
</tr>
<tr>
	<th><bean:message key="label.teacherServiceDistribution.valuate"/>:</th>
	<td>
		<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.tsdCourseType" property="tsdCourseType" value="<%= TSDCourseType.COMPETENCE_COURSE_VALUATION.toString() %>" onclick="this.form.method.value='setTSDCourseType'; this.form.submit();"><bean:message key="label.teacherServiceDistribution.tsdCompetenceCourseType"/></html:radio>
		<logic:notPresent name="tsdVirtualCourseGroup">
			&nbsp;&nbsp;
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.tsdCourseType" property="tsdCourseType" value="<%= TSDCourseType.CURRICULAR_COURSE_VALUATION.toString() %>" onclick="this.form.method.value='setTSDCourseType'; this.form.submit();"><bean:message key="label.teacherServiceDistribution.tsdCurricularCourseType"/></html:radio>
			&nbsp;&nbsp;
			<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.tsdCourseType" property="tsdCourseType" value="<%= TSDCourseType.CURRICULAR_COURSE_VALUATION_GROUP.toString() %>" onclick="this.form.method.value='setTSDCourseType'; this.form.submit();"><bean:message key="label.teacherServiceDistribution.tsdCurricularCourseGroupType"/></html:radio>
		</logic:notPresent>
	</td>
</tr>

<logic:equal name="selectedTSDCourseType" value="<%= TSDCourseType.CURRICULAR_COURSE_VALUATION.toString() %>">
<tr>
	<th>
		<bean:message key="label.teacherServiceDistribution.course"/>:
	</th>
	<td>
		<html:select property="tsdCurricularCourse" onchange="this.form.method.value='loadTSDCourses'; this.form.submit();">
			<html:options collection="tsdCurricularCourseList" property="idInternal" labelProperty="degreeName"/>
		</html:select>							
	</td>
</tr>
</logic:equal>

<logic:equal name="selectedTSDCourseType" value="<%= TSDCourseType.CURRICULAR_COURSE_VALUATION_GROUP.toString() %>">
<tr>
	<th>
		<bean:message key="label.teacherServiceDistribution.tsdCurricularCourseGroup"/>:
	</th>
	<td>

		<html:select property="tsdCurricularCourseGroup" onchange="this.form.method.value='loadTSDCourses'; document.forms[0].submit();">
			<html:options collection="tsdCurricularCourseGroupList" property="idInternal" labelProperty="name"/>
		</html:select>
		&nbsp;&nbsp;(
<logic:notEmpty name="tsdCurricularCourseGroupList">
		<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='deleteTSDCurricularCourseGroup'; document.forms[0].submit();">
			<bean:message key="label.teacherServiceDistribution.delete"/>
		</html:link>
		|
</logic:notEmpty>
		<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='prepareForTSDCurricularCourseGroupCreation'; document.forms[0].submit();">
			<bean:message key="label.teacherServiceDistribution.create"/>
		</html:link>)
	</td>
</tr>
</logic:equal>

</table>

<logic:notEqual name="selectedTSDCourseType" value="<%= TSDCourseType.NOT_DETERMINED.toString() %>">
<logic:notPresent name="tsdCourseNotSelected">

<bean:define id="tsdCourseId" name="selectedTSDCourse" property="idInternal"/>
<bean:define id="tsdId" name="selectedTSD" property="idInternal"/>

<br/>
<br/>

<logic:empty name="selectedTSDCourse"property="TSDCurricularLoads">
	<p class="mtop15">
		<em>
			<bean:message key="label.teacherServiceDistribution.noShiftsForTSDCourse"/>
		</em>
	</p>
</logic:empty>
<logic:notEmpty name="selectedTSDCourse" property="TSDCurricularLoads">
		<table class='tstyle4 mtop05 thlight' >
			<tr>
				<th>
					<b><bean:message key="label.teacherServiceDistribution.studentsTotal"/></b>&nbsp;
					(<html:link page='<%= "/tsdCourseValuation.do?method=prepareForTSDCourseValuation&amp;tsd=" + tsdId + "&amp;tsdCourse=" + tsdCourseId + "&amp;forward=" + "courseValuationStudents" %>'> 
						<bean:message key="label.teacherServiceDistribution.valuate"/>
					</html:link>)
				</th>
				<th>
				</th>
			</tr>
			<tr>
				<td class='highlight7'>
					<bean:message key="label.teacherServiceDistribution.firstTimeEnrolledStudents"/>
				</td>
				<td class='aright'>
					<bean:write name="selectedTSDCourse" property="firstTimeEnrolledStudents" />
				</td>
			</tr>
			<tr>
				<td class='highlight7'>
					<bean:message key="label.teacherServiceDistribution.secondTimeEnrolledStudents"/>
				</td>
				<td class='aright'>
					<bean:write name="selectedTSDCourse" property="secondTimeEnrolledStudents" />
				</td>
			</tr>
	<logic:iterate name="selectedTSDCourse" property="sortedTSDCurricularLoads" id="curricularLoad">
		<bean:define id="typeName" name="curricularLoad" property="type.name"/>
			<tr>
				<th>
					<b><bean:message name="curricularLoad" property="type.name"/></b>&nbsp;
					(<bean:message key="label.teacherServiceDistribution.valuate"/>:
					<html:link page='<%= "/tsdCourseValuation.do?method=prepareForTSDCourseValuation&amp;tsd=" + tsdId + "&amp;tsdCourse=" + tsdCourseId + "&amp;forward=" + "courseValuationWeights" + "&amp;shiftType=" + typeName %>'>  
						<bean:message key="label.teacherServiceDistribution.valuateWeights"/>
					</html:link>
					|
					<html:link page='<%= "/tsdCourseValuation.do?method=prepareForTSDCourseValuation&amp;tsd=" + tsdId + "&amp;tsdCourse=" + tsdCourseId + "&amp;forward=" + "courseValuationHours" + "&amp;shiftType=" + typeName %>'>  
						<bean:message key="label.teacherServiceDistribution.valuateHours"/>
					</html:link>)
				</th>
				<th>
				</th>
			</tr>
			<tr>
				<td class='highlight7'>
					<bean:message key="label.teacherServiceDistribution.weightFirstTimeEnrolledStudentsPerShift"/>
				</td>
				<td class='aright'>
					<bean:write name="curricularLoad" property="weightFirstTimeEnrolledStudentsPerShift" />
				</td>
			</tr>
			<tr>
				<td class='highlight7'>
					<bean:message key="label.teacherServiceDistribution.weightSecondTimeEnrolledStudentsPerShift"/>
				</td>
				<td class='aright'>
					<bean:write name="curricularLoad" property="weightSecondTimeEnrolledStudentsPerShift" />
				</td>
			</tr>
			<tr>
				<td class='highlight7'>
					<bean:message key="label.teacherServiceDistribution.studentsPerShift"/>
				</td>
				<td class='aright'>
					<bean:write name="curricularLoad" property="studentsPerShift" />
				</td>
			</tr>
			<tr>
				<td class='highlight7'>
					<bean:message key="label.teacherServiceDistribution.shiftFrequency"/>
				</td>
				<td class='aright'>
					<bean:write name="curricularLoad" property="frequency" />
				</td>
			</tr>
			<tr>
				<td class="highlight7">
					<bean:message key="label.teacherServiceDistribution.shiftDuration"/>
				</td>
				<td class='aright'>
					<bean:write name="curricularLoad" property="hoursPerShift" />
				</td>
			</tr>
			<tr>
				<td class='highlight7'>
						<bean:message key="label.teacherServiceDistribution.numberOfShifts"/>
				</td>
				<td class='aright'>
					<bean:write name="curricularLoad" property="numberOfShifts"/> 
				</td>
		  </tr>
		  <tr>
				<td class='highlight7'>
					<bean:message key="label.teacherServiceDistribution.hoursPerStudent"/>
				</td>
				<td class='aright'>
					<bean:write name="curricularLoad" property="numberOfHoursForStudents"/>
				</td>
		 </tr>
		 <tr>
				<td class='highlight7'>
					<bean:message key="label.teacherServiceDistribution.hoursForTeachers"/>
				</td>
				<td class='aright'>
					<bean:write name="curricularLoad" property="numberOfHoursForTeachers"/>
				</td>
			</tr>
			<tr>
				<td class='highlight7'>
					<bean:message key="label.teacherServiceDistribution.numberOfSchoolClasses"/>
					(<html:link page='<%= "/tsdCourseValuation.do?method=defineSchoolClassCalculationMethod&amp;tsd=" + tsdId + "&amp;tsdCourse=" + tsdCourseId + "&amp;tsdProcessId=" + tsdProcessId + "&amp;shiftType=" + typeName%>'>  
						<bean:message key="label.teacherServiceDistribution.defineSchoolClassCalculationMethod"/>
					</html:link>)
				</td>
				<td class='aright'>
					<bean:write name="curricularLoad" property="numberOfSchoolClasses" /> 
				</td>
			</tr>
			<tr>
				<td class='highlight7'>
					<b><bean:message key="label.teacherServiceDistribution.weeklyHours"/></b>
				</td>
				<td class='aright'>
					<bean:write name="curricularLoad" property="hours" />
				</td>
			</tr>
	</logic:iterate>
		</table>
</logic:notEmpty>

</logic:notPresent>
</logic:notEqual>
</logic:notPresent>

<logic:present name="notAvailableCompetenceCourses">
	</table>
	<p>
		<em>
		<bean:message key="label.teacherServiceDistribution.noCompetenceCoursesForExecutionPeriod"/>
		</em>
	</p>
	<br/>
</logic:present>

<br/>
<logic:notEmpty name="tsdCourseForm" property="tsdCourseViewLink">
	<bean:define id="tsdCourseID" name="tsdCourseForm" property="tsdCourseViewLink"/>
	<html:link page='<%= "/tsdProcessValuation.do?method=prepareForTSDProcessValuation&amp;tsdProcess=" + tsdProcessId 	+ "#" + tsdCourseID %>'>
		<bean:message key="link.teacherServiceDistribution.backTotsdProcessVisualization"/>
	</html:link>
</logic:notEmpty>
<logic:empty name="tsdCourseForm" property="tsdCourseViewLink">
	<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + tsdProcessId %>'>
		<bean:message key="link.back"/>
	</html:link>
</logic:empty>

</html:form>
