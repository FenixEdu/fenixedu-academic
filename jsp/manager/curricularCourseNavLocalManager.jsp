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
		<html:link page="<%= "/readCurricularCourse.do?degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&curricularCourseId=" + request.getParameter("curricularCourseId") %>">
		<bean:message key="label.manager.backReadCurricularCourse" />
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

<p><strong>&raquo; 
	<html:link page="/teachersManagement.do?method=firstPage">
		<bean:message key="link.manager.teachersManagement" />
	</html:link>
</strong></p>

<%--
<p><strong>&raquo; 
	<html:link page="/readExecutionPeriods.do">
		<bean:message key="label.manager.insert.executionCourse" />
	</html:link>
</strong></p>

<p><strong>&raquo; 
	<html:link page="/chooseDegreesForExecutionCourseMerge.do?method=prepareChooseDegreesAndExecutionPeriod">
		Juntar Disciplinas Execução
	</html:link>
</strong></p>
--%>
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
	<html:link page="/manageRoles.do?method=prepare">
		Gest&atilde;o de Privilégios
	</html:link>
</strong></p>


<p><strong>&raquo;
	<html:link page="/generateNewPassword.do?method=prepare&page=0"><bean:message key="link.operator.newPassword" /></html:link>
   </strong>	
</p>

<%--
<p><strong>&raquo; Gest&atilde;o de Privilégios</strong></p>
<ul>
  <li>Criar Role</html:link></li>
  <li>Atribuir Roles</li>
</ul>--%>