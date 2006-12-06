<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.domain.student.StudentCurriculum"%>
<%@page import="java.util.Collection"%>
<%@page import="net.sourceforge.fenixedu.domain.student.StudentCurriculum.Entry"%>
<%@page import="net.sourceforge.fenixedu.domain.StudentCurricularPlan"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.StudentSearchBeanWithExecutionYear"%>
<html:xhtml />

<logic:present role="DEGREE_ADMINISTRATIVE_OFFICE">

	<h2><bean:message key="link.declarations" /></h2>

	<hr />
	<br />

	<bean:define id="studentsSearchBean" name="studentsSearchBean" type="net.sourceforge.fenixedu.presentationTier.Action.degreeAdministrativeOffice.StudentSearchBeanWithExecutionYear"/>

	<fr:edit id="studentsSearchBean" name="studentsSearchBean" schema="student.StudentsSearchBeanByNumberWithExecutionYear" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>

	<br/>

	<logic:present name="studentsSearchBean" property="number">
		<bean:define id="students" name="studentsSearchBean" property="search"/>
		<bean:size id="studentsSize" name="students"/>
		<logic:equal name="studentsSize" value="0">
			<bean:message key="message.no.student.found"/>
		</logic:equal>
		<logic:equal name="studentsSize" value="1">
			<logic:iterate id="student" name="students">
				<table>
					<tr>
						<td rowspan="3">
							<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode=<bean:write name="student" property="person.idInternal"/></bean:define>
				  			<html:img src="<%= url %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
						</td>
						<td rowspan="3">
						</td>
						<td><bean:message key="label.number"/></td>
						<td><bean:write name="student" property="number"/></td>
					</tr>
					<tr>
						<td><bean:message key="label.person.name"/></td>
						<td><bean:write name="student" property="person.name"/></td>
					</tr>
					<tr>
						<td><bean:message bundle="ENUMERATION_RESOURCES" name="student" property="person.idDocumentType.name"/></td>
						<td><bean:write name="student" property="person.documentIdNumber"/></td>
					</tr>
				</table>
				<br/>
				<logic:iterate id="registration" type="net.sourceforge.fenixedu.domain.student.Registration" name="student" property="registrations">
					<logic:equal name="registration" property="payedTuition" value="true">

						<br/>

						<%
							final ExecutionYear executionYear = ((StudentSearchBeanWithExecutionYear) studentsSearchBean).getExecutionYear();
							request.setAttribute("executionYear", executionYear);
							final StudentCurriculum studentCurriculum = new StudentCurriculum(registration);
							request.setAttribute("studentCurriculum", studentCurriculum);
							final Collection<StudentCurriculum.Entry> curriculumEntries = studentCurriculum.getCurriculumEntries(executionYear);
							request.setAttribute("curriculumEntries", curriculumEntries);
							final StudentCurricularPlan studentCurricularPlan = studentCurriculum.getStudentCurricularPlan(executionYear);
							request.setAttribute("studentCurricularPlan", studentCurricularPlan);
							final double totalEctsCredits = studentCurriculum.getTotalEctsCredits(executionYear);
							request.setAttribute("totalEctsCredits", totalEctsCredits);
							final int numberEnrolments = studentCurricularPlan.countEnrolments(executionYear);
							request.setAttribute("numberEnrolments", numberEnrolments);
						%>

						<bean:message bundle="ENUMERATION_RESOURCES" name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.name"/>
						<bean:message bundle="APPLICATION_RESOURCES" key="label.in"/>
						<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.name"/>
						<br/>
						<bean:write name="studentCurricularPlan" property="startDateYearMonthDay"/>
						<br/>
						<bean:message key="label.numberAprovedCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/>
						<bean:size id="curricularEntiesCount" name="curriculumEntries"/>
						<bean:write name="curricularEntiesCount"/>
						<br/>
						<bean:message key="label.total.ects.credits" bundle="ACADEMIC_OFFICE_RESOURCES"/>
						<bean:write name="totalEctsCredits"/>
						<br/>
						<br/>
						<logic:present name="executionYear">
						<logic:greaterThan name="numberEnrolments" value="0">
							<bean:define id="url" type="java.lang.String">/declarations.do?method=registrationDeclaration&amp;registrationID=<bean:write name="registration" property="idInternal"/>&amp;executionYearID=<bean:write name="executionYear" property="idInternal"/></bean:define>
							<html:link action="<%= url %>"><bean:message key="link.declaration.registration.with.curricular.year.and.number.enroled.courses"/></html:link>
							<br/>
							<br/>
						</logic:greaterThan>
						<logic:lessEqual name="numberEnrolments" value="0">
							<span class="error"><bean:message key="message.student.has.no.enrolments"/></span>
						</logic:lessEqual>
						<logic:greaterThan name="numberEnrolments" value="0">
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
						</logic:present>
						<br/>
					</logic:equal>
					<logic:notEqual name="registration" property="payedTuition" value="true">
						<span class="error"><bean:message key="message.student.has.not.payed.all.tuition"/></span>
					</logic:notEqual>
				</logic:iterate>
			</logic:iterate>
		</logic:equal>
		<logic:greaterThan name="studentsSize" value="1">
			<logic:iterate id="student" name="students">
				<bean:write name="student" property="number"/>
				<bean:write name="student" property="person.name"/>
				<br/>
			</logic:iterate>
		</logic:greaterThan>
	</logic:present>

</logic:present>
