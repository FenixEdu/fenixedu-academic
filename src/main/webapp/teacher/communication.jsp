<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h3>Site Communication</h3>

<c:set var="context" value="${pageContext.request.contextPath}/teacher/${executionCourse.externalId}/communication" />

<form role="form" method="post" action="${context}/edit" class="form-horizontal">
    <div class="form-group">
        <label for="email" class="col-md-2 control-label">
            ${fr:message('resources.ApplicationResources', 'email')}
        </label>
        <div class="col-md-10">
            <input id="email" type="email" required class="form-control" name="email" placeholder="Enter email" value="${executionCourse.email}">
        </div>
    </div>

    <div class="form-group">
        <label for="siteUrl" class="col-md-2 control-label">Site Address</label>
        <div class="col-md-10">
            <input id="siteUrl" type="url" required class="form-control" name="siteUrl" placeholder="URL of the execution course site" value="${executionCourse.siteUrl}">
        </div>
    </div>

    <div class="form-group">
        <div class="pull-right">
            <button type="reset" formnovalidate class="btn btn-default">
                ${fr:message('resources.ApplicationResources', 'label.cancel')}
            </button>
            <button type="submit" class="btn btn-primary">
                ${fr:message('resources.ApplicationResources', 'button.save')}
            </button>
        </div>
    </div>
</form>
