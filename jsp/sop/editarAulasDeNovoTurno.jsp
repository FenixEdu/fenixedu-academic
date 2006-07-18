<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

 <html:html xhtml="true">
    <head>
        <title> <bean:message key="title.editAulaOfTurno"/> </title>
        <html:base/>
    </head>
    <body>
       <jsp:include page="context.jsp"/><br/>
        <br/>
        <center>
            <font color='#034D7A' size='5'>
                <b>
                    <bean:message key="title.editAulaOfTurnoXPTO"/> 
                    <bean:write name="turnoFormBean" property="nome" scope="request" filter="true"/>
                </b>
            </font>
        </center>
        <br/>
        <br/>
        <html:errors/>
        <html:form action="/editarAulasDeNovoTurno">
            <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.codigoInterno" name="turnoFormBean" property="codigoInterno"/>
            <center> <b> Aulas adicionadas </b> </center>
            <br/>
            <logic:present name="listaAulasBean" scope="request">
                <table align="center" border=1 cellpadding='5'>
                    <%! int i; %>
                    <% i = 0; %>
                    <tr align='center'>
                        <th>
                        </th>
                        <th>
                            <bean:message key="property.aula.weekDay"/>
                        </th>
                        <th>
                            <bean:message key="property.aula.time.begining"/>
                        </th>
                        <th>
                            <bean:message key="property.aula.time.end"/>
                        </th>
                        <th>
                            <bean:message key="property.aula.type"/>
                        </th>
                        <th>
                            <bean:message key="property.aula.sala"/>
                        </th>
                    </tr>
                    <logic:iterate id="elem" name="listaAulasBean">
                       <tr align="center">
                            <td>
                                <html:radio bundle="HTMLALT_RESOURCES" altKey="radio.posicao" name="posicaoAulaFormBean" property="posicao" value="<%= (new Integer(i)).toString() %>"/>
                            </td>
                            <td>
                                <bean:write name="elem" property="diaSemana" />
                            </td>
                            <td>
                                <bean:write name="elem" property="horaInicio"/> : <bean:write name="elem" property="minutosInicio"/>
                            </td>
                            <td>
                                <bean:write name="elem" property="horaFim"/> : <bean:write name="elem" property="minutosFim"/>
                            </td>
                            <td>
                                <bean:write name="elem" property="tipo"/>
                            </td>
                            <td>
                                <bean:write name="elem" property="nomeSala"/>
                            </td>
                        </tr>
                        <% i++; %>
                    </logic:iterate>
                </table>
            </logic:present>
            <logic:notPresent name="listaAulasBean" scope="request">
                <table align="center" border='1' cellpadding='5'>
                    <tr align="center">
                        <td width='200'>
                            <font color='red'> <bean:message key="errors.existAulas"/> </font>
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
                            <bean:message key="label.add.Aulas"/>
                        </html:submit>
                    </td>
                    <td width="20"> </td>
                    <td>
                        <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation">
                            <bean:message key="label.remove.Aula"/>
                        </html:submit>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html:html>
