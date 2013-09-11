<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<html:xhtml />
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>

<em><bean:message key="label.person.main.title" /></em>
<h2>
	<bean:message key="label.oauthapps.manage.authorizations" bundle="APPLICATION_RESOURCES" />
</h2>


<logic:notEmpty name="authApps">
		<fr:view name="authApps" schema="oauthapps.view.apps">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thcenter thcenter"/>
				<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, "/>
				
				<fr:property name="linkFormat(viewAuthorizations)" value="<%= "/externalApps.do?method=viewAuthorizations&appOid=${externalId}" %>" />
				<fr:property name="key(viewAuthorizations)" value="label.oauthapps.view.authorizations"/>
				<fr:property name="bundle(viewAuthorizations)" value="APPLICATION_RESOURCES"/>
				
				<fr:property name="linkFormat(revokeApplication)" value="<%= "/externalApps.do?method=revokeApplication&appOid=${externalId}" %>" />
				<fr:property name="key(revokeApplication)" value="label.oauthapps.revoke.application"/>
				<fr:property name="bundle(revokeApplication)" value="APPLICATION_RESOURCES"/>
				
			</fr:layout>
		</fr:view>
<%-- 		<p><a href="<%= "externalApps.do?method=displayAppUserSessions&oid=" + app.getExternalId()%>"> Ver autorizações </a> --%>
</logic:notEmpty>
<logic:empty name="authApps">
	<bean:message key="label.oauthapps.no.authorizations" bundle="APPLICATION_RESOURCES" />
</logic:empty>