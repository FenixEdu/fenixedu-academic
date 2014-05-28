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

<h2><bean:message key="title.student.curriculum"/></h2>

<span class="error0"><!-- Error messages go here --><html:errors /></span>

<html:form action="/viewCurriculum" focus="studentNumber">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareReadByStudentNumber"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	
	<logic:present name="executionDegreeId">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeId" property="executionDegreeId" value="<%=pageContext.findAttribute("executionDegreeId").toString()%>"/>
	</logic:present>
	<logic:present name="degreeCurricularPlanID">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%=pageContext.findAttribute("degreeCurricularPlanID").toString()%>"/>
	</logic:present>
	
	<table class="tstyle5">
		<tr>
			<td><bean:message key="label.choose.student"/></td>
			<td><input alt="input.studentNumber" type="text" name="studentNumber" size="5" maxlength="5"/></td>
		</tr>
	</table>
	
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.submit.student"/></html:submit>
	</p>
</html:form>
