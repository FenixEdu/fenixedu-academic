<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:html xhtml="true">

<head>
<title> <bean:message key="title.choose.disciplineAndType"/> </title>
<html:base/>
</head>

<body>
    <jsp:include page="context.jsp"/><br/>
    <center><font color='#034D7A' size='5'> <b> <bean:message key="title.choose.disciplineAndTypeOfAula"/> </b> </font></center>
    <br/>
    <br/>
    <html:errors/>
    <html:form action="/escolherNovasDisciplinasETipo">
        <table align="center" border="5" cellpadding='20' cellspacing='10'>
            <tr align="center">
                <td>
                    <bean:message key="property.course"/>
                    <br/>
                    <html:select property="chaveDisciplinaExecucao" size="1">
                        <html:options collection="disciplinasExecucao" property="value" labelProperty="label"/>
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

