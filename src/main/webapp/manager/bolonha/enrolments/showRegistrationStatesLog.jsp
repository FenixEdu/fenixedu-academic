<%@ page language="java"%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateLog"%>

<logic:present role="MANAGER">
	<h2><bean:message key="student.registration.states.log" bundle="APPLICATION_RESOURCES" /></h2>

	<html:link action="/bolonhaStudentEnrolment.do?method=showAllStudentCurricularPlans" paramId="studentId" paramName="registration" paramProperty="student.externalId">
		<bean:message key="label.back"  bundle="APPLICATION_RESOURCES"/>
	</html:link>

	<br/>
	<br/>

	<fr:view name="registration" property="registrationStateLogs">
		
		<fr:schema bundle="APPLICATION_RESOURCES" type="<%= RegistrationStateLog.class.getName() %>">
			<fr:slot name="stateDate" />
			<fr:slot name="stateType" />
			<fr:slot name="action.name" />
			<fr:slot name="who" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="sortBy" value="stateDate=desc" />
		</fr:layout>

	</fr:view>

</logic:present>
