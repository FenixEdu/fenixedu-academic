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
<%@ page isELIgnored="true"%>
<%@page import="org.fenixedu.academic.domain.thesis.ThesisParticipationType"%>
<%@page import="org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant"%>
<%@page import="org.fenixedu.academic.domain.thesis.ThesisFile"%>
<%@page import="org.fenixedu.academic.util.MultiLanguageString"%>
<%@page import="org.fenixedu.commons.i18n.I18N"%>
<%@page import="java.util.List"%>
<%@page import="org.fenixedu.academic.ui.struts.action.coordinator.thesis.ThesisPresentationState"%>
<%@page import="org.fenixedu.academic.domain.Degree"%>
<%@page import="org.fenixedu.academic.domain.ExecutionYear"%>
<%@page import="org.fenixedu.academic.domain.ExecutionSemester"%>
<%@page import="org.fenixedu.academic.domain.Enrolment"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<bean:define id="thesis" name="thesis" type="org.fenixedu.academic.domain.thesis.Thesis"/>

<jsp:include page="viewThesisHeader.jsp"/>

<h3 class="separator2 mtop2"><bean:message key="title.scientificCouncil.thesis.review.section.orientation.add"/></h3>

<div style="margin-left: 35px; width: 90%;">
	<fr:edit name="evaluationMemberBean"
    	     action="<%= "/manageSecondCycleThesis.do?method=addEvaluationMember&amp;thesisOid=" + thesis.getExternalId() %>"
        	 schema="thesis.jury.proposal.information.edit">
		<fr:schema type="org.fenixedu.academic.domain.thesis.Thesis" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
			<fr:slot name="thesisParticipationType" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" key="label.orientation.member.type" layout="menu-select">
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.ThesisOrientationTypeProvider"/>
			</fr:slot>
			<fr:slot name="person" layout="autoComplete" key="label.orientation.member.person" validator="org.fenixedu.academic.ui.renderers.validators.RequiredAutoCompleteSelectionValidator">
				<fr:property name="size" value="70" />
				<fr:property name="labelField" value="presentationName" />
				<fr:property name="args" value="slot=name,size=20" />
				<fr:property name="minChars" value="3" />
				<fr:property name="provider" value="org.fenixedu.academic.service.services.commons.searchers.SearchPersons" />
				<fr:property name="indicatorShown" value="true" />
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
