<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h3><bean:message key="message.insert.professorShip" /></h3>

<span class="error"><html:errors/></span>

<html:form action="/insertProfessorShipByNumber" method="get">
	<html:hidden property="method" value="insert" /> 
	<html:hidden property="page" value="1"/>
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>	
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	<html:hidden property="executionCourseId" value="<%= request.getParameter("executionCourseId") %>"/>
	<table>
		<tr>
			<td>
				<bean:message key="message.insert.teacher.number"/>
			</td>
			<td>
				<html:text size="5" property="number" />
			</td>
		</tr>
	</table>
	
	<br>

	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>			
</html:form>