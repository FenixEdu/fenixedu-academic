<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="label.coordinator.degreeCurricular.active"/></h2>
<span class="error"><html:errors/></span>
<logic:present name="allActiveCurricularCourseScopes">
			<logic:iterate id="curricularCourseScopeElem" name="allActiveCurricularCourseScopes" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope" length="1">
				<bean:define id="currentSemester" name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/>
			</logic:iterate>
			<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
	<table>
		<tr>
			<th class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularYear" bundle="MANAGER_RESOURCES" /></th>
			<th class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularSemester" bundle="MANAGER_RESOURCES" /></th>
			<th class="listClasses-header"><bean:message key="label.curricularCourse"/></th>
			<th class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.branch" bundle="MANAGER_RESOURCES" /></th>
		</tr>
			<logic:iterate id="curricularCourseScopeElem" name="allActiveCurricularCourseScopes" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope">
				<logic:notEqual name="curricularCourseScopeElem" property="infoCurricularSemester.semester" value="<%= pageContext.findAttribute("currentSemester").toString()%>">
					<tr>
						<th class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularYear" bundle="MANAGER_RESOURCES" /></th>
						<th class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.curricularSemester" bundle="MANAGER_RESOURCES" /></th>
						<th class="listClasses-header"><bean:message key="label.curricularCourse"/></th>
						<th class="listClasses-header"><bean:message key="label.manager.curricularCourseScope.branch" bundle="MANAGER_RESOURCES" /></th>
					</tr>
					<bean:define id="currentSemester" name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/>
				</logic:notEqual>
				<tr>
					<td class="listClasses"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/></td>
					<td class="listClasses"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/></td>
					<td class="listClasses" style="text-align:left">
					<bean:define id="curricularCourseCode" name="curricularCourseScopeElem" property="infoCurricularCourse.idInternal"/>
						<html:link page="<%="/degreeCurricularPlanManagement.do?method=viewActiveCurricularCourseInformation&amp;infoCurricularCourseCode=" + curricularCourseCode + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") %>">
							<bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.name"/>
						</html:link>
					</td>
					<td class="listClasses">
						&nbsp;<bean:write name="curricularCourseScopeElem" property="infoBranch.prettyCode"/>
					</td>
				</tr>
			</logic:iterate>
	</table>
</logic:present>
