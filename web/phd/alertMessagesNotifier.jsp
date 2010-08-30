<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>


<bean:define id="global" value="<%= request.getParameter("global") %>" />

<logic:equal value="true" name="global">
	<span class="mbottom1">
		<html:link action="/phdIndividualProgramProcess.do?method=viewAlertMessages"><bean:message key="label.phd.alertMessages" bundle="PHD_RESOURCES"/></html:link>
		<logic:notEmpty name="alertMessagesToNotify">
			<bean:size id="messagesSize" name="alertMessagesToNotify"/>
			(<span class="mbottom1"><bean:message key="message.pending.phd.alert.messages.notification.short" bundle="PHD_RESOURCES" arg0="<%= messagesSize.toString() %>"/></span>)
		</logic:notEmpty>
	</span>
</logic:equal>

<logic:equal value="false" name="global">
	<span class="mbottom1">
		<html:link action="/phdIndividualProgramProcess.do?method=viewProcessAlertMessages" paramId="processId" paramName="process" paramProperty="externalId"><bean:message key="label.phd.alertMessages" bundle="PHD_RESOURCES"/></html:link>
		<logic:notEmpty name="processAlertMessagesToNotify">
			<bean:size id="messagesSize" name="processAlertMessagesToNotify"/>
			(<span class="mbottom1"><bean:message key="message.pending.phd.alert.messages.notification.short" bundle="PHD_RESOURCES" arg0="<%= messagesSize.toString() %>"/></span>)
		</logic:notEmpty>
	</span>
</logic:equal>
