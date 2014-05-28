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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />

<jsp:include page="/coordinator/context.jsp" />

<h2><bean:message key="title.addCoordinator"/></h2>

<html:form action="/viewCoordinationTeam">

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>
	
<bean:define id="infoExecutionDegreeId" name="infoExecutionDegreeId"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoExecutionDegreeId" property="infoExecutionDegreeId" value="<%=  infoExecutionDegreeId.toString() %>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="AddCoordinator" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />

<p><html:link action="<%= "/viewCoordinationTeam.do?method=viewTeam&degreeCurricularPlanID=" + degreeCurricularPlanID.toString() + "&infoExecutionDegreeId=" + infoExecutionDegreeId.toString() %>">
	<bean:message bundle="COORDINATOR_RESOURCES" key="link.back"/>
</html:link></p>

<table class="tstyle5 thlight">
	<tr>
		<td><bean:message key="label.istId" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="COORDINATOR_RESOURCES"/></td>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherId" property="newCoordinatorIstUsername" /></td>
	</tr>
</table>

<style>
	.subtitled_description{
		margin: 5em;
		margin-top: -1em;
		margin-bottom: 2em;
	}
</style>
<p class="subtitled_description">
	<bean:message key="label.istIdUsageDescription" bundle="COORDINATOR_RESOURCES"/>
</p>

<p>
	<html:submit><bean:message key="label.add"/></html:submit>
</p>


</html:form>




