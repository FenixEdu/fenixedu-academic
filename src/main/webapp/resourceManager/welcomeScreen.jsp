<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:xhtml/>

<h2><bean:message key="title.resourceManager.welcome" bundle="RESOURCE_MANAGER_RESOURCES"/></h2>

<p class="mtop15 mbottom05">
	<bean:message key="message.resourceManager.welcome.header" bundle="RESOURCE_MANAGER_RESOURCES"/>
</p>

<p class="mvert05">
	<bean:message key="message.resourceManager.welcome.body" bundle="RESOURCE_MANAGER_RESOURCES"/>
</p>