<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<html:xhtml />

<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>

<em><bean:message key="label.person.main.title" /></em>
<h2>
	<bean:message key="oauthapps.label.manage.applications" bundle="APPLICATION_RESOURCES" />
</h2>

<div class="infoop2" style="width:500px;">
	Nesta secção estão listadas as suas aplicações registadas no sistema Fénix.
</div>
<login:present name="allowIstIds">
    <html:link page="/externalApps.do?method=allowIstIds">
		<logic:equal name="allowIstIds" value="true">
	 		allow.ist.id is <b>ON<b>!
		</logic:equal>
		<logic:equal name="allowIstIds" value="false">
	 		allow.ist.id is off!
		</logic:equal>
	</html:link>
</login:present>

<logic:notEmpty name="appsOwned">
		<fr:view name="appsOwned" schema="oauthapps.view.apps">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thcenter thcenter"/>
				<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, tdcenter, tdcenter"/>
				
				<fr:property name="linkFormat(viewApplicationDetails)" value="<%= "/externalApps.do?method=viewApplicationDetails&appOid=${externalId}" %>" />
				<fr:property name="key(viewApplicationDetails)" value="oauthapps.label.view.application.details"/>
				<fr:property name="bundle(viewApplicationDetails)" value="APPLICATION_RESOURCES"/>
				
				<fr:property name="linkFormat(editApplication)" value="<%= "/externalApps.do?method=prepareEditApplication&appOid=${externalId}" %>" />
				<fr:property name="key(editApplication)" value="oauthapps.label.edit.application"/>
				<fr:property name="bundle(editApplication)" value="APPLICATION_RESOURCES"/>
				
				<fr:property name="linkFormat(deleteApplication)" value="<%= "/externalApps.do?method=deleteApplication&appOid=${externalId}" %>" />
				<fr:property name="key(deleteApplication)" value="oauthapps.label.delete.application"/>
				<fr:property name="bundle(deleteApplication)" value="APPLICATION_RESOURCES"/>
			</fr:layout>
		</fr:view>
</logic:notEmpty>

<logic:empty name="appsOwned">
	<bean:message key="oauthapps.label.no.apps" bundle="APPLICATION_RESOURCES" />
</logic:empty>

<p>
	<html:link page="/externalApps.do?method=prepareCreateApplication">
		<bean:message key="oauthapps.label.create.application" bundle="APPLICATION_RESOURCES"/>
	</html:link>
</p>

<bean:define id="confirm">
	<bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.confirm.delete.application"/> 
</bean:define>

<script type="text/javascript">
		$("table img").width("75px").height("75px");
		$("a[href*=deleteApplication]").click(function(e) {
			   answer = confirm('<%= confirm %>');
			   return answer;
			});
</script>