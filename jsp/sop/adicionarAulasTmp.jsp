<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:html xhtml="true">
    <head>
        <title> Adicionar Aulas </title>
    </head>
    <body>
        <jsp:include page="context.jsp"/><br/>
        <br/>
        <center><font color='#034D7A' size='5'> <b> Adicionar Aulas </b> </font></center>
        <br/>
        <br/>
        <html:errors/>
        <html:form action="/adicionarAulasTmp">
            <center> <b> Aulas disponï¿½veis </b> </center>
            <br/>
            <!-- Cria a tabela das aulas -->
            <logic:present name="listaAulasDeDisciplinaETipoBean" scope="request">
                <table align="center" border=1 cellpadding='5'>
                    <%! int i; %>
                    <% i = 0; %>
                    <tr align='center'>
                        <th>
                        </th>
                        <th>
                            Dia da Semana
                        </th>
                        <th>
                            Inï¿½cio
                        </th>
                        <th>
                            Fim
                        </th>
                        <th>
                            Tipo
                        </th>
                        <th>
                            Sala
                        </th>
                    </tr>
                    <logic:iterate id="elem" name="listaAulasDeDisciplinaETipoBean" scope="request">
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
            <logic:notPresent name="listaAulasDeDisciplinaETipoBean" scope="request">
                <table align="center" border='1' cellpadding='5'>
                    <tr align="center">
                        <td>
                            <font color='red'> Não existem aulas da disciplina e tipo escolhidos </font>
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
                            Adicionar Aula
                        </html:submit>
                    </td>
                </tr>
            </table>
            <br/>
        </html:form>
    </body>
</html:html>
