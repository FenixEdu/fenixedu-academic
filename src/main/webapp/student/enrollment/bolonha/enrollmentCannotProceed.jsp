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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2>${fr:message('resources.StudentResources', 'label.enrollment.courses')}</h2>

<div class="alert alert-danger">
    <html:messages id="messages" message="true" bundle="STUDENT_RESOURCES">
        <c:out value="${messages}" />
    </html:messages>
</div>
<c:if test="${start != null}">
    <div class="col-sm-3">
        <dl class="dl-horizontal">
            <dt>${fr:message('resources.StudentResources', 'label.time.current')}</dt>
            <dd>${now.toString('HH:mm:ss')}</dd>

            <dt>${fr:message('resources.StudentResources', 'label.enrolment.begin')}</dt>
            <dd>${start.toString('HH:mm:ss')}</dd>

            <dt>${fr:message('resources.StudentResources', 'label.time.remaining')}</dt>
            <dd>${remaining}</dd>
        </dl>
    </div>
    <div class="col-sm-2">
       <a class="btn btn-primary" href="${pageContext.request.contextPath}/student/bolonhaStudentEnrollment.do?method=prepare&registrationOid=${registration.externalId}&executionSemesterID=<%= request.getParameter("executionSemesterID") %>">
            <span class="glyphicon glyphicon-refresh"></span> ${fr:message('resources.ApplicationResources', 'button.update')}
        </a>
    </div>
</c:if>