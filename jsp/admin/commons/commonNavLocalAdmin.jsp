<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<center>
	<img alt=""  src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/>
</center>

<p><strong>&raquo; 
	<html:link page="/manageExecutionPeriods.do?method=prepare">
		Gest&atilde;o de Periodos Execução
	</html:link>
</strong></p>

<p><strong>&raquo; Gest&atilde;o da Cache</strong></p>

<p><strong>&raquo; Gest&atilde;o de Previlégios</strong></p>
<ul>
  <li>Criar Role</html:link></li>
  <li>Atribuir Roles</li>
</ul>

