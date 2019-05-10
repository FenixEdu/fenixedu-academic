<%--

    Copyright © 2018 Instituto Superior Técnico

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
<%@ taglib prefix="fr" uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${portal.toolkit()}

<h2>Importar Dívidas (Excel)</h2>

<div class="container-fluid">
    <c:if test="${not empty error}">
        <p>
		    <span class="error">
                <c:out value="${error}"/>
    		</span>
        </p>
    </c:if>
    <div class="alert alert-info">
        <spring:message code="import.residence.information.xls.format.label"/>
        <spring:message code="import.residence.information.xls.format.description"/>
    </div>

    <div>
        <form:form method="POST" action="importEvents/upload" enctype="multipart/form-data">
            ${csrf.field()}
            <input hidden name="residenceMonth" value="${residenceMonth.externalId}"/>
            <table>
                <tr>
                    <td>Importar Dívidas (Excel)</td>
                    <td><input type="file" name="file" required/></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Submit" /></td>
                </tr>
            </table>
        </form:form>
    </div>

    <div class="col-md-4" style="margin-top: 20px">
        <c:if test="${not empty importList.unsuccessfulEvents || not empty importList.successfulEvents}">
                <div class="alert alert-info">
                    <spring:message code="label.total.imports" arguments="${importList.getNumberOfImports()}"/>
                </div>
                <c:if test="${not empty importList.unsuccessfulEvents}">
                    <div class="alert alert-danger">
                        <spring:message code="label.errors.in.import" arguments="${importList.unsuccessfulEvents.size()}"/>
                    </div>
                </c:if>
        </c:if>
    </div>

    <div class="col-md-11">
    <c:if test="${not empty importList.successfulEvents}">
        <div>
            <h3><spring:message code="label.import.residence.events.valid.debts"/></h3>
            <div>
                <p>Pretende importar as dívidas válidas apresentadas? <button class="btn-primary" type="submit" form="generateDebtsForm">Importar e Gerar Dívidas</button></p>
                <form:form id="generateDebtsForm" cssStyle="display: none" method="POST" action="importEvents/generateDebts" enctype="multipart/form-data">
                    ${csrf.field()}
                    <input hidden name="residenceMonth" value="${residenceMonth.externalId}"/>
                    <input hidden name="identifier" value="${identifier}"/>
                </form:form>

            </div>
            <table class="table table-bordered table-hover">
                <thead>
                    <tr>
                        <th><spring:message code="label.studentNumber"/></th>
                        <th><spring:message code="label.name"/></th>
                        <th><spring:message code="label.fiscalNumber"/></th>
                        <th><spring:message code="label.roomValue"/></th>
                        <th><spring:message code="label.room"/></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="event" items="${importList.successfulEvents}">
                        <tr>
                            <td><c:out value="${event.userName}"/></td>
                            <td><c:out value="${event.name}"/></td>
                            <td><c:out value="${event.fiscalNumber}"/></td>
                            <td><c:out value="${event.roomValue}"/></td>
                            <td><c:out value="${event.room}"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
    <c:if test="${not empty importList.unsuccessfulEvents}">
        <div>
            <h3><spring:message code="label.import.residence.events.invalid.debts"/></h3>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th><spring:message code="label.studentNumber"/></th>
                    <th><spring:message code="label.name"/></th>
                    <th><spring:message code="label.fiscalNumber"/></th>
                    <th><spring:message code="label.roomValue"/></th>
                    <th><spring:message code="label.room"/></th>
                    <th>Mensagem de Erro</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="event" items="${importList.unsuccessfulEvents}">
                    <tr>
                        <td><c:out value="${event.userName}"/></td>
                        <td><c:out value="${event.name}"/></td>
                        <td><c:out value="${event.fiscalNumber}"/></td>
                        <td><c:out value="${event.roomValue}"/></td>
                        <td><c:out value="${event.room}"/></td>
                        <td class="warning"><spring:message code="${event.statusMessage}"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>

    </div>
</div>



