<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:define id="degreeCurricularPlanId" name="infoDegreeCurricularPlan" property="idInternal"/>
<ul style="list-style-type: square;">
	<li><html:link page="/manageCurricularCourseGroups.do?method=prepareInsertAreaCurricularCourseGroup" paramId="degreeCurricularPlanId" paramName="degreeCurricularPlanId"><bean:message key="label.manager.curricularCourseGroups.insert.area"/></html:link></li>
	<li><html:link page="/manageCurricularCourseGroups.do?method=prepareInsertOptionalCurricularCourseGroup" paramId="degreeCurricularPlanId" paramName="degreeCurricularPlanId"><bean:message key="label.manager.curricularCourseGroups.insert.optional"/></html:link></li>
</ul>
<table>
	<tr>
		<td class="listClasses-header"><bean:message key="label.group"/></td>
		<td class="listClasses-header"><bean:message key="label.branch"/></td>
		<td class="listClasses-header"><bean:message key="label.type"/></td>
		<td class="listClasses-header">&nbsp;</td>
		<td class="listClasses-header">&nbsp;</td>
	</tr>
	<logic:iterate id="group" name="curricularCourseGroups">
	<tr>
		<td class="listClasses"><html:link page='<%= "/manageCurricularCourseGroups.do?method=prepareEditCurricularCourseGroup&degreeCurricularPlanId=" + degreeCurricularPlanId.toString() %>' paramId="groupId" paramName="group" paramProperty="idInternal"> <bean:write name="group" property="name"/></html:link></td>
		<td class="listClasses"><bean:write name="group" property="infoBranch.name"/></td>
		<td class="listClasses"><bean:message name="group" property="type"/></td>
		<td class="listClasses"><html:link page="/manageCurricularCourseGroup.do?method=manageCourses" paramId="groupId" paramName="group" paramProperty="idInternal"><bean:message key="label.curricularCourseGroup.manage.courses"/></html:link></td>
		<td class="listClasses"><html:link page='<%= "/manageCurricularCourseGroups.do?method=deleteCurricularCourseGroup&degreeCurricularPlanId=" + degreeCurricularPlanId.toString() %>' paramId="groupId" paramName="group" paramProperty="idInternal"><bean:message key="label.delete"/></html:link></td>
	</tr>
	</logic:iterate>
</table>