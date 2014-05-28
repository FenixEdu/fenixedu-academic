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
<%@ page isELIgnored="true"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriodConfirmationOption"%>
<%@ page language="java"%>
<%@page import="net.sourceforge.fenixedu.domain.Person"%>
<%@page import="net.sourceforge.fenixedu.domain.Country"%>
<%@page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup"%>
<%@page import="java.util.SortedSet"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionDegree"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod"%>
<%@page import="net.sourceforge.fenixedu.util.BundleUtil"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>
<html:xhtml />

<style type="text/css">
	.inputsize500px input { width: 500px; }
	.degreeSelectors {
    	-moz-column-count: 4; -moz-column-gap: 10px;
    	-webkit-column-count: 4; -webkit-column-gap: 10px;
    	column-count: 4; column-gap: 10px;
	}
	.fullSpace { width: 100%; }
	.savedGrade { font-size: large; color: green; font-weight: bold; }
</style>
<script type="text/javascript">
	function EscapeKeyAbort (event, toggle1, toggle2) {
		if (event.keyCode == 27) {
			ToggleVacanciesInput(toggle1, toggle2);
		}
	};

	function ToggleVacanciesInput (toggle1, toggle2) {
		var t1 = '#' + toggle1;
		$(t1).toggle();
		var t2 = '#' + toggle2;
		$(t2).toggle();
	};

	function SaveVacancies (contestOid, inputBox, gradeText, toggle1, toggle2) {
		var gt = '#' + gradeText;
		var ib = '#' + inputBox;
		var vacancies = $(ib).val();
		var checksum = $(("#deleteContestForm > input[name='_request_checksum_']")).attr("value");
		var contextPath = $(("#deleteContestForm > input[name='contentContextPath_PATH']")).attr("value");
		$.post($("#deleteContestForm").attr("action"), {
			method: "editVacancies",
			_request_checksum_: checksum,
			contentContextPath_PATH: contextPath,
			contestOid: contestOid,
			vacancies: vacancies
			}, function(data) {
				$(gt).empty().append( vacancies );
				$(gt).addClass('savedGrade');

				var t1 = '#' + toggle1;
				$(t1).toggle();

				var t2 = '#' + toggle2;
				$(t2).fadeIn(1000, function() {
	                $(gt).removeClass("savedGrade").fadeIn(550);             
	            });
			});
	};
