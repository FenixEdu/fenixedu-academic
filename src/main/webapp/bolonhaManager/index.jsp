<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<logic:messagesPresent property="message.not-authorized">
	<h4>
		<bean:message key="message.not-authorized"/>
	</h4>
</logic:messagesPresent>
<logic:messagesPresent property="message.unknown-error">
	<bean:message key="message.unknown-error"/>
</logic:messagesPresent>

<logic:messagesNotPresent>

<h2><bean:message key="bolonhaManager.portal"/></h2>

<p><bean:message key="bolonhaManager.portal.welcomeMessage"/></p>

</logic:messagesNotPresent>