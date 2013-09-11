<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>

<html:xhtml />
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>

<em><bean:message key="label.person.main.title" /></em>
<h2>
	<bean:message key="label.oauthapps.manage.applications" bundle="APPLICATION_RESOURCES" />
</h2>



<logic:notEmpty name="appsOwned">
		<fr:view name="appsOwned" schema="oauthapps.view.ownedapps">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thcenter thcenter"/>
				<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, tdcenter, tdcenter"/>
				
				<fr:property name="linkFormat(editApplication)" value="<%= "/externalApps.do?method=editApplication&appOid=${externalId}" %>" />
				<fr:property name="key(editApplication)" value="label.oauthapps.edit.application"/>
				<fr:property name="bundle(editApplication)" value="APPLICATION_RESOURCES"/>
				
			</fr:layout>
		</fr:view>
</logic:notEmpty>

<logic:empty name="appsOwned">
	<bean:message key="label.oauthapps.no.apps" bundle="APPLICATION_RESOURCES" />
</logic:empty>

<p>
	<html:link page="/externalApps.do?method=prepareCreateApplication">
		<bean:message key="label.oauthapps.create.application" bundle="APPLICATION_RESOURCES"/>
	</html:link>
</p>