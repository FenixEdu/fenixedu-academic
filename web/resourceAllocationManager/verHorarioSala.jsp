<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html xhtml="true">
    <app:checkLogon/>
    <head>
        <title> Ver Sala </title>
        <html:base/>
    </head>
    <body>
        <center>
            <font color='#034D7A' size='5'>
                <b> Sala <bean:write name="salaFormBean" property="nome" scope="request" filter="true"/> </b>
            </font>
        </center>
        <br/>
        <app:gerarHorario name="aulasDeSala"/>    
        <br/>
    </body>
</html:html>

