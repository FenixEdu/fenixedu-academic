<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<html:html xhtml="true">
    <app:checkLogon/>
    <head>
      <title> Serviço de Organização Pedagógica </title>
    </head>

    <frameset cols="150,*" border="0" frameborder="0">
        <frame src="sop/barraIST.jsp" name="BarraIST" scrolling="no">
        <frameset rows="180,*,80" border="0" frameborder="0">
            <frame src="sop/topoSOP.jsp" name="TopoSOP" scrolling="no">
            <frame src="paginaPrincipal.do" name="AreaPrincipal" scrolling="auto">
            <frame src="sop/baseSOP.jsp" name="BaseSOP" scrolling="no">
        </frameset>
    </frameset>
</html:html>