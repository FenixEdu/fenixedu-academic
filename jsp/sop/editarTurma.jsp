<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html>
    <app:checkLogon/>
    <head>
        <logic:equal name="turmaFormBean" property="accao" scope="session" value="Criar">
            <title> Criar Turma </title>
        </logic:equal>
        <logic:equal name="turmaFormBean" property="accao" scope="session" value="Editar">
            <title> Editar Turma </title>
        </logic:equal>
        <html:base/>
    </head>
    <body>
        <jsp:include page="context.jsp"/><br/>
        <br/>
        <logic:equal name="turmaFormBean" property="accao" scope="session" value="Criar">
            <center><font color='#034D7A' size='5'> <b> Criar Turma </b> </font></center>
        </logic:equal>
        <logic:equal name="turmaFormBean" property="accao" scope="session" value="Editar">
            <center><font color='#034D7A' size='5'> <b> Editar Turma </b> </font></center>
        </logic:equal>
        <br/>
        <br/>
        <html:errors/>
        <html:form action="/editarTurma">
            <html:hidden property="accao"/>
            <html:hidden property="semestre"/>
            <html:hidden property="codigoInterno"/>
            <html:hidden property="chaveLicenciaturaExecucao"/>
            <html:hidden property="anoCurricular"/>
            <logic:equal name="turmaFormBean" property="accao" scope="session" value="Editar">
                <html:hidden property="chaveRamo"/>
            </logic:equal>
            <table align="center" cellspacing="10">
                <tr>
                    <td align="right" height="40">
                        Nome
                    </td>
                    <td align="left" height="40">
                        <html:text property="nome" size="11" maxlength="20"/>
                    </td>
                </tr>
                <logic:equal name="turmaFormBean" property="accao" scope="session" value="Criar">
                    <logic:present name="ramos" scope="session">
                         <tr>
                            <td align="right" height="40">
                                Ramo
                            </td>
                            <td align="left" height="40">
                                <html:select property="chaveRamo" size="1">
                                    <html:options collection="ramos" property="value" labelProperty="label"/>
                                </html:select>
                            </td>
                        </tr>
                    </logic:present>
                    <logic:notPresent name="ramos" scope="session">
                        <html:hidden property="chaveRamo"/>
                    </logic:notPresent>
                </logic:equal>
            </table>
            <logic:equal name="turmaFormBean" property="accao" scope="session" value="Editar">
                <br/>
                <table align="center">
                    <tr align="center">
                        <td>
                            <html:link page="/prepararEditarHorario.do"> Editar Horário </html:link>
                        </td>
                    </tr>
                </table>
                <br/>
            </logic:equal>
            <br/>
            <table align="center">
                <tr align="center">
                    <td>
                        <html:submit>
                            Guardar
                        </html:submit>
                    </td>
                    <td width="20"> </td>
                    <td>
                        <html:reset>
                            Limpar
                        </html:reset>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html:html>

