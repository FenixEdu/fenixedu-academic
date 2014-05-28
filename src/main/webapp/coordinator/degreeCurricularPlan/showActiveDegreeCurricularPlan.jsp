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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<jsp:include page="/coordinator/context.jsp" />

<h2><bean:message key="label.coordinator.degreeCurricular.active"/></h2>

<html:link page="/degreeCurricularPlanManagement.do?method=showCurricularCoursesHistory&degreeCurricularPlanID=${degreeCurricularPlanID}">
    <bean:message key="link.coordinator.degreeCurricular.viewHistory" />
</html:link>

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
