<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:html xhtml="true">
    <app:checkLogon/>
    <head>
        <title> Gestao de Turmas </title>
    </head>
    <body>
        <jsp:include page="context.jsp"/><br/>
        <br/>
        <center><font color='#034D7A' size='5'> <b> Manipular Turmas </b> </font></center>
        <br/>
        <br/>
        <html:errors/>
        <center> <b> Turmas existentes </b> </center>
        <html:form action="/manipularTurmas">
            <logic:present name="listaTurmasBean" scope="request">
                <table align="center" border=1 cellpadding='5'>
                    <%! int i; %>
                    <% i = 0; %>
                    <tr align='center'>
                        <th>
                        </th>
                        <th>
                            Nome
                        </th>
                    </tr>
                    <logic:iterate id="elem" name="listaTurmasBean">
                        <tr align="center">
                            <td width="25">
                                <html:radio name="posicaoTurmaFormBean" property="posicao" value="<%= (new Integer(i)).toString()%>"/>
                            </td>
                            <td>
                                <bean:write name="elem" property="nome"/>
                            </td>
                        </tr>
                        <% i++; %>
                    </logic:iterate>
                </table>
            </logic:present>
            <logic:notPresent name="listaTurmasBean" scope="request">
                <table align="center" border='1' cellpadding='5''>
                    <tr align="center">
                        <td>
                            <font color='red'> Não existem turmas </font>
                        </td>
                    </tr>
                </table>
            </logic:notPresent>
            <br/>
            <br/>
            <table align="center">
                <tr align="center">
                    <td>
                        <html:submit property="operation">
                            Ver Turma
                        </html:submit>
                    </td>
                    <td width="20"> </td>
                    <td>
                        <html:submit property="operation">
                            Editar Turma
                        </html:submit>
                    </td>
                    <td width="20"> </td>
                    <td>
                        <html:submit property="operation">
                            Apagar Turma
                        </html:submit>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html:html>
