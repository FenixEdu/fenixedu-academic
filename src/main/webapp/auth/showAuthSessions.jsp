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
		<logic:iterate id="authSession" name="authSessions" type="net.sourceforge.fenixedu.domain.AppUserSession">
		<fr:view name="authSession" layout="tabular" schema="my.list.sessions"/>
		<p><a href="<%= "externalAuth.do?method=removeAuth&oid=" + authSession.getApplication().getExternalId() + "&deviceId=" + authSession.getDeviceId()%>">Remover</a>
		</logic:iterate>
		
			<p><a href="<%= "externalAuth.do?method=removeAllAuths&appOid=" + appOid %>">Remover todas as autorizações</a>
			
		
	</logic:present>
	
	
	
	
	
	