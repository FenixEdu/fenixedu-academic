<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>

<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="title.erasmus.send.reception.email" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="idInternal" />
<bean:define id="processName" name="processName" />

<html:link action='<%= "/caseHandlingErasmusCandidacyProcess.do?method=prepareExecuteViewErasmusCoordinators&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>

<p><bean:message key="title.erasmus.erasmusCoordinators.assign" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>

<fr:form action='<%= "/caseHandlingErasmusCandidacyProcess.do?method=&amp;processId=" + processId.toString() %>'>
	<fr:edit id="" name="" visible="false" />
	
	<fr:edit id="" name="">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="">
			<fr:slot name="" required="true" />
			<fr:slot name="" required="true" />
			<fr:slot name="" required="true" />
		</fr:schema>
	</fr:edit>
	
	
	<fr:destination name="invalid" path="" />
	<fr:destination name="cancel" path="" />	
</fr:form>