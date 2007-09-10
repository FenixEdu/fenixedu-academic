<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="electionOID" name="electionPeriodBean" property="election.idInternal" />

<h2><bean:message key="label.showElectionResults" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<ul>
	<li>
		<p class="mtop2 mbottom2"><html:link page="<%= "/delegatesManagement.do?method=goBackToViewDelegates&selectedElection=" + electionOID %>">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.back" /></html:link></p>
	</li>
</ul>

<logic:present name="electionPeriodBean" >
	<p class="mtop2 mbottom05">
		<b><bean:message key="label.delegates.election.resume" bundle="PEDAGOGICAL_COUNCIL"/></b></p>
		
	<fr:view name="electionPeriodBean" layout="tabular-nonNullValues" schema="delegates.electionPeriod.resume" >
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thright mtop05"/>
			<fr:property name="columnClasses" value="nowrap, nowrap"/>
			<fr:property name="rowClasses" value="bold,bold,bold,,,bold"/>
		</fr:layout>
	</fr:view>
	
</logic:present>

<logic:present name="votingResultsByStudent" >
	<logic:notEmpty name="votingResultsByStudent" >
		<p class="mtop1 mbottom05">
			<b><bean:message key="label.delegates.election.results" bundle="PEDAGOGICAL_COUNCIL"/></b></p>
		
		<logic:equal name="electionPeriodBean" property="election.canYearDelegateBeElected" value="true">
			<p class="color888 mvert05">
				<bean:message key="label.delegates.election.results.help" bundle="PEDAGOGICAL_COUNCIL" /></p>
		</logic:equal>
		
		<logic:equal name="electionPeriodBean" property="election.canYearDelegateBeElected" value="false">
			<logic:present name="electionPeriodBean" property="election.electedStudent">
				<p class="color888 mvert05">
					<bean:message key="label.delegates.election.results.alreadyElectedDelegate.help" bundle="PEDAGOGICAL_COUNCIL" /></p>
			</logic:present>
			
			<logic:notPresent name="electionPeriodBean" property="election.electedStudent">
				<p class="color888 mvert05">
					<bean:message key="label.delegates.election.results.currentPeriod.help" bundle="PEDAGOGICAL_COUNCIL" /></p>
			</logic:notPresent>
		</logic:equal>
		
		
		<fr:view name="votingResultsByStudent" layout="tabular-sortable" schema="yearDelegateElection.showResults.completeInfo" >
			<fr:layout>
				<fr:property name="classes" value="tstyle1 thlight tdcenter mtop0"/>
				<fr:property name="columnClasses" value=",width200px aleft,,,,,"/>
				<fr:property name="link(add)" value="/delegatesManagement.do?method=addYearDelegate" />
				<fr:property name="param(add)" value="student.idInternal/selectedStudent,election.idInternal/selectedElection"/>
				<fr:property name="key(add)" value="link.delegates.addRole"/>
				<fr:property name="bundle(add)" value="PEDAGOGICAL_COUNCIL"/>
				<fr:property name="visibleIf(add)" value="election.canYearDelegateBeElected"/>
				<fr:property name="link(remove)" value="/delegatesManagement.do?method=removeDelegate" />
				<fr:property name="param(remove)" value="election.electedStudent.idInternal/selectedDelegate"/>
				<fr:property name="key(remove)" value="link.delegates.removeRole"/>
				<fr:property name="bundle(remove)" value="PEDAGOGICAL_COUNCIL"/>
				<fr:property name="visibleIf(remove)" value="isElectedStudent"/>
				<fr:property name="sortParameter" value="sortBy"/>
				<fr:property name="sortableSlots" value="student.number,student.person.name,votesNumber,votesRelativePercentage"/>
            	<fr:property name="sortUrl" value="<%= String.format("/electionsPeriodsManagement.do?method=showVotingResults&amp;selectedCandidacyPeriod=" + electionOID ) %>"/>
            	<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "votesNumber=descending" : request.getParameter("sortBy")  %>"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
</logic:present>