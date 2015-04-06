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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>

<c:set var="base" value="${pageContext.request.contextPath}/teacher" />
<c:set var="professorship" value="${executionCourse.professorshipForCurrentUser}" />

<fp:select actionClass="org.fenixedu.academic.ui.struts.action.teacher.ManageExecutionCourseDA" />

<div class="row">
	<main class="col-lg-10">
		<ol class="breadcrumb">
			<em><c:out value="${executionCourse.name} - ${executionCourse.executionPeriod.qualifiedName}" />
				(<c:forEach var="degree" items="${executionCourse.degreesSortedByDegreeName}"> <c:out value="${degree.sigla}" /> </c:forEach>)
			</em>
		</ol>