<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html:xhtml/>

<h2><bean:message key="title.resourceManager.welcome" bundle="RESOURCE_MANAGER_RESOURCES"/></h2>

<p class="mtop15 mbottom05">
	<bean:message key="message.resourceManager.welcome.header" bundle="RESOURCE_MANAGER_RESOURCES"/>
</p>

<p class="mvert05">
	<bean:message key="message.resourceManager.welcome.body" bundle="RESOURCE_MANAGER_RESOURCES"/>
</p>