<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="java.util.Collection"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.domain.StudentCurricularPlan"%>
<%@page import="net.sourceforge.fenixedu.domain.student.Registration"%>
<%@page import="net.sourceforge.fenixedu.domain.student.StudentCurriculum"%>
<%@page import="net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean"%>
<html:xhtml />

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean"/>
<%
	final Registration registration = ((RegistrationSelectExecutionYearBean) bean).getRegistration();
	request.setAttribute("registration", registration);
	final ExecutionYear executionYear = ((RegistrationSelectExecutionYearBean) bean).getExecutionYear();
	request.setAttribute("executionYear", executionYear);
%>

<ul class="mtop2">
	<li>
	<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="idInternal">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
	</li>
</ul>

<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.idInternal"/>
	<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span style="background-color: #ecf3e1; border-bottom: 1px solid #ccdeb2; padding: 0.4em 0.6em;">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>

<logic:present name="registration" property="ingressionEnum">
<h3 class="mbottom025"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
	</fr:layout>
</fr:view>
</logic:present>


<logic:notPresent name="registration" property="ingressionEnum">
<h3 class="mbottom025"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
	</fr:layout>
</fr:view>
</logic:notPresent>


<fr:edit id="bean" name="bean" schema="registration-select-execution-year" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
	</fr:layout>
</fr:edit>

<logic:notEmpty name="executionYear">
	<%
		final StudentCurriculum studentCurriculum = new StudentCurriculum(registration);
		request.setAttribute("studentCurriculum", studentCurriculum);
		final StudentCurricularPlan studentCurricularPlan = studentCurriculum.getStudentCurricularPlan(executionYear);
		request.setAttribute("studentCurricularPlan", studentCurricularPlan);
		final int enrolmentsCount = registration.getEnrolments(executionYear).size();
		request.setAttribute("enrolmentsCount", enrolmentsCount);
		final Collection<StudentCurriculum.Entry> curriculumEntries = studentCurriculum.getCurriculumEntries(executionYear);
		request.setAttribute("curriculumEntries", curriculumEntries);
		final double totalEctsCredits = studentCurriculum.getTotalEctsCredits(executionYear);
		request.setAttribute("totalEctsCredits", totalEctsCredits);
		final double average = registration.getAverage(executionYear);
		request.setAttribute("average", average);
		final int curricularYear = studentCurriculum.calculateCurricularYear(executionYear);
		request.setAttribute("curricularYear", curricularYear);
	%>

	<p style="padding-top: 1em;">
		<span class="error0">
			<bean:message key="following.info.refers.to.begin.of.execution.year" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</span>
	</p>
	
	<table class="tstyle4 thright thlight">
		<tr>
			<th>
				<bean:message key="label.numberEnroledCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</th>
			<td>
				<bean:write name="enrolmentsCount"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.numberAprovedCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</th>
			<td>
				<bean:size id="curricularEntriesCount" name="curriculumEntries"/>
				<bean:write name="curricularEntriesCount"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.total.ects.credits" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</th>
			<td>
				<bean:write name="totalEctsCredits"/>
			</td>
		</tr>
<%-- 
		<tr>
			<th>
				<bean:message key="average" bundle="STUDENT_RESOURCES"/>
			</th>
			<td>
				<bean:write name="average"/>
			</td>
		</tr>
		<logic:equal name="registration" property="concluded" value="true">
			<%
				final int finalAverage = registration.getFinalAverage();
				request.setAttribute("finalAverage", finalAverage);
			%>
			<tr>
				<th>
					<bean:message key="final.average" bundle="STUDENT_RESOURCES"/>
				</th>
				<td>
					<bean:write name="finalAverage"/>
				</td>
			</tr>
		</logic:equal>
		<tr>
			<th>
				<bean:message key="label.curricular.year" bundle="STUDENT_RESOURCES"/>
			</th>
			<td>
				<bean:write name="curricularYear"/>
			</td>
		</tr>
--%>
	</table>

	<logic:equal name="curricularEntriesCount" value="0">
		<p class="mvert15">
			<span class="warning0"><em>
				<bean:message key="no.approvements" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</em></span>
		</p>	
	</logic:equal>
	<logic:greaterThan name="curricularEntriesCount" value="0">
		<table class="tstyle4">
			<tr>
				<th>
					<bean:message bundle="APPLICATION_RESOURCES" key="label.curricular.course.from.curriculum"/>
				</th>
				<th>
					<bean:message bundle="APPLICATION_RESOURCES" key="label.type.of.aproval"/>
				</th>
				<th colspan="2">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.curricular.course.aproved"/>
				</th>
				<th>
					<bean:message bundle="APPLICATION_RESOURCES" key="label.ects.credits"/>
				</th>
			</tr>
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<tr>
					<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.StudentCurriculum$NotNeedToEnrolEntry">
						<td><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>
						<td><bean:message bundle="APPLICATION_RESOURCES" key="label.not.need.to.enrol"/></td>
						<td colspan="2"></td>
						<td><bean:write name="curriculumEntry" property="ectsCredits"/></td>
					</logic:equal>
					<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.StudentCurriculum$EnrolmentEntry">
						<td><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>
						<td><bean:message bundle="APPLICATION_RESOURCES" key="label.directly.approved"/></td>
						<td colspan="2"><bean:write name="curriculumEntry" property="enrolment.curricularCourse.name"/></td>
						<td><bean:write name="curriculumEntry" property="ectsCredits"/></td>
					</logic:equal>
					<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.StudentCurriculum$EquivalentEnrolmentEntry">
						<bean:define id="numberEntries" name="curriculumEntry" property="entries"/>
						<td rowspan="<%= numberEntries %>"><bean:write name="curriculumEntry" property="curricularCourse.name"/></td>
						<td rowspan="<%= numberEntries %>"><bean:message bundle="APPLICATION_RESOURCES" key="label.equivalency"/></td>
						<logic:iterate id="simpleEntry" name="curriculumEntry" property="entries">
							<logic:equal name="simpleEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.StudentCurriculum$NotNeedToEnrolEntry">
								<td><bean:message bundle="APPLICATION_RESOURCES" key="label.not.need.to.enrol"/></td>
							</logic:equal>
							<logic:equal name="simpleEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.StudentCurriculum$EnrolmentEntry">
								<td><bean:message bundle="APPLICATION_RESOURCES" key="label.directly.approved"/></td>
							</logic:equal>
							<td><bean:write name="simpleEntry" property="curricularCourse.name"/></td>
						</logic:iterate>
						<td rowspan="<%= numberEntries %>"><bean:write name="curriculumEntry" property="ectsCredits"/></td>
					</logic:equal>
					<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.StudentCurriculum$ExtraCurricularEnrolmentEntry">
						<td></td>
						<td><bean:message bundle="APPLICATION_RESOURCES" key="label.extra.curricular.course"/></td>
						<td colspan="2"><bean:write name="curriculumEntry" property="enrolment.curricularCourse.name"/></td>
						<td><bean:write name="curriculumEntry" property="ectsCredits"/></td>
					</logic:equal>
				</tr>
			</logic:iterate>
		</table>
	</logic:greaterThan>

</logic:notEmpty>
