<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<logic:present role="role(STUDENT)">
	<em><bean:message key="title.student.portalTitle" bundle="STUDENT_RESOURCES" /></em>
	<h2><bean:message key="label.transition.bolonha.registrationsInTransition"
		bundle="STUDENT_RESOURCES" /></h2>

	<logic:empty name="registrations">
		<em><bean:message bundle="STUDENT_RESOURCES"
			key="label.transition.bolonha.registrationsInTransition.noRegistrations" />.
		</em>
	</logic:empty>

	<logic:notEmpty name="registrations">
		<fr:view name="registrations"
			schema="student.registrationDetail.short">
			<fr:layout name="tabular">
				<fr:property name="sortBy" value="startDate=desc"/>			
				<fr:property name="classes" value="tstyle4 thlight mtop025"/>
				<fr:property name="linkFormat(view)" value="/bolonhaTransitionManagement.do?method=showStudentCurricularPlan&amp;registrationId=${externalId}&amp;studentId=${student.externalId}" />
				<fr:property name="key(view)" value="label.view"/>
				<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
				<fr:property name="contextRelative(view)" value="true"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

</logic:present>




