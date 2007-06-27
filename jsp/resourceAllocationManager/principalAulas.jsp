<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:html xhtml="true">
    <head>
        <title> <bean:message key="title.manage.aulas"/> </title>
    </head>
    <body>
    	<jsp:include page="context.jsp"/><br/>
        <br/>
        <center><font color='#034D7A' size='5'> <b> <bean:message key="title.manage.aulas"/> </b> </font></center>
        <br/>
        <br/>
        <table align="center" border="5" cellpadding='20' cellspacing='10'>
            <tr align="center">
                <td height="50">
                    <html:link page="/prepararAula.do">
                    	<bean:message key="title.createAula"/>
                    </html:link>
                </td>
            </tr>

            <tr align="center">
                <td height="50">
                    <html:link page="/escolherDisciplinaExecucaoForm.do">
                        <bean:message key="title.manage.aulas"/>
                    </html:link>
                </td>
            </tr>       
        </table>
        <br/>
    </body>
</html:html>
