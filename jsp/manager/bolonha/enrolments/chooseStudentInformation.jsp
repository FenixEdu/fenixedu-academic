<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:present role="MANAGER">
	<h2><bean:message key="title.student.curriculum" bundle="APPLICATION_RESOURCES" /></h2>

	<fr:hasMessages for="student-number-bean" type="conversion">
		<ul class="nobullet list6">
			<fr:messages>
				<li><span class="error0"><fr:message /></span></li>
			</fr:messages>
		</ul>
	</fr:hasMessages>


	<fr:form action="/bolonhaStudentEnrolment.do?method=prepareSearchStudent">

		<fr:edit id="student-number-bean" name="studentNumberBean" schema="StudentNumberBean.edit">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
			</fr:layout>
		</fr:edit>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.submit" /></html:submit>
	</fr:form>
	
	<logic:present name="registrations">
		<logic:notEmpty name="registrations">
			<br/>
			<logic:iterate id="registration" name="registrations">
				<br/>
				<strong><bean:write name="registration" property="lastDegreeCurricularPlan.degree.sigla"/> - <bean:write name="registration" property="lastDegreeCurricularPlan.degree.name"/></strong> (<bean:message name="registration" property="activeStateType.qualifiedName" bundle="ENUMERATION_RESOURCES" />)
				<html:link action="/registrationConclusion.do?method=show" paramId="registrationId" paramName="registration" paramProperty="idInternal" ><bean:message  key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
				<logic:notEmpty name="registration" property="studentCurricularPlans">
					<table class="tstyle4">
						<tr>
							<th scope="col"><bean:message key="label.startDate" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
							<th scope="col"><bean:message key="label.degreeType" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
							<th scope="col"><bean:message key="label.curricularPlan" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
							<th scope="col"> </th>
						</tr>
						<logic:iterate id="studentCurricularPlan" name="registration" property="studentCurricularPlans">
						<tr>
							<td><bean:write name="studentCurricularPlan" property="startDateYearMonthDay"/></td>
							<td><bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso.localizedName"/></td>
							<td><bean:write name="studentCurricularPlan" property="degreeCurricularPlan.name"/></td>
							<td>
								<html:link action="/bolonhaStudentEnrolment.do?method=viewStudentCurriculum" paramId="scpId" paramName="studentCurricularPlan" paramProperty="idInternal"><bean:message key="link.student.viewCurriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
								<logic:equal name="studentCurricularPlan" property="transition" value="false">
									, 
									<html:link action="/bolonhaStudentEnrolment.do?method=prepareChooseExecutionPeriod" paramId="scpId" paramName="studentCurricularPlan" paramProperty="idInternal"><bean:message key="link.student.enrolInCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
								</logic:equal>
								<logic:equal name="studentCurricularPlan" property="transition" value="false">
									, 
									<html:link action="/curriculumLinesLocationManagement.do?method=prepare" paramId="scpID" paramName="studentCurricularPlan" paramProperty="idInternal"><bean:message key="label.course.moveEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
								</logic:equal>
								, 
								<html:link action="/studentDismissals.do?method=manage" paramId="scpID" paramName="studentCurricularPlan" paramProperty="idInternal"><bean:message key="link.student.dismissal.management" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
								<logic:equal name="studentCurricularPlan" property="transition" value="true">
									,
									<html:link action="/bolonhaStudentEnrolment.do?method=prepareTransit" paramId="scpId" paramName="studentCurricularPlan" paramProperty="idInternal"><bean:message key="link.student.transitToBolonha" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
								</logic:equal>
							</td>
						</tr>
						</logic:iterate>
					</table>
				</logic:notEmpty>
			</logic:iterate>
		</logic:notEmpty>
	</logic:present>

</logic:present>