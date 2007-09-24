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
	
	<logic:present name="studentCurricularPlans">
	
		<logic:notEmpty name="studentCurricularPlans">
		
			<fr:view name="studentCurricularPlans" schema="student.studentCurricularPlans">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 mtop15" />
				
				<fr:property name="linkFormat(view)" value="<%="/bolonhaStudentEnrolment.do?method=viewStudentCurriculum&amp;scpId=${idInternal}"%>"/>
				<fr:property name="key(view)" value="link.student.viewCurriculum"/>
				<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
				<fr:property name="order(view)" value="1"/>
				
				<fr:property name="linkFormat(enrol)" value="<%="/bolonhaStudentEnrolment.do?method=prepareChooseExecutionPeriod&amp;scpId=${idInternal}"%>"/>
				<fr:property name="key(enrol)" value="link.student.enrolInCourses"/>
				<fr:property name="bundle(enrol)" value="ACADEMIC_OFFICE_RESOURCES"/>
				<fr:property name="order(enrol)" value="2"/>

				<fr:property name="linkFormat(moveCurriculumLines)" value="<%="/curriculumLinesLocationManagement.do?method=prepare&amp;scpID=${idInternal}"%>"/>
				<fr:property name="key(moveCurriculumLines)" value="label.course.moveEnrolments"/>
				<fr:property name="bundle(moveCurriculumLines)" value="ACADEMIC_OFFICE_RESOURCES"/>
				<fr:property name="order(moveCurriculumLines)" value="3"/>
				
				<fr:property name="linkFormat(dismissal)" value="/studentDismissals.do?method=manage&amp;scpID=${idInternal}" />
				<fr:property name="key(dismissal)" value="link.student.dismissal.management"/>
				<fr:property name="bundle(dismissal)" value="ACADEMIC_OFFICE_RESOURCES"/>
				<fr:property name="contextRelative(dismissal)" value="true"/>      	
				<fr:property name="order(dismissal)" value="4"/>
			</fr:layout>
		</fr:view>
		
		</logic:notEmpty>
	
	</logic:present>

</logic:present>