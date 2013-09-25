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

<em><bean:message key="label.person.main.title" /></em>
<h2>
	<bean:message key="oauthapps.title.edit.application" bundle="APPLICATION_RESOURCES" />
</h2>

<fr:edit id="edit" name="application" type="net.sourceforge.fenixedu.domain.ExternalApplication" schema="oauthapps.create.app">
	<fr:destination name="success" path="/externalApps.do?method=createApplication" />
	<fr:destination name="cancel" path="/externalApps.do?method=createApplication" />
</fr:edit>
