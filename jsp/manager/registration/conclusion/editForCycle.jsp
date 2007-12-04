<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<logic:present role="MANAGER">
	<h3><bean:message key="student.registrationConclusionProcess"
		bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>

	<bean:define id="cycleCurriculumGroupId" name="cycleCurriculumGroup"
		property="idInternal" />
		
	<bean:define id="registrationId" name="cycleCurriculumGroup" property="studentCurricularPlan.registration.idInternal" />

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


