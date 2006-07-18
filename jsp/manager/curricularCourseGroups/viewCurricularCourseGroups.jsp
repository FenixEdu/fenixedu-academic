<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:define id="degreeCurricularPlanId" name="infoDegreeCurricularPlan" property="idInternal"/>
<bean:define id="degreeId" name="degreeId"/>

<ul style="list-style-type: square;">

	<li><html:link module="/manager" module="/manager" page='<%="/manageCurricularCourseGroups.do?method=prepareInsertAreaCurricularCourseGroup&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>'><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.curricularCourseGroups.insert.area"/></html:link></li>
	<li><html:link module="/manager" module="/manager" page='<%="/manageCurricularCourseGroups.do?method=prepareInsertOptionalCurricularCourseGroup&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>'><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.curricularCourseGroups.insert.optional"/></html:link></li>
<%--
	<li><html:link module="/manager" module="/manager" page="/manageCurricularCourseGroups.do?method=prepareInsertAreaCurricularCourseGroup" paramId="degreeCurricularPlanId" paramName="degreeCurricularPlanId" paramId="degreeId" paramName="degreeId"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.curricularCourseGroups.insert.area"/></html:link></li>
	<li><html:link module="/manager" module="/manager" page="/manageCurricularCourseGroups.do?method=prepareInsertOptionalCurricularCourseGroup" paramId="degreeCurricularPlanId" paramName="degreeCurricularPlanId" paramId="degreeId" paramName="degreeId"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.curricularCourseGroups.insert.optional"/></html:link></li>
--%>
</ul>
<table>
	<tr>
		<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.group"/></th>
		<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.branch"/></th>
		<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.type"/></th>
		<th class="listClasses-header">&nbsp;</th>
		<th class="listClasses-header">&nbsp;</th>
	</tr>
	<logic:iterate id="group" name="curricularCourseGroups">
	<tr>
		<td class="listClasses"><html:link module="/manager" module="/manager" page='<%= "/manageCurricularCourseGroups.do?method=prepareEditCurricularCourseGroup&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + degreeCurricularPlanId.toString() %>' paramId="groupId" paramName="group" paramProperty="idInternal"> <bean:write name="group" property="name"/></html:link></td>
		<%--<td class="listClasses"><html:link module="/manager" module="/manager" page='<%= "/manageCurricularCourseGroups.do?method=prepareEditCurricularCourseGroup&degreeCurricularPlanId=" + degreeCurricularPlanId.toString() %>' paramId="groupId" paramName="group" paramProperty="idInternal"> <bean:write name="group" property="name"/></html:link></td>--%>
		<td class="listClasses">&nbsp;<bean:write name="group" property="infoBranch.name"/></td>
		<td class="listClasses"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" name="group" property="type"/></td>
		<td class="listClasses"><html:link module="/manager" module="/manager" page='<%= "/manageCurricularCourseGroup.do?method=manageCourses&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + degreeCurricularPlanId.toString() %>' paramId="groupId" paramName="group" paramProperty="idInternal"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.curricularCourseGroup.manage.courses"/></html:link></td>
		<td class="listClasses"><html:link module="/manager" module="/manager" page='<%= "/manageCurricularCourseGroups.do?method=deleteCurricularCourseGroup&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + degreeCurricularPlanId.toString() %>' paramId="groupId" paramName="group" paramProperty="idInternal"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.delete"/></html:link></td>
		<%--<td class="listClasses"><html:link module="/manager" module="/manager" page="/manageCurricularCourseGroup.do?method=manageCourses" paramId="groupId" paramName="group" paramProperty="idInternal"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.curricularCourseGroup.manage.courses"/></html:link></td>
		<td class="listClasses"><html:link module="/manager" module="/manager" page='<%= "/manageCurricularCourseGroups.do?method=deleteCurricularCourseGroup&degreeCurricularPlanId=" + degreeCurricularPlanId.toString() %>' paramId="groupId" paramName="group" paramProperty="idInternal"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.delete"/></html:link></td>--%>
	</tr>
	</logic:iterate>
</table>