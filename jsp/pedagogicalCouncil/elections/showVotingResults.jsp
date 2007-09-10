<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="forwardTo" value="showVotingResults" />
<bean:define id="degreeOID" name="electionPeriodBean" property="degree.idInternal" />
<bean:define id="electionOID" name="electionPeriodBean" property="election.idInternal" />

<h2><bean:message key="label.showVotingResults" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<p class="mtop1 mbottom1"><b><bean:message key="label.currentExecutionYear" bundle="PEDAGOGICAL_COUNCIL" />:</b>
	<bean:write name="currentExecutionYear" property="year" /></p>
<%--
<p class="mtop1 mbottom1"><b><bean:message key="label.degree" bundle="PEDAGOGICAL_COUNCIL" />:</b>
	<bean:write name="electionPeriodBean" property="degree.name" /></p>		
--%>

<ul>
	<li>
		<p class="mtop2 mbottom2"><html:link page="<%= "/electionsPeriodsManagement.do?method=selectDegreeType&degreeOID=" + degreeOID + "&forwardTo=showVotingPeriods" %>">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.back" /></html:link></p>
	</li>
</ul>


<logic:present name="electionPeriodBean" >
	<p class="mtop1 mbottom05">
		<b><bean:message key="label.elections.votingPeriod.resume" bundle="PEDAGOGICAL_COUNCIL"/></b></p>
		
	<fr:view name="electionPeriodBean" layout="tabular-nonNullValues" schema="electionPeriod.showResults.resume" >
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thright mtop05"/>
			<fr:property name="columnClasses" value="nowrap, nowrap"/>
			<fr:property name="rowClasses" value="bold,,,,,bold"/>
		</fr:layout>
	</fr:view>
	
</logic:present>

<logic:present name="votingResultsByStudent" >

	<logic:empty name="votingResultsByStudent" >
		<p class="mtop2 mbottom2">
		<span class="error0"><b><bean:message key="elections.showVotingResults.noVotes" bundle="PEDAGOGICAL_COUNCIL"/></b></span></p>
	</logic:empty>
	
	<logic:notEmpty name="votingResultsByStudent" >
		<p class="mtop1 mbottom05">
			<b><bean:message key="label.elections.votingPeriod.results" bundle="PEDAGOGICAL_COUNCIL"/></b></p>
		<fr:view name="votingResultsByStudent" layout="tabular-sortable" schema="yearDelegateElection.showResults" >
			<fr:layout>
				<fr:property name="classes" value="tstyle2 thlight tdcenter mtop0"/>
				<fr:property name="columnClasses" value="width80px,width200px aleft,,"/>
				<fr:property name="sortParameter" value="sortBy"/>
				<fr:property name="sortableSlots" value="student.number,student.person.name,votesNumber,votesRelativePercentage"/>
            	<fr:property name="sortUrl" value="<%= String.format("/electionsPeriodsManagement.do?method=showVotingResults&amp;selectedVotingPeriod=" + electionOID) %>"/>
            	<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "votesNumber=descending" : request.getParameter("sortBy")  %>"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
</logic:present>