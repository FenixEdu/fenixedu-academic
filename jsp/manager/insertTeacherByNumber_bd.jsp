<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<table>
	<tr>
			<td>
				<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.execution.course.name"/></h3>
			</td>
			<td>
			<bean:parameter id="executionCourseName" name="executionCourseName"/>
				<h2><b><bean:write name="executionCourseName" /></b></h2>
			</td>	
	</tr>
	<tr>
      		<h3><bean:message bundle="MANAGER_RESOURCES" key="message.insert.professorShip" /></h3>
	</tr>
</table>
<span class="error"><html:errors/></span>

<html:form action="/insertProfessorShipByNumber" method="get">
	<html:hidden property="method" value="insert" /> 
	<html:hidden property="page" value="1"/>
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>	
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	<html:hidden property="executionCourseId" value="<%= request.getParameter("executionCourseId") %>"/>
	<html:hidden property="executionCourseName" value="<%= request.getParameter("executionCourseName") %>"/>
	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.insert.teacher.number"/>
			</td>
			<td>
				<html:text size="5" property="number" />
			</td>
		</tr>
	</table>
	
	<br>

	<html:submit styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>			
</html:form>