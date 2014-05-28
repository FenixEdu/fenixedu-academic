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

<jsp:include page="/coordinator/context.jsp" />

<h2><bean:message key="title.studentListByCourse" /></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<logic:present name="curricularCourses">
	<p>
		<bean:message key="title.masterDegree.administrativeOffice.chooseCurricularCourse" />:
	</p>

	<bean:define id="path" value="/listStudentsByCourse" />
	<ul>
		<!-- CurricularCourse -->
		<logic:iterate id="curricularCourseElem" name="curricularCourses">
		   	<bean:define id="courseID" name="curricularCourseElem" property="externalId"/>
		   	<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
		   	<li>
				<html:link page="<%= path + ".do?method=chooseCurricularCourseByID&amp;courseID=" + courseID + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
					<bean:write name="curricularCourseElem" property="name"/>
				</html:link>
			</li>
		</logic:iterate>
	</ul>
</logic:present>