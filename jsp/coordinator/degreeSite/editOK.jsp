<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<p><span class="error"><html:errors/></span></p>

<h2><bean:message key="link.coordinator.degreeSite.management"/></h2>

<p><bean:message key="text.coordinator.degreeSite.editOK"/><br />
<%--<html:link href="<%= request.getContextPath()+"/publico/showDegreeSite.do?method=showDescription&amp;executionDegreeID=" + request.getAttribute("infoExecutionDegreeID")%>" target="_blank">--%>
<html:link href="<%= request.getContextPath()+"/publico/showDegreeSite.do?method=showDescription&amp;degreeID=" + request.getAttribute("infoDegreeID") %>" target="_blank">
<bean:message key="link.coordinator.degreeSite.viewSite" /></p>
</html:link>