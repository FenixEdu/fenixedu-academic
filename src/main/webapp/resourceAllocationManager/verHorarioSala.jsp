<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:html xhtml="true">
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

