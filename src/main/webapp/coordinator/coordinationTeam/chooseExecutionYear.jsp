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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>

<jsp:include page="/coordinator/context.jsp" />

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" />

<h2><bean:message key="title.coordinationTeam"/></h2>

<ul>
	<logic:iterate id="executionDegree" name="executionDegrees" >
		<li>
			<bean:define id="executionDegreeId" name="executionDegree" property="externalId" />
			<html:link page='<%= "/viewCoordinationTeam.do?method=viewTeam&infoExecutionDegreeId="+ executionDegreeId + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>'>
				<bean:write name="executionDegree" property="infoExecutionYear.year" />
			</html:link>
		</li>
	</logic:iterate>
</ul>