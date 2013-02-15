<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest"%>
<%@page import="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod"%>
<%@page import="net.sourceforge.fenixedu.util.BundleUtil"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

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
	    		<fr:slot name="candidacyPeriodsAsList" layout="option-select" key="label.candidacy.periods" bundle="ACADEMIC_OFFICE_RESOURCES">
    	    		<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.OutboundMobilityCandidacyPeriodProvider" />
        			<fr:property name="eachSchema" value="net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod.interval"/>
        			<fr:property name="eachLayout" value="values"/>
        			<fr:property name="classes" value="nobullet noindent"/>
    			</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thmiddle thright mtop1"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>
		</fr:edit>
		<html:submit value="<%= BundleUtil.getStringFromResourceBundle("resources.AcademicAdminOffice", "label.submit") %>"/>
	</fr:form>

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

	<% if (outboundMobilityContextBean.getCandidacyPeriods().size() == 1) { %>
		<style type="text/css">
			.inputsize500px input { width: 500px; }
    	</style>
		<bean:define id="providerArgs" type="java.lang.String">provider=net.sourceforge.fenixedu.presentationTier.renderers.providers.executionDegree.ExecutionDegreeAutoCompleteProvider,executionYearOid=<%= outboundMobilityContextBean.getExecutionYear().getExternalId() %></bean:define>
		<fr:edit id="outboundMobilityContextBeanCreateCandidacyPeriod" name="outboundMobilityContextBean"
				action="/outboundMobilityCandidacy.do?method=createNewOutboundMobilityCandidacyContest">
			<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound.OutboundMobilityContextBean" bundle="ACADEMIC_OFFICE_RESOURCES">
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
        			<fr:property name="args" value="provider=net.sourceforge.fenixedu.presentationTier.renderers.providers.ExternalUnitAutoCompleteProvider" />
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
	<% } %>

	<table class="tstyle1 mtop05">
		<tr>
			<th>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.candidacy.period"/>
			</th>
			<th>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.degree"/>
			</th>
			<th>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.country"/>
			</th>
			<th>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.university"/>
			</th>
			<th>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.code"/>
			</th>
			<th>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.vacancies"/>
			</th>
			<th>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.candidacy.count"/>
			</th>
		</tr>
		<% for (final OutboundMobilityCandidacyContest contest : outboundMobilityContextBean.getOutboundMobilityCandidacyContest()) {%>
			<tr>
				<td>
					TODO
				</td>
				<td>
					<%= contest.getExecutionDegree().getDegree().getSigla() %>
				</td>
				<td>
					TODO
				</td>
				<td>
					<%= contest.getUnit().getPresentationName() %>
				</td>
				<td>
					<%= contest.getCode() %>
				</td>
				<td>
					<%= contest.getVacancies() %>
				</td>
				<td>
					<%= contest.getOutboundMobilityCandidacyCount() %>
				</td>
			</tr>
		<% } %>
	</table>
	
	<%--
			Listagem
			
			(periodo de candidatura) - Degree - University - Code - Vacancies - Candidacies
			(periodo de candidatura) - Degree - University - Code - Vacancies - Candidacies
			(periodo de candidatura) - Degree - University - Code - Vacancies - Candidacies
			(periodo de candidatura) - Degree - University - Code - Vacancies - Candidacies
			(periodo de candidatura) - Degree - University - Code - Vacancies - Candidacies
	--%>

	<%--
			(periodo de candidatura) - Degree - Student Nº - Student Name - Candidacy Status
			(periodo de candidatura) - Degree - Student Nº - Student Name - Candidacy Status
			(periodo de candidatura) - Degree - Student Nº - Student Name - Candidacy Status
			(periodo de candidatura) - Degree - Student Nº - Student Name - Candidacy Status
			(periodo de candidatura) - Degree - Student Nº - Student Name - Candidacy Status
	--%>	

</logic:present>