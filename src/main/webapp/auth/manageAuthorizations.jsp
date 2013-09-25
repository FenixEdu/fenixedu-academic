<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<html:xhtml />
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>

<em><bean:message key="label.person.main.title" /></em>
<h2>
	<bean:message key="oauthapps.label.manage.authorizations" bundle="APPLICATION_RESOURCES" />
</h2>

<div class="infoop2" style="width:600px;">
	<p>Nesta secção estão listadas as autorizações que concedeu a aplicações externas registadas no Fénix.</p>
	<p>É possível revogar a aplicação ou cada autorização individualmente.</p>
</div>


<logic:notEmpty name="authApps">
		<fr:view name="authApps" schema="oauthapps.view.apps">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thcenter thcenter"/>
				<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, "/>
				
				<fr:property name="linkFormat(viewAuthorizations)" value="<%= "/externalApps.do?method=viewAuthorizations&appOid=${externalId}" %>" />
				<fr:property name="key(viewAuthorizations)" value="oauthapps.label.view.authorizations"/>
				<fr:property name="bundle(viewAuthorizations)" value="APPLICATION_RESOURCES"/>
				
				<fr:property name="linkFormat(revokeApplication)" value="<%= "/externalApps.do?method=revokeApplication&appOid=${externalId}" %>" />
				<fr:property name="key(revokeApplication)" value="oauthapps.label.revoke.application"/>
				<fr:property name="bundle(revokeApplication)" value="APPLICATION_RESOURCES"/>
				
			</fr:layout>
		</fr:view>
<%-- 		<p><a href="<%= "externalApps.do?method=displayAppUserSessions&oid=" + app.getExternalId()%>"> Ver autorizações </a> --%>
</logic:notEmpty>
<logic:empty name="authApps">
	<bean:message key="oauthapps.label.no.authorizations" bundle="APPLICATION_RESOURCES" />
</logic:empty>

<bean:define id="confirm">
	<bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.confirm.revoke.application"/> 
</bean:define>

<script type="text/javascript">
		$("table img").width("75px").height("75px");
		$("a[href*=revokeApplication]").click(function(e) {
			   answer = confirm('<%= confirm %>');
			   return answer;
			});
</script>