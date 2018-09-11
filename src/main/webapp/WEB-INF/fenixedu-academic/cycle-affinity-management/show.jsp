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
<!DOCTYPE html>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="baseUrl" value="/cycle-affinity-management"/>
<spring:url var="logsUrl" value="/cycle-affinity-management/logs"/>
<spring:url var="deleteUrl" value="/cycle-affinity-management/deleteAffinity"/>

<script type='text/javascript'>

    $(document).ready(function() {

        $(function() {
            $("#selectFirstCycle").val("${firstCycle.externalId}").prop('selected', true);
        });

        $("form#firstCycleForm select").change(function() {
            $("form#firstCycleForm").submit();
        });

        $(".delete-affinity").click(function(el) {
            var result = confirm('<spring:message code="label.are.you.sure"/>');
            if (!result) {
                el.preventDefault();
            }
        });

    });

</script>

<div class="page-header">
<h1>
    <spring:message code="title.manage.cycles.affinity"/>
    <small><spring:message code="label.listing" /></small>
</h1>
</div>
<div class="btn-group">
    <a class="btn btn-default" href="${logsUrl}"><spring:message code="label.show.logs"/></a>
</div>
<hr />
<section>
    <form:form id="firstCycleForm" role="form" method="GET" class="form-horizontal">
        <div class="form-group">
            <label for="selectFirstCycle" class="col-sm-1 control-label"><spring:message code="label.first.cycle" /></label>
            <div class="col-sm-9">
                <select name="firstCycle" id="selectFirstCycle" class="form-control">
                    <c:forEach var="fc" items="${firstCycles}">
                        <option value="${fc.externalId}"><c:out value="${fc.parentDegreeCurricularPlan.presentationName}"/></option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit" class="btn btn-primary"><spring:message code="label.search"/></button>
        </div>
    </form:form>
</section>
<hr />
<section>
    <form:form role="form" method="POST" class="form-horizontal">
        ${csrf.field()}
        <input hidden name="firstCycle" value="${firstCycle.externalId}"/>
        <div class="form-group">
            <label for="addAffinity" class="col-sm-1 control-label"><spring:message code="label.new.affinity"/></label>
            <div class="col-sm-9">
                <select name="newAffinity" id="addAffinity" class="form-control">
                    <c:forEach items="${potentialAffinities}" var="pa">
                        <option value="${pa.externalId}"><c:out value="${pa.parentDegreeCurricularPlan.presentationName}"/></option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit" class="btn btn-primary"><spring:message code="label.add"/></button>
        </div>
    </form:form>
</section>
<hr />
<br>
<section>
    <h4>
        <spring:message code="affinity.list.first.course" arguments="${firstCycle.parentDegreeCurricularPlan.presentationName}"/>
    </h4>
    <table class="table" id="affinities">
        <tbody>
        <c:forEach var="affinity" items="${affinities}">
            <tr>
                <td><c:out value="${affinity.parentDegreeCurricularPlan.presentationName}" /></td>
                <td>
                    <form:form role="form" action="${deleteUrl}" method="POST" class="form-horizontal">
                        ${csrf.field()}
                        <input hidden name="firstCycle" value="${firstCycle.externalId}"/>
                        <input hidden name="affinity" value="${affinity.externalId}"/>
                        <button class="btn btn-danger delete-affinity"><spring:message code="label.delete"/>
                    </form:form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</section>
