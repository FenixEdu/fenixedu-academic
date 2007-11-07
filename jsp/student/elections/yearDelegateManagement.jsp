<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.student.elections.operations" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.student.elections.electionsInfoTitle" bundle="APPLICATION_RESOURCES"/></h2>

<!-- AVISOS E ERROS -->
<span class="error0"><html:errors /></span>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
		<p><span class="warning0"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:notPresent name="candidatedYearDelegate" >
	<logic:notPresent name="notCandidatedYearDelegate" >
		<logic:notPresent name="votedYearDelegate" >
			<logic:notPresent name="notVotedYearDelegate" >
				<p class="mvert15">
					<em><bean:message key="label.student.elections.noCandidacyOrVotePeriods" bundle="APPLICATION_RESOURCES"/>.</em>
				</p>
					<logic:present name="yearDelegateResultsElection" >
						<bean:define id="electionOID" name="yearDelegateElection" property="idInternal" />
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
								<fr:property name="classes" value="tstyle2 thlight tdcenter mtop0"/>
								<fr:property name="columnClasses" value="width80px,aleft,,"/>
								<fr:property name="sortParameter" value="sortBy"/>
								<fr:property name="sortableSlots" value="student.number,student.person.name,votesNumber,votesRelativePercentage"/>
				            	<fr:property name="sortUrl" value="/yearDelegateManagement.do?method=prepare"/>
				            	<fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "votesNumber=descending" : request.getParameter("sortBy")  %>"/>
							</fr:layout>
						</fr:view>
					</logic:present>
			</logic:notPresent>
		</logic:notPresent>
	</logic:notPresent>
</logic:notPresent>


<!-- CANDIDATED STUDENT -->
<logic:present name="candidatedYearDelegate" >	
	<p class="mtop1 mbottom05">
		<b><bean:message key="label.elections.candidacyPeriod" bundle="APPLICATION_RESOURCES"/></b></p>
	<p class="color888 mvert05">
		<bean:message key="label.elections.candidacyPeriod.candidate.help" bundle="APPLICATION_RESOURCES" /></p>
	
	<fr:view name="candidatedYearDelegate" layout="tabular-nonNullValues" schema="student.elections.electionPeriod" >
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thright mtop05"/>
			<fr:property name="columnClasses" value="nowrap, nowrap"/>
			<fr:property name="rowClasses" value="bold,,,"/>
		</fr:layout>
	</fr:view>
	
	<ul>
		<li>
			<p class="mtop0 mbottom2">
				<html:link page="/yearDelegateManagement.do?method=removeCandidateStudent" >
					<bean:message key="link.student.elections.removeCandidateStudent" bundle="APPLICATION_RESOURCES" /></html:link>	
		</li>
	</ul>	
</logic:present>

<!-- NOT CANDIDATED STUDENT -->
<logic:present name="notCandidatedYearDelegate" >
	<p class="mtop1 mbottom05">
		<b><bean:message key="label.elections.candidacyPeriod" bundle="APPLICATION_RESOURCES"/></b></p>
	<p class="color888 mvert05">
		<bean:message key="label.elections.candidacyPeriod.notCandidate.help" bundle="APPLICATION_RESOURCES" /></p>
		
	<fr:view name="notCandidatedYearDelegate" layout="tabular-nonNullValues" schema="student.elections.electionPeriod" >
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thright mtop05"/>
			<fr:property name="columnClasses" value="nowrap, nowrap"/>
			<fr:property name="rowClasses" value="bold,,,"/>
		</fr:layout>
	</fr:view>

	<ul>
		<li>
			<p class="mtop0 mbottom1">
				<html:link page="/yearDelegateManagement.do?method=addCandidateStudent" >
					<bean:message key="link.student.elections.addCandidateStudent" bundle="APPLICATION_RESOURCES" /></html:link></p>
		</li>
	</ul>
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
		<span class="warning0"><bean:message key="label.elections.votingPeriod.voted" bundle="APPLICATION_RESOURCES" /></span>
	</p>
