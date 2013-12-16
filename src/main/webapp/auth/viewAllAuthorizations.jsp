<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<html:xhtml />

<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>

<em><bean:message key="label.person.main.title" /></em>
<h2>
	<bean:message key="oauthapps.label.manage.applications" bundle="APPLICATION_RESOURCES" />
</h2>

<html:link page="/externalApps.do?method=viewAllApplications">
		<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>
</html:link>


<logic:notEmpty name="userAuthorizations">
		<fr:view name="userAuthorizations" schema="oauthapps.view.app.all.authorizations">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thcenter thcenter"/>
				<fr:property name="linkFormat(viewAllAuthorizations)" value="<%= "/externalApps.do?method=viewAllSessions&session=${externalId}" %>" />
				<fr:property name="key(viewAllAuthorizations)" value="oauthapps.label.view.application.details"/>
				<fr:property name="bundle(viewAllAuthorizations)" value="APPLICATION_RESOURCES"/>
			</fr:layout>
		</fr:view>
</logic:notEmpty>