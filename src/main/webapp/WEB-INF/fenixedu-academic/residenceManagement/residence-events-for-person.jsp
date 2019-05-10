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

<spring:url value="./{person}/pay" var="payUrl">
    <spring:param name="person" value="${person.externalId}"/>
</spring:url>

<spring:url value="./{person}/cancel" var="cancelUrl">
    <spring:param name="person" value="${person.externalId}"/>
</spring:url>


<div class="col-md-10">
    <h2>Residente</h2>

    <jsp:include page="../../fenixedu-academic/accounting/heading-person.jsp"/>
</div>

<div class="col-md-10">
    <h2>Dívidas do Residente</h2>
    <table class="table table-bordered table-hover">

        <thead>
        <tr>
            <th>Ano</th>
            <th>Mês</th>
            <th>Quarto</th>
            <th>Mensalidade</th>
            <th>Total Pago</th>
            <th>Total Não Utilizado</th>
            <th>Data do Último Pagamento</th>
            <th>Estado</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${personEvents}" var="event">
            <spring:url value="/accounting-management/{event}/details" var="eventDetails">
                <spring:param name="event" value="${event.externalId}"/>
            </spring:url>
                <tr>
                    <td><spring:message code="${event.residenceMonth.year.year}"/></td>
                    <td><spring:message code="${event.residenceMonth.month}"/></td>
                    <td><c:out value="${event.room}"/></td>
                    <td><c:out value="${event.roomValue}"/></td>
                    <td>
                        <c:out value="${event.isCancelled() ? '0.00' : event.payedAmount.amount}"/>
                    </td>
                    <td>
                        <c:if test="${event.isCancelled()}">
                            <c:out value="${'0.00'}"/>
                        </c:if>
                        <c:if test="${not event.isCancelled()}">
                            <c:out value="${event.payedAmount.amount > event.originalAmountToPay.amount ? event.payedAmount.amount - event.originalAmountToPay.amount : '0.00'}"/>
                        </c:if>
                    </td>
                    <td><c:out value="${event.lastPaymentDate.toString('yyyy-MM-dd HH:mm:ss')}"/></td>
                    <td><spring:message code="${event.currentEventState}"/></td>
                    <td>
                        <c:if test="${event.isOpen()}">
                            <form:form role="form" class="form-horizontal" action="${payUrl}" method="post">
                                ${csrf.field()}
                                <input hidden name="event" value="${event.externalId}"/>
                                <div class="col-sm-3">
                                    <button class="btn btn-primary" onclick="return confirm('Are you sure?')" type="submit">
                                        <spring:message text="Saldar" code="label.pay.residence.event"/>
                                    </button>
                                </div>
                            </form:form>

                            <form:form role="form" class="form-horizontal" action="${cancelUrl}" method="post">
                                ${csrf.field()}
                                <input hidden name="event" value="${event.externalId}"/>
                                <div class="col-sm-offset-2 col-sm-2">
                                    <button class="btn btn-danger" onclick="return confirm('Are you sure?')" type="submit">
                                        <spring:message text="Cancelar" code="label.cancel.residence.event"/>
                                    </button>
                                </div>
                            </form:form>
                        </c:if>
                    </td>
                </tr>
        </c:forEach>
        </tbody>

    </table>
</div>
