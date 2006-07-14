<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:html xhtml="true">
    <head>
        <title> <bean:message key="title.manage.turnos"/> </title>
    </head>
    <body>
    <jsp:include page="context.jsp"/><br/>
    <br/>
        <center><font color='#034D7A' size='5'> <b> <bean:message key="title.manage.turnos"/> </b> </font></center>
        <br/>
        <br/>
        <html:errors/>
        <table align="center" border="5" cellpadding='20' cellspacing='10'>
            <tr align="center">
                <td>
                     <html:link page="/prepararCriarTurno.do"> <bean:message key="title.createTurno"/> </html:link>
                </td>
            </tr>
            <tr align="center">
                <td>
                     <html:link page="/escolherDisciplinaETipoForm.do"> <bean:message key="title.manage.turnos"/> </html:link>
                </td>
            </tr>
        </table>
    </body>
</html:html>