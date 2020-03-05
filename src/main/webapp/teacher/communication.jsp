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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>${fr:message('resources.ApplicationResources', 'label.executionCourseManagement.menu.communication')}</h2>
<hr />

<c:set var="context" value="${pageContext.request.contextPath}/teacher/${executionCourse.externalId}/communication" />

<form role="form" method="post" action="${context}/edit" class="form-horizontal">
    ${csrf.field()}
    <div class="form-group">
        <label for="email" class="col-md-2 control-label">
            ${fr:message('resources.ApplicationResources', 'email')}
        </label>
        <div class="col-md-10">
            <input id="email" type="email" class="form-control" name="email" placeholder="Email" value="${executionCourse.email}">
        </div>
    </div>

    <div class="form-group">
        <label for="siteUrl" class="col-md-2 control-label">${fr:message('resources.ApplicationResources', 'label.partyContacts.WebAddress')}</label>
        <div class="col-md-10">
            <input id="siteUrl" type="url" required class="form-control" name="siteUrl" value="${executionCourse.siteUrl}">
        </div>
    </div>

    <div class="form-group">
        <div class="col-md-2 col-md-offset-2">
            <button type="submit" class="btn btn-primary">
                ${fr:message('resources.ApplicationResources', 'button.save')}
            </button>
        </div>
    </div>
</form>
