<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<html:xhtml/>

<bean:define id="registration" name="registration" type="org.fenixedu.academic.domain.student.Registration"/>

<h2><bean:message bundle="STUDENT_RESOURCES" key="message.student.shift.enrollment" /></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<logic:notPresent name="registration">
	<p>
		<span class="error"><!-- Error messages go here --><bean:message bundle="STUDENT_RESOURCES" key="error.notAuthorized.ShiftEnrollment" /></span>
	</p>
</logic:notPresent>

<logic:present name="registration">

<bean:define id="registrationOID" name="registration" property="externalId" />
<bean:define id="executionSemesterID" name="executionSemesterID" type="java.lang.String" />

<div>
	<bean:define id="link">proceedToShiftEnrolment</bean:define>
	<logic:notPresent name="ram">

		<div class="infoop2">
			<p><bean:message bundle="STUDENT_RESOURCES" key="message.shift.enrollement.resume.item1"/> <html:link page="<%= "/studentEnrollmentManagement.do?method=prepare" %>"><bean:message bundle="STUDENT_RESOURCES" key="message.warning.student.enrolmentClasses.Fenix" /></html:link></p>
			<p><bean:message bundle="STUDENT_RESOURCES" key="message.shift.enrollement.resume.item2"/> <html:link page="<%= "/studentShiftEnrollmentManager.do?method=start&selectCourses=true&registrationOID=" + registrationOID  + "&executionSemesterID=" + executionSemesterID %>"><bean:message bundle="STUDENT_RESOURCES" key="message.warning.student.enrolmentClasses.notEnroll.chooseCourse.link" /></html:link></p>
		</div>


		<ul class="mtop1 mbottom2">
			<li>
				<html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" + registrationOID.toString() + "&executionSemesterID=" + executionSemesterID %>">
					<strong><bean:message bundle="STUDENT_RESOURCES" key="link.shift.enrollment.item1" /></strong>
				</html:link>
			</li>
			<li>
				<html:link page="<%= "/studentTimeTable.do?method=showTimeTable" + "&executionSemesterID=" + executionSemesterID %>" target="_blank" paramId="registrationId" paramName="registrationOID">
					<strong><bean:message bundle="STUDENT_RESOURCES" key="link.shift.enrollment.item2" /></strong>
				</html:link>
			</li>
			<li>
				<html:link href="<%= request.getContextPath() + "/student/showStudentPortal.do" %>"><strong><bean:message bundle="STUDENT_RESOURCES" key="link.shift.enrollment.item3" /></strong></html:link>
			</li>
		</ul>
	</logic:notPresent>


	<logic:present name="openedEnrolmentPeriodsSemesters">		
		<ul class="nav nav-tabs">
			<logic:iterate id="period" name="openedEnrolmentPeriodsSemesters">				
				<logic:equal name="executionSemesterID" value="${period.externalId}">
					<li role="presentation" class="active"><a href="#">${period.qualifiedName}</a></li>
				</logic:equal>
				<logic:notEqual name="executionSemesterID" value="${period.externalId}">
					<li role="presentation">							
						<html:link action="studentShiftEnrollmentManager.do?method=prepareShiftEnrollment&registrationOID=${registration.externalId}&executionSemesterID=${period.externalId}">
							${period.qualifiedName}
						</html:link>
					</li>
				</logic:notEqual>
			</logic:iterate>	
		</ul>			
	</logic:present>		

	<div style="background: #fafafa; border: 1px solid #ccc; padding: 0.5em 1em;">

	<logic:present name="studentShifts">
		<bean:define id="numberCourseUnenrolledShifts" name="numberOfExecutionCoursesHavingNotEnroledShifts"/>
		<logic:lessEqual name="numberCourseUnenrolledShifts" value="0">
			<p><span class="t_success"><strong><bean:message bundle="STUDENT_RESOURCES" key="message.student.shiftEnrollment.confirmation" /></strong></span></p>
		</logic:lessEqual>
		
		<logic:greaterThan  name="numberCourseUnenrolledShifts" value="0">
			<p><span class="t_warning"><strong><bean:message bundle="STUDENT_RESOURCES" key="message.student.shiftEnrollment.lacksCourses" arg0="<%= numberCourseUnenrolledShifts.toString() %>"/></strong></span></p>
		</logic:greaterThan>
	</logic:present>


	<p class="mtop15 mbottom05"><strong><bean:message bundle="STUDENT_RESOURCES" key="message.shift.enrollement.curricular"/></strong></p>
	
		<logic:notEmpty name="shiftsToEnrolFromEnroledExecutionCourses">
		<table class="tstyle1 mvert05">
			<tr>
				<th><bean:message bundle="STUDENT_RESOURCES" key="label.curricular.course.name"/></th>
				<th style="width: 5%;" title="Teórica"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.theoric"/></th>
				<th style="width: 5%;" title="Prática"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.pratic"/></th>
				<th style="width: 5%;" title="Laboratório"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.laboratory"/></th>
				<th style="width: 5%;" title="Teórico-prática"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.theoricoPratic"/></th>
				<th style="width: 5%;" title="<bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.description.fieldWork"/>"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.fieldWork"/></th>
				<th style="width: 5%;" title="<bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.description.problems"/>"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.problems"/></th>
				<th style="width: 5%;" title="<bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.description.seminary"/>"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.seminary"/></th>
				<th style="width: 5%;" title="<bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.description.trainingPeriod"/>"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.trainingPeriod"/></th>
				<th style="width: 5%;" title="<bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.description.tutorialOrientation"/>"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.tutorialOrientation"/></th>

			</tr>			
			<logic:iterate id="shiftToEnrol" name="shiftsToEnrolFromEnroledExecutionCourses" type="org.fenixedu.academic.dto.ShiftToEnrol">
				<bean:define id="executionCourseID" name="shiftToEnrol" property="executionCourse.externalId"/>
				<tr>
					<td class="disciplina">
						<bean:write name="shiftToEnrol" property="executionCourse.nome"/> - <bean:write name="shiftToEnrol" property="executionCourse.sigla"/>
					</td>
					
					<logic:notEmpty name="shiftToEnrol" property="theoricType">
						<logic:notEmpty name="shiftToEnrol" property="theoricShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="theoricShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="theoricType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="shiftToEnrol" property="praticType">
						<logic:notEmpty name="shiftToEnrol" property="praticShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="praticShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="praticType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="shiftToEnrol" property="laboratoryType">
						<logic:notEmpty name="shiftToEnrol" property="laboratoryShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="laboratoryShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="laboratoryType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="shiftToEnrol" property="theoricoPraticType">
						<logic:notEmpty name="shiftToEnrol" property="theoricoPraticShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="theoricoPraticShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="theoricoPraticType">
						<td></td>
					</logic:empty>																				

					<logic:notEmpty name="shiftToEnrol" property="fieldWorkType">
						<logic:notEmpty name="shiftToEnrol" property="fieldWorkShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="fieldWorkShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="fieldWorkType">
						<td></td>
					</logic:empty>																				

					<logic:notEmpty name="shiftToEnrol" property="problemsType">
						<logic:notEmpty name="shiftToEnrol" property="problemsShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="problemsShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="problemsType">
						<td></td>
					</logic:empty>																				

					<logic:notEmpty name="shiftToEnrol" property="seminaryType">
						<logic:notEmpty name="shiftToEnrol" property="seminaryShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="seminaryShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="seminaryType">
						<td></td>
					</logic:empty>																				

					<logic:notEmpty name="shiftToEnrol" property="trainingType">
						<logic:notEmpty name="shiftToEnrol" property="trainingShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="trainingShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="trainingType">
						<td></td>
					</logic:empty>																				

					<logic:notEmpty name="shiftToEnrol" property="tutorialOrientationType">
						<logic:notEmpty name="shiftToEnrol" property="tutorialOrientationShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="tutorialOrientationShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="tutorialOrientationType">
						<td></td>
					</logic:empty>																				
				</tr>
			</logic:iterate>
			</table>
		</logic:notEmpty>
	
					
		<logic:notEmpty name="shiftsToEnrolFromUnenroledExecutionCourses">
		<p><strong><bean:message bundle="STUDENT_RESOURCES" key="message.shift.enrollement.extra"/></strong></p>
		<table class="tstyle1 mvert05">
			<tr>
				<th><bean:message bundle="STUDENT_RESOURCES" key="label.curricular.course.name"/></h>
				<th style="width: 5%;" title="Teórica"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.theoric"/></th>
				<th style="width: 5%;" title="Prática"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.pratic"/></th>
				<th style="width: 5%;" title="Laboratório"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.laboratory"/></th>
				<th style="width: 5%;" title="Teórico-prática"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.theoricoPratic"/></th>
				<th style="width: 5%;" title="<bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.description.fieldWork"/>"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.fieldWork"/></th>
				<th style="width: 5%;" title="<bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.description.problems"/>"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.problems"/></th>
				<th style="width: 5%;" title="<bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.description.seminary"/>"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.seminary"/></th>
				<th style="width: 5%;" title="<bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.description.trainingPeriod"/>"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.trainingPeriod"/></th>
				<th style="width: 5%;" title="<bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.description.tutorialOrientation"/>"><bean:message bundle="STUDENT_RESOURCES" key="label.shiftType.tutorialOrientation"/></th>
			</tr>			
			<logic:iterate id="shiftToEnrol" name="shiftsToEnrolFromUnenroledExecutionCourses" type="org.fenixedu.academic.dto.ShiftToEnrol">
				<bean:define id="executionCourseID" name="shiftToEnrol" property="executionCourse.externalId"/>
				<tr>
					<td class="disciplina">
						<bean:write name="shiftToEnrol" property="executionCourse.nome"/> - <bean:write name="shiftToEnrol" property="executionCourse.sigla"/>
					</td>
					
					<logic:notEmpty name="shiftToEnrol" property="theoricType">
						<logic:notEmpty name="shiftToEnrol" property="theoricShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="theoricShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="theoricType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="shiftToEnrol" property="praticType">
						<logic:notEmpty name="shiftToEnrol" property="praticShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="praticShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="praticType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="shiftToEnrol" property="laboratoryType">
						<logic:notEmpty name="shiftToEnrol" property="laboratoryShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="laboratoryShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="laboratoryType">
						<td></td>
					</logic:empty>
					
					<logic:notEmpty name="shiftToEnrol" property="theoricoPraticType">
						<logic:notEmpty name="shiftToEnrol" property="theoricoPraticShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="theoricoPraticShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="theoricoPraticType">
						<td></td>
					</logic:empty>

					<logic:notEmpty name="shiftToEnrol" property="fieldWorkType">
						<logic:notEmpty name="shiftToEnrol" property="fieldWorkShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="fieldWorkShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="fieldWorkType">
						<td></td>
					</logic:empty>																				

					<logic:notEmpty name="shiftToEnrol" property="problemsType">
						<logic:notEmpty name="shiftToEnrol" property="problemsShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="problemsShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="problemsType">
						<td></td>
					</logic:empty>																				

					<logic:notEmpty name="shiftToEnrol" property="seminaryType">
						<logic:notEmpty name="shiftToEnrol" property="seminaryShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="seminaryShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="seminaryType">
						<td></td>
					</logic:empty>																				

					<logic:notEmpty name="shiftToEnrol" property="trainingType">
						<logic:notEmpty name="shiftToEnrol" property="trainingShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="trainingShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="trainingType">
						<td></td>
					</logic:empty>																				

					<logic:notEmpty name="shiftToEnrol" property="tutorialOrientationType">
						<logic:notEmpty name="shiftToEnrol" property="tutorialOrientationShift">
							<td class="bgdfd acenter"><bean:message bundle="STUDENT_RESOURCES" key="label.shift.enrolled"/></td>
						</logic:notEmpty>
						<logic:empty name="shiftToEnrol" property="tutorialOrientationShift">
							<td class="bgfdd acenter"><html:link page="<%="/studentShiftEnrollmentManagerLookup.do?method=" + link + "&amp;registrationOID=" 
							+ registrationOID.toString() + "&amp;executionCourseID=" + executionCourseID + "&amp;executionSemesterID=" + executionSemesterID %>">
							<bean:message bundle="STUDENT_RESOURCES" key="label.shift.toEnroll"/></html:link></td>
						</logic:empty>
					</logic:notEmpty>
					<logic:empty name="shiftToEnrol" property="tutorialOrientationType">
						<td></td>
					</logic:empty> 

				</tr>														
			</logic:iterate>
			</table>
		</logic:notEmpty>
		
	<p class="mtop05"><em style="color: #777;"><bean:message bundle="STUDENT_RESOURCES" key="message.shift.enrollement.legend"/></em></p>
	
	</div>

	<p class="mtop3 mbottom05">
		<strong><bean:message bundle="STUDENT_RESOURCES" key="message.shift.enrollement.list"/></strong>
	</p>
	
	<logic:present name="studentShifts">	
	
	<table class="tstyle2 thlight mtop05">
		<bean:define id="elem" value="" type="java.lang.String"/>
		<logic:iterate id="shift" name="studentShifts" type="org.fenixedu.academic.domain.Shift">
			<%-- COURSES --%>
			<logic:present name="elem">
				<logic:notEqual name="elem" value="<%=shift.getDisciplinaExecucao().getNome()%>">
					<tr>
						<td class="listClasses-subheader" style="text-align:left; background:#4F82B5" colspan='6' >
							<bean:write name="shift" property="disciplinaExecucao.nome" />
						</td>
					</tr>	
				</logic:notEqual>
			</logic:present>
			<%-- SHIFTS --%>
			<tr>
				<th class="listClasses-header" style="text-align:left" colspan='6'>
					<bean:message bundle="STUDENT_RESOURCES" key="property.turno" />:</b>&nbsp;
					<bean:write name="shift" property="nome" />
					<bean:define id="shiftId" name="shift" property="externalId" />
					-
					<html:link page="<%= "/studentShiftEnrollmentManager.do?method=unEnroleStudentFromShift&amp;registrationOID="
												+ registrationOID.toString()
												+ "&amp;shiftId="
												+ shiftId.toString()
												+ "&amp;executionSemesterID=" + executionSemesterID
												%>">
						<bean:message bundle="STUDENT_RESOURCES" key="link.unenrole.shift" />
					</html:link>
				</th>
			</tr>

			<%-- LESSONS --%>
			<logic:iterate id="lesson" name="shift" property="associatedLessons">
				<tr>
					<td style="text-align:center">
						<bean:message bundle="STUDENT_RESOURCES" key="label.week" />: 
						<bean:write name="lesson" property="occurrenceWeeksAsString" />
					</td>
					<td style="text-align:center">
						<bean:write name="shift" property="shiftTypesPrettyPrint" />								
					</td>
					<td style="text-align:center">
						<bean:write name="lesson" property="diaSemana" />
					</td>
					<td style="text-align:center">
						<dt:format pattern="HH:mm">
							<bean:write name="lesson" property="begin.time" />
						</dt:format>								
						&nbsp;-&nbsp;
						<dt:format pattern="HH:mm">
							<bean:write name="lesson" property="end.time" />
						</dt:format>
					</td>
					<td style="text-align:center">
						<logic:notEmpty name="lesson" property="sala">
							<logic:notEmpty name="lesson" property="sala.name">
								<bean:write name="lesson" property="sala.name" />
							</logic:notEmpty>
						</logic:notEmpty>	
					</td>
				</tr>			
			</logic:iterate>
			<bean:define id="elem" name="shift" property="disciplinaExecucao.nome" type="java.lang.String"/>
		</logic:iterate>
	</logic:present>
			
	<logic:notPresent name="studentShifts">
		<tr>
			<td>
				<span class="error"><!-- Error messages go here --><bean:message bundle="STUDENT_RESOURCES" key="message.warning.student.notYet.enroll" /></span>	
			</td>
		</tr>
	</logic:notPresent>
	</table>

	</div>
	
</logic:present>
