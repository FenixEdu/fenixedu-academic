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
<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoRoom" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<h2><bean:message key="title.exams.list"/></h2>
<table width="100%">
	<tr>
		<td class="infoselected"><p>O dia e turno selecionados s&atilde;o:</p>
			<strong><jsp:include page="timeContext.jsp"/></strong>
        </td>
    </tr>
</table>
<br />
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>
<bean:define id="deleteConfirm">
	return confirm('<bean:message key="message.confirm.delete.exam"/>')
</bean:define>
<logic:notPresent name="<%= PresentationConstants.LIST_EXAMSANDINFO %>" scope="request">
	<table align="center">
		<tr>
			<td>
				<span class="error"><!-- Error messages go here --><bean:message key="message.exams.none.for.day.shift"/></span>
			</td>
		</tr>
	</table>
</logic:notPresent>
<logic:present name="<%= PresentationConstants.LIST_EXAMSANDINFO %>" scope="request">
	<table width="100%">
		<tr>
			<th class="listClasses-header"><bean:message key="property.course"/></th>
			<th class="listClasses-header"><bean:message key="property.degrees"/></th>
			<th class="listClasses-header"><bean:message key="property.number.students.attending.course"/></th>
			<th class="listClasses-header"><bean:message key="property.exam.reservedRooms"/></th>
			<th class="listClasses-header"><bean:message key="property.exam.number.vacancies"/></th>
			<th class="listClasses-header"><bean:message key="property.exam.manage"/></th>
		</tr>
		<logic:iterate id="infoViewExam" indexId="index" name="<%= PresentationConstants.LIST_EXAMSANDINFO %>" scope="request">
			<% int seatsReserved = 0; %>
			<tr>
				<td class="listClasses">
					<logic:iterate id="infoExecutionCourse" name="infoViewExam" property="infoExecutionCourses">
						<bean:write name="infoExecutionCourse" property="nome"/><br />
					</logic:iterate>					
				</td>
				<td class="listClasses">
					<logic:iterate id="infoDegree" name="infoViewExam" property="infoDegrees">
						<bean:write name="infoDegree" property="sigla"/><br />
					</logic:iterate>
				</td>
				<td class="listClasses"><bean:write name="infoViewExam" property="numberStudentesAttendingCourse"/></td>
				<td class="listClasses">
					<logic:present name="infoViewExam" property="infoExam.associatedRooms">
						<logic:iterate id="infoRoom" name="infoViewExam" property="infoExam.associatedRooms">
							<bean:write name="infoRoom" property="nome"/>; 
							<% seatsReserved += ((InfoRoom) infoRoom).getCapacidadeExame().intValue(); %>
						</logic:iterate> 
					</logic:present>
					<logic:notPresent name="infoViewExam" property="infoExam.associatedRooms">
						<bean:message key="message.exam.no.rooms"/>
					</logic:notPresent>					
				</td>
				<td class="listClasses">
					<%= ((InfoViewExamByDayAndShift) infoViewExam).getNumberStudentesAttendingCourse().intValue() - seatsReserved %>
				</td>
				<td class="listClasses">
					<html:link paramId="indexExam" paramName="index"
							   page="<%= "/viewExamsDayAndShiftForm.do?method=edit&amp;"
							   				+ PresentationConstants.EXECUTION_PERIOD_OID
											+ "="
  											+ pageContext.findAttribute("executionPeriodOID")
											+ "&amp;"
							   				+ PresentationConstants.EXAM_DATEANDTIME
											+ "="
  											+ pageContext.findAttribute("examDateAndTime")
  									%>">
						<bean:message key="label.edit"/>
					</html:link>;
					<html:link paramId="indexExam" paramName="index"
							   onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'
							   page="<%= "/viewExamsDayAndShiftForm.do?method=delete&amp;"
							   				+ PresentationConstants.EXECUTION_PERIOD_OID
											+ "="
  											+ pageContext.findAttribute("executionPeriodOID")
											+ "&amp;"
							   				+ PresentationConstants.EXAM_DATEANDTIME
											+ "="
  											+ pageContext.findAttribute("examDateAndTime")
  									%>">
						<bean:message key="label.delete"/>
					</html:link>
					<html:link paramId="indexExam" paramName="index"
							   page="<%= "/viewExamsDayAndShiftForm.do?method=addExecutionCourse&amp;"
							   				+ PresentationConstants.EXECUTION_PERIOD_OID
											+ "="
  											+ pageContext.findAttribute("executionPeriodOID")
											+ "&amp;"
							   				+ PresentationConstants.EXAM_DATEANDTIME
											+ "="
  											+ pageContext.findAttribute("examDateAndTime")
  									%>">
						<br /><bean:message key="label.add.executionCourse"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br />
	<br />
	- N� de vagas para exames: <bean:write name="<%= PresentationConstants.AVAILABLE_ROOM_OCCUPATION %>" scope="request"/>
<br />
</logic:present>