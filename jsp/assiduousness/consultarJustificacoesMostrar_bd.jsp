<%@page contentType="text/html"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<div align="left">
  <table >
    <tr bgcolor='#ffffff'>
      <td> 
        <html:errors/>
      </td>
    </tr>
    <tr>
      <td colspan='2'>
          <div style='width=580' align="right">
            <font size="5" face="Arial, Helvetica, sans-serif">
              <bean:message key="consultar.justificacoes" />&nbsp;&nbsp;
              <bean:write name="numMecanografico" scope="session"/>
            </font>
          </div><br>
      </td>
    </tr>
    <%-- <tr>
      <td  colspan='2' align="center">
        <font size="2" face="Arial, Helvetica, sans-serif">
          <b><bean:message key="prompt.listaJustificacoes" /></b>
        </font>
      </td>
    </tr> --%>
    <tr>
      <td  colspan='2'>
      	<bean:define id="headers" name="MostrarListaForm" property="headers" />      	
		<bean:define id="body" name="MostrarListaForm" property="body" />	
		<tiles:insert definition="definition.report" flush="true">
			<tiles:put name="emptyRows" value="error.listaJustificacao.semOcorrencias" />
			<tiles:put name="title" value="prompt.listaJustificacoes" />
			<tiles:put name="headers" beanName="headers" />
			<tiles:put name="rows" beanName="body" />
		</tiles:insert>      	
        <%-- <app:listagem name="MostrarListaForm" headers="headers" border="1" tabAlign="center" body="body" scope="session" />         --%>
      </td>
    </tr>
    <tr>
      <td colspan='2' align="center">
        &nbsp;&nbsp;&nbsp; <p></p><p></p>
      </td>
    </tr>
    <tr>
      <td colspan='2' align="center">
          <html:link forward="ConsultarFuncionarioMostrarAction"><bean:message key="link.novaConsulta"/></html:link>
        &nbsp;&nbsp;&nbsp;
      </td>
      <%--
      <td align="left">
        &nbsp;&nbsp;&nbsp;
        <html:link forward="PortalAssiduidadeAction"><bean:message key="link.menu"/></html:link> 
      </td>
      --%>
    </tr>
  </table>
</div>