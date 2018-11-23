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

<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js"/>
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"/>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css"/>

<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<link rel="stylesheet" href="${datatablesCssUrl}">

<h2><spring:message code="label.debtManagement"/></h2>

<style>
    tfoot {
        display: table-header-group;
    }
</style>

<script type="text/javascript">
    $(document).ready(function() {
        <c:if test="${not empty selectedResidenceYear}">
            $('#residenceYearSelect').val(${selectedResidenceYear.externalId});
            <c:if test="${not empty selectedResidenceMonth}">
                $('#residenceMonthSelect').val(${selectedResidenceMonth.externalId});
            </c:if>
        </c:if>
        $('#residenceYearSelect').bind('change', function() {
            var elements = $('div.container').children().hide(); // hide all the elements
            var value = $(this).val();

            if (value.length) { // if somethings' selected
                elements.children().hide();
                elements.children().filter('.' + value).show(); // show the ones we want
                elements.show()
            }
            else {
                elements.val("");
                elements.hide();

            }
        }).trigger('change');


        $('#eventsTable').DataTable( {
            "paging": false,
            "columnDefs": [
                { "orderable": false, "targets": [8] }
            ],
            initComplete: function () {
                this.api().columns([7,8]).every( function () {
                    var column = this;
                    var select = $('<select><option value="">----</option></select>')
                        .appendTo( $(column.footer()).empty() )
                        .on( 'change', function () {
                            var val = $.fn.dataTable.util.escapeRegex(
                                $(this).val()
                            );

                            column
                                .search( val ? '^'+val+'$' : '', true, false )
                                .draw();
                        } );

                    column.data().unique().sort().each( function ( d, j ) {
                        if(column.search() === '^'+d+'$'){
                            select.append( '<option value="'+d+'" selected="selected">'+d+'</option>' )
                        } else {
                            select.append( '<option value="'+d+'">'+d+'</option>' )
                        }
                    } );
                } );
            }
        } );
        $('.dataTables_filter').remove();


    });

</script>

<form:form role="form" class="form-horizontal" method="get">
    <div class="form-group">
        <label for="residenceYearSelect" class="control-label col-sm-1">Year</label>
        <div class="col-sm-4">
            <select id="residenceYearSelect" name="residenceYear" class="form-control" required>
                <option value=""></option>
                <c:forEach var="residenceYear" items="${residenceYears}">
                    <option value="${residenceYear.externalId}"><c:out value="${residenceYear.year}"/></option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-1">Month</label>
        <div class="col-sm-4 container">
            <select id="residenceMonthSelect" name="residenceMonth" class="form-control" required>
                <option value=""></option>
            <c:forEach var="residenceYear" items="${residenceYears}">
                <c:forEach var="residenceMonth" items="${residenceYear.sortedMonths}">
                    <option class="${residenceYear.externalId}" value="${residenceMonth.externalId}"><spring:message code="${residenceMonth.month}"/></option>
                </c:forEach>
            </c:forEach>
            </select>
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

<c:if test="${not empty selectedResidenceMonth}">
    <div>
        <spring:url value="importEvents?residenceMonth=${selectedResidenceMonth.externalId}" var="importEvents"/>
        <a href="${importEvents}"><spring:message code="label.importFile"/></a>
    </div>
</c:if>

<c:if test="${empty events}">
    <div>
        <p><spring:message code="label.residence.event.search.no.events"/></p>
    </div>
</c:if>

<c:if test="${not empty events}">
    <div class="col-md-11">
        <h3>Dívidas</h3>
        <table id="eventsTable" class="table table-bordered table-hover">

            <thead>
                <tr>
                    <th><spring:message code="label.studentNumber"/></th>
                    <th><spring:message code="label.fiscalNumber"/></th>
                    <th><spring:message code="label.name"/></th>
                    <th><spring:message code="label.room"/></th>
                    <th><spring:message code="label.roomValue"/></th>
                    <th><spring:message code="label.total.paid"/></th>
                    <th><spring:message code="label.total.unused"/></th>
                    <th><spring:message code="label.lastPaymentDate"/></th>
                    <th><spring:message code="label.currentEventState"/></th>
                </tr>
            </thead>

            <tfoot>
            <tr>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
                <th></th>
            </tr>
            </tfoot>

            <tbody>
            <c:forEach items="${events}" var="event">
                <spring:url value="/accounting-management/{event}/details" var="eventDetails">
                    <spring:param name="event" value="${event.externalId}"/>
                </spring:url>
                <spring:url value="{person}" var="personEvents">
                    <spring:param name="person" value="${event.person.externalId}"/>
                </spring:url>
                    <tr>
                        <td><c:out value="${event.person.student.number}"/></td>
                        <td><c:out value="${event.person.socialSecurityNumber}"/></td>
                        <td><a href="${personEvents}"><c:out value="${event.person.name}"/></a></td>
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
                        <td><c:out value="${event.lastPaymentDate.toString('yyyy-MM-dd')}" default="N/A"/></td>
                        <td><spring:message code="${event.currentEventState}"/></td>
                    </tr>
            </c:forEach>
            </tbody>

        </table>
    </div>
</c:if>
