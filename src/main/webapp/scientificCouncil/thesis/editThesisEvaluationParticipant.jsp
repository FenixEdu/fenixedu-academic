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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<bean:define id="thesisEvaluationParticipant" name="thesisEvaluationParticipant" type="net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant"/>
<bean:define id="thesis" name="thesisEvaluationParticipant" property="thesis" type="net.sourceforge.fenixedu.domain.thesis.Thesis"/>
<%
	request.setAttribute("thesis", thesis);
%>

<jsp:include page="viewThesisHeader.jsp"/>

<%
	final ThesisParticipationType type = thesisEvaluationParticipant.getType();
	final String schemaName = type == ThesisParticipationType.ORIENTATOR ? "thesis.jury.proposal.participant.edit.with.credits" :
	    	type == ThesisParticipationType.COORIENTATOR ? "thesis.jury.proposal.participant.edit.with.credits.co" :
	    	    "thesis.jury.proposal.participant.edit";
%>
<div style="margin-left: 35px; width: 90%;">
<fr:edit name="thesisEvaluationParticipant"
         action="<%= "/manageSecondCycleThesis.do?method=showThesisDetails&amp;thesisOid=" + thesis.getExternalId() %>"
         schema="<%= schemaName %>">
     <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 tdtop thlight thright mtop05"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>    
    <fr:destination name="cancel" path="<%= "/manageSecondCycleThesis.do?method=showThesisDetails&amp;thesisOid=" + thesis.getExternalId() %>"/>
</fr:edit>
</div>

<jsp:include page="viewThesisJury.jsp"/>
