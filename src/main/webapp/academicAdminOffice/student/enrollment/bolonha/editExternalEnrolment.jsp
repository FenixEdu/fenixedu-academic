<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="label.externalUnits.editExternalEnrolment" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="externalCurricularCourseId">&oid=<bean:write name="externalEnrolmentBean" property="externalCurricularCourse.externalId" /></bean:define>

<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="errMsg" /></span>
	</p>
</html:messages>

<bean:define id="contextInformation" name="contextInformation" />
<bean:define id="parameters" name="parameters" />

<fr:form action="<%= contextInformation.toString() + parameters.toString() %>">
	<html:hidden property="method" value="editExternalEnrolment"/>
	
	<bean:define id="registrationId" name="registration" property="externalId" />
	<html:hidden property="registrationId" value="<%= registrationId.toString() %>"/>

	<fr:edit id="editExternalEnrolmentBean" 
			 name="externalEnrolmentBean"
			 schema="ExternalEnrolmentBean"
			 action="/externalUnits.do?method=editExternalEnrolment">
			 
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	<html:cancel onclick="this.form.method.value='backToMainPage';" ><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>

</fr:form>
