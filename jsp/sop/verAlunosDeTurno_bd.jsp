<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoselected">
            	<p>O curso seleccionado &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
    </table>     
    <br/>
        <!-- Cria a tabela com os alunos -->
        <center>
                <b>
	                <h2><bean:message key="listAlunos.OfTurno"/> <bean:write name="infoTurno" property="nome" scope="request" filter="true"/></h2>                
                </b>
	    </center>
        <br/>
        <logic:present name="infoAlunosDeTurno" scope="request">
            <table align="center" border=1 cellpadding='5'>
                <tr align="center">
                    <td>
                        <font color='#034D7A'>
                            Número
                        </font>
                    </td>
                    <td>
                        <font color='#034D7A'>
                            Nome
                        </font>
                    </td>
                    <td>
                        <font color='#034D7A'>
                            Email
                        </font>
                    </td>
                </tr>
                <logic:iterate id="elem" name="infoAlunosDeTurno">
                    <tr align="center">
                        <td>
                            <bean:write name="elem" property="number"/>
                        </td>
                        <td>
                            <bean:write name="elem" property="infoPerson.nome"/>
                        </td>
                        <td>
                            <a href="mailto:"><bean:write name="elem" property="infoPerson.email"/></a>
                        </td>
                    </tr>
                </logic:iterate>
            </table>
        </logic:present>
        <logic:notPresent name="infoAlunosDeTurno" scope="request">
            <table align="center" border='1' cellpadding='5'>
                <tr align="center">
                    <td>
                        <font color='red'><bean:message key="errors.existAlunosDeTurno"/></font>
                    </td>
                </tr>
            </table>
        </logic:notPresent>