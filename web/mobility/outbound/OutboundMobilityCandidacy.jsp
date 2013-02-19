<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup"%>
<%@page import="java.util.SortedSet"%>
<%@ page language="java"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionDegree"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod"%>
<%@page import="net.sourceforge.fenixedu.util.BundleUtil"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<style type="text/css">
	.inputsize500px input { width: 500px; }
	.degreeSelectors {
    	-moz-column-count: 4; -moz-column-gap: 10px;
    	-webkit-column-count: 4; -webkit-column-gap: 10px;
    	column-count: 4; column-gap: 10px;
    	width: 100%;
	}
	.fullSpace { width: 100%; }
	.fullSpace th { width: 15%; }
</style>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound"/></h2>

<logic:present name="outboundMobilityContextBean">

	<bean:define id="outboundMobilityContextBean" name="outboundMobilityContextBean" type="net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean"/>

	<fr:form action="/outboundMobilityCandidacy.do">
		<html:hidden property="method" value="prepare"/>
				<fr:edit id="outboundMobilityContextBeanExecutionIntervalSelection" name="outboundMobilityContextBean">
					<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean" bundle="MANAGER_RESOURCES">
						<fr:slot name="executionYear" bundle="CARD_GENERATION_RESOURCES" key="label.execution.year"
								layout="menu-select-postback">
							<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
							<fr:property name="format" value="${year}" />
						</fr:slot>
			    		<fr:slot name="mobilityProgramsAsList" layout="option-select" key="label.mobility.program" bundle="ACADEMIC_OFFICE_RESOURCES">
    			    		<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.OutboundMobilityProgramProvider" />
        					<fr:property name="eachSchema" value="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityProgram.description"/>
        					<fr:property name="eachLayout" value="values"/>
        					<fr:property name="classes" value="nobullet noindent degreeSelectors"/>
        					<fr:property name="listItemStyle" value=""/>
		    			</fr:slot>
			    		<fr:slot name="candidacyPeriodsAsList" layout="option-select" key="label.candidacy.periods" bundle="ACADEMIC_OFFICE_RESOURCES">
    			    		<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.OutboundMobilityCandidacyPeriodProvider" />
        					<fr:property name="eachSchema" value="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod.interval"/>
        					<fr:property name="eachLayout" value="values"/>
        					<fr:property name="classes" value="nobullet noindent"/>
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
			<a href="#" onclick="$('#outboundMobilityContextBeanCreateCandidacyContestBlock').toggle()">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.create.new.contest"/>
			</a>
		<% } %>
	</fr:form>

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

		<div id="outboundMobilityContextBeanCreateCandidacyContestBlock" style="display: none;">
			<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.create.new.contest"/></h3>
			<bean:define id="providerArgs" type="java.lang.String">provider=net.sourceforge.fenixedu.presentationTier.renderers.providers.executionDegree.ExecutionDegreeAutoCompleteProvider,executionYearOid=<%= outboundMobilityContextBean.getExecutionYear().getExternalId() %></bean:define>
			<fr:edit id="outboundMobilityContextBeanCreateCandidacyContest" name="outboundMobilityContextBean"
					action="/outboundMobilityCandidacy.do?method=createNewOutboundMobilityCandidacyContest">
				<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean" bundle="ACADEMIC_OFFICE_RESOURCES">
	    			<fr:slot name="mobilityProgram" layout="simpleAutoComplete" key="label.mobilityProgram" bundle="ACADEMIC_OFFICE_RESOURCES" required="true">
    	    			<fr:property name="args" value="provider=net.sourceforge.fenixedu.presentationTier.renderers.providers.MobilityProgramProvider" />
        				<fr:property name="labelField" value="registrationAgreement.description"/>
        				<fr:property name="format" value="${registrationAgreement.description}"/>
        				<fr:property name="classes" value="inputsize500px"/>
        				<fr:property name="minChars" value="1"/>
        				<fr:property name="sortBy" value="presentationName"/>
						<fr:property name="saveOptions" value="true"/>
    				</fr:slot>
    				<fr:slot name="executionDegree" layout="simpleAutoComplete" key="label.degree" bundle="ACADEMIC_OFFICE_RESOURCES" required="true">
        				<fr:property name="args" value="<%= providerArgs %>" />
	        			<fr:property name="labelField" value="presentationName"/>
    	    			<fr:property name="format" value="${presentationName}"/>
        				<fr:property name="classes" value="inputsize500px"/>
        				<fr:property name="minChars" value="2"/>
        				<fr:property name="sortBy" value="presentationName"/>
						<fr:property name="saveOptions" value="true"/>
    				</fr:slot>
    				<fr:slot name="unit" layout="simpleAutoComplete" key="label.university" bundle="ACADEMIC_OFFICE_RESOURCES" required="true">
        				<fr:property name="args" value="provider=net.sourceforge.fenixedu.presentationTier.renderers.providers.ExternalUniversityUnitAutoCompleteProvider" />
	        			<fr:property name="labelField" value="presentationName"/>
    	    			<fr:property name="format" value="${presentationName}"/>
        				<fr:property name="classes" value="inputsize500px"/>
        				<fr:property name="minChars" value="2"/>
        				<fr:property name="sortBy" value="presentationName"/>
						<fr:property name="saveOptions" value="true"/>
	    			</fr:slot>
					<fr:slot name="code" bundle="ACADEMIC_OFFICE_RESOURCES" key="label.code"
							validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
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

	<br/>
	<h3><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.contests"/></h3>
	<%
		final SortedSet<OutboundMobilityCandidacyContest> contests = outboundMobilityContextBean.getOutboundMobilityCandidacyContest();
		if (contests.isEmpty()) {
	%>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mobility.outbound.contests.none"/>
	<%  } else { %>
		<table class="tstyle1 mtop05">
			<tr>
				<% if (outboundMobilityContextBean.getCandidacyPeriods().size() > 1) { %>
					<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.candidacy.period"/></th>
				<% } %>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.degrees"/></th>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.country"/></th>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.university"/></th>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.vacancies"/></th>
				<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.candidacy.count"/></th>
		</tr>
		<% for (final OutboundMobilityCandidacyContest contest : contests) {%>
			<tr>
				<% if (outboundMobilityContextBean.getCandidacyPeriods().size() > 1) { %>
					<td><%= contest.getOutboundMobilityCandidacyPeriod().getIntervalAsString() %></td>
				<% } %>
				<td>
					<% for (final ExecutionDegree executionDegree : contest.getOutboundMobilityCandidacyContestGroup().getSortedExecutionDegrees()) { %>
						<%= executionDegree.getDegree().getSigla() %>
					<% } %>
				</td>
				<td><%= contest.getMobilityAgreement().getUniversityUnit().getCountry().getLocalizedName().toString() %></td>
				<td><%= contest.getMobilityAgreement().getUniversityUnit().getPresentationName() %></td>
				<td><%= contest.getVacancies() == null ? "" : contest.getVacancies() %></td>
				<td><%= contest.getOutboundMobilityCandidacyCount() %></td>
			</tr>
		<% } %>
	</table>
	<% } %>
</logic:present>