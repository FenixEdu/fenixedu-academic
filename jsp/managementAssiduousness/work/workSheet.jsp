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
      <td width="325"></td>
      <td width="275" align="left">
          <br>                         
          <bean:write name="numMecanografico" scope="session"/><p style="margin-top: -2px; margin-bottom: -2px">&nbsp;</p>
		  <bean:write name="pessoa" property="nome" scope="session" /><p style="margin-top: -2px; margin-bottom: -2px">&nbsp;</p>
          <bean:write name="centroCusto" property="sigla" scope="session" />
          <bean:write name="centroCusto" property="departamento" scope="session" /><br>
          <bean:write name="centroCusto" property="seccao1" scope="session" /><br>
          <bean:write name="centroCusto" property="seccao2" scope="session" /><br>           
      </td>
    </tr>
    <tr><td colspan="2"><br /></td></tr>
    <tr>
      <td colspan="2">
		<bean:define id="headers" name="headers" />      	
		<bean:define id="body" name="body" />	
		<tiles:insert definition="definition.report" flush="true">
			<tiles:put name="title" value="consultar.verbete" />
			<tiles:put name="headers" beanName="headers" />
			<tiles:put name="rows" beanName="body" />
		</tiles:insert>
      </td>
    </tr>    
    <tr><td colspan="2" align="left"><br /></td></tr>
    <tr>
      <td colspan="2">
      	<bean:define id="headers" name="headers2" />      	
		<bean:define id="body" name="body2" />	
		<tiles:insert definition="definition.report" flush="true">
			<tiles:put name="title" value="resumo.valores" />
			<tiles:put name="width" value="100%" />
			<tiles:put name="headers" beanName="headers" />
			<tiles:put name="rows" beanName="body" />
		</tiles:insert>
      </td>            
    </tr>
    <tr><td colspan="2" align="left"><br></td></tr>
    <tr>
      <td colspan="2">
      	<bean:define id="headers" name="headers3" />      	
		<bean:define id="body" name="body3" />	
		<tiles:insert definition="definition.report" flush="true">
			<tiles:put name="title" value="resumo.valores" />
			<tiles:put name="width" value="100%" />
			<tiles:put name="headers" beanName="headers" />
			<tiles:put name="rows" beanName="body" />
		</tiles:insert>
      </td>            
    </tr>
</table>
  