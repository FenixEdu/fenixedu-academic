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
      <td width="325">&nbsp;&nbsp;</td>
      <td width="275" align="left">
        <font size="2" face="Arial, Helvetica, sans-serif">
          <br>                         
          <bean:write name="numMecanografico" scope="session"/><p style="margin-top: -5; margin-bottom:-5">&nbsp;</p>
		  <bean:write name="pessoa" property="nome" scope="session" /><p style="margin-top: -5; margin-bottom:-5">&nbsp;</p>
          <bean:write name="centroCusto" property="sigla" scope="session" />&nbsp;
          <bean:write name="centroCusto" property="departamento" scope="session" /><br>
          <bean:write name="centroCusto" property="seccao1" scope="session" /><br>
          <bean:write name="centroCusto" property="seccao2" scope="session" /><br>           
        </font>
      </td>
    </tr>
    <tr>
      <td colspan='2' align="center">
        &nbsp;&nbsp;&nbsp; <p></p><p></p><p></p><br>
      </td>
    </tr>
    <%--<tr>
      <td colspan='2'>
          <div style='width=580' align="center">
            <font size="5" face="Arial, Helvetica, sans-serif">
              <bean:message key="consultar.verbete" />              
            </font>
          </div><p style="margin-top: -5; margin-bottom:-5">&nbsp;</p>
      </td>
    </tr> --%>

    <tr>
      <td  colspan='2'>
		<bean:define id="headers" name="MostrarListaForm" property="headers" />      	
		<bean:define id="body" name="MostrarListaForm" property="body" />	
		<tiles:insert definition="definition.report" flush="true">
			<tiles:put name="title" value="consultar.verbete" />
			<tiles:put name="headers" beanName="headers" />
			<tiles:put name="rows" beanName="body" />
		</tiles:insert>
		
       <%-- <app:listagem name="MostrarListaForm" headers="headers" fontSize="2.5" border="1" tabWidth="600" tabAlign="center" body="body" scope="session" />         --%>
      </td>
    </tr>    
    <tr>
      <td colspan='2' align="left"><br></td>
    </tr>

    <%--<tr>
      <td colspan='2' align="center">
        <font size="3" face="Arial, Helvetica, sans-serif">
              <b><bean:message key="resumo.valores" /></b>
        </font><p style="margin-top: -5; margin-bottom:-5">&nbsp;</p>
      </td>
    </tr>--%>
    
    <tr>
      <td  colspan='2'>
      	<bean:define id="headers" name="MostrarListaForm" property="headers2" />      	
		<bean:define id="body" name="MostrarListaForm" property="body2" />	
		<tiles:insert definition="definition.report" flush="true">
			<tiles:put name="title" value="resumo.valores" />
			<tiles:put name="headers" beanName="headers" />
			<tiles:put name="rows" beanName="body" />
		</tiles:insert>
      	
        <%-- <app:listagem name="MostrarListaForm" headers="headers2" fontSize="2.5" border="1" tabWidth="600" tabAlign="center" body="body2" scope="session" />         --%>
      </td>
    </tr>
    <tr>
      <td colspan='2' align="left"><br></td>
    </tr>
	<%--    
    <tr>
      <td colspan='2'>
        <table width="600" border="0" cellpadding="0" cellspacing="0">
		    <tr>
		      <td align="center" width="300">
		        <font size="2" face="Arial, Helvetica, sans-serif">
		              <b><bean:message key="trabalhoExtra.diurno" /></b>
		        </font><p style="margin-top: -5; margin-bottom:-5">&nbsp;</p>
		      </td>
		      <td align="center" width="300">
		        <font size="2" face="Arial, Helvetica, sans-serif">
		              <b><bean:message key="trabalhoExtra.nocturno" /></b>
		        </font><p style="margin-top: -5; margin-bottom:-5">&nbsp;</p>
		      </td>
		    </tr>
    	</table>
      </td>
    </tr>

    <tr>
      <td  colspan='2'>
        <app:listagem name="MostrarListaForm" headers="headers3" fontSize="2.5" border="1" tabWidth="600" tabAlign="center" body="body3" scope="session" />         
      </td>
    </tr> --%>
    <tr>
      <td colspan='2' align="center">
        &nbsp;&nbsp;&nbsp; <p></p><p></p>
      </td>
    </tr>
    
    <tr>
      <td  colspan='2'>
        <table border="0">
      	  <tr>
            <td width="300" align="right">
              <html:form action="/consultarFuncionarioMostrar" focus="submit">
                <html:submit property="submit" styleClass="inputbutton">
                  <bean:message key="link.novaConsulta"/>
                </html:submit>
    	      </html:form>		   			
              &nbsp;&nbsp;&nbsp;
            </td>
            <td width="300" align="left">
              &nbsp;&nbsp;&nbsp;
              <html:link forward="ImprimirVerbetesAction" paramId="impressaoFuncionario" paramName="numMecanografico"><bean:message key="link.imprimir"/></html:link>
              &nbsp;&nbsp;&nbsp;
            </td>
            <%--
            <td width="200" align="left">
              &nbsp;&nbsp;&nbsp;
              <html:link forward="PortalAssiduidadeAction"><bean:message key="link.menu"/></html:link> 
            </td>
            --%>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</div>
