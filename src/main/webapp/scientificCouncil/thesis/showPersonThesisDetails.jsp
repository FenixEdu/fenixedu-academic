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
<%@page import="org.fenixedu.academic.ui.struts.action.coordinator.thesis.ThesisPresentationState"%>
<%@page import="org.fenixedu.academic.domain.thesis.Thesis"%>
<%@page import="org.fenixedu.academic.domain.ExecutionSemester"%>
<%@page import="org.fenixedu.academic.domain.ExecutionYear"%>
<%@page import="org.fenixedu.academic.domain.Enrolment"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.SortedSet"%>
<%@page import="org.fenixedu.academic.domain.ExecutionDegree"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<jsp:include page="styles.jsp"/>

<h2 class="separator2">
	<bean:write name="person" property="name"/>
	<span class="color777" style="font-weight:normal;">(
	<bean:write name="person" property="username"/>
	)</span>
</h2>

<table>
	<tr>
		<td>
			<br/>
			<div style="border: 1px solid #ddd; padding: 8px; margin: 0 20px 20px 0;">
				<bean:define id="url" type="java.lang.String">/user/photo/<bean:write name="person" property="username"/></bean:define>
				<img src="<%= request.getContextPath() + url %>"/>
			</div> 
		</td>
		<td>
			<logic:notPresent name="person" property="student">
				<br/> <br/>
				<em style="color: red;">
					<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.person.not.a.student"/>
				</em>
				<br/> <br/> <br/> <br/>
			</logic:notPresent>
			<logic:present name="person" property="student">
				<div style="font-weight: bold; display: block;">
					<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.registrations"/>
				</div>
				<table class="tstyle1 thlight mtop025">
					<tr>
						<th>
							<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.registration.startDate"/>
						</th>
						<th>
							<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.registration.number"/>
						</th>
						<th>
							<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.registration.degree"/>
						</th>
						<th>
							<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.registrationAgreement"/>
						</th>
						<th>
							<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.registration.state"/>
						</th>
					</tr>
					<logic:iterate id="registration" name="person" property="student.registrations">
						<tr>
							<td>
								<bean:write name="registration" property="startDate"/>
							</td>
							<td>
								<bean:write name="registration" property="number"/>
							</td>
							<td>
								<bean:write name="registration" property="degreeNameWithDescription"/>
							</td>
							<td>
								<bean:write name="registration" property="registrationProtocol.description.content"/>
							</td>
							<td>
								<bean:write name="registration" property="activeStateType.description"/>
							</td>
						</tr>
					</logic:iterate>
				</table>
			</logic:present>
		</td>
	</tr>
</table>

<logic:present name="person" property="student">
	<bean:define id="student" name="person" property="student" type="org.fenixedu.academic.domain.student.Student"/>
	<h3 class="separator2">
		<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.processes"/>
	</h3>
	<%
		final Set<Enrolment> enrolments = student.getDissertationEnrolments();
		if (enrolments.isEmpty()) {
	%>
			<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.processes.none"/>
	<%
		} else {
	%>
			<table class="tstyle4 thlight mtop05" style="margin-left: 35px; width: 90%;">
				<tr>
					<th>
						<bean:message bundle="STUDENT_RESOURCES" key="label.execution.year"/>
					</th>
					<th>
						<bean:message bundle="STUDENT_RESOURCES" key="label.semester"/>
					</th>
					<th>
						<bean:message bundle="STUDENT_RESOURCES" key="label.student.degree"/>
					</th>
					<th>
						<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.state"/>
					</th>
					<th>
						<bean:message bundle="STUDENT_RESOURCES" key="finalDegreeWorkProposalHeader.title"/>
					</th>
				</tr>
	<%
				for (final Enrolment enrolment : enrolments) {
				    final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
				    final ExecutionYear executionYear = executionSemester.getExecutionYear();
				    for (final Thesis thesis : enrolment.getThesesSet()) {
	%>
					<tr>
						<td>
							<%= executionYear.getYear() %>
						</td>
						<td>
							<%= executionSemester.getSemester() %>
						</td>
						<td>
							<%= enrolment.getCurricularCourse().getDegree().getSigla() %>
						</td>
						<td>
							<%= ThesisPresentationState.getThesisPresentationState(thesis).getLabel() %>
						</td>
						<td>
							<html:link action="<%= "/manageSecondCycleThesis.do?method=showThesisDetails&amp;thesisOid=" + thesis.getExternalId() %>">
								<%= thesis.getTitle().getContent() %>
							</html:link>
						</td>
					</tr>
	<%
					}
					if (enrolment.getThesesSet().size() == 0) {
	%>
						<tr>
							<td>
								<%= executionYear.getYear() %>
							</td>
							<td>
								<%= executionSemester.getSemester() %>
							</td>
							<td>
								<%= enrolment.getCurricularCourse().getDegree().getSigla() %>
							</td>
							<td>
								<%= ThesisPresentationState.UNEXISTING.getLabel() %>
							</td>
							<td>
							</td>
						</tr>
	<%
				    }
				}
	%>
			</table>
			<div style="margin-left: 35px; width: 90%; color: graytext;">
	<%
				for (final ThesisPresentationState thesisPresentationState : ThesisPresentationState.values()) {
	%>
					<%= thesisPresentationState.getLabel() %>
	<%
				}
	%>
			</div>
	<%
		}
	%>

	<br/>

</logic:present>
