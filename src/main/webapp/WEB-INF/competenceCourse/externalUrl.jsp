<%@ page import="org.fenixedu.academic.domain.CompetenceCourse" %>
<%@ page import="org.fenixedu.bennu.core.util.CoreConfiguration" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
${portal.toolkit()}

<%
    final CompetenceCourse competenceCourse =  (CompetenceCourse) request.getAttribute("competenceCourse");
%>

<h2>
    <%= competenceCourse.getNameI18N().getContent() %>
</h2>

<form class="form-horizontal" method="post" style="margin-top: 25px;">
    ${csrf.field()}
    <div class="mb-2 row">
        <label class="col-sm-2 col-form-label" for="externalUrl"><spring:message code="label.externalUrl" text="External URL"/></label>
        <div class="col-sm-10">
            <input type="url" name="externalUrl" id="externalUrl" class="form-control" value="<%= competenceCourse.getExternalUrl() %>">
        </div>
    </div>
    <div class="mb-2 row">
        <div class="col-sm-2 col-form-label"></div>
        <div class="col-sm-10" style="margin-top: 5px;">
            <button class="btn btn-primary">
                <spring:message code="label.save" text="Save"/>
            </button>
        </div>
    </div>
</form>
