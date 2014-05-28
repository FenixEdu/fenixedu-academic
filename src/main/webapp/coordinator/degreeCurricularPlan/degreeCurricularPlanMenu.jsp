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
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree" %>

<%
String degreeCurricularPlanID = null;
if(request.getAttribute("degreeCurricularPlanID") instanceof String){
    degreeCurricularPlanID = (String) request.getAttribute("degreeCurricularPlanID");
}
if (degreeCurricularPlanID == null) {
    degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
}
%>

<logic:present name="<%= PresentationConstants.MASTER_DEGREE %>"  >
	<bean:define id="infoExecutionDegree" name="<%= PresentationConstants.MASTER_DEGREE %>" type="InfoExecutionDegree" />
	<br/>
	<ul>
		<li class="navheader">
			<bean:message key="link.coordinator.degreeCurricularPlan.management"/>
		</li>
		<logic:equal name="infoExecutionDegree" property="bolonhaDegree" value="false">
			<li>
				<html:link page="<%= "/degreeCurricularPlanManagement.do?method=showActiveCurricularCourses&amp;degreeCurricularPlanID=" + degreeCurricularPlanID%>">
				    <bean:message key="link.coordinator.degreeCurricular.viewActive" />
				</html:link>
			</li>
			<li>
				<html:link page="<%= "/degreeCurricularPlanManagement.do?method=showCurricularCoursesHistory&amp;degreeCurricularPlanID=" + degreeCurricularPlanID%>">
				    <bean:message key="link.coordinator.degreeCurricular.viewHistory" />
				</html:link>
			</li>
		</logic:equal>
		<logic:equal name="infoExecutionDegree" property="bolonhaDegree" value="true">
			<li>
				<html:link page="<%= "/degreeCurricularPlan/showAllCompetenceCourses.faces?degreeCurricularPlanID=" + degreeCurricularPlanID%>">
				    <bean:message key="list.competence.courses" />
				</html:link>

				<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showPlan&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
				    <bean:message key="link.equivalency.plan" />
				</html:link>

			</li>
		</logic:equal>
	</ul>
</logic:present>
