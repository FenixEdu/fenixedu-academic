<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

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
	</tr>
</table>

<ul style="list-style-type: square;">
	<li><html:link module="/manager" page="<%="/insertProfessorShipByNumber.do?method=prepareInsert&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId") + "&amp;executionCourseId=" + request.getParameter("executionCourseId")%>" paramId="executionCourseName" paramName="executionCourseName">
			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.professorShip.by.number"/>
		</html:link>
	</li>
	<li><html:link module="/manager" page="<%="/insertProfessorShipNonAffiliatedTeacher.do?method=prepare&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")  + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId") + "&amp;executionCourseId=" + request.getParameter("executionCourseId")%>" paramId="executionCourseName" paramName="executionCourseName">
			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.professorShip.notAffiliated.to.ist"/>
		</html:link>
	</li>
</ul>
	
<logic:notPresent name="infoTeachersList">
	<i><bean:message bundle="MANAGER_RESOURCES" key="label.manager.teachers.nonExisting"/></i>
</logic:notPresent>
	
<logic:present name="infoTeachersList" scope="request">
	<html:form action="/saveTeachersBody" method="get">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getParameter("degreeId") %>"/>	
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseId" property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= request.getParameter("executionCourseId") %>"/>
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
					<bean:define id="teacherId" name="infoTeacher" property="idInternal"/>	
					<td class="listClasses"><bean:write name="infoTeacher" property="infoPerson.nome"/>
					</td>
					<td class="listClasses"><bean:write name="infoTeacher" property="teacherNumber"/>
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
						<bean:define id="nonAffiliatedTeacherId" name="infoNonAffiliatedTeacher" property="idInternal"/>	
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
		<br>
		<html:submit><bean:message bundle="MANAGER_RESOURCES" key="label.manager.save.modifications"/></html:submit>
	</html:form> 	
</logic:present>
