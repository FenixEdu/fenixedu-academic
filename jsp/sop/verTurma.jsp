<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html xhtml="true">
    <app:checkLogon/>
    <head>
        <title> Ver Turma </title>
        <html:base/>
    </head>
    <body>
        <jsp:include page="context.jsp"/><br/>
        <br/>
            <h2>Turma <bean:write name="turmaFormBean" property="nome" scope="request" filter="true"/></h2>
        <br/>
        <br/>
        <app:gerarHorario name="aulasDeTurma"/>
        <br/>
    </body>
</html:html>

