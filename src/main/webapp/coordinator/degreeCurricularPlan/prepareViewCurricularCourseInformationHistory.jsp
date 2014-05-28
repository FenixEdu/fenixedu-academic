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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.0.1" prefix="str" %>
<logic:present name="infoExecutionYears">
	<bean:define id="infoCurricularCourseName"><%=pageContext.findAttribute("infoCurricularCourseName")%></bean:define>
	<h2><str:upperCase><bean:message key="label.coordinator.degreeCurricularPlan.history.information"/></str:upperCase>&nbsp;-&nbsp;<bean:write name="infoCurricularCourseName"/></h2>	
	<%-- <h2><bean:write name="infoCurricularCourseName"/></h2> --%>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<p>(<html:link page="<%="/degreeCurricularPlanManagement.do?method=viewActiveCurricularCourseInformation&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID")%>">
		<font color="#0066CC"><bean:message key="link.coordinator.degreeCurricularPlan.see"/>&nbsp;<bean:message key="label.coordinator.degreeCurricularPlan.current.information"/></font>
	</html:link>)</p>
	<table>
		<tr>
			<td><bean:message key="title.masterDegree.administrativeOffice.chooseExecutionYear"/>:</td>
		</tr>
		<logic:iterate id="executionYear" name="infoExecutionYears">
			<tr>
				<td>
<%--					<bean:define id="stringExecutionYear" name="executionYear" property="label"/>
					<html:link page="<%="/degreeCurricularPlanManagement.do?method=viewCurricularCourseInformationHistory&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") + "&amp;executionYear=" + stringExecutionYear + "&amp;infoCurricularCourseName=" + infoCurricularCourseName + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID")%>">
						<bean:write name="executionYear" property="label"/>
					</html:link> --%>
					<bean:define id="stringExecutionYear" name="executionYear" property="year"/>
					<html:link page="<%="/degreeCurricularPlanManagement.do?method=viewCurricularCourseInformationHistory&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") + "&amp;executionYear=" + stringExecutionYear + "&amp;infoCurricularCourseName=" + infoCurricularCourseName + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID")%>">
						<bean:write name="executionYear" property="year"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>