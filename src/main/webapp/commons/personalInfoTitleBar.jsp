<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<logic:notPresent name="USER_SESSION_ATTRIBUTE">
	<% response.sendRedirect(response.encodeRedirectURL("http://www.google.com")); %>
</logic:notPresent>
<bean:message key="label.user" bundle="GLOBAL_RESOURCES"/>: 
<bean:write name="USER_SESSION_ATTRIBUTE" property="user.person.nickname"/>	


<%--
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.0.1" prefix="str" %>
-
<dt:format pattern="dd.MM.yyyy"><dt:currentTime/></dt:format>
--%>