<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType"%>
<%@page import="net.sourceforge.fenixedu.domain.thesis.Thesis"%>
<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant"%>
<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisFile"%>
<%@page import="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"%>
<%@page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@page import="java.util.List"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisPresentationState"%>
<%@page import="net.sourceforge.fenixedu.domain.Degree"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionSemester"%>
<%@page import="net.sourceforge.fenixedu.domain.Enrolment"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<%-- Jury --%>
<h3 class="separator2 mtop2"><bean:message key="title.scientificCouncil.thesis.review.section.jury"/></h3>

<%
	final Thesis thesis = (Thesis) request.getAttribute("thesis");
	final ThesisEvaluationParticipant orientator = thesis.getOrientator();
	final ThesisEvaluationParticipant coorientator = thesis.getCoorientator();
	final ThesisEvaluationParticipant president = thesis.getPresident();
%>
<table class="tstyle4 thlight mtop05" style="margin-left: 35px; width: 90%;">
	<tr>
		<th style="width: 5%;">
			<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.jury.member"/>
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
		if (orientator != null) {
		    request.setAttribute("thesisEvaluationParticipant", orientator);
	%>
			<jsp:include page="thesisEvaluationParticipantLine.jsp"/>
	<%
		}
	%>
	<%
		if (coorientator != null) {
		    request.setAttribute("thesisEvaluationParticipant", coorientator);
	%>
			<jsp:include page="thesisEvaluationParticipantLine.jsp"/>
	<%
		}
	%>
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

<div style="margin-left: 35px; width: 90%;">
	<html:link action="<%= "/manageSecondCycleThesis.do?method=prepareAddJuryMember&amp;thesisOid=" + thesis.getExternalId() %>">
		<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.add.jury.member"/>
	</html:link>
</div>
