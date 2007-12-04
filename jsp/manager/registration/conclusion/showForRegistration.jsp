<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<logic:present role="MANAGER">
	<h2><bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>
	
	<bean:define id="registrationId" name="registrationConclusionBean" property="registration.idInternal" />
	 
	<fr:view name="registrationConclusionBean"
		schema="RegistrationConclusionBean.viewForRegistrationWithConclusionProcessedInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thright thlight" />
			<fr:property name="rowClasses" value=",,tdhl1,,,,,," />
		</fr:layout>
	</fr:view>
	
	
	<logic:equal name="registrationConclusionBean" property="registration.registrationConclusionProcessed" value="true">
		<html:link action="<%="/registrationConclusion.do?method=prepareEditForRegistration" %>" paramId="registrationId" paramName="registrationConclusionBean" paramProperty="registration.idInternal">
			<bean:message  key="label.edit" bundle="APPLICATION_RESOURCES"/>
		</html:link>
	</logic:equal>
	
	<br/>
	<br/>
	<bean:define id="studentId" name="registrationConclusionBean" property="registration.student.idInternal" />
	<fr:form action="<%="/bolonhaStudentEnrolment.do?method=showAllStudentCurricularPlans&amp;studentId=" + studentId%>">
		<html:cancel altKey="cancel.cancel" bundle="HTMLALT_RESOURCES">
			<bean:message  key="label.back" bundle="APPLICATION_RESOURCES"/>
		</html:cancel>
	</fr:form>

</logic:present>


