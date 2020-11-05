<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<jsp:include page="/coordinator/context.jsp" />

<h2><bean:message key="title.student.curriculum" /></h2>

<ul class="nobullet list6">
	<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
		<li><span class="error0"><bean:write name="messages" /></span></li>
	</html:messages>
</ul>

<html:form focus="studentNumber" method="post" action="/viewStudentCurriculumSearch.do">
	<html:hidden property="degreeCurricularPlanID" value="<%= request.getAttribute("degreeCurricularPlanID").toString() %>"/>
	<html:hidden property="method" value="showStudentCurriculum"/>
	<html:hidden property="executionDegreeId" />
	<html:hidden property="degreeCurricularPlanId" />

	<table class="tstyle5">
		<tr>
			<td><bean:message key="label.choose.student" /></td>
			<td><html:text property="studentNumber"
				alt="input.studentNumber" maxlength="6" size="6"></html:text></td>
		</tr>
	</table>

	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
		styleClass="inputbutton">
		<bean:message key="button.submit.student" />
	</html:submit></p>
</html:form>