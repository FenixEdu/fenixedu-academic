<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<bean:define id="groupId" name="groupWithAll" property="infoCurricularCourseGroup.idInternal"/>
<html:form action="/manageCurricularCourseGroup">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="removeCourses" />
	
	<html:hidden property="groupId" value="<%= groupId.toString() %>"/>
<%--	
	<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlanId"/>
	<html:link module="/manager" module="/manager" page='<%= "/manageCurricularCourseGroups.do?method=viewCurricularCourseGroups&degreeCurricularPlanId=" + degreeCurricularPlanId.toString() %>'>Voltar Atr�s</html:link>
--%>
<br/>
<br/>
<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.curricularCourseGroup.courses"/>
<table>
<tr> 
<td class="listClasses-header">&nbsp</td><td class="listClasses-header">Disciplina</td>
<td class="listClasses-header">&nbsp</td><td class="listClasses-header">Codigo</td>
</tr>

<logic:iterate id="infoCurricularCourse" name="groupWithAll" property="infoCurricularCourses">
<tr> 
<td class="listClasses"><html:multibox  property="courseIds">
<bean:write name="infoCurricularCourse" property="idInternal"/>
</html:multibox> </td>
<td class="listClasses"><bean:write name="infoCurricularCourse" property="name"/></td>
<td class="listClasses"><bean:write name="infoCurricularCourse" property="code"/></td>
</tr>
</logic:iterate>
</table>
<br/>
<br/>
	<html:submit styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
</html:form>

<html:form action="/manageCurricularCourseGroup">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="addCourses" />
		<html:hidden property="groupId" value="<%= groupId.toString() %>"/>
	<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.curricularCourseGroup.coursesToAdd"/>
<table>
<tr> 
<td>&nbsp</td><td class="listClasses-header">Disciplina</td>
<td>&nbsp</td><td class="listClasses-header">Codigo</td>
</tr>
<logic:iterate id="infoCurricularCourse" name="groupWithAll" property="infoCurricularCoursesToAdd">
<tr> 
<td class="listClasses"><html:multibox  property="courseIdsToAdd">
<bean:write name="infoCurricularCourse" property="idInternal"/>
</html:multibox> </td>
<td class="listClasses"><bean:write name="infoCurricularCourse" property="name"/></td>
<td class="listClasses"><bean:write name="infoCurricularCourse" property="code"/></td>
</tr>
</logic:iterate>
</table>
<br/>
<br/>
	<html:submit styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
</html:form>