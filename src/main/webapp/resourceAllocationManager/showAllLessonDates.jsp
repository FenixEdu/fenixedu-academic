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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@page import="org.joda.time.Weeks"%>
<%@page import="org.joda.time.Interval"%>
<%@page import="org.fenixedu.academic.domain.ExecutionDegree"%>
<%@page import="java.util.Set"%>
<%@page import="org.fenixedu.academic.domain.OccupationPeriod"%>
<%@page import="org.fenixedu.academic.domain.ExecutionCourse"%>
<%@page import="org.joda.time.YearMonthDay"%>
<%@page import="org.fenixedu.academic.domain.Lesson"%>
<%@ page import="org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants"%>
<html:xhtml/>

<h2><bean:message key="title.show.all.lesson.dates" bundle="SOP_RESOURCES"/></h2>

<script type="text/javascript">
	function invertSelection() {
		$('input[name="lessonDatesToDelete"]').each(function() {
			var inputVal = $(this).val();
			$(this).attr('checked', !($(this).is(':checked')));
		});
	};
</script>

<style>
<!--
.selectedLesson {
	font-weight: bolder;
	font-size: large;
}
.notSelectedLesson {
	color: gray;
}
-->
</style>

<p>
<span class="error0"><!-- Error messages go here -->
	<html:errors/>
</span>
</p>

<bean:define id="parameters"><%=PresentationConstants.LESSON_OID%>=<bean:write name="lesson_" property="externalId"/>&amp;<%=PresentationConstants.SHIFT_OID%>=<bean:write name="shift" property="externalId"/>&amp;<%=PresentationConstants.EXECUTION_COURSE_OID%>=<bean:write name="execution_course" property="externalId"/>&amp;<%=PresentationConstants.ACADEMIC_INTERVAL%>=<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString()%>&amp;<%=PresentationConstants.CURRICULAR_YEAR_OID%>=<bean:write name="curricular_year" property="externalId"/>&amp;<%=PresentationConstants.EXECUTION_DEGREE_OID%>=<bean:write name="execution_degree" property="externalId"/></bean:define>
<bean:define id="linkToReturn">/manageShift.do?method=prepareEditShift&amp;page=0&amp;<bean:write name="parameters" filter="false"/></bean:define>
<bean:define id="linkToCreateNewLessonInstance">/manageLesson.do?method=prepareCreateNewLessonInstance&amp;page=0&amp;<bean:write name="parameters" filter="false"/></bean:define>

<p class="mtop20">
	<ul class="mvert">
		<li>
			<html:link page="<%= linkToReturn %>">
				<bean:message key="link.return"/>
			</html:link>
		</li>
	</ul>
</p>

<%
	final Lesson lesson = (Lesson) request.getAttribute("lesson");
	final ExecutionCourse executionCourse = lesson.getExecutionCourse();
	final Set<ExecutionDegree> executionDegrees = executionCourse.getExecutionDegrees();
	final YearMonthDay firstPossibleLessonDay = executionCourse.getMaxLessonsPeriod().getLeft();
%>
	<h4>
	</h4>
	<table class="tstyle1 mtop025 mbottom0 tdcenter">
		<tr>
			<th>
				<bean:message key="label.semester" bundle="SOP_RESOURCES"/>
			</th>
			<td>
				<%= lesson.getExecutionPeriod().getQualifiedName() %>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.executionCourse" bundle="SOP_RESOURCES"/>
			</th>
			<td>
				<%= lesson.getExecutionCourse().getName() %> (<%= executionCourse.getDegreePresentationString() %>)
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.lesson.period" bundle="SOP_RESOURCES"/>
			</th>
			<td>
				<% final Interval maxInterval = executionCourse.getMaxLessonsInterval(); %>
				<% if (maxInterval != null) { %>
					<%= maxInterval.getStart().toString("yyyy-MM-dd") %>
					-
					<%= maxInterval.getEnd().toString("yyyy-MM-dd") %>
				<% } %>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.shift" bundle="SOP_RESOURCES"/>
			</th>
			<td>
				<%= lesson.getShift().getNome() %>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="property.capacity" bundle="SOP_RESOURCES"/>
			</th>
			<td>
				<%= lesson.getShift().getLotacao() %>
			</td>
		</tr>
	</table>

	<table class="tstyle4 thlight tdcenter">
		<tr>
			<th>
				<bean:message key="label.lesson.period" bundle="SOP_RESOURCES"/>
			</th>
			<th>
				<bean:message key="property.weekday"/>
			</th>
			<th>
				<bean:message key="property.time.start"/>
			</th>
			<th>
				<bean:message key="property.time.end"/>
			</th>
			<th>
				<bean:message key="property.room"/>
			</th>
			<th>
				<bean:message key="property.capacity"/>
			</th>
		</tr>
		<logic:iterate id="olesson" name="lesson" property="shift.associatedLessonsSet">
			<tr <% if (olesson == lesson) { %> class="selectedLesson" <% } else { %> class="notSelectedLesson" <% } %>>
				<td>
