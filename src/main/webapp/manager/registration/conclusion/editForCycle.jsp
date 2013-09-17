<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>

<logic:present role="MANAGER">
	<h3><bean:message key="student.registrationConclusionProcess"
		bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>

	<bean:define id="cycleCurriculumGroupId" name="cycleCurriculumGroup"
		property="externalId" />
		
	<bean:define id="registrationId" name="cycleCurriculumGroup" property="studentCurricularPlan.registration.externalId" />

	<fr:edit name="cycleCurriculumGroup"
		schema="CycleCurriculumGroup.editConclusionInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05" />
		</fr:layout>
		<fr:destination name="cancel"
			path="<%="/registrationConclusion.do?method=show&registrationId=" + registrationId%>" />
		<fr:destination name="success" 
			path="<%="/registrationConclusion.do?method=show&registrationId=" + registrationId%>" />
		
	</fr:edit>
</logic:present>


