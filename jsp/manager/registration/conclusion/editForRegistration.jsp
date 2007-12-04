<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<logic:present role="MANAGER">
	<h3><bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	
	<bean:define id="registrationId" name="registration" property="idInternal" />
	 
	<fr:edit name="registration"
		schema="Registration.editConclusionInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05" />			
			<fr:destination name="cancel"
				path="<%="/registrationConclusion.do?method=show&registrationId=" + registrationId%>" />
			<fr:destination name="success" 
			path="<%="/registrationConclusion.do?method=show&registrationId=" + registrationId%>" />

		</fr:layout>
	</fr:edit>

</logic:present>


