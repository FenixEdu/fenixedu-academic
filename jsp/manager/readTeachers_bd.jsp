<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<table>
	<tr>
		<logic:present name="infoCurricularCourse">
			<td>
				<h3><bean:message key="label.manager.curricularCourse.administrating"/></h3>
			</td>
		<%--	<td>
				<bean:define id="curricularCourseName" value="<%=request.getParameter("infoCurricularCourse")%>"/>
				<h2><b><bean:write name="curricularCourseName"/></b></h2>
			</td>--%>
		</logic:present>		
	</tr>
</table>
<%--
<br>

<span class="error"><html:errors/></span>
	
<h3><bean:message key="label.manager.executionCourses"/></h3>

	<logic:empty name="infoTeachersList">
		<i><bean:message key="label.manager.teachers.nonExisting"/></i>
	</logic:empty>
	
	<logic:present name="infoTeachersList" scope="request">
	
	
	
		<logic:notEmpty name="infoTeachersList">
			<table width="80%" cellpadding="0" border="0">
				<tr>
					<td class="listClasses-header"><bean:message key="label.manager.executionCourse.name" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.executionCourse.code" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.executionCourse.executionPeriod" />
					</td>
					
				</tr>
				
				<logic:iterate id="infoTeacher" name="infoTeachersList">
					<%--<bean:define id="infoExecutionPeriod" name="infoTeacher" property="infoExecutionPeriod"/>--%>
				<%--	<tr>	 			
						<td class="listClasses"><bean:write name="infoTeacher" property="infoPerson.nome"/>
						</td>
						<td class="listClasses"><bean:write name="infoTeacher" property="teacherNumber"/>
						</td>
						
	 				</tr>
	 			</logic:iterate>						
			</table>
		</logic:notEmpty>	 	
	</logic:present>
--%>