</logic:present>

<!-- STUDENT THAT HAS NOT VOTED YET -->
<logic:present name="notVotedYearDelegate" >
	<p class="mtop1 mbottom05">
		<b><bean:message key="label.elections.votingPeriod" bundle="APPLICATION_RESOURCES"/></b></p>
		
	<p class="infoop2">
		<bean:message key="label.elections.votingPeriod.notVoted.help" bundle="APPLICATION_RESOURCES" />
	</p>
	
	<fr:view name="notVotedYearDelegate" layout="tabular-nonNullValues" schema="student.elections.electionPeriod" >
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thright mtop05"/>
			<fr:property name="columnClasses" value="nowrap, nowrap"/>
			<fr:property name="rowClasses" value="bold,,,,"/>
		</fr:layout>
	</fr:view>
	
	<logic:present name="candidatesBeanList">
		<logic:notEmpty name="candidatesBeanList">	
		
		<h4><bean:message key="label.elections.candidatesList" bundle="APPLICATION_RESOURCES" /></h4>
		<p class="mtop1 mbottom05">
			<span class="warning0"><bean:message key="message.warning.votingDelegate" bundle="DELEGATES_RESOURCES" /></span>	
		</p>
		<table class="tstyle2 mtop15 tdleft">
			<logic:iterate id="candidate" name="candidatesBeanList">	
			
					<tr>			
						<td class="personInfo_photo">
						
				          	<logic:notEqual name="candidate" property="student.person.availablePhoto" value="true">
				          		<bean:define id="language" name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language"/>
								<div><img src="<%= request.getContextPath() %>/images/photo_placer01_<%= language == null ? "pt" : String.valueOf(language) %>.gif"/></div>
				          	</logic:notEqual>
				
				          	<logic:equal name="candidate" property="student.person.availablePhoto" value="true">
				      			<bean:define id="personID" name="candidate" property="student.person.idInternal"/>
				      			<div><img src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>"/></div>
				   			</logic:equal>
						</td>			
									
						<td> <bean:write name="candidate" property="student.person.name" /> (<bean:write name="candidate" property="student.number" />)</td>
					
						<td>
							<fr:form action="/yearDelegateManagement.do?method=vote">
								<fr:edit id="notVotedYearDelegate" name="notVotedYearDelegate" visible="false" />
								
								<fr:edit id="candidate" name="candidate" layout="tabular-editable" schema="student.yearDelegateElection.voteCandidate">
									<fr:layout>
										<fr:property name="classes" value=" thlight tdcenter mtop05 mbottom05"/>
										<fr:property name="columnClasses" value="tdclear,tdclear,tdclear"/>
									</fr:layout>
									
									<fr:destination name="invalid" path="/yearDelegateManagement.do?method=vote" />
									<fr:destination name="postBack" path="/yearDelegateManagement.do?method=vote"/>
								</fr:edit>
							</fr:form>
						</td>
					
					</tr>
			</logic:iterate>	
		</table>	
	
		</logic:notEmpty>
	</logic:present>
		
	<fr:form action="/yearDelegateManagement.do?method=vote">
		<fr:edit id="notVotedYearDelegate" name="notVotedYearDelegate" visible="false" />
		
		<h4><bean:message key="label.elections.otherStudentsList" bundle="APPLICATION_RESOURCES" /></h4>
		
		<fr:edit id="otherStudentsBeanList" name="otherStudentsBeanList" layout="tabular-editable" schema="student.yearDelegateElection.vote">
			<fr:layout>
				<fr:property name="classes" value="tstyle1 thlight tdcenter mtop05 mbottom05"/>
				<fr:property name="columnClasses" value=",aleft,"/>
			</fr:layout>
		<fr:destination name="invalid" path="/yearDelegateManagement.do?method=vote" />
		<fr:destination name="postBack" path="/yearDelegateManagement.do?method=vote"/>
		</fr:edit>
	</fr:form>
</logic:present>