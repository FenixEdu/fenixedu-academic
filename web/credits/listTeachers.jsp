<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<logic:iterate id="departmentTeachersDTO" name="departmentTeachersDTOList">
	<html:form action="/creditsManagement">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepare"/>
		<h2><bean:write name="departmentTeachersDTO" property="infoDepartment.name"/></h2>
		<bean:size id="numberOfTeachers" name="departmentTeachersDTO" property="infoTeacherList"/>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.teacherOID" property="teacherOID" value="0" style="display:inline">
			<logic:iterate id="infoTeacher" name="departmentTeachersDTO" property="infoTeacherList">
				<option value="<jsp:getProperty name='infoTeacher' property='idInternal'/>">
					<jsp:getProperty name="infoTeacher" property="teacherNumber"/> - <bean:write name="infoTeacher" property="infoPerson.nome"/>
				</option>
			</logic:iterate>
		</html:select>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Editar" styleClass="inputbutton"/>
	</html:form>
</logic:iterate>

<%--
<logic:iterate id="departmentTeachersDTO" name="departmentTeachersDTOList">
	<h2><bean:write name="departmentTeachersDTO" property="infoDepartment.name"/></h2>
	<bean:size id="numberOfTeachers" name="departmentTeachersDTO" property="infoTeacherList"/>
	<logic:notEqual name="numberOfTeachers" value="0">
		<table>
			<logic:iterate id="infoTeacher" name="departmentTeachersDTO" property="infoTeacherList">
				<tr>
					<td>
						<jsp:getProperty name="infoTeacher" property="teacherNumber"/>
					</td>
					<td>
						<html:link page="/creditsManagement.do?method=prepare&amp;page=0" paramName="infoTeacher" paramProperty="idInternal" paramId	="teacherOID">
							<bean:write name="infoTeacher" property="infoPerson.nome"/>
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table> 
	</logic:notEqual>	
</logic:iterate> --%>