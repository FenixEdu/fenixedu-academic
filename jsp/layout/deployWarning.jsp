<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html:xhtml/>

<app:rootDomainObjectDefiner id="rootDomainObject"/>
<logic:present name="rootDomainObject" property="deployNotifier">

	<logic:equal name="rootDomainObject" property="deployNotifier.notifierState" value="true">
		<bean:define id="timeoutInMiliSeconds" value="300000"/>
		<bean:define id="frequencyInSeconds" value="300"/>
		<script type="text/javascript" src="<%= request.getContextPath() %>/javaScript/prototype.js"></script>
		<script type="text/javascript">
			function verifyForDowntime(request) {
					var responseText = request.responseText;
					if(responseText.indexOf('<html>') >= 0) {
						hideElement('jsWarning');
					}else {
					   showElement('jsWarning');
					}
			}
 			setTimeout("new Ajax.PeriodicalUpdater({success: 'deployWarning', failure: null}, <%= "'" + request.getContextPath() + "/ajax/DeployNotifierServlet'" %>, { method: 'get', frequency: <%= frequencyInSeconds %>, decay: 1, onSuccess: verifyForDowntime})",<%= timeoutInMiliSeconds %>); 
		</script>
	
		<div id="jsWarning" class="switchInline">
			<div id="deployWarning">
				<logic:equal name="rootDomainObject" property="deployNotifier.notifyUsers" value="true">
					<div class="deploywarning">
						<bean:define id="minutes" name="rootDomainObject" property="deployNotifier.estimateMinutesForDeploy" type="java.lang.Integer"/> 
						<logic:equal name="minutes" value="0">
								<bean:message key="label.deploy.warning.moment" bundle="APPLICATION_RESOURCES"/>
						</logic:equal>
						<logic:notEqual name="minutes" value="0">
							<bean:message key="label.deploy.warning" bundle="APPLICATION_RESOURCES" arg0="<%= minutes.toString() %>" arg1="<%=  new Integer(minutes + 1).toString() %>"/>
						</logic:notEqual>
					</div>
				</logic:equal>
			</div>
		</div>
	
			<logic:equal name="rootDomainObject" property="deployNotifier.notifyUsers" value="true">
		
					<div class="deploywarning switchNone">
						<bean:define id="minutes" name="rootDomainObject" property="deployNotifier.estimateMinutesForDeploy" type="java.lang.Integer"/> 
						<logic:equal name="minutes" value="0">
								<bean:message key="label.deploy.warning.moment" bundle="APPLICATION_RESOURCES"/>
						</logic:equal>
						<logic:notEqual name="minutes" value="0">
							<bean:message key="label.deploy.warning" bundle="APPLICATION_RESOURCES" arg0="<%= minutes.toString() %>" arg1="<%=  new Integer(minutes + 1).toString() %>"/>
						</logic:notEqual> 
					</div>
			</logic:equal>
	</logic:equal>

<script type="text/javascript" language="javascript">switchGlobal();</script>
</logic:present>