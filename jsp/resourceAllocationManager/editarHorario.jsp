<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:html xhtml="true">
    <app:checkLogon/>
    <head>
        <title> Editar Horário </title>
    </head>
    <body>
       <jsp:include page="context.jsp"/><br/>
       
        <br/>
        <center><font color='#034D7A' size='5'> <b> Editar Horário </b> </font></center>
        <br/>
        <br/>
        <html:errors/>
        <html:form action="/editarHorario">
            <center> <b> Turnos adicionados </b> </center>
            <br/>
            <logic:present name="listaTurnosBean" scope="request">
                <table align="center" border=1 cellpadding='5'>
                    <%! int i; %>
                    <% i = 0; %>
                    <logic:iterate id="elem" name="listaTurnosBean">
                        <tr align="center">
                            <td width="25">
                                <html:radio bundle="HTMLALT_RESOURCES" altKey="radio.posicao" name="posicaoTurnoFormBean" property="posicao" value="<%= (new Integer(i)).toString() %>"/>
                            </td>
                            <td width="100" height="25">
                                <bean:write name="elem" property="nome"/>
                            </td>
                        </tr>
                        <% i++; %>
                    </logic:iterate>
                </table>
            </logic:present>
            <logic:notPresent name="listaTurnosBean" scope="request">
                <table align="center" border=1 cellpadding='5'>
                    <tr align="center">
                        <td width='200'>
                            <font color='red'> Não existem turnos </font>
                        </td>
                    </tr>
                </table>
            </logic:notPresent>
            <br/>
            <br/>
            <table align="center">
                <tr align="center">
                    <td>
                        <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation">
                            Adicionar Turno
                        </html:submit>
                    </td>
                    <td width="20"> </td>
                    <td>
                        <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation">
                            Remover Turno
                        </html:submit>
                    </td>
                </tr>
            </table>
            <br/>
        </html:form>
    </body>
</html:html>
