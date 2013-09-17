<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:html xhtml="true">
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
