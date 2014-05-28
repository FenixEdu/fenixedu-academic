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

<bean:define id="thesis" name="thesis" type="net.sourceforge.fenixedu.domain.thesis.Thesis"/>

<jsp:include page="viewThesisHeader.jsp"/>

<div style="margin-left: 35px; width: 90%;">
	<fr:edit name="thesis"
    	     action="<%= "/manageSecondCycleThesis.do?method=showThesisDetails&amp;thesisOid=" + thesis.getExternalId() %>"
        	 schema="thesis.jury.proposal.information.edit">
		<fr:schema type="net.sourceforge.fenixedu.domain.thesis.Thesis" bundle="APPLICATION_RESOURCES">
    		<fr:slot name="finalTitle" validator="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredMultiLanguageStringValidator">
        		<fr:property name="size" value="80"/>
    		</fr:slot>
    		<fr:slot name="keywords" layout="null-as-label" key="label.thesis.keywords">
        		<fr:property name="label" value="label.thesis.field.empty"/>
        		<fr:property name="key" value="true"/>
        		<fr:property name="size" value="80"/>
    		</fr:slot>
		    <fr:slot name="thesisAbstract" layout="area" key="label.thesis.abstract">
        		<fr:property name="label" value="label.thesis.field.empty"/>
        		<fr:property name="key" value="true"/>
        		<fr:property name="columns" value="80"/>
        		<fr:property name="rows" value="10"/>
    		</fr:slot>
    		<fr:slot name="comment" layout="longText">
        		<fr:property name="columns" value="80"/>
        		<fr:property name="rows" value="10"/>
    		</fr:slot>
		</fr:schema>
     	<fr:layout name="tabular">
        	<fr:property name="classes" value="tstyle5 tdtop thlight thright mtop05"/>
        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
    	</fr:layout>
    
    	<fr:destination name="cancel" path="<%= "/manageSecondCycleThesis.do?method=showThesisDetails&amp;thesisOid=" + thesis.getExternalId() %>"/>
 	</fr:edit>
</div>
