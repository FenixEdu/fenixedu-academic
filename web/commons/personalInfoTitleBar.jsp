<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:notPresent name="USER_SESSION_ATTRIBUTE">
	<% response.sendRedirect(response.encodeRedirectURL("http://www.google.com")); %>
</logic:notPresent>
<bean:message key="label.user" bundle="GLOBAL_RESOURCES"/>: 
<bean:write name="USER_SESSION_ATTRIBUTE" property="person.nickname"/>	


<%--
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
-
<dt:format pattern="dd.MM.yyyy"><dt:currentTime/></dt:format>
--%>