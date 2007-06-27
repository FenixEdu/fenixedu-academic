<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:html xhtml="true">
    <app:checkLogon/>
    <head>
        <title> Gestão Turmas </title>
    </head>
    <body>
        <jsp:include page="context.jsp"/><br/>
        <br/>
        <center><font color='#034D7A' size='5'> <b> Gestão Turmas </b> </font></center>
        <br/>
        <br/>
        <table align="center" border="5" cellpadding='20' cellspacing='10''>
            <tr align="center">
                <td height="50">
                    <html:link page="/prepararEditarTurma.do?accao=Criar"> Criar Turma </html:link>
                </td>
            </tr>
            <tr align="center">
                <td height="50">
                    <html:link page="/escolherDisciplinaETipoForm.do"> Manipular Turmas </html:link>
                </td>
            </tr>
        </table>
        <br/>
    </body>
</html:html>
