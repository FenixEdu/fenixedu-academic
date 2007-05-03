<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:present role="RESEARCHER">

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em> 
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createIssues.useCasetitle"/></h2>


<bean:define id="schema" value="journalIssueCreation" type="java.lang.String"/>

<logic:present name="bean" property="scientificJournal">
	<bean:define id="schema" value="journalIssueCreation.journalSelected" type="java.lang.String"/>
</logic:present>

<logic:notPresent name="newJournal">
	<logic:notPresent name="bean" property="scientificJournal">
		<p class="mvert15 breadcumbs">
			<span class="actual"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 1 : </strong>	Escolher Revista</span>	 > 
			<span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 2 : </strong> Escolher Volume/Número e Adicionar Participação</span>
		</p>
	</logic:notPresent>
	<logic:present name="bean" property="scientificJournal">
		<p class="mvert15 breadcumbs">
			<span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 1 : </strong>	Escolher Revista</span>	 > 
			<span class="actual"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 2 : </strong> Escolher Volume/Número e Adicionar Participação</span>
		</p>
		<bean:define id="schema" value="journalIssueCreation.journalSelected" type="java.lang.String"/>
	</logic:present>
</logic:notPresent>

<bean:define id="createJournal" value="false"/>

<logic:notPresent name="newJournal">
	<logic:notPresent name="bean" property="scientificJournal">
		<logic:present name="bean" property="scientificJournalName">
			<bean:define id="createJournal" value="true"/>
		</logic:present>
	</logic:notPresent>
</logic:notPresent>

<logic:equal name="createJournal" value="true">
	<div class="warning0">
		<strong><bean:message key="label.attention" bundle="RESEARCHER_RESOURCES"/>:</strong><br/>
		<bean:message key="label.informationForCreateMagazine" bundle="RESEARCHER_RESOURCES"/>
	</div>
</logic:equal>

<div class="dinline forminline">
<fr:form action="/activities/createJournalIssue.do?method=prepareJournalIssueParticipation">
	<fr:edit id="name" name="bean" visible="false"/>
	
	<logic:notPresent name="issueBean">
	<fr:edit id="createParticipation" name="bean" schema="<%= schema %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 dinline"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit><br/>
	<html:submit><bean:message key="button.submit" bundle="RESEARCHER_RESOURCES"/></html:submit>
	<logic:present name="bean" property="scientificJournal">
		<html:submit property="newIssue"><bean:message key="label.createNewIssue" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</logic:present>
	</logic:notPresent>
	
	<logic:present name="issueBean">
		<fr:edit id="issueBean" name="issueBean" visible="false"/>
		<logic:notPresent name="newIssue">
		<p><strong><bean:message key="label.journal" bundle="RESEARCHER_RESOURCES"/>:</strong></p>	
			<fr:edit id="issueJournalData" name="issueBean" schema="result.publication.create.Article.createMagazine">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 dinline"/>
				</fr:layout>
			</fr:edit>
		</logic:notPresent>
	
		<logic:present name="newIssue">
			<strong><bean:message key="label.ScientificJournal" bundle="RESEARCHER_RESOURCES"/></strong>: <fr:view name="bean" property="scientificJournal.name"/> 
		</logic:present>
	<br/>

	<bean:define id="issueSchema" value="result.publication.create.Article.createIssue"/>
	<logic:equal name="issueBean" property="specialIssue" value="true">
		<bean:define id="issueSchema" value="result.publication.create.Article.createSpecialIssue"/>
	</logic:equal>
	
	<logic:notPresent name="newIssue">
		<p><strong><bean:message key="label.volume" bundle="RESEARCHER_RESOURCES"/>:</strong></p>	
	</logic:notPresent>
	
	<fr:edit id="issueIssueData" name="issueBean" schema="<%= issueSchema %>">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle dinline"/>
		        <fr:property name="columnClasses" value="width125px,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="postBack" path="/activities/createJournalIssue.do?method=createPostback"/>
		</fr:edit>
		<br/>
	</logic:present>

	<logic:equal name="createJournal" value="true">
		<html:submit property="prepareCreateNewJournal"><bean:message key="label.createMagazine" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</logic:equal>
	<logic:present name="newJournal">
		<html:submit property="createNewJournal"><bean:message key="label.createMagazine" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</logic:present>
	<logic:present name="newIssue">
		<html:submit property="createNewIssue"><bean:message key="label.createIssue" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</logic:present>
</fr:form>
</logic:present>

<logic:present name="bean" property="scientificJournal">
	<fr:form action="/activities/createJournalIssue.do?method=prepareJournalIssueParticipation">
		<html:submit><bean:message key="button.back" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</fr:form>
</logic:present>

<fr:form action="/activities/activitiesManagement.do?method=listActivities">
		<html:submit><bean:message key="button.cancel" bundle="RESEARCHER_RESOURCES"/></html:submit>
</fr:form>

</div>
