<%@page contentType="text/html"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
  <table align="center">
    <tr>
      <td> 
        <span class="error"><html:errors/></span>
      </td>
    </tr>
    <tr>
      <td colspan='2'>
  			<h2>       
              <bean:message key="consultar.marcacao" />
			</h2>          
          <br>
      </td>
    </tr>
<!--    <tr>
      <td  colspan="2" align="center">
        <font size="2" face="Arial, Helvetica, sans-serif">
          <b><bean:message key="prompt.listaMarcacoesPonto" /></b>
        </font>
      </td>
    </tr> -->
    <tr>
      <td colspan="2">
      	<bean:define id="headers" name="MostrarListaForm" property="headers" />      	
		<bean:define id="body" name="MostrarListaForm" property="body" />	
		<tiles:insert definition="definition.report" flush="true">
			<tiles:put name="emptyRows" value="error.listaMarcacaoPonto.semOcorrencias" />
			<tiles:put name="title" value="prompt.listaMarcacoesPonto" />
			<tiles:put name="headers" beanName="headers" />
			<tiles:put name="rows" beanName="body" />
		</tiles:insert>        	
        <%-- <app:listagem name="MostrarListaForm" headers="headers" border="1" tabAlign="center" body="body" scope="session" />         --%>
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <br />
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <logic:equal name="linkBotao" scope="session" value="PrepararConsultarMarcacaoPontoAction">
          <html:link forward="PrepararConsultarMarcacaoPontoAction"><bean:message key="link.novaConsulta"/></html:link>
        </logic:equal>
        <logic:equal name="linkBotao" scope="session" value="ConsultarFuncionarioMostrar">
          <html:link forward="ConsultarFuncionarioMostrarAction"><bean:message key="link.novaConsulta"/></html:link>
        </logic:equal>
        &nbsp;&nbsp;&nbsp;
    <tr>	
      <td colspan='2' align="center">
        <html:form action="/consultarFuncionarioMostrar" focus="submit">
    	  <html:submit property="submit" styleClass="inputbutton">
            <bean:message key="link.novaConsulta"/>
          </html:submit>
    	</html:form>		   			
      </td>
      <%--
      <td align="left">
        &nbsp;&nbsp;&nbsp;
        <html:link forward="PortalAssiduidadeAction"><bean:message key="link.menu"/></html:link> 
      </td>
      --%>
    </tr>
  </table>
 <br />