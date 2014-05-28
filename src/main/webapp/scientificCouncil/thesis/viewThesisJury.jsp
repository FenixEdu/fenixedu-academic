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
<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisState"%>
<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType"%>
<%@page import="net.sourceforge.fenixedu.domain.thesis.Thesis"%>
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

<%-- Jury --%>
<h3 class="separator2 mtop2"><bean:message key="title.scientificCouncil.thesis.review.section.jury"/></h3>

<%
	final Thesis thesis = (Thesis) request.getAttribute("thesis");
	final ThesisEvaluationParticipant president = thesis.getPresident();
%>

<div style="margin-left: 35px; width: 90%;">
	<html:link action="<%= "/manageSecondCycleThesis.do?method=prepareAddJuryMember&amp;thesisOid=" + thesis.getExternalId() %>">
		<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.add.jury.member"/>
	</html:link>
	|
	<bean:define id="url">/scientificCouncilManageThesis.do?method=listScientificComission&amp;degreeId=<%= thesis.getDegree().getExternalId() %>&amp;executionYearId=<%= thesis.getEnrolment().getExecutionYear().getExternalId() %></bean:define>
	<html:link page="<%= url %>">
		<bean:message key="link.list.scientific.comission"/>
	</html:link>
	<%
		if (thesis.getState().ordinal() >= ThesisState.SUBMITTED.ordinal()) {
	%>
	|
	<html:link href="<%= request.getContextPath() + String.format("/coordinator/manageThesis.do?method=printApprovalDocument&amp;executionYearId=%s&amp;thesisID=%s", thesis.getExecutionYear().getExternalId(), thesis.getExternalId()) %>">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.coordinator.list.submitted.thesis.reprint"/>
	</html:link>
	<%
		}
	%>
</div>
<table class="tstyle4 thlight mtop05" style="margin-left: 35px; width: 90%;">
	<tr>
		<th style="width: 5%;">
			<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.jury.member"/>
		</th>
		<th>
		</th>
		<th>
			<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.person.name"/>
		</th>
		<th>
			<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.teacher.category"/>
		</th>
		<th>
			<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.coordinator.thesis.edit.teacher.currentWorkingDepartment"/>
		</th>
		<th style="width: 5%;">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.coordinator.thesis.edit.teacher.credits"/>
		</th>
		<th>
		</th>
	</tr>
	<%
		if (president != null) {
		    request.setAttribute("thesisEvaluationParticipant", president);
	%>
			<jsp:include page="thesisEvaluationParticipantLine.jsp"/>
	<%
		}
		for (final ThesisEvaluationParticipant participant : thesis.getAllParticipants(ThesisParticipationType.VOWEL)) {
			request.setAttribute("thesisEvaluationParticipant", participant);
	%>
			<jsp:include page="thesisEvaluationParticipantLine.jsp"/>
	<%		    
		}
	%>
</table>
