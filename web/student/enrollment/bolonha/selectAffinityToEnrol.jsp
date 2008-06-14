<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="STUDENT">
	<em><bean:message bundle="STUDENT_RESOURCES"  key="title.student.portalTitle" /></em>

	<bean:define id="cycleTypeToEnrolQualifiedName" name="cycleEnrolmentBean" property="cycleTypeToEnrol.qualifiedName" />
	<h2><bean:message key="label.enrollment.choose" bundle="STUDENT_RESOURCES" /> <bean:message  key="<%=cycleTypeToEnrolQualifiedName.toString()%>" bundle="ENUMERATION_RESOURCES"/></h2>

	<bean:define id="registrationId" name="cycleEnrolmentBean" property="studentCurricularPlan.registration.idInternal" />
	<logic:empty name="cycleEnrolmentBean" property="cycleDestinationAffinities">
		<span class="error0">
			<bean:message  key="label.enrollment.cycleCourseGroup.noCycleDestinationAffinities" bundle="STUDENT_RESOURCES"/>
		</span>
		<br/><br/>
		<fr:form action="<%="/studentEnrollmentManagement.do?method=chooseRegistration&registrationId=" + registrationId%>">
			<html:cancel altKey="cancel.cancel" bundle="HTMLALT_RESOURCES">
				<bean:message  key="label.back" bundle="APPLICATION_RESOURCES"/>
			</html:cancel>
		</fr:form>
	</logic:empty>
	
	<logic:notEmpty name="cycleEnrolmentBean" property="cycleDestinationAffinities">

		<logic:messagesPresent message="true">
			<div class="error0" style="padding: 0.5em;">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<span><bean:write name="messages" /></span>
			</html:messages>
			</div>
		</logic:messagesPresent>
		
		<fr:view name="cycleEnrolmentBean" schema="CycleEnrolmentBean.showAffinityToEnrol.source">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thright thlight"/>
			</fr:layout>
		</fr:view>
		
		<br/>
		
		<fr:edit id="cycleEnrolmentBean.select" 
				 name="cycleEnrolmentBean" 
				 schema="CycleEnrolmentBean.chooseCycleCourseGroupToEnrol"
				 action="/studentEnrollmentManagement.do?method=showAffinityToEnrol">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thmiddle"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%="/studentEnrollmentManagement.do?method=chooseRegistration&registrationId=" + registrationId%>"/>
		</fr:edit>

	</logic:notEmpty>
	
</logic:present>
