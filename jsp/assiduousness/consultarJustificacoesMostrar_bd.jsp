<%@page contentType="text/html"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-form.tld" prefix="form" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<div align="left">
  <table align="center">
    <tr>
      <td> 
        <span class="error"><html:errors/></span>
      </td>
    </tr>
    <tr>
      <td colspan='2'>
            <h2>
              <bean:message key="consultar.justificacoes" />:&nbsp;&nbsp;
              <bean:write name="numMecanografico" scope="session"/>
            </h2>
      <br>
      </td>
    </tr>
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
      </td>
    </tr>
    <tr>
      <td colspan='2' align="center">
        &nbsp;&nbsp;&nbsp; <p></p><p></p>
      </td>
    </tr>
    <tr>
      <td colspan='2' align="center">
        <html:form action="/consultarFuncionarioMostrar" focus="submit">
    	  <html:submit property="submit" styleClass="inputbutton">
            <bean:message key="link.novaConsulta"/>
          </html:submit>
    	</html:form>		   			
      </td>
    </tr>
  </table>
</div>