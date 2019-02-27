<!DOCTYPE html>
${portal.toolkit()}
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="backUrl" value="/registrationProtocols/"/>

<spring:url var="actionUrl" value="/registrationProtocols/${empty registrationProtocol ? 'create' : registrationProtocol.externalId}"/>

<c:set var="yes"><spring:message code="label.yes"/></c:set>
<c:set var="no"><spring:message code="label.no"/></c:set>

<div class="page-header">
    <h2>
        <spring:message code="title.manage.registrationProtocols"/>
        <small>
            <c:if test="${not empty registrationProtocol}">
                <spring:message code="label.edit"/>
            </c:if>
            <c:if test="${empty registrationProtocol}">
                <spring:message code="label.create"/>
            </c:if>
        </small>
    </h2>
</div>

<section>
    <div class="alert alert-danger">
        <h3 class="mvert0"><spring:message code="label.registrationProtocol.warning.title" /></h3>
        <p class="mtop1" style="font-size:11pt"><spring:message code="label.registrationProtocol.warning.text" /></p>
        <p class="mtop1 col-sm-offset-0" style="font-size:12pt"><input type="checkbox" onchange="document.getElementById('submitButton').disabled = !this.checked"/>    <spring:message code="label.registrationProtocol.warning.checkbox" /></p>
    </div>
    <form:form role="form" modelAttribute="bean" method="POST" class="form-horizontal" action="${actionUrl}">
        ${csrf.field()}
        <div class="form-group">
            <label for="code" class="col-sm-3 control-label"><spring:message code="label.registrationProtocol.code" /></label>
            <div>
                <input id="code" name="code" type="text" class="col-md-5" value='<c:out value="${bean.code}"/>' required/>
            </div>
        </div>
        <div class="form-group">
            <label for="description" class="col-sm-3 control-label"><spring:message code="label.registrationProtocol.description" /></label>
            <div>
                <input bennu-localized-string type="text" id="description" name="description" type="text" value='<c:out value="${bean.description.json()}"/>' required/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="label.registrationProtocol.enrolmentByStudentAllowed"/></label>
            <form:checkbox path="enrolmentByStudentAllowed" value="${bean.enrolmentByStudentAllowed}" />
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="label.registrationProtocol.payGratuity"/></label>
            <form:checkbox path="payGratuity" value="${bean.payGratuity}" />
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="label.registrationProtocol.allowsIDCard"/></label>
            <form:checkbox path="allowsIDCard" value="${bean.allowsIDCard}" />
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="label.registrationProtocol.onlyAllowedDegreeEnrolment"/></label>
            <form:checkbox path="onlyAllowedDegreeEnrolment" value="${bean.onlyAllowedDegreeEnrolment}" />
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="label.registrationProtocol.alien"/></label>
            <form:checkbox path="alien" value="${bean.alien}" />
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="label.registrationProtocol.exempted"/></label>
            <form:checkbox path="exempted" value="${bean.exempted}" />
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="label.registrationProtocol.mobility"/></label>
            <form:checkbox path="mobility" value="${bean.mobility}" />
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="label.registrationProtocol.military"/></label>
            <form:checkbox path="military" value="${bean.military}" />
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="label.registrationProtocol.forOfficialMobilityReporting"/></label>
            <form:checkbox path="forOfficialMobilityReporting" value="${bean.forOfficialMobilityReporting}" />
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><spring:message code="label.registrationProtocol.attemptAlmaMatterFromPrecedent"/></label>
            <form:checkbox path="attemptAlmaMatterFromPrecedent" value="${bean.attemptAlmaMatterFromPrecedent}" />
        </div>

        <div class="form-group">
            <div class="col-sm-offset-1">
                <a class="btn btn-default" href="${backUrl}"><spring:message code="label.cancel"/></a>
                <button type="submit" id="submitButton" class="btn btn-primary" disabled><spring:message code="label.save"/></button>
            </div>
        </div>
    </form:form>
</section>