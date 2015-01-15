<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>${fr:message('resources.ApplicationResources', 'label.executionCourseManagement.menu.communication')}</h2>
<hr />

<c:set var="context" value="${pageContext.request.contextPath}/teacher/${executionCourse.externalId}/communication" />

<form role="form" method="post" action="${context}/edit" class="form-horizontal">
    <div class="form-group">
        <label for="email" class="col-md-2 control-label">
            ${fr:message('resources.ApplicationResources', 'email')}
        </label>
        <div class="col-md-10">
            <input id="email" type="email" required class="form-control" name="email" placeholder="Email" value="${executionCourse.email}">
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
