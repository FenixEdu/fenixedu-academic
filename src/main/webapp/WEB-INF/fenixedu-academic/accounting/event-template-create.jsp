<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

${portal.toolkit()}

<c:choose>
    <c:when test="${parent != null}">
        <spring:url var="backUrl" value="/event-template-management/{parent}">
            <spring:param name="parent" value="${parent.externalId}"/>
        </spring:url>
    </c:when>
    <c:otherwise>
        <spring:url var="backUrl" value="/event-template-management/"></spring:url>
    </c:otherwise>
</c:choose>

<style>
.actions {
    margin-top: 18px;
    margin-bottom: 9px;
}
</style>

<div class="container-fluid">
    <main>

        <header>
            <div class="row">
                <div class="col-sm-12">
                    <h1><spring:message code="accounting.management.event.templates.title"/></h1>
                </div>
            </div>
        </header>
        
        <section>
            <div class="row">
                <div class="col-sm-8">
                    <h2>
                        <c:choose>
                            <c:when test="${parent != null}">
                                <spring:message code="accounting.management.event.templates.action.create.alternative"/>
                            </c:when>
                            <c:otherwise>
                                <spring:message code="accounting.management.event.templates.action.create.template"/>
                            </c:otherwise>
                        </c:choose>
                    </h2>
                </div>
                
                <div class="col-sm-4 actions">
                    <a class="btn btn-block btn-default" href="${backUrl}"><spring:message code="accounting.management.event.templates.action.back"/></a>
                </div>
            </div>
        </section>
        
        <c:if test="${parent != null}">
            <section>
                <div class="alert alert-info" role="alert">
                    <spring:message code="accounting.management.event.templates.alert.creatingAlternativeToTemplate"/>
                    <ul>
                        <li> <c:out value='${parent.code}'/> </li>
                        <li> <c:out value='${parent.title.content}'/> </li>
                        <li> <c:out value='${parent.description.content}'/> </li>
                    </ul>
                </div>
            </section>
        </c:if>

        <section>
            <form role="form" class="form-horizontal" action="" method="POST">
                ${csrf.field()}
                <div class="form-group">
                    <label for="code" class="control-label col-sm-2"><spring:message code="accounting.management.event.templates.label.code"/></label>
                    <div class="col-sm-10">
                        <input class="form-control" id="code" name="code" value='${parent == null ? "" : parent.code}' required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="title" class="control-label col-sm-2"><spring:message code="accounting.management.event.templates.label.title"/></label>
                    <div class="col-sm-10">
                        <input id="title" name="title" value='${parent == null ? "" : parent.title.json()}' bennu-localized-string required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="description" class="control-label col-sm-2"><spring:message code="accounting.management.event.templates.label.description"/></label>
                    <div class="col-sm-10">
                        <textarea id="description" name="description" bennu-localized-string required>${parent == null ? "" : parent.description.json()}</textarea>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-offset-8 col-sm-4">
                        <button id="submitForm" class="btn btn-block btn-primary" type="submit"><spring:message code="accounting.management.event.templates.action.create"/></button>
                    </div>
                </div>
            </form>
        </section>

    </main>
</div>
