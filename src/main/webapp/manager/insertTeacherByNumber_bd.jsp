<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

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
<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/insertProfessorShipByNumber" >
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
	
	<br/>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>			
</html:form>