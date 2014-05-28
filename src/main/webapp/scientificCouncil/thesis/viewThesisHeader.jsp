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
<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType"%>
<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant"%>
<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisFile"%>
<%@page import="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"%>
<%@page import="org.fenixedu.commons.i18n.I18N"%>
<%@page import="java.util.List"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisPresentationState"%>
<%@page import="net.sourceforge.fenixedu.domain.Degree"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionSemester"%>
<%@page import="net.sourceforge.fenixedu.domain.Enrolment"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<jsp:include page="styles.jsp"/>

<bean:define id="thesis" name="thesis" type="net.sourceforge.fenixedu.domain.thesis.Thesis"/>
<%
	final Enrolment enrolment = thesis.getEnrolment();
	final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
	final ExecutionYear executionYear = executionSemester.getExecutionYear();
	final Degree degree = enrolment.getCurricularCourse().getDegree();
%>

<h2 class="separator2">
	<html:link style="border-bottom: none; color: black;" action="<%= "/manageSecondCycleThesis.do?method=showPersonThesisDetails&amp;personOid=" + thesis.getStudent().getPerson().getExternalId() %>">
		<bean:write name="thesis" property="student.person.name"/>
	</html:link>
	<span class="color777" style="font-weight:normal;">(
   	<html:link action="<%= "/manageSecondCycleThesis.do?method=showPersonThesisDetails&amp;personOid=" + thesis.getStudent().getPerson().getExternalId() %>">
		<bean:write name="thesis" property="student.person.username"/>
	</html:link>
	)</span>
</h2>

<table>
	<tr>
		<td>
			<div style="border: 1px solid #ddd; padding: 8px; margin: 0 20px 20px 0;">
				<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;uuid=<bean:write name="thesis" property="student.person.username"/></bean:define>
				<img src="<%= request.getContextPath() + url %>"/>
			</div> 
		</td>
		<td>
			<div id="operations" class="cf"> 
				<div class="grey-box" style="max-width: none;">
					<table>
						<tr>
							<td>
								<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.period"/>:
							</td>
							<td>
								<%= executionSemester.getQualifiedName() %>
							</td>
						</tr>
						<tr>
							<td>
								<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.registration.degree"/>:
							</td>
							<td>
								<%= degree.getPresentationName() %>
							</td>
						</tr>
						<tr>
							<td>
								<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.state"/>:
							</td>
							<td style="font-weight: bold;">
								<%= ThesisPresentationState.getThesisPresentationState(thesis).getLabel() %>
							</td>
						</tr>
						<tr>
							<td>
								<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.evaluate.discussion.date"/>:
							</td>
							<td>
								<%= thesis.getDiscussed() == null ? "" : thesis.getDiscussed().toString("yyyy-MM-dd HH:mm") %>
							</td>							
						</tr>
						<tr>
							<td>
								<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.evaluate.mark"/>:
							</td>
							<td>
								<%= thesis.getMark() == null ? "" : thesis.getMark().toString() %>
							</td>							
						</tr>
						<tr>
							<td>
								<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.evaluate.approval.date"/>:
							</td>
							<td>
								<%= thesis.getEvaluation() == null ? "" : thesis.getEvaluation().toString("yyyy-MM-dd HH:mm")  %>
							</td>							
						</tr>
					</table>
				</div> 
			</div>
		</td>
	</tr>
</table>
