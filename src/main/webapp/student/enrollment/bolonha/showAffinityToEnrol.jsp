<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<logic:present role="role(STUDENT)">
	<em><bean:message bundle="STUDENT_RESOURCES"  key="title.student.portalTitle" /></em>
	<bean:define id="cycleTypeToEnrolQualifiedName" name="cycleEnrolmentBean" property="cycleTypeToEnrol.qualifiedName" />
	<h2><bean:message key="label.enrollment.enrolIn" bundle="STUDENT_RESOURCES" /> <bean:message  key="<%=cycleTypeToEnrolQualifiedName.toString()%>" bundle="ENUMERATION_RESOURCES"/></h2>
	
	<bean:define id="registrationId" name="cycleEnrolmentBean" property="studentCurricularPlan.registration.externalId" />
	
	<logic:messagesPresent message="true">
		<div class="error0" style="padding: 0.5em;">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
			<span><bean:write name="messages" /></span>
		</html:messages>
		</div>
	</logic:messagesPresent>
	
	<fr:form action="/studentEnrollmentManagement.do">
		<html:hidden property="method" value="enrolInAffinityCycle" />
		
		<fr:edit id="cycleEnrolmentBean.show" name="cycleEnrolmentBean" visible="false" />
		
		<fr:view name="cycleEnrolmentBean" schema="CycleEnrolmentBean.showAffinityToEnrol.source">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thright thlight"/>
			</fr:layout>
		</fr:view>
		
		<br/>
		
		<fr:view name="cycleEnrolmentBean" schema="CycleEnrolmentBean.showAffinityToEnrol.destination">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thright thlight"/>
			</fr:layout>
		</fr:view>
		
		<br/>
		<strong><bean:message key="label.showAffinityToEnrol.message" bundle="APPLICATION_RESOURCES" /></strong>
		<br/>
		<br/>
		
		<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel onclick="this.form.method.value='showWelcome';"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
		
	</fr:form>
</logic:present>
