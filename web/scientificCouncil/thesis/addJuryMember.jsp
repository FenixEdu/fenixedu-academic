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

<bean:define id="thesis" name="thesis" type="net.sourceforge.fenixedu.domain.thesis.Thesis"/>

<jsp:include page="viewThesisHeader.jsp"/>

<div style="margin-left: 35px; width: 90%;">
	<fr:edit name="juryMemberBean"
    	     action="<%= "/manageSecondCycleThesis.do?method=addJuryMember&amp;thesisOid=" + thesis.getExternalId() %>"
        	 schema="thesis.jury.proposal.information.edit">
		<fr:schema type="net.sourceforge.fenixedu.domain.thesis.Thesis" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
			<fr:slot name="thesisParticipationType" key="label.jury.member.type" layout="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ThesisParticipationTypeProvider"/>
			</fr:slot>
			<fr:slot name="person" layout="autoComplete" key="label.jury.member.person" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredAutoCompleteSelectionValidator">
				<fr:property name="size" value="70" />
				<fr:property name="labelField" value="name" />
				<fr:property name="format" value="${name} - <strong>${istUsername}</strong>" />
				<fr:property name="serviceArgs" value="slot=name,size=20" />
				<fr:property name="minChars" value="3" />
				<fr:property name="serviceName" value="SearchPeopleByNameOrISTID" />
				<fr:property name="indicatorShown" value="true" />
				<fr:property name="className" value="net.sourceforge.fenixedu.domain.Person" />
				<fr:property name="required" value="true"/>
			</fr:slot>
		</fr:schema>
     	<fr:layout name="tabular">
        	<fr:property name="classes" value="tstyle5 tdtop thlight thright mtop05"/>
        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
    	</fr:layout>
    
    	<fr:destination name="cancel" path="<%= "/manageSecondCycleThesis.do?method=showThesisDetails&amp;thesisOid=" + thesis.getExternalId() %>"/>
 	</fr:edit>
</div>