</script>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound"/></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<logic:present name="outboundMobilityContextBean">

	<bean:define id="outboundMobilityContextBean" name="outboundMobilityContextBean" type="net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean"/>

	<fr:form id="prepareForm" action="/outboundMobilityCandidacy.do">
		<html:hidden property="method" value="prepare"/>
				<fr:edit id="outboundMobilityContextBeanExecutionIntervalSelection" name="outboundMobilityContextBean">
					<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean" bundle="MANAGER_RESOURCES">
						<fr:slot name="executionYear" bundle="MANAGER_RESOURCES" key="label.executionYear"
								layout="menu-select-postback">
							<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
							<fr:property name="format" value="${year}" />
						</fr:slot>
			    		<fr:slot name="candidacyPeriodsAsList" layout="option-select" key="label.candidacy.periods" bundle="ACADEMIC_OFFICE_RESOURCES">
    			    		<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.OutboundMobilityCandidacyPeriodProvider" />
        					<fr:property name="eachSchema" value="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod.interval"/>
        					<fr:property name="eachLayout" value="values"/>
        					<fr:property name="classes" value="nobullet noindent"/>
		    			</fr:slot>
			    		<fr:slot name="mobilityProgramsAsList" layout="option-select" key="label.mobility.program" bundle="ACADEMIC_OFFICE_RESOURCES">
    			    		<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.OutboundMobilityProgramProvider" />
        					<fr:property name="eachSchema" value="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityProgram.description"/>
        					<fr:property name="eachLayout" value="values"/>
        					<fr:property name="classes" value="nobullet noindent degreeSelectors"/>
        					<fr:property name="listItemStyle" value=""/>
		    			</fr:slot>
			    		<fr:slot name="mobilityGroupsAsList" layout="option-select" key="label.mobility.group" bundle="ACADEMIC_OFFICE_RESOURCES">
    			    		<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.OutboundMobilityCandidacyGroupProvider" />
        					<fr:property name="eachSchema" value="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup.description"/>
        					<fr:property name="eachLayout" value="values"/>
        					<fr:property name="classes" value="nobullet noindent degreeSelectors"/>
        					<fr:property name="listItemStyle" value=""/>
		    			</fr:slot>
					</fr:schema>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thmiddle thright mtop1 fullSpace"/>
						<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
					</fr:layout>
				</fr:edit>
		<html:submit value="<%= BundleUtil.getStringFromResourceBundle("resources.AcademicAdminOffice", "label.submit") %>"/>
		<academic:allowed operation="MANAGE_MOBILITY_OUTBOUND">
		<% if (outboundMobilityContextBean.getCandidacyPeriods().size() == 0) { %>
			&nbsp;&nbsp;|&nbsp;&nbsp;
			<a href="#" onclick="$('#outboundMobilityContextBeanCreateCandidacyPeriodBlock').toggle()">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.create.new.period"/>
			</a>
		<% } %>
		<% if (outboundMobilityContextBean.getCandidacyPeriods().size() == 1) { %>
			&nbsp;&nbsp;|&nbsp;&nbsp;
			<a href="#" onclick="$('#EditCandidacyPeriodBlock').toggle()">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.edit.period"/>
			</a>
			&nbsp;&nbsp;|&nbsp;&nbsp;
			<bean:define id="confirmDelete" type="java.lang.String"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.studentDataByExecutionYear.delete.confirmation.message"/></bean:define>
			<html:link href="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?method=deletePeriod&candidacyPeriodOid=" + outboundMobilityContextBean.getCandidacyPeriods().first().getExternalId() %>"
					onclick="<%= "return confirm('" + confirmDelete + "')" %>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.delete"/>
			</html:link>
			&nbsp;&nbsp;|&nbsp;&nbsp;
			<a href="#" onclick="$('#EditCandidateOptionsBlock').toggle()">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.edit.candidate.options"/>
			</a>
			&nbsp;&nbsp;|&nbsp;&nbsp;
			<a href="#" onclick="$('#outboundMobilityContextBeanCreateCandidacyContestBlock').toggle()">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.create.new.contest"/>
			</a>
			&nbsp;&nbsp;|&nbsp;&nbsp;
			<html:link href="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?method=downloadSelectedCandidates&candidacyPeriodOid=" + outboundMobilityContextBean.getCandidacyPeriods().first().getExternalId() %>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.period.export.selected.candidates"/>
			</html:link>
			&nbsp;&nbsp;|&nbsp;&nbsp;
			<html:link href="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?method=selectCandidatesForAllGroups&candidacyPeriodOid=" + outboundMobilityContextBean.getCandidacyPeriods().first().getExternalId() %>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.period.select.candidates.for.all.groups"/>
			</html:link>
		<% } %>
		</academic:allowed>
		<% if (outboundMobilityContextBean.getMobilityGroups().size() == 1) { %>
			<academic:allowed operation="MANAGE_MOBILITY_OUTBOUND">
			&nbsp;&nbsp;|&nbsp;&nbsp;
			<a href="#" onclick="$('#outboundMobilityContextBeanAddDegreeToGroupBlock').toggle()">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.add.degree.to.group"/>
			</a>
			&nbsp;&nbsp;|&nbsp;&nbsp;
			<a href="#" onclick="$('#outboundMobilityContextBeanRemoveDegreeFromGroupBlock').toggle()">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.remove.degree.from.group"/>
			</a>
			&nbsp;&nbsp;|&nbsp;&nbsp;
			<a href="#" onclick="$('#outboundMobilityContextBeanAddMobilityCoordinatorBlock').toggle()">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.coordinator.group.add.member"/>
			</a>
			</academic:allowed>
			&nbsp;&nbsp;|&nbsp;&nbsp;
			<a href="#" onclick="<%= "document.getElementById('prepareForm').method.value = 'manageCandidacies' ; document.getElementById('prepareForm').submit()" %>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.manage.candidacies"/>
			</a>
		<% } %>
	</fr:form>


	<logic:present name="error">
		<div class="error0" style="padding-left: 25px;">
			<pre><bean:write name="error" /></pre>
		</div>
	</logic:present>
	<logic:present name="result">
		<div class="error0" style="padding-left: 25px;">
			<pre><logic:empty name="result"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.no.changes"/></logic:empty><logic:notEmpty name="result"><bean:write name="result" /></logic:notEmpty></pre>
		</div>
	</logic:present>


	<academic:allowed operation="MANAGE_MOBILITY_OUTBOUND">
	<div id="outboundMobilityContextBeanAddMobilityCoordinatorBlock" style="display: none;">
		<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.coordinator.group.add.member"/></h3>
		<fr:edit id="outboundMobilityContextBeanAddMobilityCoordinator" name="outboundMobilityContextBean"
				action="/outboundMobilityCandidacy.do?method=addMobilityCoordinator">
			<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean" bundle="ACADEMIC_OFFICE_RESOURCES">
				<fr:slot name="person" layout="autoComplete" key="label.person" bundle="ACADEMIC_OFFICE_RESOURCES" required="true">
  					<fr:property name="provider" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.person.PersonAutoCompleteProvider" />
        			<fr:property name="labelField" value="presentationName"/>
   	    			<fr:property name="format" value="${presentationName}"/>
      				<fr:property name="classes" value="inputsize500px"/>
       				<fr:property name="minChars" value="2"/>
       				<fr:property name="sortBy" value="presentationName"/>
					<fr:property name="saveOptions" value="true"/>
   				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thmiddle thright mtop1"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>
		</fr:edit>
	</div>

	<div id="outboundMobilityContextBeanCreateCandidacyPeriodBlock" style="display: none;">
		<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.create.new.period"/></h3>
		<fr:edit id="outboundMobilityContextBeanCreateCandidacyPeriod" name="outboundMobilityContextBean"
				action="/outboundMobilityCandidacy.do?method=createNewOutboundMobilityCandidacyPeriod">
			<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean" bundle="ACADEMIC_OFFICE_RESOURCES">
				<fr:slot name="startDateTime" bundle="ACADEMIC_OFFICE_RESOURCES" key="label.startDate"
						validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="endDateTime" bundle="ACADEMIC_OFFICE_RESOURCES" key="label.endDate"
						validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thmiddle thright mtop1"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>
		</fr:edit>
	</div>

	<% if (outboundMobilityContextBean.getMobilityGroups().size() == 1) {
	    	final OutboundMobilityCandidacyContestGroup mobilityGroup = outboundMobilityContextBean.getMobilityGroups().iterator().next();
	%>
		<div id="outboundMobilityContextBeanAddDegreeToGroupBlock" style="display: none;">
			<h3>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.add.degree.to.group"/>
				<%= mobilityGroup.getDescription() %>
			</h3>
			<bean:define id="providerArgs2" type="java.lang.String">executionYearOid=<%= outboundMobilityContextBean.getExecutionYear().getExternalId() %></bean:define>
			<fr:edit id="outboundMobilityContextBeanCreateCandidacyPeriod" name="outboundMobilityContextBean"
					action="/outboundMobilityCandidacy.do?method=addDegreeToGroup">
				<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean" bundle="ACADEMIC_OFFICE_RESOURCES">
   					<fr:slot name="executionDegree" layout="autoComplete" key="label.degree" bundle="ACADEMIC_OFFICE_RESOURCES" required="true">
       					<fr:property name="args" value="<%= providerArgs2 %>" />
       					<fr:property name="provider" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.executionDegree.ExecutionDegreeAutoCompleteProvider" />
        				<fr:property name="labelField" value="presentationName"/>
   	    				<fr:property name="format" value="${presentationName}"/>
       					<fr:property name="classes" value="inputsize500px"/>
       					<fr:property name="minChars" value="2"/>
       					<fr:property name="sortBy" value="presentationName"/>
						<fr:property name="saveOptions" value="true"/>
   					</fr:slot>
   				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thmiddle thright mtop1"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
			</fr:edit>
		</div>
		<div id="outboundMobilityContextBeanRemoveDegreeFromGroupBlock" style="display: none;">
			<h3>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.remove.degree.from.group"/>
				<%= mobilityGroup.getDescription() %>
			</h3>
			<ul class="degreeSelectors">
				<%
					for (final ExecutionDegree executionDegree : mobilityGroup.getExecutionDegreeSet()) {
					    final String params = "/outboundMobilityCandidacy.do?method=removeDegreeFromGroup&amp;executionDegreeOid=" + executionDegree.getExternalId();
					    final String formId = "removeDegree" + executionDegree.getExternalId();
				%>
					<li style="margin-bottom: 10px;">
						<fr:form id="<%= formId %>" action="<%= params %>">
							<%--
							<fr:hidden id="<%= "outboundMobilityContextBean" + executionDegree.getExternalId() " name="outboundMobilityContextBean"/>
							--%>
							<fr:edit id="<%= "outboundMobilityContextBean" + executionDegree.getExternalId() %>" name="outboundMobilityContextBean" visible="false"/>
							<a href="#" onclick="<%= "document.getElementById('" + formId + "').submit()" %>">
								<%= executionDegree.getDegree().getSigla() %>
							</a>
						</fr:form>
					</li>
				<%
					}
				%>
			</ul>
		</div>
	<% } %>

	<%
		if (outboundMobilityContextBean.getCandidacyPeriods().size() == 1) {
		    final OutboundMobilityCandidacyPeriod candidacyPeriod = outboundMobilityContextBean.getCandidacyPeriods().iterator().next();
		    request.setAttribute("candidacyPeriod", candidacyPeriod);
	%>
		<div id="EditCandidacyPeriodBlock" style="display: none;">
			<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.edit.period"/></h3>
			<fr:edit id="editCandidacyPeriod" name="candidacyPeriod" action="/outboundMobilityCandidacy.do?method=editCandidacyPeriod">
				<fr:schema type="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod" bundle="ACADEMIC_OFFICE_RESOURCES">
					<fr:slot name="start" bundle="ACADEMIC_OFFICE_RESOURCES" key="label.startDate"
							validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
					<fr:slot name="end" bundle="ACADEMIC_OFFICE_RESOURCES" key="label.endDate"
							validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thmiddle thright mtop1"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
			</fr:edit>
		</div>

		<div id="EditCandidateOptionsBlock" style="display: none;">
			<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.edit.candidate.options"/></h3>

			<div class="section1">
				<h4><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.optionIntroductoryDestription"/></h4>
				<div style="margin-left: 25px;">
					<%= candidacyPeriod.getOptionIntroductoryDestription() %>
				</div>
				<h4><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.option"/></h4>
				<div style="margin-left: 25px;">
					<ul>
						<% for (final OutboundMobilityCandidacyPeriodConfirmationOption option : candidacyPeriod.getSortedOptions()) { %>
							<li>
								<%= option.getOptionValue() %>
								&nbsp;&nbsp;
								<html:link href="<%= request.getContextPath() + "/academicAdministration/outboundMobilityCandidacy.do?method=deleteOption&candidacyPeriodOid=" + candidacyPeriod.getExternalId() + "&optionOid=" + option.getExternalId() %>"
									style="border-bottom: 0px;"><img src="<%= request.getContextPath() + "/images/iconRemoveOff.png" %>" alt="remove"></html:link>
								<% if (option.getAvailableForCandidates() != null && option.getAvailableForCandidates().booleanValue()) { %>
									<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.isavailableForCandidates"/>
								<% } %>
							</li>
						<% } %>
					</ul>
				</div>
			</div>
			<fr:edit id="editCandidateOptions" name="outboundMobilityContextBean" action="/outboundMobilityCandidacy.do?method=addCandidateOption">
				<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean" bundle="ACADEMIC_OFFICE_RESOURCES">
					<fr:slot name="optionIntroductoryDestription" bundle="ACADEMIC_OFFICE_RESOURCES" layout="rich-text" key="label.optionIntroductoryDestription"
							validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
					<fr:slot name="optionValue" bundle="ACADEMIC_OFFICE_RESOURCES" key="label.option"
							validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
					<fr:slot name="availableForCandidates" bundle="ACADEMIC_OFFICE_RESOURCES" key="label.availableForCandidates"/>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thmiddle thright mtop1"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
			</fr:edit>
		</div>

		<div id="outboundMobilityContextBeanCreateCandidacyContestBlock" style="display: none;">
			<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.create.new.contest"/></h3>
			<bean:define id="providerArgs" type="java.lang.String">executionYearOid=<%= outboundMobilityContextBean.getExecutionYear().getExternalId() %></bean:define>
			<fr:edit id="outboundMobilityContextBeanCreateCandidacyContest" name="outboundMobilityContextBean"
					action="/outboundMobilityCandidacy.do?method=createNewOutboundMobilityCandidacyContest">
				<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean" bundle="ACADEMIC_OFFICE_RESOURCES">
	    			<fr:slot name="mobilityProgram" layout="autoComplete" key="label.mobilityProgram" bundle="ACADEMIC_OFFICE_RESOURCES" required="true">
    	    			<fr:property name="provider" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.MobilityProgramProvider" />
        				<fr:property name="labelField" value="registrationAgreement.description"/>
        				<fr:property name="format" value="${registrationAgreement.description}"/>
        				<fr:property name="classes" value="inputsize500px"/>
        				<fr:property name="minChars" value="1"/>
        				<fr:property name="sortBy" value="presentationName"/>
						<fr:property name="saveOptions" value="true"/>
    				</fr:slot>
					<% if (outboundMobilityContextBean.getMobilityGroups().size() == 0) { %>
    					<fr:slot name="executionDegree" layout="autoComplete" key="label.degree" bundle="ACADEMIC_OFFICE_RESOURCES" required="true">
        					<fr:property name="args" value="<%= providerArgs %>" />
        					<fr:property name="provider" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.executionDegree.ExecutionDegreeAutoCompleteProvider" />
	        				<fr:property name="labelField" value="presentationName"/>
    	    				<fr:property name="format" value="${presentationName}"/>
        					<fr:property name="classes" value="inputsize500px"/>
        					<fr:property name="minChars" value="2"/>
        					<fr:property name="sortBy" value="presentationName"/>
							<fr:property name="saveOptions" value="true"/>
    					</fr:slot>
					<% } %>
    				<fr:slot name="unit" layout="autoComplete" key="label.university" bundle="ACADEMIC_OFFICE_RESOURCES" required="true">
        				<fr:property name="provider" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExternalUniversityUnitAutoCompleteProvider" />
	        			<fr:property name="labelField" value="presentationName"/>
    	    			<fr:property name="format" value="${presentationName}"/>
        				<fr:property name="classes" value="inputsize500px"/>
        				<fr:property name="minChars" value="2"/>
        				<fr:property name="sortBy" value="presentationName"/>
						<fr:property name="saveOptions" value="true"/>
	    			</fr:slot>
					<fr:slot name="vacancies" bundle="ACADEMIC_OFFICE_RESOURCES" key="label.vacancies"
							validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thmiddle thright mtop1"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
			</fr:edit>
		</div>
	<% } %>
	</academic:allowed>

	<% if (outboundMobilityContextBean.getMobilityGroups().size() == 1) {
	    	final OutboundMobilityCandidacyContestGroup mobilityGroup = outboundMobilityContextBean.getMobilityGroups().iterator().next();
	%>
		<br/>
		<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.coordinator.group"/></h3>

		<% if (mobilityGroup.getMobilityCoordinatorCount() == 0) { %>
				<span><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.coordinator.group.empty"/></span>
		<% } else { %>
				<fr:form id="removeMobilityCoordinatorForm" action="/outboundMobilityCandidacy.do">
					<html:hidden property="method" value="removeMobilityCoordinator"/>
					<html:hidden property="mobilityGroupOid" value="<%= mobilityGroup.getExternalId() %>"/>
					<html:hidden property="personOid" value=""/>
					<fr:edit id="removeMobilityCoordinatorFormBean" name="outboundMobilityContextBean" visible="false"/>
					<table class="tstyle1 mtop05">
						<tr>
							<th></th>
							<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.username"/></th>
							<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.name"/></th>
							<academic:allowed operation="MANAGE_MOBILITY_OUTBOUND">
								<th></th>
							</academic:allowed>
						</tr>
						<% for (final Person person : mobilityGroup.getMobilityCoordinatorSet()) { %>
								<tr>
									<td>
										<div><img src="<%= request.getContextPath() +"/publico/retrievePersonalPhoto.do?method=retrievePhotographOnPublicSpace&amp;personId=" + person.getExternalId() %>"  style="padding: 1em 0;" /></div>
									</td>
									<td><%= person.getUsername() %></td>
									<td><%= person.getName() %></td>
									<academic:allowed operation="MANAGE_MOBILITY_OUTBOUND">
										<td>
											<a href="#" onclick="<%= "document.getElementById('removeMobilityCoordinatorForm').personOid.value = " + person.getExternalId() + " ; document.getElementById('removeMobilityCoordinatorForm').submit()" %>">
												<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.delete"/>
											</a>
										</td>
									</academic:allowed>
								</tr>
						<% } %>
					</table>
				</fr:form>
		<% } %>
	<% } %>

	<br/>
	<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.contests"/></h3>
	<%
		final SortedSet<OutboundMobilityCandidacyContest> contests = outboundMobilityContextBean.getOutboundMobilityCandidacyContest();
		if (contests.isEmpty()) {
	%>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.contests.none"/>
	<%  } else { %>
			<fr:form id="deleteContestForm" action="/outboundMobilityCandidacy.do">
				<html:hidden property="method" value=""/>
				<html:hidden property="contestOid" value=""/>
				<fr:edit id="deleteContestFormBean" name="outboundMobilityContextBean" visible="false"/>
	
		<table class="tstyle1 mtop05">
			<tr>
				<% if (outboundMobilityContextBean.getCandidacyPeriods().size() > 1) { %>
					<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.candidacy.period"/></th>
				<% } %>
				<% if (outboundMobilityContextBean.getMobilityGroups().size() > 1) { %>
					<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.degrees"/></th>
				<% } %>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.country"/></th>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.university"/></th>
				<% if (outboundMobilityContextBean.getMobilityPrograms().size() > 1) { %>
					<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.program"/></th>
				<% } %>
				<th>&nbsp;&nbsp;<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.vacancies"/>&nbsp;&nbsp;</th>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.candidacy.count"/></th>
				<academic:allowed operation="MANAGE_MOBILITY_OUTBOUND">
					<th></th>
				</academic:allowed>
		</tr>
		<% for (final OutboundMobilityCandidacyContest contest : contests) {
		    final Unit unit = contest.getMobilityAgreement().getUniversityUnit();
		    final Country country = unit.getCountry();
			final String hideVacanciesID = "hideVacancies" + contest.getExternalId();
			final String showVacanciesID = "showVacancies" + contest.getExternalId();
			final String inputVacanciesID = "inputVacancies" + contest.getExternalId();
			final String vacanciesText = contest.getExternalId();
		%>
			<tr>
				<% if (outboundMobilityContextBean.getCandidacyPeriods().size() > 1) { %>
					<td><%= contest.getOutboundMobilityCandidacyPeriod().getIntervalAsString() %></td>
				<% } %>
				<% if (outboundMobilityContextBean.getMobilityGroups().size() > 1) { %>
					<td>
						<% for (final ExecutionDegree executionDegree : contest.getOutboundMobilityCandidacyContestGroup().getSortedExecutionDegrees()) { %>
							<%= executionDegree.getDegree().getSigla() %>
						<% } %>
					</td>
				<% } %>
				<td><%= country == null ? "" : country.getLocalizedName().toString() %></td>
				<td><%= contest.getMobilityAgreement().getUniversityUnit().getPresentationName() %></td>
				<% if (outboundMobilityContextBean.getMobilityPrograms().size() > 1) { %>
					<td><%= contest.getMobilityAgreement().getMobilityProgram().getRegistrationAgreement().getDescription() %></td>
				<% } %>
				<td>
						<span id="<%= showVacanciesID %>">
							<em id="<%= vacanciesText %>"><%= contest.getVacancies() == null ? "" : contest.getVacancies().toString() %></em>
							&nbsp;
							<a href="#" onclick="<%= "ToggleVacanciesInput('" + showVacanciesID + "', '" + hideVacanciesID + "'); $('#" + inputVacanciesID + "').focus()" %>"
									style="border-bottom: 0px; float: right; vertical-align: middle;">
								<img src="<%= request.getContextPath() +"/images/iconEditOn.png" %>" />
								&nbsp;&nbsp;
							</a>
						</span>
						<span id="<%= hideVacanciesID %>" style="display: none;">
							<input id="<%= inputVacanciesID %>" name="grade" value="<%= contest.getVacancies() == null ? "" : contest.getVacancies().toString() %>"
								onchange="<%= "document.getElementById('deleteContestForm').method.value = 'editVacancies' ; SaveVacancies('" + contest.getExternalId() + "', '" + inputVacanciesID + "', '" + vacanciesText + "', '" + hideVacanciesID + "', '" + showVacanciesID + "');" %>"
								onkeydown="<%= "EscapeKeyAbort(event, '" + hideVacanciesID + "', '" + showVacanciesID + "');" %>"
								size="5"/>
						</span>
				</td>
				<td>
					<% if (contest.getOutboundMobilityCandidacyCount() == 0) { %>
							0
					<% } else { %>
							<a href="#" onclick="<%= "document.getElementById('deleteContestForm').method.value = 'viewContestForm' ; document.getElementById('deleteContestForm').contestOid.value = " + contest.getExternalId() + " ; document.getElementById('deleteContestForm').submit()" %>">
								<%= contest.getOutboundMobilityCandidacyCount() %>
							</a>
					<% } %>
				</td>
				<academic:allowed operation="MANAGE_MOBILITY_OUTBOUND">
					<td>
						<a href="#" onclick="<%= "document.getElementById('deleteContestForm').method.value = 'deleteContest' ; document.getElementById('deleteContestForm').contestOid.value = " + contest.getExternalId() + " ; document.getElementById('deleteContestForm').submit()" %>">
							<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.delete"/>
						</a>
					</td>
				</academic:allowed>
			</tr>
		<% } %>
	</table>
		</fr:form>
	<% } %>
</logic:present>