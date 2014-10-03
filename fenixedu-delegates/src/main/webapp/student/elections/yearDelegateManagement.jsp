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
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="label.student.elections.electionsInfoTitle" bundle="APPLICATION_RESOURCES"/></h2>

<!-- AVISOS E ERROS -->
<span class="error0"><html:errors /></span>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
		<p><span class="error0"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:present name="votedYearDelegate"> 
	<logic:equal value="true" name="votedYearDelegate" property="lastVotingPeriod.secondRoundElections">
		<h3><bean:message key="label.elections.secondRoundElections" bundle="PEDAGOGICAL_COUNCIL" /></h3>
	</logic:equal>  
</logic:present>

<logic:present name="notVotedYearDelegate"> 
	<logic:equal value="true" name="notVotedYearDelegate" property="lastVotingPeriod.secondRoundElections">
		<h3><bean:message key="label.elections.secondRoundElections" bundle="PEDAGOGICAL_COUNCIL" /></h3>
	</logic:equal>  
</logic:present>

<logic:notPresent name="currentYearDelegateElection" >
	<logic:notPresent name="votedYearDelegate" >
		<logic:notPresent name="notVotedYearDelegate" >
			<p class="mvert15">
				<em><bean:message key="label.student.elections.noCandidacyOrVotePeriods" bundle="APPLICATION_RESOURCES"/>.</em>
			</p>
				<logic:present name="yearDelegateResultsElection" >
					<bean:define id="electionOID" name="yearDelegateElection" property="externalId" />
					<p class="mtop1 mbottom05">
						<b><bean:message key="title.student.elections.results.elections" bundle="APPLICATION_RESOURCES"/></b>
					</p>
				
					<fr:view name="yearDelegateElection" layout="tabular-nonNullValues" schema="student.electionPeriod.showResults.resume" >
						<fr:layout>
							<fr:property name="classes" value="tstyle2 thlight thright mtop05"/>
							<fr:property name="columnClasses" value="nowrap, nowrap"/>
							<fr:property name="rowClasses" value="bold,,,,,bold"/>
						</fr:layout>
					</fr:view>
				
					<fr:view name="yearDelegateResultsElection" layout="tabular-sortable" schema="student.yearDelegateElection.showResults" >
						<fr:layout>
							<fr:property name="classes" value="tstyle2 thlight tdcenter"/>
							<fr:property name="columnClasses" value="width80px,aleft,,"/>
							<fr:property name="sortParameter" value="sortBy"/>
							<fr:property name="sortableSlots" value="student.number,student.person.name,votesNumber,votesRelativePercentage"/>
			            	<fr:property name="sortUrl" value="/yearDelegateManagement.do?method=prepare"/>
			            	<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "votesNumber=descending" : request.getParameter("sortBy")  %>"/>
						</fr:layout>
					</fr:view>
					<b><bean:message key="label.elections.nrBlankVotes" bundle="PEDAGOGICAL_COUNCIL"/></b>
					<bean:write name="yearDelegateElection"  property="lastVotingPeriod.blankVotesElection"/>
				</logic:present>
		</logic:notPresent>
	</logic:notPresent>
</logic:notPresent>


<logic:present name="currentYearDelegateElection" >	
	<p class="mtop1 mbottom05">
		<b><bean:message key="label.elections.candidacyPeriod" bundle="APPLICATION_RESOURCES"/></b></p>
	<p class="color888 mvert05">
		<bean:message key="label.elections.candidacyPeriod.candidate.help" bundle="APPLICATION_RESOURCES" /></p>
	
	<fr:view name="currentYearDelegateElection" layout="tabular-nonNullValues" schema="student.elections.electionPeriod" >
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thright mtop05"/>
			<fr:property name="columnClasses" value="nowrap, nowrap"/>
			<fr:property name="rowClasses" value="bold,,,"/>
		</fr:layout>
	</fr:view>

	<logic:present name="candidatedYearDelegate">
		<p><strong><bean:message key="label.elections.candidated" bundle="APPLICATION_RESOURCES" /></strong></p>
		<fr:view name="candidatedYearDelegate" layout="tabular" schema="student.candidated.year.delegate">
			<fr:layout>
				<fr:property name="classes" value="tstyle2 thlight thleft tdleft mtop0"/>
				<fr:property name="linkFormat(remove)" value="/yearDelegateManagement.do?method=removeCandidateStudent" />
				<fr:property name="key(remove)" value="link.remove.candidate"/>
				<fr:property name="bundle(remove)" value="APPLICATION_RESOURCES"/>
				<fr:property name="contextRelative(remove)" value="true"/>      
				<fr:property name="order(remove)" value="1"/>
			</fr:layout>
		</fr:view>	
	</logic:present>
	<logic:notPresent name="candidatedYearDelegate">
		<ul>
			<li>
				<p class="mtop0 mbottom1">
					<html:link page="/yearDelegateManagement.do?method=addCandidateStudent" >
						<bean:message key="link.student.elections.addCandidateStudent" bundle="APPLICATION_RESOURCES" /></html:link></p>
			</li>
		</ul>
	</logic:notPresent>

	