<%
	for (OccupationPeriod occupationPeriod = ((Lesson) olesson).getPeriod(); occupationPeriod != null; occupationPeriod = occupationPeriod.getNextPeriod()) {
%>
			<% if (occupationPeriod.getPreviousPeriod() != null) { %>
				<br/>
			<% } %>
			<% for (final Interval interval : occupationPeriod.getIntervals()) { %>
				<% if (!interval.getStart().equals(occupationPeriod.getIntervals().iterator().next().getStart())) { %>
					;
				<% } %>
				<%= interval.getStart().toString("yyyy-MM-dd") %>
				-
				<%= interval.getEnd().toString("yyyy-MM-dd") %>
			<% } %>
<%
	}
%>
				</td>
				<td>
					<bean:write name="olesson" property="diaSemana"/>
				</td>
				<td>
					<dt:format pattern="HH:mm">
						<bean:write name="olesson" property="inicio.timeInMillis"/>
					</dt:format>
				</td>
				<td>
					<dt:format pattern="HH:mm">
						<bean:write name="olesson" property="fim.timeInMillis"/>
					</dt:format>
				</td>
				<td>
					<logic:notEmpty name="olesson" property="sala">
						<bean:write name="olesson" property="sala.name"/>
					</logic:notEmpty>
				</td>
				<td>
					<logic:notEmpty name="olesson" property="sala">
						<bean:write name="olesson" property="sala.allocatableCapacity"/>
					</logic:notEmpty>
				</td>
			</tr>
		</logic:iterate>
	</table>

<%-- Delete Lesson Instances --%>
<bean:define id="linkToDelete">/manageLesson.do?method=deleteLessonInstance&amp;<bean:write name="parameters" filter="false"/></bean:define>
<bean:define id="linkToDeleteMultiple">/resourceAllocationManager/manageLesson.do?method=deleteLessonInstances&amp;<bean:write name="parameters" filter="false"/></bean:define>
<a href="#" onclick="invertSelection();"><bean:message key="label.invert.selection" bundle="SOP_RESOURCES"/></a>
<form action="<%= request.getContextPath() + linkToDeleteMultiple %>" method="post">
	<table class="tstyle1 mtop025 mbottom0 tdcenter">
		<tr>
			<th></th>
			<th><bean:message bundle="SOP_RESOURCES" key="label.lesson.week"/></th>
			<th><bean:message bundle="SOP_RESOURCES" key="property.weekday"/></th>
			<th><bean:message bundle="SOP_RESOURCES" key="label.lesson.day"/></th>
			<th><bean:message bundle="SOP_RESOURCES" key="property.room"/></th>
			<th><bean:message bundle="SOP_RESOURCES" key="label.lesson.summary"/></th>
			<th><bean:message bundle="SOP_RESOURCES" key="label.lesson.instance"/></th>
			<th></th>
		</tr>
		<logic:iterate id="lessonDate" name="lessonDates" type="org.fenixedu.academic.dto.teacher.executionCourse.NextPossibleSummaryLessonsAndDatesBean">
			<bean:define id="selectableValue" name="lessonDate" property="checkBoxValue"/>
			<tr>
				<td>
					<logic:equal name="lessonDate" property="isPossibleDeleteLessonInstance" value="true">
						<input type="checkbox" name="lessonDatesToDelete" value="<%= selectableValue %>"/>
					</logic:equal>
				</td>
				<td>
					<%= Weeks.weeksBetween(firstPossibleLessonDay, lessonDate.getDate()).getWeeks() + 1 %>
				</td>
				<td><%= lessonDate.getDate().toLocalDate().toString("E") %></td>
				<td><%= lessonDate.getDate().toLocalDate().toString("yyyy-MM-dd") %></td>
				<td><logic:present name="lessonDate" property="room"><bean:write name="lessonDate" property="room.name"/></logic:present></td>
				<td><fr:view name="lessonDate" property="writtenSummary"/></td>
				<td><fr:view name="lessonDate" property="hasLessonInstance"/></td>
				<td>
					<logic:equal name="lessonDate" property="isPossibleDeleteLessonInstance" value="true">
						<html:link action="<%= linkToDelete %>" paramId="lessonDate" paramName="lessonDate" paramProperty="checkBoxValue"><bean:message bundle="SOP_RESOURCES" key="label.delete"/></html:link>
					</logic:equal>
				</td>
			</tr>
		</logic:iterate>
	</table>
	<html:submit><bean:message bundle="SOP_RESOURCES" key="label.delete"/></html:submit>
</form>
