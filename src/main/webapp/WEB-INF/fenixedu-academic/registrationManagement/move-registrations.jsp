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
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

${portal.toolkit()}

<spring:url var="findUsersUrl" value="/academic/move-registrations/find"></spring:url>
<spring:url var="mergeUrl" value="/academic/move-registrations/merge"></spring:url>

<div class="page-header">
    <h1>
        <spring:message code="label.student.registrationManagement" />
        <small><spring:message code="label.student.registrationManagement.transfer" /></small>
    </h1>
</div>
<c:choose>
    <c:when test="${success}">
        <div class="alert alert-success">
            <spring:message code="label.student.registrationManagement.transfer.success" />
        </div>
    </c:when>
    <c:otherwise>
        <div class="alert alert-info">
            <spring:message code="label.student.registrationManagement.transfer.info" />
        </div>
    </c:otherwise>
</c:choose>
<h4><spring:message code="label.student.registrationManagement.targetStudent" /></h4>
<div class="panel panel-default">
    <div class="panel-heading">
         ${bean.target.username}
         <c:if test='${not bean.target.loginExpired}'>
             <span class="label label-success">
                <c:choose>
                    <c:when test="${bean.target.loginExpired}"><spring:message code="label.student.registrationManagement.inactive" /></c:when>
                    <c:otherwise><spring:message code="label.student.registrationManagement.active" /></c:otherwise>
                </c:choose>
            </span>
        </c:if>
    </div>
    <div class="panel-body">
        <div class="media">
            <div class="pull-left">
                <c:url var="targetPhoto" value="/user/photo/${bean.target.username}"></c:url>
                <img src="${targetPhoto}" class="media-object">
            </div>
            <div class="media-body">
                <h4><spring:message code="label.student.registrationManagement.personalData" /></h4>
                <dl class="dl-horizontal">
                    <dt><spring:message code="label.student.registrationManagement.personalData.name" /></dt>
                    <dd><c:out value="${bean.target.name}"/></dd>
                    <dt><spring:message code="label.student.registrationManagement.personalData.idNumber" /></dt>
                    <dd><c:out value="${bean.target.person.documentIdNumber}"/> (<spring:message code="IDDocumentType.${bean.target.person.idDocumentType}" />)</dd>
                    <dt><spring:message code="label.student.registrationManagement.personalData.taxNumber" /></dt>
                    <dd><c:choose><c:when test="${not empty bean.target.person.socialSecurityNumber}"><c:out value="${bean.target.person.socialSecurityNumber}"/></c:when><c:otherwise>&nbsp;</c:otherwise></c:choose></dd>
                    <dt><spring:message code="label.student.registrationManagement.personalData.birthdate" /></dt>
                    <dd><c:choose><c:when test="${not empty bean.target.person.dateOfBirthYearMonthDay}"><c:out value="${bean.target.person.dateOfBirthYearMonthDay}"/></c:when><c:otherwise>&nbsp;</c:otherwise></c:choose></dd>
                    <dt><spring:message code="label.student.registrationManagement.personalData.nationality" /></dt>
                    <dd><c:choose><c:when test="${not empty bean.target.person.country}"><c:out value="${bean.target.person.country.nationality}"/></c:when><c:otherwise>&nbsp;</c:otherwise></c:choose></dd>
                </dl>
            </div>
        </div>
        <h4><spring:message code="label.student.registrationManagement.registrations" /></h4>
        <table class="table">
            <thead>
                <tr>
                    <th><spring:message code="label.student.registrationManagement.registration.startdate" /></th>
                    <th><spring:message code="label.student.registrationManagement.registration.number" /></th>
                    <th><spring:message code="label.student.registrationManagement.registration.degree" /></th>
                    <th><spring:message code="label.student.registrationManagement.registration.state" /></th>
                    <th><spring:message code="label.student.registrationManagement.registration.protocol" /></th>
                    <th><spring:message code="label.student.registrationManagement.registration.enrolments" /></th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="registration" items="${bean.target.person.student.registrationsSet}">
                    <tr>
                        <td><c:out value="${registration.startDate}"/></td>
                        <td><c:out value="${registration.number}"/></td>
                        <td><c:out value="${registration.degreeNameWithDescription}"/></td>
                        <td><spring:message code="RegistrationStateType.${registration.activeStateType}" /></td>
                        <td><c:out value="${registration.registrationProtocol.description.content}"/></td>
                        <td><c:out value="${registration.numberEnroledCurricularCoursesInCurrentYear}"/></td>
                        <td>
                            <a href="${fr:checksumLink(pageContext.request, '/academicAdministration/student.do?method=visualizeRegistration&amp;registrationID='.concat(registration.externalId))}">
                                <spring:message code="link.student.registrationManagement.viewCurriculum" />
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <h4><spring:message code="label.student.registrationManagement.phds" /></h4>
        <table class="table">
            <thead>
                <tr>
                    <th><spring:message code="label.student.registrationManagement.phd.year" /></th>
                    <th><spring:message code="label.student.registrationManagement.phd.processNumber" /></th>
                    <th><spring:message code="label.student.registrationManagement.phd.phdProgram" /></th>
                    <th><spring:message code="label.student.registrationManagement.phd.state" /></th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="phd" items="${bean.target.person.phdIndividualProgramProcessesSet}">
                    <tr>
                        <td><c:out value="${phd.executionYear.name}"/></td>
                        <td><c:out value="${phd.processNumber}"/></td>
                        <td><c:out value="${phd.phdProgram.name}"/></td>
                        <td><c:out value="${phd.activeState.localizedName}"/></td>
                        <td>
                            <a href="${fr:checksumLink(pageContext.request, '/academicAdministration/phdIndividualProgramProcess.do?method=viewProcess&processId='.concat(phd.externalId))}">
                                <spring:message code="link.student.registrationManagement.viewCurriculum" />
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<c:if test="${not empty bean.similars}">
    <h4><spring:message code="label.student.registrationManagement.possibleDuplicates" /></h4>
    <c:forEach var="similar" items="${bean.similars}">
      <div class="panel panel-default">
        <div class="panel-heading">
             ${similar.username}
             <c:if test='${not similar.loginExpired}'>
                 <span class="label label-success">
                    <c:choose>
                        <c:when test="${similar.loginExpired}"><spring:message code="label.student.registrationManagement.inactive" /></c:when>
                        <c:otherwise><spring:message code="label.student.registrationManagement.active" /></c:otherwise>
                    </c:choose>
                </span>
            </c:if>
        </div>
        <div class="panel-body">
            <span class="pull-right">
                <spring:message var="transferConfirmation" code="label.student.registrationManagement.transferconfirmation" arguments="${similar.username},${bean.target.username}" />
                <form:form role="form" modelAttribute="search" action="${mergeUrl}" onsubmit="return confirm('${transferConfirmation}');">
                    <input type="hidden" name="target" value="${bean.target.username}">
                    <input type="hidden" name="source" value="${similar.username}">
                    <button type="submit" class="btn btn-primary"><spring:message code="link.student.registrationManagement.transfer" /></button>
                </form:form>
            </span>
            <div class="media">
                <div class="pull-left">
                    <c:url var="similarPhoto" value="/user/photo/${similar.username}"></c:url>
                    <img src="${similarPhoto}" class="media-object">
                </div>
                <div class="media-body">
                    <h4><spring:message code="label.student.registrationManagement.personalData" /></h4>
                    <dl class="dl-horizontal">
                        <dt><spring:message code="label.student.registrationManagement.personalData.name" /></dt>
                        <dd><c:out value="${similar.name}"/></dd>
                        <dt><spring:message code="label.student.registrationManagement.personalData.idNumber" /></dt>
                        <dd><c:out value="${similar.person.documentIdNumber}"/> (<spring:message code="IDDocumentType.${similar.person.idDocumentType}" />)</dd>
                        <dt><spring:message code="label.student.registrationManagement.personalData.taxNumber" /></dt>
                        <dd><c:choose><c:when test="${not empty similar.person.socialSecurityNumber}"><c:out value="${similar.person.socialSecurityNumber}"/></c:when><c:otherwise>&nbsp;</c:otherwise></c:choose></dd>
                        <dt><spring:message code="label.student.registrationManagement.personalData.birthdate" /></dt>
                        <dd><c:choose><c:when test="${not empty similar.person.dateOfBirthYearMonthDay}"><c:out value="${similar.person.dateOfBirthYearMonthDay}"/></c:when><c:otherwise>&nbsp;</c:otherwise></c:choose></dd>
                        <dt><spring:message code="label.student.registrationManagement.personalData.nationality" /></dt>
                        <dd><c:choose><c:when test="${not empty similar.person.country}"><c:out value="${similar.person.country.nationality}"/></c:when><c:otherwise>&nbsp;</c:otherwise></c:choose></dd>
                    </dl>
                </div>
            </div>
            <h4><spring:message code="label.student.registrationManagement.registrations" /></h4>
            <c:if test="${not empty similar.person.student.registrationsSet}">
                <table class="table">
                    <thead>
                        <tr>
                            <th><spring:message code="label.student.registrationManagement.registration.startdate" /></th>
                            <th><spring:message code="label.student.registrationManagement.registration.number" /></th>
                            <th><spring:message code="label.student.registrationManagement.registration.degree" /></th>
                            <th><spring:message code="label.student.registrationManagement.registration.state" /></th>
                            <th><spring:message code="label.student.registrationManagement.registration.protocol" /></th>
                            <th><spring:message code="label.student.registrationManagement.registration.enrolments" /></th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="registration" items="${similar.person.student.registrationsSet}">
                            <tr>
                                <td><c:out value="${registration.startDate}"/></td>
                                <td><c:out value="${registration.number}"/></td>
                                <td><c:out value="${registration.degreeNameWithDescription}"/></td>
                                <td><spring:message code="RegistrationStateType.${registration.activeStateType}" /></td>
                                <td><c:out value="${registration.registrationProtocol.description.content}"/></td>
                                <td><c:out value="${registration.numberEnroledCurricularCoursesInCurrentYear}"/></td>
                                <td>
                                    <a href="${fr:checksumLink(pageContext.request, '/academicAdministration/student.do?method=visualizeRegistration&amp;registrationID='.concat(registration.externalId))}">
                                        <spring:message code="link.student.registrationManagement.viewCurriculum" />
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${empty similar.person.student.registrationsSet}">
                <spring:message code="label.student.registrationManagement.noRegistrations" />
            </c:if>
            <h4><spring:message code="label.student.registrationManagement.phds" /></h4>
            <c:if test="${not empty similar.person.phdIndividualProgramProcessesSet}">
                <table class="table">
                    <thead>
                        <tr>
                            <th><spring:message code="label.student.registrationManagement.phd.year" /></th>
                            <th><spring:message code="label.student.registrationManagement.phd.processNumber" /></th>
                            <th><spring:message code="label.student.registrationManagement.phd.phdProgram" /></th>
                            <th><spring:message code="label.student.registrationManagement.phd.state" /></th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="phd" items="${similar.person.phdIndividualProgramProcessesSet}">
                            <tr>
                                <td><c:out value="${phd.executionYear.name}"/></td>
                                <td><c:out value="${phd.processNumber}"/></td>
                                <td><c:out value="${phd.phdProgram.name}"/></td>
                                <td><c:out value="${phd.activeState.localizedName}"/></td>
                                <td>
                                    <a href="${fr:checksumLink(pageContext.request, '/academicAdministration/phdIndividualProgramProcess.do?method=viewProcess&processId='.concat(phd.externalId))}">
                                        <spring:message code="link.student.registrationManagement.viewCurriculum" />
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${empty similar.person.phdIndividualProgramProcessesSet}">
                <spring:message code="label.student.registrationManagement.noPhds" />
            </c:if>
        </div>
    </div>
  </c:forEach>
