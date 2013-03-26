<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<em><bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="directiveCouncil"/></em>
<h2><bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="title.externalSupervisorsManagement"/></h2>

<div class="infoop2"><bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="label.selectRegistrationAgreement.explanation"/></div>

<fr:form action="/manageExternalSupervision.do?method=showSupervisors">
	<fr:edit id="sessionBean" name="sessionBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.manageExternalSupervision.ManageExternalSupervisionBean" bundle="DIRECTIVE_COUNCIL_RESOURCES">
			<fr:slot name="registrationAgreement" layout="menu-select" key="label.selectRegistrationAgreement.registrationAgreement" required="true">
				<fr:property name="format" value="${description}"/>
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.lists.RegistrationAgreementTypeProvider"/>
				<fr:property name="saveOptions" value="true"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thmiddle thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<html:submit>
		<bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="button.continue"/>
	</html:submit>
</fr:form>