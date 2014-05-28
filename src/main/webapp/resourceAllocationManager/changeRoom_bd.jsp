<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page import="java.util.List"%>
<%@page import="org.fenixedu.spaces.domain.Space"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<%@page import="org.joda.time.Weeks"%>
<%@page import="org.joda.time.Interval"%>
<%@page import="net.sourceforge.fenixedu.domain.OccupationPeriod"%>
<%@page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson"%>
<%@page import="org.joda.time.YearMonthDay"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionDegree"%>
<%@page import="java.util.Set"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionCourse"%>
<%@page import="net.sourceforge.fenixedu.domain.Lesson"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<jsp:include page="/commons/contextLessonAndShiftAndExecutionCourseAndExecutionDegreeAndCurricularYear.jsp" />

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

<h2><bean:message key="link.manage.turnos"/></h2>

<jsp:include page="context.jsp"/>

	<%
		final Lesson lesson = ((InfoLesson) pageContext.findAttribute("lesson")).getLesson();
		final ExecutionCourse executionCourse = lesson.getExecutionCourse();
		final Set<ExecutionDegree> executionDegrees = executionCourse.getExecutionDegrees();
		final YearMonthDay firstPossibleLessonDay = executionCourse.getMaxLessonsPeriod().getLeft();
	%>
		<h4>
		</h4>
		<table class="tstyle1 mtop025 mbottom0 tdcenter">
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
	<%
		for (final OccupationPeriod occupationPeriod : executionCourse.getLessonPeriods()) {
	%>
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
			<logic:iterate id="olesson" name="lesson" property="lesson.shift.associatedLessonsSet">
				<tr <% if (olesson == lesson) { %> class="selectedLesson" <% } else { %> class="notSelectedLesson" <% } %>>
					<td>
	<%
		for (OccupationPeriod occupationPeriod = ((Lesson) olesson).getPeriod(); occupationPeriod != null; occupationPeriod = occupationPeriod.getNextPeriod()) {
	%>
				<% if (occupationPeriod.hasPreviousPeriod()) { %>
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

		<h2>
			<bean:message key="title.editAula"/>
		</h2>
		<html:form action="/manageLesson.do?method=changeRoom">

			<html:hidden alt="<%= PresentationConstants.EXECUTION_PERIOD_OID %>" property="<%= PresentationConstants.EXECUTION_PERIOD_OID %>"
						 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
			<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
			 			value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
			<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
			 			value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
			<html:hidden alt="<%= PresentationConstants.EXECUTION_COURSE_OID %>" property="<%= PresentationConstants.EXECUTION_COURSE_OID %>"
			 			value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
			<html:hidden alt="<%= PresentationConstants.LESSON_OID %>" property="<%= PresentationConstants.LESSON_OID %>"
			 			value="<%= pageContext.findAttribute("lessonOID").toString() %>"/>
			<html:hidden alt="<%= PresentationConstants.SHIFT_OID %>" property="<%= PresentationConstants.SHIFT_OID %>"
			 			value="<%= lesson.getShift().getExternalId().toString() %>"/>

			<select name="spaceOID">
				<% for (final Space space : (List<Space>) request.getAttribute("emptySpaces")) { %>
					<option value="<%= space.getExternalId() %>"><%= space.getPresentationName() %></option>
				<% } %>
			</select>

			<br/>

			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation" styleClass="inputbutton">
				<bean:message key="label.save"/>
			</html:submit>
		</html:form>

		<br/>

		<table class="tstyle1 mtop025 mbottom0 tdcenter">
			<tr>
				<th><bean:message bundle="SOP_RESOURCES" key="label.lesson.week"/></th>
				<th><bean:message bundle="SOP_RESOURCES" key="property.weekday"/></th>
				<th><bean:message bundle="SOP_RESOURCES" key="label.lesson.day"/></th>
				<th><bean:message bundle="SOP_RESOURCES" key="property.room"/></th>
				<th><bean:message bundle="SOP_RESOURCES" key="label.lesson.summary"/></th>
				<th><bean:message bundle="SOP_RESOURCES" key="label.lesson.instance"/></th>
			</tr>
			<logic:iterate id="lessonDate" name="lessonDates" type="net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.NextPossibleSummaryLessonsAndDatesBean">
				<bean:define id="selectableValue" name="lessonDate" property="checkBoxValue"/>
				<tr>
					<td>
						<%= Weeks.weeksBetween(firstPossibleLessonDay, lessonDate.getDate()).getWeeks() + 1 %>
					</td>
					<td><%= lessonDate.getDate().toLocalDate().toString("E") %></td>
					<td><%= lessonDate.getDate().toLocalDate().toString("yyyy-MM-dd") %></td>
					<td><logic:present name="lessonDate" property="room"><bean:write name="lessonDate" property="room.name"/></logic:present></td>
					<td><fr:view name="lessonDate" property="writtenSummary"/></td>
					<td><fr:view name="lessonDate" property="hasLessonInstance"/></td>
				</tr>
			</logic:iterate>
		</table>
