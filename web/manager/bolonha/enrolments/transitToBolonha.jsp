<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<logic:present role="MANAGER">

	<em><bean:message key="label.manager" bundle="MANAGER_RESOURCES" /></em>
	<h2>
		<bean:message key="label.registration.transitToBolonha" bundle="MANAGER_RESOURCES" />
	</h2>
	
	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
	</html:messages>
	

	<bean:define id="scpId" name="studentCurricularPlan" property="idInternal" />
	<bean:define id="studentId" name="studentCurricularPlan" property="registration.student.idInternal" />
	<html:form action="<%= "/bolonhaStudentEnrolment.do?scpId=" + scpId.toString() + "&amp;studentId=" + studentId.toString() %>">
		<html:hidden property="method" value="transitToBolonha" />
		<strong><bean:message bundle="MANAGER_RESOURCES"  key="message.registration.transitToBolonha" /> (<bean:write name="studentCurricularPlan" property="name" /> )?</strong>
		<br/>
		<br/>
		<bean:message bundle="APPLICATION_RESOURCES" key="label.date"/>: <html:text property="date" size="10" /> <bean:message bundle="APPLICATION_RESOURCES" key="label.date.instructions.small" />
		<br/>
		<br/>
		<html:submit><bean:message bundle="MANAGER_RESOURCES"  key="label.manager.yes" /></html:submit>
		<html:cancel onclick="this.form.method.value='showAllStudentCurricularPlans';"><bean:message bundle="MANAGER_RESOURCES"  key="label.manager.no" /></html:cancel>
	</html:form>
	
</logic:present>
