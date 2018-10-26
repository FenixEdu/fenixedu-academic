<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true" %>

<style>
<!--
.info-div {
    border: 1px solid lightgray;
    padding: 2rem;
}

.info-div dl {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-orient: horizontal;
    -webkit-box-direction: normal;
    -ms-flex-flow: row nowrap;
    flex-flow: row nowrap;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between;
}

.info-div dl dd {
    text-align: right;
}

.info-div dl dt {
    text-align: left;
}

.part1 {
    margin-right: 20px;
}

.img-p-circle {
    border-radius: 50%;
    width: 75px;
}

.docType {
    color: gray;
}

-->
</style>

<div class="info-div">
    <div class="row">
        <div class="col-md-1">
            <c:if test="${not empty person.user}">
                <img class="img-p-circle" alt="" src="<%= request.getContextPath() %>/user/photo/${person.user.username}"/>
            </c:if>
        </div>
        <div class="col-md-4">
            <dl>
                <dt><spring:message code="label.name" text="Name"/></dt>
                <dd>
                    <c:if test="${fn:containsIgnoreCase(requestScope['javax.servlet.forward.request_uri'], 'owner-accounting-events')}">
                        <a href="<%= request.getContextPath() %>/owner-accounting-events/${person.externalId}">
                            <c:out value="${person.name}"/>
                        </a>
                    </c:if>
                    <c:if test="${not fn:containsIgnoreCase(requestScope['javax.servlet.forward.request_uri'], 'owner-accounting-events')}">
                        <a href="<%= request.getContextPath() %>/accounting-management/${person.externalId}">
                            <c:out value="${person.name}"/>
                        </a>
                    </c:if>
                    <c:if test="${not empty person.user}">
                        <span class="docType">
                            <c:out value="${person.user.username}"/>
                        </span>
                    </c:if>
                </dd>
            </dl>
            <dl>
                <dt><spring:message code="label.document.id" text="ID Document"/></dt>
                <dd>
                    <c:out value="${person.documentIdNumber}"/>
                    <span class="docType">
                        <c:out value="${person.idDocumentType.localizedName}"/>
                    </span>
                </dd>
            </dl>
            <dl>
                <dt><spring:message code="label.document.vatNumber" text="VAT Number"/></dt>
                <dd>
                    <c:out value="${person.socialSecurityNumber}"/>
                </dd>
            </dl>
        </div>

        <div class="col-md-1">
        </div>

        <div class="col-md-4">
            <dl>
                <dt><spring:message code="label.email" text="Email"/></dt>
                <dd>
                    <c:if test="${not empty person.user}">
                        <a href="mailto:${person.user.email}">
                            <c:out value="${person.user.email}"/>
                        </a>
                    </c:if>
                </dd>
            </dl>
            <dl>
                <dt><spring:message code="label.address" text="Address"/></dt>
                <dd>
                    <c:set var="fiscalAddress" value="${person.fiscalAddress}"/>
                    <c:if test="${not empty fiscalAddress}">
                        <c:out value="${fiscalAddress.address}"/>
                        <br/>
                        <c:out value="${fiscalAddress.areaCode}"/>
                        &nbsp;
                        <c:out value="${fiscalAddress.areaOfAreaCode}"/>
                        <br/>
                        <c:out value="${fiscalAddress.countryOfResidence.localizedName.content}"/>
                    </c:if>
                </dd>
            </dl>
        </div>

    </div>
</div>