</logic:present>


<!-- CANDIDATES LIST -->
<logic:notPresent name="votedYearDelegate" >
	<logic:notPresent name="notVotedYearDelegate" >
		<logic:present name="candidates" >
			<logic:notEmpty name="candidates">
				<p><strong><bean:message key="label.elections.candidatesList" bundle="APPLICATION_RESOURCES" /></strong></p>
				<fr:view name="candidates" layout="tabular-sortable" schema="student.yearDelegateElection.candidates" >
					<fr:layout>
						<fr:property name="classes" value="tstyle2 thlight thleft tdleft mtop0"/>
						<fr:property name="columnClasses" value="width80px,"/>
						<fr:property name="sortParameter" value="sortBy"/>
						<fr:property name="sortableSlots" value="number,person.name"/>
		            	<fr:property name="sortUrl" value="<%= String.format("/yearDelegateManagement.do?method=prepare") %>"/>
		            	<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "number" : request.getParameter("sortBy") %>"/>
					</fr:layout>
				</fr:view>
			</logic:notEmpty>
		</logic:present>
	</logic:notPresent>
</logic:notPresent>

<!-- STUDENT THAT VOTED -->
<logic:present name="votedYearDelegate" >
	<p class="mtop1 mbottom05">
		<b><bean:message key="label.elections.votingPeriod" bundle="APPLICATION_RESOURCES"/></b></p>
	
	
	<fr:view name="votedYearDelegate" layout="tabular-nonNullValues" schema="student.elections.electionPeriod" >
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thright mtop05"/>
			<fr:property name="columnClasses" value="nowrap, nowrap"/>
			<fr:property name="rowClasses" value="bold,,,,"/>
		</fr:layout>
	</fr:view>
	<p>
		<span class="warning0"><bean:message bundle="APPLICATION_RESOURCES" key="label.elections.votingPeriod.voted" /></span>
	</p>
</logic:present>

<!-- STUDENT THAT HAS NOT VOTED YET -->

<logic:notPresent name="studentVote">
<logic:notPresent name="blankVote">
	<logic:present name="notVotedYearDelegate" >
		
		<fr:view name="notVotedYearDelegate" layout="tabular-nonNullValues" schema="student.elections.electionPeriod" >
			<fr:layout>
				<fr:property name="classes" value="tstyle2 thlight thright mvert05"/>
				<fr:property name="columnClasses" value="nowrap, nowrap"/>
				<fr:property name="rowClasses" value="bold,,,,"/>
			</fr:layout>
		</fr:view>
		
		<p class="mtop1 mbottom05">
			<b><bean:message key="label.elections.votingPeriod" bundle="APPLICATION_RESOURCES"/></b>
		</p>
		<p class="infoop2">
			<bean:message key="label.elections.votingPeriod.notVoted.help" bundle="APPLICATION_RESOURCES" />
		</p>
		
	
		
		<logic:present name="candidatesBeanList">
			<logic:notEmpty name="candidatesBeanList">	
			
			<div class="mvert15" style="background: #f8f8f8; padding: 0.5em 1em; border: 1px solid #ccc;">
				<h4 class="mvert05"><bean:message key="label.elections.candidatesList" bundle="APPLICATION_RESOURCES" /></h4>
		
				<fr:form action="/yearDelegateManagement.do">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareVote" />
	
	<style>
	.column1, .column2, .column3 {
	display: block;
	}
	.column1 {
	display: block;
	width: 40px;
	padding-left: 0.3em;
	}
	.column2 {
	display: block;
	width: 250px;
	padding: 0 0.3em;
	}
	.column3 {
	display: block;
	padding: 0 0.3em;
	}
	.delegate tr td ul li {
	clear: both;
	display: block;
	padding-top: 1.5em !important;
	}
	.delegate tr td ul li input {
	}
	.delegate tr td ul li * {
	float: left;
	}
	.delegate tr td ul li span {
	margin-top: 2.5em;
	}
	.delegate tr td ul li span span {
	margin-top: 0;
	}
	</style>
	
			
					<fr:edit id="candidate" name="otherStudentsBeanList" schema="student.yearDelegateElection.voteCandidate">
						<fr:layout name="tabular">
							<fr:property name="classes" value="noborder thdnone mtop05 ulnomargin width100 delegate"/>
						</fr:layout>
						<fr:destination name="invalid" path="/yearDelegateManagement.do?method=prepare"/>
					</fr:edit>
					
					
					
					<p class="mtop2 mbottom05">
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="DELEGATES_RESOURCES" key="label.submit" /></html:submit>
						<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='prepare';"><bean:message bundle="DELEGATES_RESOURCES" key="label.clear"/></html:cancel>
					</p>
					
				</fr:form>
			</div>
			
			</logic:notEmpty>
		</logic:present>


	<logic:equal value="false" name="notVotedYearDelegate" property="lastVotingPeriod.secondRoundElections">
		<fr:form action="/yearDelegateManagement.do">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareVote" />
			<fr:edit id="notVotedYearDelegate" name="notVotedYearDelegate" visible="false" />
			
			<div class="mvert15" style="background: #f8f8f8; padding: 0.5em 1em; border: 1px solid #ccc;">
			
				<h4 class="mvert05"><bean:message key="label.elections.otherStudentsList" bundle="APPLICATION_RESOURCES" /></h4>
				
				<fr:edit id="otherStudentsBeanList" name="otherStudentsBeanList"  schema="student.yearDelegateElection.vote" >
					<fr:layout>
						<fr:property name="classes" value="tstyle2 thmiddle thright thlight mtop05 thdnone tstylenone"/>
						<fr:property name="columnClasses" value=",,tdclear tderror1"/>	
						
					</fr:layout>
				<fr:destination name="invalid" path="/yearDelegateManagement.do?method=prepare" />
				</fr:edit> 
				
				<p class="mtop15 mbottom05">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="DELEGATES_RESOURCES" key="label.submit" /></html:submit>
					<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='prepare';"><bean:message bundle="DELEGATES_RESOURCES" key="label.clear"/></html:cancel>
				</p>
				
			</div>
		</fr:form>
	</logic:equal>
		
	</logic:present>
