<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<bean:define id="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>

<ul>
<li><html:link page="/studentTests.do?method=testsFirstPage" paramId="objectCode" paramName="objectCode"><bean:message key="link.tests"/></html:link></li>
</ul>
