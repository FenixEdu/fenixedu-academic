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

<frameset rows="*,0" cols="109,*" frameborder="no" border="0" framespacing="0">
  <frame src="barraIST.html" name="barraIST" scrolling="NO" noresize>
  <frameset rows="115,*" cols="*" framespacing="0" frameborder="no" border="0">
    <frame src="topAdministrativa.do" name="topFrame" scrolling="NO" noresize>
    <frame src="output.jsp" name="_output">
  </frameset>
  <frame src="UntitledFrame-6.htm">
  <frame src="UntitledFrame-7.htm">
</frameset>
<noframes><body>
</body>
</noframes>
</html:html>
