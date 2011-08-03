<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html:xhtml/>

<app:rootDomainObjectDefiner id="rootDomainObject"/>
<logic:present name="rootDomainObject" property="deployNotifier">

	<logic:equal name="rootDomainObject" property="deployNotifier.notifierState" value="true">
		<bean:define id="timeoutInMiliSeconds" value="3000"/>
		<bean:define id="frequencyInMiliSeconds" value="3000"/>
		
		<script type="text/javascript">
			jQuery(document).ready(function() {
				setTimeout("invokeAjax()", <%= timeoutInMiliSeconds %>);
			});

			function verifyForDowntime(responseText, textStatus) {
				if(responseText.length > 0) {
					jQuery("#jsWarning").html(responseText);
					jQuery("#jsWarning").css("display", "block");
				} else {
					jQuery("#jsWarning").css("display", "none");
				}
			}
		
			function invokeAjax() {
				jQuery.ajax({
					url: <%= "'" + request.getContextPath() + "/ajax/DeployNotifierServlet'" %>,
					success: verifyForDowntime,
					complete: function() {
						setTimeout("invokeAjax()", <%= frequencyInMiliSeconds %>);
					}
				});
			}
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