<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html>
    <app:checkLogon/>
    <head>
        <title> Ver Turma </title>
        <html:base/>
    </head>
    <body>
        <jsp:include page="context.jsp"/><br/>
        <br/>
        <center>
            <font color='#034D7A' size='5'>
                <b> Turma <bean:write name="turmaFormBean" property="nome" scope="request" filter="true"/> </b>
            </font>
        </center>
        <br/>
        <br/>
        <app:gerarHorario name="aulasDeTurma"/>
        <br/>
    </body>
</html:html>

