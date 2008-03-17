<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html:xhtml/>

<app:rootDomainObjectDefiner id="rootDomainObject"/>
<logic:present name="rootDomainObject" property="deployNotifier">

	<logic:equal name="rootDomainObject" property="deployNotifier.notifierState" value="true">
		<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/prototype.js"></script>
		<script type="text/javascript">
 			setTimeout("new Ajax.PeriodicalUpdater('deployWarning', <%= "'" + request.getContextPath() + "/ajax/DeployNotifierServlet'" %>, { method: 'get', frequency: 300, decay: 1})",300000); 
		</script>
	
		<div class="switchInline">
			<div id="deployWarning">
				<logic:equal name="rootDomainObject" property="deployNotifier.notifyUsers" value="true">
					<div class="deploywarning">
						<bean:define id="minutes" name="rootDomainObject" property="deployNotifier.estimateMinutesForDeploy"/> 
						<bean:message key="label.deploy.warning" bundle="APPLICATION_RESOURCES" arg0="<%= minutes.toString() %>"/>
					</div>
				</logic:equal>
			</div>
		</div>
	
			<logic:equal name="rootDomainObject" property="deployNotifier.notifyUsers" value="true">
		
					<div class="deploywarning switchNone">
						<bean:define id="minutes" name="rootDomainObject" property="deployNotifier.estimateMinutesForDeploy"/> 
						<bean:message key="label.deploy.warning" bundle="APPLICATION_RESOURCES" arg0="<%= minutes.toString() %>"/> 
					</div>
			</logic:equal>
	</logic:equal>

<script type="text/javascript" language="javascript">switchGlobal();</script>
</logic:present>