</c:if>
<h4><spring:message code="label.student.registrationManagement.search" /></h4>
<form:form role="form" modelAttribute="search" method="POST" class="form" action="${findUsersUrl}">
    <div class="form-group">
        <input type="hidden" name="target" value="${bean.target.username}">
        <input type="text" class="form-control" name="searched" value="${bean.searched.username}" bennu-user-autocomplete onchange="this.form.submit()" placeholder="<spring:message code="label.student.registrationManagement.search.placeholder" />">
    </div>
</form:form>
<c:if test="${bean.searched != null &&  bean.searched.person.student == null}">
    <div class="alert alert-warning">
        <spring:message code="label.student.registrationManagement.search.notStudent" />
    </div>
</c:if>
<c:if test="${bean.searched != null &&  bean.searched.person.student != null}">
    <div class="panel panel-default">
        <div class="panel-heading">
             ${bean.searched.username}
             <c:if test='${not bean.searched.loginExpired}'>
                 <span class="label label-success">
                    <c:choose>
                        <c:when test="${bean.searched.loginExpired}"><spring:message code="label.student.registrationManagement.inactive" /></c:when>
                        <c:otherwise><spring:message code="label.student.registrationManagement.active" /></c:otherwise>
                    </c:choose>
                </span>
            </c:if>
        </div>
        <div class="panel-body">
            <span class="pull-right">
                <spring:message var="transferConfirmation" code="label.student.registrationManagement.transferconfirmation" arguments="${bean.searched.username},${bean.target.username}" />
                <form:form role="form" modelAttribute="search" action="${mergeUrl}" onsubmit="return confirm('${transferConfirmation}');">
                    <input type="hidden" name="target" value="${bean.target.username}">
                    <input type="hidden" name="source" value="${bean.searched.username}">
                    <button type="submit" class="btn btn-primary"><spring:message code="link.student.registrationManagement.transfer" /></button>
                </form:form>
            </span>
            <div class="media">
                <div class="pull-left">
                    <c:url var="searchedPhoto" value="/user/photo/${bean.searched.username}"></c:url>
                    <img src="${searchedPhoto}" class="media-object">
                </div>
                <div class="media-body">
                    <h4><spring:message code="label.student.registrationManagement.personalData" /></h4>
                    <dl class="dl-horizontal">
                        <dt><spring:message code="label.student.registrationManagement.personalData.name" /></dt>
                        <dd><c:out value="${bean.searched.name}"/></dd>
                        <dt><spring:message code="label.student.registrationManagement.personalData.idNumber" /></dt>
                        <dd><c:out value="${bean.searched.person.documentIdNumber}"/> (<spring:message code="IDDocumentType.${bean.searched.person.idDocumentType}" />)</dd>
                        <dt><spring:message code="label.student.registrationManagement.personalData.taxNumber" /></dt>
                        <dd><c:choose><c:when test="${not empty bean.searched.person.socialSecurityNumber}"><c:out value="${bean.searched.person.socialSecurityNumber}"/></c:when><c:otherwise>&nbsp;</c:otherwise></c:choose></dd>
                        <dt><spring:message code="label.student.registrationManagement.personalData.birthdate" /></dt>
                        <dd><c:choose><c:when test="${not empty bean.searched.person.dateOfBirthYearMonthDay}/"><c:out value="${bean.searched.person.dateOfBirthYearMonthDay}"/></c:when><c:otherwise>&nbsp;</c:otherwise></c:choose></dd>
                        <dt><spring:message code="label.student.registrationManagement.personalData.nationality" /></dt>
                        <dd><c:choose><c:when test="${not empty bean.searched.person.country}"><c:out value="${bean.searched.person.country.nationality}"/></c:when><c:otherwise>&nbsp;</c:otherwise></c:choose></dd>
                    </dl>
                </div>
            </div>
            <h4><spring:message code="label.student.registrationManagement.registrations" /></h4>
            <c:if test="${not empty bean.searched.person.student.registrationsSet}">
                <table class="table">
                    <thead>
                        <tr>
                            <th><spring:message code="label.student.registrationManagement.registration.startdate" /></th>
                            <th><spring:message code="label.student.registrationManagement.registration.number" /></th>
                            <th><spring:message code="label.student.registrationManagement.registration.degree" /></th>
                            <th><spring:message code="label.student.registrationManagement.registration.state" /></th>
                            <th><spring:message code="label.student.registrationManagement.registration.protocol" /></th>
                            <th><spring:message code="label.student.registrationManagement.registration.enrolments" /></th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="registration" items="${bean.searched.person.student.registrationsSet}">
                            <tr>
                                <td><c:out value="${registration.startDate}"/></td>
                                <td><c:out value="${registration.number}"/></td>
                                <td><c:out value="${registration.degreeNameWithDescription}"/></td>
                                <td><spring:message code="RegistrationStateType.${registration.activeStateType}" /></td>
                                <td><c:out value="${registration.registrationProtocol.description.content}"/></td>
                                <td><c:out value="${registration.numberEnroledCurricularCoursesInCurrentYear}"/></td>
                                <td>
                                    <a href="${fr:checksumLink(pageContext.request, '/academicAdministration/student.do?method=visualizeRegistration&amp;registrationID='.concat(registration.externalId))}">
                                        <spring:message code="link.student.registrationManagement.viewCurriculum" />
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${empty bean.searched.person.student.registrationsSet}">
                <spring:message code="label.student.registrationManagement.noRegistrations" />
            </c:if>
            <h4><spring:message code="label.student.registrationManagement.phds" /></h4>
            <c:if test="${not empty bean.searched.person.phdIndividualProgramProcessesSet}">
                <table class="table">
                    <thead>
                        <tr>
                            <th><spring:message code="label.student.registrationManagement.phd.year" /></th>
                            <th><spring:message code="label.student.registrationManagement.phd.processNumber" /></th>
                            <th><spring:message code="label.student.registrationManagement.phd.phdProgram" /></th>
                            <th><spring:message code="label.student.registrationManagement.phd.state" /></th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="phd" items="${bean.searched.person.phdIndividualProgramProcessesSet}">
                            <tr>
                                <td><c:out value="${phd.executionYear.name}"/></td>
                                <td><c:out value="${phd.processNumber}"/></td>
                                <td><c:out value="${phd.phdProgram.name}"/></td>
                                <td><c:out value="${phd.activeState.localizedName}"/></td>
                                <td>
                                    <a href="${fr:checksumLink(pageContext.request, '/academicAdministration/phdIndividualProgramProcess.do?method=viewProcess&processId='.concat(phd.externalId))}">
                                        <spring:message code="link.student.registrationManagement.viewCurriculum" />
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <c:if test="${empty bean.searched.person.phdIndividualProgramProcessesSet}">
                <spring:message code="label.student.registrationManagement.noPhds" />
            </c:if>
        </div>
    </div>
</c:if>
