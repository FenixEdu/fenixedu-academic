<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<logic:present role="DEGREE_ADMINISTRATIVE_OFFICE">

	<h2><bean:message key="link.declarations" /></h2>

	<hr />
	<br />

	<fr:edit name="studentsSearchBean" schema="student.StudentsSearchBeanByNumber" >
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
				<logic:iterate id="registration" name="student" property="registrations">
					<logic:equal name="registration" property="payedTuition" value="true">
						<logic:present name="registration" property="activeStudentCurricularPlan">
							<bean:define id="studentCurricularPlan" name="registration" property="activeStudentCurricularPlan"/>
							<bean:define id="numberCompetedCourses" name="studentCurricularPlan" property="countCurrentEnrolments"/>
							<logic:greaterThan name="numberCompetedCourses" value="0">
								<br/>
								<bean:define id="url" type="java.lang.String">/declarations.do?method=registrationDeclaration&amp;registrationID=<bean:write name="registration" property="idInternal"/></bean:define>
								<html:link action="<%= url %>"><bean:message key="link.declaration.registration.with.curricular.year.and.number.enroled.courses"/></html:link>
								<br/>

								<bean:define id="curricularCourses" name="registration" property="curricularCoursesOfCurrentCurricularPlanThatTheStudentHasConcluded"/>
								<br/>
								<bean:size id="numberAprovedCurricularCourses" name="curricularCourses"/>
								<bean:message key="label.numberAprovedCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/>
								<bean:write name="numberAprovedCurricularCourses"/>
								<br/>
								<bean:message key="label.total.ects.credits" bundle="ACADEMIC_OFFICE_RESOURCES"/>
								<bean:write name="registration" property="ectsCredits"/>
								<br/>
								<fr:view name="curricularCourses" schema="student.approved.curricular.courses.with.ects.credits" >
									<fr:layout name="tabular" >
										<fr:property name="classes" value="tstyle4"/>
								        <fr:property name="columnClasses" value="listClasses,,"/>
									</fr:layout>
								</fr:view>
							</logic:greaterThan>
							<logic:lessEqual name="numberCompetedCourses" value="0">
								<span class="error"><bean:message key="message.student.has.no.enrolments"/></span>
							</logic:lessEqual>
						</logic:present>
						<logic:notPresent name="registration" property="activeStudentCurricularPlan">
							<span class="error"><bean:message key="message.student.has.no.active.student.curricular.plan"/></span>
						</logic:notPresent>
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