</logic:notPresent>
</logic:notPresent>

<logic:present name="studentVote">
		<fr:view name="notVotedYearDelegate" layout="tabular-nonNullValues" schema="student.elections.electionPeriod" >
			<fr:layout>
				<fr:property name="classes" value="tstyle2 thlight thright mvert05"/>
				<fr:property name="columnClasses" value="nowrap, nowrap"/>
				<fr:property name="rowClasses" value="bold,,,,"/>
			</fr:layout>
		</fr:view>
	
		<fr:form action="/yearDelegateManagement.do">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="vote" />
		<fr:edit id="notVotedYearDelegate" name="notVotedYearDelegate" visible="false" />
		<fr:edit id="prepareVote" name="votedStudentBean"  visible="false" />
		<bean:define id="nome" name="votedStudentBean" property="student.person.name"/>
		<bean:define id="number" name="votedStudentBean" property="student.number"/>
	
	
		<p class="mtop15">
			<span class="highlight1">
				<!--<bean:message key="label.elections.votingPeriod.confirmation.help" bundle="APPLICATION_RESOURCES" />-->
				Confirma o seu voto em <strong><%= nome.toString()%> (<%= number.toString()%>)</strong> ?
			</span>
		</p>
		<p class="mtop1">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="DELEGATES_RESOURCES" key="label.confirm" /></html:submit>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='prepare';"><bean:message bundle="DELEGATES_RESOURCES" key="label.cancel"/></html:cancel>
		</p>
	</fr:form>
</logic:present>

<!--Confirmation Blank Vote -->
<logic:notPresent name="studentVote">
	<logic:present name="blankVote">
		<fr:view name="notVotedYearDelegate" layout="tabular-nonNullValues" schema="student.elections.electionPeriod" >
			<fr:layout>
				<fr:property name="classes" value="tstyle2 thlight thright mvert05"/>
				<fr:property name="columnClasses" value="nowrap, nowrap"/>
				<fr:property name="rowClasses" value="bold,,,,"/>
			</fr:layout>
		</fr:view>
	<fr:form action="/yearDelegateManagement.do">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="vote" />
		<fr:edit id="notVotedYearDelegate" name="notVotedYearDelegate" visible="false" />
		<fr:edit id="prepareVote" name="votedStudentBean"  visible="false" />
	
	
		<p class="mtop15">
			<span class="highlight1">
				<bean:message key="label.elections.votingPeriod.confirmation.blankVote" />
			</span>
		</p>
		<p class="mtop1">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="DELEGATES_RESOURCES" key="label.confirm" /></html:submit>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='prepare';"><bean:message bundle="DELEGATES_RESOURCES" key="label.cancel"/></html:cancel>
		</p>
	</fr:form>
	</logic:present>
</logic:notPresent>