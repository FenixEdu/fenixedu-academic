<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:present role="COORDINATOR">
	<h2><bean:message key="label.transition.bolonha.registrationsInTransition"
		bundle="STUDENT_RESOURCES" /></h2>

	<logic:empty name="registrations">
		<span class="error0"> <bean:message bundle="STUDENT_RESOURCES"
			key="label.transition.bolonha.registrationsInTransition.noRegistrations" />
		</span>
	</logic:empty>

	<logic:notEmpty name="registrations">
		<fr:view name="registrations"
			schema="student.registrationsToList">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight mtop025 boldlink1" />
				<fr:property name="columnClasses" value=",tdhl1,," />
				<fr:property name="linkFormat(view)" value="/bolonhaTransitionManagement.do?method=showStudentCurricularPlan&amp;registrationId=${idInternal}&amp;studentId=${student.idInternal}" />
				<fr:property name="key(view)" value="label.view"/>
				<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
				<fr:property name="contextRelative(view)" value="true"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

</logic:present>
