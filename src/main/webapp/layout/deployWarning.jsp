<%@page import="org.joda.time.DateTime"%>
<%@page import="org.joda.time.Interval"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<html:xhtml/>

<app:rootDomainObjectDefiner id="rootDomainObject"/>
<logic:present name="rootDomainObject" property="deployNotifier">

	<logic:equal name="rootDomainObject" property="deployNotifier.notifierState" value="true">
		<bean:define id="timeoutInMiliSeconds" value="30000"/>
		<bean:define id="frequencyInMiliSeconds" value="30000"/>
		
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
	<%
		final DateTime sWindow1 = new DateTime(2011, 12, 5, 18, 0, 0, 0);
		final Interval window1 = new Interval(sWindow1.minusHours(1), sWindow1.plusHours(2));
		final DateTime sWindow2 = new DateTime(2011, 12, 12, 18, 0, 0, 0);
		final Interval window2 = new Interval(sWindow2.minusHours(1), sWindow2.plusHours(2));
		final DateTime sWindow3 = new DateTime(2011, 12, 19, 18, 0, 0, 0);
		final Interval window3 = new Interval(sWindow3.minusHours(1), sWindow3.plusHours(2));
		final DateTime now = new DateTime();
		if (window1.contains(now) || window2.contains(now) || window3.contains(now)) {
	%>
		<bean:define id="timeoutInMiliSeconds" value="30000"/>
		<bean:define id="frequencyInMiliSeconds" value="30000"/>

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

			<div style="background: #4AA02C; border-bottom: 3px solid #347C17; color: #FFFFFF; padding: 0.75em; text-align: center;">
				<bean:message key="label.deploy.warning.undergoing.tests" bundle="APPLICATION_RESOURCES"/>
			</div>
	<%
		}

	%>

<script type="text/javascript" language="javascript">switchGlobal();</script>
</logic:present>
