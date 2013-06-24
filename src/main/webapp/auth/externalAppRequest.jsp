<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<html:xhtml />
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>

<em><bean:message key="label.person.main.title" /></em>
<h2>
	<bean:message key="label.auth" bundle="APPLICATION_RESOURCES" />
</h2>



	<logic:present name="authApps">
		<logic:iterate id="app" name="authApps" type="net.sourceforge.fenixedu.domain.ExternalApplication">
		<fr:view name="app" layout="tabular" schema="my.list.apps"/>
		<p><a href="<%= "externalAuth.do?method=removeAuth&oid=" + app.getExternalId()%>">Remover</a>
		</logic:iterate>
	</logic:present>
