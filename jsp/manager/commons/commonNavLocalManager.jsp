<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<center>
	<img alt=""  src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/>
</center>

<br />
<strong>Gestão de Execuções</strong>
<p><strong>&raquo; 
		<html:link page="/readDegrees.do">
			<bean:message key="label.manager.readDegrees" />
		</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/manageExecutionPeriods.do?method=prepare">
		Gest&atilde;o de Periodos Execução
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/executionCourseManagement.do?method=firstPage">
		<bean:message key="label.manager.executionCourseManagement" />
	</html:link>
</strong></p>

<br />
<strong>Gestão de Pessoal</strong>
<p><strong>&raquo; 
	<html:link page="/teachersManagement.do?method=firstPage">
		<bean:message key="link.manager.teachersManagement" />
	</html:link>
</strong></p>

<p><strong>&raquo;
	<html:link page="/personManagement.do?method=firstPage">
		<bean:message key="label.manager.personManagement" />
	</html:link>
</strong></p>

<br />
<strong>Gestão do Sistema</strong>
<p><strong>&raquo; 

	<html:link page="/manageCache.do?method=prepare">
		Gest&atilde;o da Cache
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/manageAdvisories.do?method=prepare">
		Gest&atilde;o da Avisos
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/monitorServices.do?method=monitor">
		Monitorização de Serviços
	</html:link>
</strong></p>