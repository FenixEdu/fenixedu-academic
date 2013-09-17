<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>

<logic:present role="MANAGER">
	<h3><bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	
	<bean:define id="registrationId" name="registration" property="externalId" />
	 
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


