<%@ page import="org.fenixedu.academic.domain.Exam" %>
<%@ page import="org.fenixedu.academic.domain.ExecutionSemester" %>
<%@ page import="org.fenixedu.academic.domain.WrittenEvaluation" %>
<%@ page import="org.fenixedu.academic.domain.WrittenTest" %>
<%@ page import="org.fenixedu.academic.domain.space.WrittenEvaluationSpaceOccupation" %>
<%@ page import="org.fenixedu.bennu.core.util.CoreConfiguration" %>
<%@ page import="org.fenixedu.spaces.domain.Space" %>
<%@ page import="java.util.SortedSet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

${portal.angularToolkit()}

<%
    final SortedSet<ExecutionSemester> executionSemesters = (SortedSet<ExecutionSemester>) request.getAttribute("executionSemesters");
    final SortedSet<WrittenEvaluation> writtenEvaluations = (SortedSet<WrittenEvaluation>) request.getAttribute("writtenEvaluations");
%>

<h2>
    <spring:message code="label.evaluations.enrolment" text="Evaluations"/>
</h2>

<form class="form-horizontal" method="post" style="margin-top: 25px;"
      action="<%= CoreConfiguration.getConfiguration().applicationUrl() %>/safe-file-share/shareFile">
    <input type="hidden" name="fileInfo" id="fileInfo" value="">
    <div class="mb-2 row">
        <label class="col-sm-2 col-form-label" for="executionSemester"><spring:message code="label.execution.period" text="Execution Period"/></label>
        <div class="col-sm-10">
            <select name="executionSemester" id="executionSemester" class="form-control" onchange="return filterEvaluations();">
                <% for (final ExecutionSemester executionSemester : executionSemesters) { %>
                <option value="<%= executionSemester.getExternalId() %>" <% if (executionSemester.isCurrent()) { %>selected<% } %>
                ><%= executionSemester.getQualifiedName() %></option>
                <% } %>
            </select>
        </div>
    </div>
    <div class="mb-2 row">
        <label class="col-sm-2 col-form-label" for="evaluationType"><spring:message code="label.evaluationType" text="Evaluation Type"/></label>
        <div class="col-sm-10">
            <select name="evaluationType" id="evaluationType" class="form-control" onchange="return filterEvaluations();">
                <option value="ALL" selected><spring:message code="label.evaluationType.all" text="All"/></option>
                <option value="Exam"><spring:message code="label.evaluationType.exams" text="Exams"/></option>
                <option value="WrittenTest"><spring:message code="label.evaluationType.tests" text="Tests"/></option>
            </select>
        </div>
    </div>
</form>

<h3 style="margin-top: 50px;">
    <spring:message code="label.evaluations.not.enrolled" text="Evaluation Not Enrolled"/>
</h3>

