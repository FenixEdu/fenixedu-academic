<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<html:html>
    <app:checkLogon/>
    <head>
        <title> <bean:message key="title.choose.semester"/> </title>
        <html:base/>
    </head>
    <body>
        <center><font color='#034D7A' size='5'> <b> <bean:message key="title.choose.semester"/> </b> </font></center>
        <br/>
        <html:errors/>
        <html:form action="/escolherSemestre">
            <p align="center"> Escolha o Semestre para o qual pretende ver a ocupação da sala. </p>
            <table align="center" border="5" cellpadding='20' cellspacing='10'>
                <tr align="center">
                    <td width="150" height="60">
                        <bean:message key="property.semester"/>
                        <br/>
                        <html:select property="semestre" size="1">
                            <html:options collection="semestres" property="value" labelProperty="label"/>
                        </html:select>
                    </td>
                </tr>
            </table>
            <br/>
            <table align="center">
                <tr align="center">
                    <td width="100" height="50">
                        <html:submit>
                            <bean:message key="label.next"/>
                        </html:submit>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html:html>
