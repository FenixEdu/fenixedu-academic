<%@page contentType="text/html"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<app:checkLogon/>

<html:html>
<head>
<title><bean:message key="menuPrincipal.titulo"/></title>
</head>

<html:base/>

<body>
<bean:message key="menuPrincipal.titulo"/><br>
<html:link forward="PortalAssiduidadeAction"><bean:message key="portalAssiduidade"/></html:link>
</body>
</html:html>