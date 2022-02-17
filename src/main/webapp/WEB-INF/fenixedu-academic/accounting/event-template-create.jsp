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
                    <h1>Event Templates</h1>
                </div>
            </div>
        </header>
        
        <section>
            <div class="row">
                <div class="col-sm-8">
                    <h2>
                        Create
                        <c:if test="${parent != null}">
                            Alternative
                        </c:if>
                        Event Template
                    </h2>
                </div>
                
                <div class="col-sm-4 actions">
                    <a class="btn btn-block btn-default" href="${backUrl}">Back</a>
                </div>
            </div>
        </section>
        
        <c:if test="${parent != null}">
            <section>
                <div class="alert alert-info" role="alert">
                    You are creating an alternative to the following event template:
                    <ul>
                        <li> ${parent.code} </li>
                        <li> ${parent.title.content} </li>
                        <li> ${parent.description.content} </li>
                    </ul>
                </div>
            </section>
        </c:if>

        <section>
            <form role="form" class="form-horizontal" action="" method="POST">
                ${csrf.field()}
                <div class="form-group">
                    <label for="code" class="control-label col-sm-2">Code</label>
                    <div class="col-sm-10">
                        <input class="form-control" id="code" name="code" value='${parent == null ? "" : parent.code}' required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="title" class="control-label col-sm-2">Title</label>
                    <div class="col-sm-10">
                        <input id="title" name="title" value='${parent == null ? "" : parent.title.json()}' bennu-localized-string required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="description" class="control-label col-sm-2">Description</label>
                    <div class="col-sm-10">
                        <textarea id="description" name="description" bennu-localized-string required>${parent == null ? "" : parent.description.json()}</textarea>
                    </div>
                </div>

                <div class="row">
                    <div class="col-sm-offset-8 col-sm-4">
                        <button id="submitForm" class="btn btn-block btn-primary" type="submit">Create</button>
                    </div>
                </div>
            </form>
        </section>

    </main>
</div>
