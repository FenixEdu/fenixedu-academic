<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://fenixedu.org/taglib/intersection" prefix="modular" %>
<%@ page trimDirectiveWhitespaces="true" %>

<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/accounting.css"/>

<spring:url var="createUrl" value="/event-template-management/create"></spring:url>

<style>
.form-horizontal {
    margin-top: 18px;
    margin-bottom: 9px;
}
</style>

<div class="container-fluid">
    <main>
        <header>
            <div class="row">
                <div class="col-md-12">
                    <h1>Event Templates</h1>
                </div>
            </div>
        </header>
        <section>
                ${csrf.field()}
            <form role="form" class="form-horizontal" action="" method="GET">
                <div class="form-group">
                    <label for="config" class="control-label col-sm-8">View</label>
                    <div class="col-sm-4">
                        <select class="form-control" id="showAll" name="showAll">
                            <option value="false" ${!showAll ? "selected" : ""}>only top level templates</option>
                            <option value="true" ${showAll ? "selected" : ""}>all templates</option>
                        </select>
                    </div>
                </div>
            </form>
            <div class="table-responsive">
                <table class="table table-stripped">
                    <thead>
                        <tr>
                            <th class="col-sm-2 col-md-3">Code</th>
                            <th class="col-sm-4 col-md-5">Title</th>
                            <th class="col-sm-2 col-md-1">First Config From</th>
                            <th class="col-sm-2 col-md-1">Last Config Until</th>
                            <th class="col-sm-2"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${templates}" var="template">
                            <spring:url var="detailsUrl" value="/event-template-management/{template}">
                                <spring:param name="template" value="${template.externalId}"/>
                            </spring:url>
                            <c:set var="appliedInterval" value="${template.appliedInterval}"/>
                            <tr>
                                <td>${template.code}</td>
                                <td>${template.title.content}</td>
                                <td>${appliedInterval == null ? "N/A" : appliedInterval.start.toString("yyyy-MM-dd")}</td>
                                <td>${appliedInterval == null ? "N/A" : appliedInterval.end.toString("yyyy-MM-dd")}</td>
                                <td class="text-right"><a href="${detailsUrl}">Details</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="row">
                <div class="col-sm-offset-8 col-sm-4">
                    <section>
                        <div class="actions">
                            <a class="btn btn-block btn-primary" href="${createUrl}">Create Event Template</a>
                        </div>
                    </section>
                </div>
            </div>
        </section>
    </main>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $("#showAll").change(function() {
            $(this).parents("form").submit();
        })
    });
</script>
