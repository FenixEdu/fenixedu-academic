<%@ page language="java" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<ul>
<li><html:link page="<%= "/sectionsManagement.do?method=prepare&amp;objectCode=1" %>"><bean:message key="link.sectionsManagement"/></html:link></li>
</ul>
