<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<h2><bean:message key="label.coordinator.degreeCurricular.active"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:present name="allActiveCurricularCourseScopes">
			<logic:iterate id="curricularCourseScopeElem" name="allActiveCurricularCourseScopes" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope" length="1">
				<bean:define id="currentSemester" name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/>
			</logic:iterate>
			<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
	<table class="tstyle4">
		<tr>
			<th><bean:message key="label.manager.curricularCourseScope.curricularYear" bundle="MANAGER_RESOURCES" /></th>
			<th><bean:message key="label.manager.curricularCourseScope.curricularSemester" bundle="MANAGER_RESOURCES" /></th>
			<th><bean:message key="label.curricularCourse"/></th>
			<th><bean:message key="label.manager.curricularCourseScope.branch" bundle="MANAGER_RESOURCES" /></th>
		</tr>
			<logic:iterate id="curricularCourseScopeElem" name="allActiveCurricularCourseScopes" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope">
				<logic:notEqual name="curricularCourseScopeElem" property="infoCurricularSemester.semester" value="<%= pageContext.findAttribute("currentSemester").toString()%>">
					<tr>
						<th><bean:message key="label.manager.curricularCourseScope.curricularYear" bundle="MANAGER_RESOURCES" /></th>
						<th><bean:message key="label.manager.curricularCourseScope.curricularSemester" bundle="MANAGER_RESOURCES" /></th>
						<th><bean:message key="label.curricularCourse"/></th>
						<th><bean:message key="label.manager.curricularCourseScope.branch" bundle="MANAGER_RESOURCES" /></th>
					</tr>
					<bean:define id="currentSemester" name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/>
				</logic:notEqual>
				<tr>
					<td class="acenter"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/></td>
					<td class="acenter"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/></td>
					<td style="text-align:left">
					<bean:define id="curricularCourseCode" name="curricularCourseScopeElem" property="infoCurricularCourse.externalId"/>
						<html:link page="<%="/degreeCurricularPlanManagement.do?method=viewActiveCurricularCourseInformation&amp;infoCurricularCourseCode=" + curricularCourseCode + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") %>">
							<bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.name"/>
						</html:link>
					</td>
					<td>
						&nbsp;<bean:write name="curricularCourseScopeElem" property="infoBranch.prettyCode"/>
					</td>
				</tr>
			</logic:iterate>
	</table>
</logic:present>
