<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<%@page
	import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>
<%@page import="net.sourceforge.fenixedu.domain.person.RoleType"%>
<html:xhtml />

<logic:present role="role(DEVELOPER)">
<em><bean:message key="label.person.main.title" /></em>
<h2>
	<bean:message key="oauthapps.label.create.application" bundle="APPLICATION_RESOURCES" />
</h2>

<fr:create type="net.sourceforge.fenixedu.domain.ExternalApplication" id="create" schema="oauthapps.create.app">
	<fr:hidden slot="author" name="currentUser"/>
	<fr:destination name="success" path="/externalApps.do?method=createApplication"/>
	<fr:destination name="cancel" path="/externalApps.do?method=createApplication" />
</fr:create>
</logic:present>
<logic:notPresent  role="role(DEVELOPER)">
	<p>Nesta interface pode registar aplicações criadas por si que utilizem as API's disponibilizadas pelo sistema FenixEdu. No site de desenvolvimento do FenixEdu pode encontrar informação sobre como utilizar a API em:</p>
</logic:notPresent>