<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="siteView">
	<bean:define id="commonComponent" name="siteView" property="commonComponent"/>
	<h1><bean:write name="commonComponent" property="title"/></h1>	
	<bean:define id="executionCourse" name="commonComponent" property="executionCourse"/>
</logic:present>
