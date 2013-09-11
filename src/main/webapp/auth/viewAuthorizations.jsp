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
	<bean:message key="label.auth" bundle="APPLICATION_RESOURCES" />
</h2>

<bean:define id="appOid" name="applicationOid" scope="request"/>

<logic:present name="authSessions">
		<fr:view name="authSessions" schema="view.oauthapps.authorizations"/>
			<fr:layout name="tabular">
				
			</fr:layout>
		</fr:view>		
</logic:present>
	
	
	
	
	
	