<table class="table" style="margin-top: 25px;">
    <thead>
    <tr>
        <th><spring:message code="label.evaluations.type" text="Tipo"/></th>
        <th><spring:message code="label.evaluations.execution.course" text="Program"/></th>
        <th><spring:message code="label.evaluations.evaluation" text="Evaluation"/></th>
        <th><spring:message code="label.evaluations.evaluation.rooms" text="Room(s)"/></th>
        <th><spring:message code="label.evaluations.evaluation.date" text="Evaluation Date"/></th>
        <th><spring:message code="label.evaluations.evaluation.enrolment.period" text="Enrolment Period"/></th>
        <td></td>
    </tr>
    </thead>
    <tbody>
    <% for (final WrittenEvaluation writtenEvaluation : writtenEvaluations) {
        if (writtenEvaluation.getEnrolmentPeriodStart() != null && writtenEvaluation.getEnrolmentPeriodEnd() != null
                && !writtenEvaluation.isEnrolled()) {
    %>
    <tr class="evaluation <%= writtenEvaluation.getClass().getSimpleName() + " " + writtenEvaluation.getAssociatedExecutionCoursesSet().iterator().next().getExecutionPeriod().getExternalId() %>">
        <td>
            <% if (writtenEvaluation instanceof Exam) { %>
            <spring:message code="label.evaluationType.exams" text="Exams"/>
            <% } else if (writtenEvaluation instanceof WrittenTest) { %>
            <spring:message code="label.evaluationType.tests" text="Tests"/>
            <% } %>
        </td>
        <td><%= writtenEvaluation.getFullName() %></td>
        <td>
            <% if (writtenEvaluation instanceof Exam) { %>
            <%= writtenEvaluation.getPresentationName() %>
            <% } else if (writtenEvaluation instanceof WrittenTest) { %>
            <%= ((WrittenTest) writtenEvaluation).getDescription() %>
            <% } %>
        </td>
        <td>
            <% for(final Space room : writtenEvaluation.getAssociatedRooms()) { %>
                <%= room.getName() %>
            <% } %>
        </td>
        <td>
            <%= writtenEvaluation.getBeginningDateTime().toString("yyyy-MM-dd") %>
            &nbsp;
            <%= writtenEvaluation.getBeginningDateTime().toString("HH:mm") %>
            <% if (writtenEvaluation.getEndDateHourMinuteSecond() != null) { %>
                <spring:message code="label.evaluations.evaluation.date.until" text="until"/>
                <%= writtenEvaluation.getEndDateTime().toString("HH:mm") %>
            <% } %>
        </td>
        <td>
            <% if (writtenEvaluation.getIsInEnrolmentPeriod()) { %>
            <spring:message code="label.evaluations.evaluation.enrolment.period.open" text="Open"/>
            <% } else { %>
            <spring:message code="label.evaluations.evaluation.enrolment.period.closed" text="Closed"/>
            <% } %>
            <br/>
            <spring:message code="label.evaluations.evaluation.date.from" text="until"/>
            <%= writtenEvaluation.getEnrolmentPeriodStart().toString("yyyy-MM-dd HH:mm") %>
            <br/>
            <spring:message code="label.evaluations.evaluation.date.until" text="until"/>
            <%= writtenEvaluation.getEnrolmentPeriodEnd().toString("yyyy-MM-dd HH:mm") %>
        </td>
        <td>
            <% if (writtenEvaluation.getIsInEnrolmentPeriod()) { %>
                <a href="<%= CoreConfiguration.getConfiguration().applicationUrl()%>/enrollment/evaluations/showEvaluations/<%= writtenEvaluation.getExternalId() %>/enrol" class="btn btn-primary">
                    <spring:message code="label.evaluations.evaluation.enrol" text="Enrol"/>
                </a>
            <% } %>
        </td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>

<h3 style="margin-top: 50px;">
    <spring:message code="label.evaluations.enrolled" text="Evaluation Enrolled"/>
</h3>

<table class="table" style="margin-top: 25px;">
    <thead>
    <tr>
        <th><spring:message code="label.evaluations.type" text="Tipo"/></th>
        <th><spring:message code="label.evaluations.execution.course" text="Program"/></th>
        <th><spring:message code="label.evaluations.evaluation" text="Evaluation"/></th>
        <th><spring:message code="label.evaluations.evaluation.rooms" text="Room(s)"/></th>
        <th><spring:message code="label.evaluations.evaluation.date" text="Evaluation Date"/></th>
        <th><spring:message code="label.evaluations.evaluation.enrolment.period" text="Enrolment Period"/></th>
        <td></td>
    </tr>
    </thead>
    <tbody>
    <% for (final WrittenEvaluation writtenEvaluation : writtenEvaluations) {
        if (writtenEvaluation.isEnrolled()) {
    %>
    <tr class="evaluation <%= writtenEvaluation.getClass().getSimpleName() + " " + writtenEvaluation.getAssociatedExecutionCoursesSet().iterator().next().getExecutionPeriod().getExternalId() %>">
        <td>
            <% if (writtenEvaluation instanceof Exam) { %>
            <spring:message code="label.evaluationType.exams" text="Exams"/>
            <% } else if (writtenEvaluation instanceof WrittenTest) { %>
            <spring:message code="label.evaluationType.tests" text="Tests"/>
            <% } %>
        </td>
        <td><%= writtenEvaluation.getFullName() %></td>
        <td>
            <% if (writtenEvaluation instanceof Exam) { %>
            <%= writtenEvaluation.getPresentationName() %>
            <% } else if (writtenEvaluation instanceof WrittenTest) { %>
            <%= ((WrittenTest) writtenEvaluation).getDescription() %>
            <% } %>
        </td>
        <td>
            <% for (final WrittenEvaluationSpaceOccupation roomOccupation : writtenEvaluation.getWrittenEvaluationSpaceOccupationsSet()) { %>
                <% final Space room = roomOccupation.getRoom(); %>
                <%= room.getName() %>
            <% } %>
        </td>
        <td>
            <%= writtenEvaluation.getBeginningDateTime().toString("yyyy-MM-dd") %>
            &nbsp;
            <%= writtenEvaluation.getBeginningDateTime().toString("HH:mm") %>
            <% if (writtenEvaluation.getEndDateHourMinuteSecond() != null) { %>
                <spring:message code="label.evaluations.evaluation.date.until" text="until"/>
                <%= writtenEvaluation.getEndDateTime().toString("HH:mm") %>
            <% } %>
        </td>
        <td>
            <% if (writtenEvaluation.getIsInEnrolmentPeriod()) { %>
                <spring:message code="label.evaluations.evaluation.enrolment.period.open" text="Open"/>
            <% } else { %>
                <spring:message code="label.evaluations.evaluation.enrolment.period.closed" text="Closed"/>
            <% } %>
            <br/>
            <spring:message code="label.evaluations.evaluation.date.from" text="until"/>
            <%= writtenEvaluation.getEnrolmentPeriodStart() == null ? "" : writtenEvaluation.getEnrolmentPeriodStart().toString("yyyy-MM-dd HH:mm") %>
            <br/>
            <spring:message code="label.evaluations.evaluation.date.until" text="until"/>
            <%= writtenEvaluation.getEnrolmentPeriodEnd() == null ? "" : writtenEvaluation.getEnrolmentPeriodEnd().toString("yyyy-MM-dd HH:mm") %>
        </td>
        <td>
            <% if (writtenEvaluation.getIsInEnrolmentPeriod()) { %>
            <a href="<%= CoreConfiguration.getConfiguration().applicationUrl()%>/enrollment/evaluations/showEvaluations/<%= writtenEvaluation.getExternalId() %>/unenrol" class="btn btn-primary">
                <spring:message code="label.evaluations.evaluation.unenrol" text="Unenrol"/>
            </a>
            <% } %>
        </td>
    </tr>
    <%
            }
        }
    %>
    </tbody>
</table>


<h3 style="margin-top: 50px;">
    <spring:message code="label.evaluations.without.enrolment.period" text="Evaluations Without Enrolment Period"/>
</h3>

<table class="table" style="margin-top: 25px;">
    <thead>
    <tr>
        <th><spring:message code="label.evaluations.type" text="Tipo"/></th>
        <th><spring:message code="label.evaluations.execution.course" text="Program"/></th>
        <th><spring:message code="label.evaluations.evaluation" text="Evaluation"/></th>
        <th><spring:message code="label.evaluations.evaluation.rooms" text="Room(s)"/></th>
        <th><spring:message code="label.evaluations.evaluation.date" text="Evaluation Date"/></th>
    </tr>
    </thead>
    <tbody>
        <% for (final WrittenEvaluation writtenEvaluation : writtenEvaluations) {
            if (writtenEvaluation.getEnrolmentPeriodStart() == null || writtenEvaluation.getEnrolmentPeriodEnd() == null) {
        %>
            <tr class="evaluation <%= writtenEvaluation.getClass().getSimpleName() + " " + writtenEvaluation.getAssociatedExecutionCoursesSet().iterator().next().getExecutionPeriod().getExternalId() %>">
                <td>
                    <% if (writtenEvaluation instanceof Exam) { %>
                        <spring:message code="label.evaluationType.exams" text="Exams"/>
                    <% } else if (writtenEvaluation instanceof WrittenTest) { %>
                        <spring:message code="label.evaluationType.tests" text="Tests"/>
                    <% } %>
                </td>
                <td><%= writtenEvaluation.getFullName() %></td>
                <td>
                    <% if (writtenEvaluation instanceof Exam) { %>
                        <%= writtenEvaluation.getPresentationName() %>
                    <% } else if (writtenEvaluation instanceof WrittenTest) { %>
                        <%= ((WrittenTest) writtenEvaluation).getDescription() %>
                    <% } %>
                </td>
                <td>
                    <% for(final Space room : writtenEvaluation.getAssociatedRooms()) { %>
                        <%= room.getName() %>
                    <% } %>
                </td>
                <td>
                    <%= writtenEvaluation.getBeginningDateTime().toString("yyyy-MM-dd") %>
                    &nbsp;
                    <%= writtenEvaluation.getBeginningDateTime().toString("HH:mm") %>
                    <% if (writtenEvaluation.getEndDateHourMinuteSecond() != null) { %>
                        <spring:message code="label.evaluations.evaluation.date.until" text="until"/>
                        <%= writtenEvaluation.getEndDateTime().toString("HH:mm") %>
                    <% } %>
                </td>
            </tr>
        <%
            }
           }
        %>
    </tbody>
</table>

<style>
    .table thead tr {background: #f1f1f1 }
    .table tbody tr:nth-child(even) {background: #edfffe }
    .table tbody tr:nth-child(odd) {background: #ffffff}
</style>

<script>
    function filterEvaluations() {
        var executionSemester = document.getElementById("executionSemester").value;
        var evaluationType = document.getElementById("evaluationType").value;
        var evaluations = document.getElementsByClassName("evaluation");

        for (i = 0; i < evaluations.length; i++) {
            var row = evaluations[i];
            if ((evaluationType === "ALL" || row.classList.contains(evaluationType))
                && row.classList.contains(executionSemester)) {
                row.style.display = "table-row";
            } else {
                row.style.display = "none";
            }
        }
    }

    filterEvaluations();
</script>