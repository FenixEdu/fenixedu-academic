<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2>Preview Email Templates</h2>

<bean:define id="processId" name="process" property="externalId" />

<fr:form action="<%= "/caseHandlingMobilityApplicationProcess.do?method=editEmailTemplate&processId=" + processId %>">
	<fr:edit id="mobilityEmailTemplateBean" name="mobilityEmailTemplateBean" visible="false" />
	
	<fr:view name="mobilityEmailTemplateBean">
	
		<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityEmailTemplateBean" bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="subject" />
			<fr:slot name="body" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
		</fr:layout>
		
	</fr:view>
	
	<p><html:submit>Save</html:submit></p>
	
</fr:form>
