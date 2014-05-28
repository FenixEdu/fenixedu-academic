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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<table>
	<tr>
			<td>
				<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.execution.course.name"/></h3>
			</td>
			<td>
				
				<bean:define id="executionCourseName" name="executionCourseName"/>
				<h2><b><bean:write name="executionCourseName"/></b></h2>
			</td>	
	</tr>
	<tr>
		<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.teachers.modification"/></h3>
		<span class="error"><!-- Error messages go here --><html:errors /></span>
	</tr>
</table>

<ul style="list-style-type: square;">
	<li><html:link page="<%="/insertProfessorShipByNumber.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId") + "&amp;executionCourseId=" + request.getParameter("executionCourseId")%>" paramId="executionCourseName" paramName="executionCourseName">
			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.professorShip.by.number"/>
		</html:link>
	</li>
</ul>
	
<logic:notPresent name="infoTeachersList">
	<i><bean:message bundle="MANAGER_RESOURCES" key="label.manager.teachers.nonExisting"/></i>
</logic:notPresent>
	
<logic:present name="infoTeachersList" scope="request">
	<html:form action="/saveTeachersBody" >
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getParameter("degreeId") %>"/>	
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseId" property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= request.getParameter("executionCourseId") %>"/>	
		
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseID" property="curricularCourseID" value="<%= request.getParameter("curricularCourseId") %>"/>	
		<table width="80%" cellpadding="0" border="0">
			<tr>
				<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.teacher.name" />
				</th>
				<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.teacher.number" />
				</th>
				<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.teaches" />
				</th>
				<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.responsible" />
				</th>	
			</tr>
			<logic:iterate id="infoTeacher" name="infoTeachersList">
				<tr>
					<bean:define id="teacherId" name="infoTeacher" property="externalId"/>	
					<td class="listClasses"><bean:write name="infoTeacher" property="infoPerson.nome"/>
					</td>
					<td class="listClasses"><bean:write name="infoTeacher" property="teacherId"/>
					</td>
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.professorShipTeachersIds" property="professorShipTeachersIds">
							<bean:write name="teacherId"/>
						</html:multibox>
					</td>
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.responsibleTeachersIds" property="responsibleTeachersIds">
							<bean:write name="teacherId"/>
						</html:multibox>
					</td>
	 			</tr>
	 		</logic:iterate>
			<logic:present name="infoNonAffiliatedTeachers">
				<logic:iterate id="infoNonAffiliatedTeacher" name="infoNonAffiliatedTeachers">
					<tr>
						<bean:define id="nonAffiliatedTeacherId" name="infoNonAffiliatedTeacher" property="externalId"/>	
						<td class="listClasses">
							<bean:write name="infoNonAffiliatedTeacher" property="name"/>
						</td>
						<td class="listClasses"></td>
						<td class="listClasses">
							<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.nonAffiliatedTeachersIds" property="nonAffiliatedTeachersIds">
								<bean:write name="nonAffiliatedTeacherId"/>
							</html:multibox>
						</td>
						<td class="listClasses"></td>
		 			</tr>
		 		</logic:iterate>
	 		</logic:present>		 							
		</table>
		<br/>
		<html:submit><bean:message bundle="MANAGER_RESOURCES" key="label.manager.save.modifications"/></html:submit>
	</html:form> 	
</logic:present>
