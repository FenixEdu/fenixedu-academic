<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<logic:present name="siteView">
	<bean:define id="commonComponent" name="siteView" property="commonComponent"/>
	<h1><bean:write name="commonComponent" property="title"/></h1>	
	<bean:define id="executionCourse" name="commonComponent" property="executionCourse"/>
</logic:present>
