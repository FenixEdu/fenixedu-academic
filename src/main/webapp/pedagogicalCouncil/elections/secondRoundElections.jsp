<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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




