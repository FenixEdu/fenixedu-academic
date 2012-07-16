<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType"%>
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

<jsp:include page="styles.jsp"/>

<em><bean:message key="scientificCouncil"/></em>

<bean:define id="thesisEvaluationParticipant" name="thesisEvaluationParticipant" type="net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant"/>
<bean:define id="thesis" name="thesisEvaluationParticipant" property="thesis" type="net.sourceforge.fenixedu.domain.thesis.Thesis"/>
<%
	final Enrolment enrolment = thesis.getEnrolment();
	final ExecutionSemester executionSemester = enrolment.getExecutionPeriod();
	final ExecutionYear executionYear = executionSemester.getExecutionYear();
	final Degree degree = enrolment.getCurricularCourse().getDegree();
%>

<h3 class="mtop3 mbottom05">
	<bean:write name="thesis" property="student.person.name"/>
	<span class="color777" style="font-weight:normal;">(
	<bean:write name="thesis" property="student.person.username"/>
	)</span>
</h3>

<table>
	<tr>
		<td>
			<br/>
			<div style="border: 1px solid #ddd; padding: 8px; margin: 0 20px 20px 0;">
				<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;contentContextPath_PATH=/homepage&amp;uuid=<bean:write name="thesis" property="student.person.username"/></bean:define>
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
					</table>
				</div> 
			</div>
		</td>
	</tr>
</table>

<%
	final ThesisParticipationType type = thesisEvaluationParticipant.getType();
	final String schemaName = type == ThesisParticipationType.ORIENTATOR ? "thesis.jury.proposal.participant.edit.with.credits" :
	    	type == ThesisParticipationType.COORIENTATOR ? "thesis.jury.proposal.participant.edit.with.credits.co" :
	    	    "thesis.jury.proposal.participant.edit";
%>
<fr:edit name="thesisEvaluationParticipant"
         action="<%= "/manageSecondCycleThesis.do?method=showThesisDetails&amp;thesisOid=" + thesis.getExternalId() %>"
         schema="<%= schemaName %>">
     <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 tdtop thlight thright mtop05"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>    
    <fr:destination name="cancel" path="<%= "/manageSecondCycleThesis.do?method=showThesisDetails&amp;thesisOid=" + thesis.getExternalId() %>"/>
</fr:edit>

<%-- Jury --%>
<h3 class="separator2 mtop2"><bean:message key="title.scientificCouncil.thesis.review.section.jury"/></h3>

<%
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

