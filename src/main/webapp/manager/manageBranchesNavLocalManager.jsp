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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<li>
	<html:link module="/manager" page="<%= "/readDegree.do?degreeId=" + request.getParameter("degreeId")%>">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.backReadDegree" />
	</html:link>
</li>
<li>
	<html:link module="/manager" page="<%= "/readDegreeCurricularPlan.do?degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.backReadDegreeCurricularPlan" />
	</html:link>
</li>
<li>
	<html:link module="/manager" page="<%= "/manageBranches.do?method=showBranches&degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>">
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.backManageBranches" />
	</html:link>
</li>
<br/>

<jsp:include page="commons/commonNavLocalManager.jsp"></jsp:include>
