<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<center>
	<img alt=""  src="<%= request.getContextPath() %>/images/logo-fenix.gif" width="100" height="100"/>
</center>

<p><strong>&raquo; 
		<html:link page="/readDegrees.do">
			<bean:message key="label.manager.readDegrees" />
		</html:link>
</strong></p>

<p><strong>&nbsp; &raquo;
		<html:link page="<%= "/readDegree.do?degreeId=" + request.getParameter("degreeId") %>">
		<bean:message key="label.manager.backReadDegree" />
		</html:link>
</strong></p>

<p><strong>&nbsp; &nbsp; &raquo;
		<html:link page="<%= "/readDegreeCurricularPlan.do?degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") %>">
		<bean:message key="label.manager.backReadDegreeCurricularPlan" />
		</html:link>
</strong></p>

<p><strong>&nbsp; &nbsp; &nbsp;&raquo;
		<html:link page="<%= "/manageBranches.do?method=showBranches&degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>">
		<bean:message key="label.manager.backManageBranches" />
		</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/manageExecutionPeriods.do?method=prepare">
		Gest&atilde;o de Periodos Execução
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/manageCache.do?method=prepare">
		Gest&atilde;o da Cache
	</html:link>
</strong></p>

<%--
<p><strong>&raquo; Gest&atilde;o de Privilégios</strong></p>
<ul>
  <li>Criar Role</html:link></li>
  <li>Atribuir Roles</li>
</ul>--%>