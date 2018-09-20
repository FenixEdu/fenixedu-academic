<%--

    Copyright © 2017 Instituto Superior Técnico

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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="org.fenixedu.academic.domain.person.IDDocumentType"%>

<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/accounting.css"/>

${portal.toolkit()}

<spring:url value="/search-accounting-events" var="searchUrl"/>

<div class="container-fluid">
    <header>
        <div class="row">
            <div class="col-md-10">
                <h3><spring:message code="title.accounting.management.entrypoint"/></h3>
            </div>
        </div>
        <div class="row">
            <c:if test="${not empty sizeWarning}">
                <section>
                    <ul class="nobullet list6">
                        <li><span class="error0"><c:out value="${sizeWarning}"/></span></li>
                    </ul>
                </section>
            </c:if>
        </div>
    </header>

    <div class="row">
        <h3>Pesquisa</h3>
        <%--@elvariable id="searchBean" type="org.fenixedu.academic.dto.person.SimpleSearchPersonWithStudentBean"--%>
        <form:form modelAttribute="searchBean" role="form" class="form-horizontal" action="${searchUrl}" method="post">
            ${csrf.field()}
            <div class="form-group">
                <label class="control-label col-sm-2"><spring:message code="label.org.fenixedu.academic.dto.person.SimpleSearchPersonWithStudentBean.username"/></label>
                <div class="col-sm-10">
                    <form:input path="username" cssClass="form-control"/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-2"><spring:message code="label.org.fenixedu.academic.dto.person.SimpleSearchPersonWithStudentBean.documentIdNumber"/></label>
                <div class="col-sm-10">
                    <form:input path="documentIdNumber" cssClass="form-control"/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-2"><spring:message code="label.org.fenixedu.academic.dto.person.SimpleSearchPersonWithStudentBean.idDocumentType"/></label>
                <div class="col-sm-10">
                    <form:select path="idDocumentType" items="${idDocumentTypes}" itemLabel="localizedName" itemValue="name" class="form-control"/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-2"><spring:message code="label.org.fenixedu.academic.dto.person.SimpleSearchPersonWithStudentBean.name"/></label>
                <div class="col-sm-10">
                    <form:input path="name" size="70" cssClass="form-control"/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-2"><spring:message code="label.org.fenixedu.academic.dto.person.SimpleSearchPersonWithStudentBean.paymentCode"/></label>
                <div class="col-sm-10">
                    <form:input path="paymentCode" cssClass="form-control"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-1 col-sm-4">
                    <button class="btn btn-primary" type="submit">
                        <spring:message code="label.submit"/>
                    </button>
                </div>
            </div>
        </form:form>
    </div>
    <c:if test="${not empty persons}">
        <div class="row">
            <div class="col-md-12">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Nome</th>
                        <th>Tipo de Documento</th>
                        <th>Número de Documento</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="person" items="${persons}">
                        <tr>
                            <spring:url var="personPaymentUrl" value="/accounting-management/{person}">
                                <spring:param name="person" value="${person.externalId}"/>
                            </spring:url>
                            <td><a href="${personPaymentUrl}"><c:out value="${person.presentationName}"/></a></td>
                            <td><c:out value="${person.idDocumentType.localizedName}"/></td>
                            <td><c:out value="${person.documentIdNumber}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>
</div>
