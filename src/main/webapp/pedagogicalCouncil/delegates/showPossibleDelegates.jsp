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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="electionOID" name="electionPeriodBean" property="election.externalId" />

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
				<fr:property name="columnClasses" value=",aleft,,,,,"/>
				<fr:property name="link(add)" value="/delegatesManagement.do?method=addYearDelegate" />
				<fr:property name="param(add)" value="student.externalId/selectedStudent,election.externalId/selectedElection"/>
				<fr:property name="key(add)" value="link.delegates.addRole"/>
				<fr:property name="bundle(add)" value="PEDAGOGICAL_COUNCIL"/>
				<fr:property name="visibleIfNot(add)" value="isElectedStudent"/>
				<%--
				<fr:property name="link(remove)" value="/delegatesManagement.do?method=removeDelegate" />
				<fr:property name="param(remove)" value="election.electedStudent.externalId/selectedDelegate"/>
				<fr:property name="key(remove)" value="link.delegates.removeRole"/>
				<fr:property name="bundle(remove)" value="PEDAGOGICAL_COUNCIL"/>
				<fr:property name="visibleIf(remove)" value="isElectedStudent"/>
				 --%>
				<fr:property name="sortParameter" value="sortBy"/>
				<fr:property name="sortableSlots" value="student.number,student.person.name,votesNumber,votesRelativePercentage"/>
            	<fr:property name="sortUrl" value="<%= String.format("/electionsPeriodsManagement.do?method=showVotingResults&amp;selectedCandidacyPeriod=" + electionOID ) %>"/>
            	<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "votesNumber=descending" : request.getParameter("sortBy")  %>"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
</logic:present>