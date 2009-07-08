<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="java.util.List"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Arrays"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


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
	
	<%-- 
	<div id="notificationArea" style="padding: 10px 0px 10px 2px">
		<bean:size id="messagesSize" name="alertMessagesToNotify"/>
		Tem <bean:write name="messagesSize"/> mensagem(s) de alerta com tarefas por realizar.
		<html:link action="/phdIndividualProgramProcess.do?method=viewAlertMessages"><bean:message bundle="PHD_RESOURCES" key="label.details"/></html:link> 
	</div>
	
	<script type="text/javascript" src='<%= request.getContextPath() + "/javaScript/prototype.js"%>'></script>
	<script type="text/javascript" src='<%= request.getContextPath() + "/javaScript/scriptaculous/scriptaculous.js"%>'></script>
	<script type="text/javascript" src='<%= request.getContextPath() + "/javaScript/scriptaculous/effects.js"%>'></script>
	
	<script type="text/javascript">
	
		function highlightNotificationArea() {
			$('notificationArea').visualEffect('highlight',{duration:2});
		}
	
		highlightNotificationArea();
		
		setInterval ( "highlightNotificationArea()", 2000 );
		
		
	</script>
	--%>
	
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





