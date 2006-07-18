<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<bean:define id="groupId" name="groupWithAll" property="infoCurricularCourseGroup.idInternal"/>
<html:form action="/manageCurricularCourseGroup">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="removeCourses" />
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupId" property="groupId" value="<%= groupId.toString() %>"/>
<%--	
	<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlanId"/>
	<html:link module="/manager" module="/manager" page='<%= "/manageCurricularCourseGroups.do?method=viewCurricularCourseGroups&degreeCurricularPlanId=" + degreeCurricularPlanId.toString() %>'>Voltar Atr�s</html:link>
--%>
<br/>
<br/>
<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.curricularCourseGroup.courses"/>
<table>
<tr> 
<th class="listClasses-header">&nbsp</th><th class="listClasses-header">Disciplina</th>
<th class="listClasses-header">&nbsp</th><th class="listClasses-header">Codigo</th>
</tr>

<logic:iterate id="infoCurricularCourse" name="groupWithAll" property="infoCurricularCourses">
<tr> 
<td class="listClasses"><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.courseIds"  property="courseIds">
<bean:write name="infoCurricularCourse" property="idInternal"/>
</html:multibox> </td>
<td class="listClasses"><bean:write name="infoCurricularCourse" property="name"/></td>
<td class="listClasses"><bean:write name="infoCurricularCourse" property="code"/></td>
</tr>
</logic:iterate>
</table>
<br/>
<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
</html:form>

<html:form action="/manageCurricularCourseGroup">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="addCourses" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupId" property="groupId" value="<%= groupId.toString() %>"/>
	<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.curricularCourseGroup.coursesToAdd"/>
<table>
<tr> 
<td>&nbsp</td><th class="listClasses-header">Disciplina</th>
<td>&nbsp</td><th class="listClasses-header">Codigo</th>
</tr>
<logic:iterate id="infoCurricularCourse" name="groupWithAll" property="infoCurricularCoursesToAdd">
<tr> 
<td class="listClasses"><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.courseIdsToAdd"  property="courseIdsToAdd">
<bean:write name="infoCurricularCourse" property="idInternal"/>
</html:multibox> </td>
<td class="listClasses"><bean:write name="infoCurricularCourse" property="name"/></td>
<td class="listClasses"><bean:write name="infoCurricularCourse" property="code"/></td>
</tr>
</logic:iterate>
</table>
<br/>
<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
</html:form>