<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>


<bean:define id="global" value="<%= request.getParameter("global") %>" />

<logic:equal value="true" name="global">
	<logic:notEmpty name="alertMessagesToNotify">
	
		<div class="warning0 mbottom1">
			<div style="padding: 10px;">
			<bean:size id="messagesSize" name="alertMessagesToNotify"/>
			
			<bean:message key="message.pending.phd.alert.messages.notification" bundle="PHD_RESOURCES" arg0="<%= messagesSize.toString() %>"/>
			<html:link action="/phdIndividualProgramProcess.do?method=viewAlertMessages"><bean:message bundle="PHD_RESOURCES" key="label.details"/></html:link>
			</div>
		</div>
	
	</logic:notEmpty>
</logic:equal>

<logic:equal value="false" name="global">
	<logic:notEmpty name="processAlertMessagesToNotify">
	
	
	<div class="warning0 mbottom1">
		<div style="padding: 10px;">
		<bean:size id="messagesSize" name="processAlertMessagesToNotify"/>
		
		<bean:message key="message.pending.phd.alert.messages.for.process.notification" bundle="PHD_RESOURCES" arg0="<%= messagesSize.toString() %>"/>
		<html:link action="/phdIndividualProgramProcess.do?method=viewProcessAlertMessages" paramId="processId" paramName="process" paramProperty="externalId"><bean:message bundle="PHD_RESOURCES" key="label.details"/></html:link>
		</div>
	</div>
	
	</logic:notEmpty>

</logic:equal>





