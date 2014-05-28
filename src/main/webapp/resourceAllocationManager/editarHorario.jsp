<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:html xhtml="true">
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
