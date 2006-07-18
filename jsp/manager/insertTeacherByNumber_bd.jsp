<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
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
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insert" /> 
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getParameter("degreeId") %>"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseId" property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= request.getParameter("executionCourseId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseName" property="executionCourseName" value="<%= request.getParameter("executionCourseName") %>"/>
	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.insert.teacher.number"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.number" size="5" property="number" />
			</td>
		</tr>
	</table>
	
	<br>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>			
</html:form>