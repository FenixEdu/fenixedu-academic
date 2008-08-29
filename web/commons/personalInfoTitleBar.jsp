<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:message key="label.user" bundle="GLOBAL_RESOURCES"/>
<bean:write name="USER_SESSION_ATTRIBUTE" property="person.nickname"/>

<%--
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
-
<dt:format pattern="dd.MM.yyyy"><dt:currentTime/></dt:format>
--%>