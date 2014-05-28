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

<bean:define id="forwardTo" value="createEditVotingPeriods" />


<h2><bean:message key="label.elections.secondRoundElections" bundle="PEDAGOGICAL_COUNCIL" /></h2>

<div class="infoop2">
	<bean:message key="label.elections.secondRoundElections.description" bundle="PEDAGOGICAL_COUNCIL"/>
</div> 

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
		<p><span class="error0"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<bean:size id="nrCandidates" name="secondRoundElectionsCandidatesBean" property="candidates"/>
<bean:size id="nrNotCandidates" name="secondRoundElectionsNotCandidatesBean" property="candidates"/> 
<logic:equal name="nrCandidates" value="0">
	<logic:equal name="nrNotCandidates" value="0">
		<bean:message key="label.elections.secondRoundElections.emptyCandidates" bundle="PEDAGOGICAL_COUNCIL" />
	</logic:equal>
</logic:equal>



 
<fr:form  action="<%="/electionsPeriodsManagement.do?method=addCandidatesToSecondRoundElections&forwardTo="+ forwardTo%>">	
	<b><bean:message key="label.elections.nrBlankVotes" bundle="PEDAGOGICAL_COUNCIL"/></b>
					<bean:write name="newElectionPeriodBean"  property="election.lastVotingPeriod.blankVotesElection"/> 
	<logic:notEqual name="nrCandidates" value="0">
		<h3><bean:message key="label.elections.secondRoundElections.votedCandidates" bundle="PEDAGOGICAL_COUNCIL" /></h3>
	</logic:notEqual>
 	
 	<fr:edit id="secondRoundElectionsCandidatesBean" name="secondRoundElectionsCandidatesBean" schema="election.secondRoundElections.candidates.show" > 
	 	<fr:layout>
				<fr:property name="displayLabel" value="false"/>
			</fr:layout>
	</fr:edit>
	
	<logic:notEqual name="nrNotCandidates" value="0">
		<h3><bean:message key="label.elections.secondRoundElections.otherVotedCandidates" bundle="PEDAGOGICAL_COUNCIL" /></h3>
  	</logic:notEqual>
  	
  	<fr:edit id="secondRoundElectionsNotCandidatesBean" name="secondRoundElectionsNotCandidatesBean" schema="election.secondRoundElections.candidates.show" > 
	 	<fr:layout>
				<fr:property name="displayLabel" value="false"/>
			</fr:layout>
	</fr:edit> 
	
	
   	<h3><bean:message key="label.elections.secondRoundElections.votingPeriod" bundle="PEDAGOGICAL_COUNCIL" /></h3>
	
	<fr:edit id="electionPeriodBean" name="electionPeriodBean" visible="false" />
	

	<fr:edit id="newElectionPeriodBean" name="newElectionPeriodBean" layout="tabular-editable" schema="elections.createSecondRoundElectionVotingPeriod">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright thleft thmiddle mtop0 mbottom0"/>
			<fr:property name="columnClasses" value="width150px,aleft width250px,tdclear tderror1"/> 
		</fr:layout>
		
	</fr:edit>
	
	 

	
	
	<html:submit>
		<bean:message key="button.elections.createPeriod" bundle="PEDAGOGICAL_COUNCIL"/>
	</html:submit>
	
</fr:form>




