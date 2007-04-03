<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		
	<bean:define id="schema" name="journalCreationSchema" type="java.lang.String" />	
	
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em> 
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createScientificJournal.useCasetitle"/></h2>

	<logic:present name="journalBean">
		<p class="mvert1 breadcumbs">
			<span class="actual"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 1 : </strong>
			<bean:message key="researcher.activity.createJournal.searchJournal" bundle="RESEARCHER_RESOURCES"/></span>
				 > 
			<span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 2 : </strong>
			<bean:message key="researcher.activity.createJournal.createParticipation" bundle="RESEARCHER_RESOURCES"/></span>
	 	</p>
		<p class="mtop2 mbottom1">
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createJournalUseCase.searchJournal"/></strong>
		</p>
		<fr:form action="/activities/createScientificJournal.do?method=prepareCreateScientificJournalParticipation" >
			<fr:edit id="journalBean" name="journalBean"  schema="<%= schema  %>">
				<fr:destination name="invalid" path="/activities/createScientificJournal.do?method=prepareJournalSearch"/>			
				<fr:destination name="cancel" path="/activities/activitiesManagement.do?method=listActivities"/>				
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thleft mtop0"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
			</fr:edit>
			<html:submit><bean:message key="button.continue" bundle="RESEARCHER_RESOURCES"/></html:submit>
			<html:cancel><bean:message key="button.cancel" bundle="RESEARCHER_RESOURCES"/></html:cancel>
		</fr:form>
	</logic:present>
	
	<logic:present name="existentJournalBean">
		<p class="mvert1 breadcumbs">
			<span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 1 : </strong>
			<bean:message key="researcher.activity.createJournal.searchJournal" bundle="RESEARCHER_RESOURCES"/></span>
				 > 
			<span class="actual"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 2 : </strong>
			<bean:message key="researcher.activity.createJournal.createParticipation" bundle="RESEARCHER_RESOURCES"/></span>
	 	</p>
		<p class="mtop2 mbottom025">
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createJournalUseCase.insertJournalParticipationRole"/></strong>
		</p>
		<div class="dinline forminline">
			<fr:form action="/activities/createScientificJournal.do?method=createExistentJournalParticipation">
				<fr:edit id="existentJournalBean" name="existentJournalBean"  schema="<%= schema  %>">
					<fr:destination name="invalid" path="/activities/createScientificJournal.do?method=prepareCreateScientificJournalParticipation"/>
					<fr:destination name="cancel" path="/activities/activitiesManagement.do?method=listActivities"/>
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight thleft"/>
						<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
				</fr:edit>
				<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.submit"/></html:submit>
				<html:cancel><bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/></html:cancel>	
			</fr:form>
			<fr:form action="/activities/createScientificJournal.do?method=prepareJournalSearch">
				<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.back"/></html:submit>
			</fr:form>
		</div>
	</logic:present>

	<logic:present name="inexistentJournalBean">
		<p class="mvert1 breadcumbs">
			<span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 1 : </strong>
			<bean:message key="researcher.activity.createJournal.searchJournal" bundle="RESEARCHER_RESOURCES"/></span>
				 > 
			<span class="actual"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 2 : </strong>
			<bean:message key="researcher.activity.createJournal.createParticipation" bundle="RESEARCHER_RESOURCES"/></span>
	 	</p>
		<p class="mtop2 mbottom025">
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createJournalUseCase.insertJournalData"/></strong>
		</p>
		<div class="dinline forminline">
			<fr:form action="/activities/createScientificJournal.do?method=createInexistentJournalParticipation">
				<fr:edit id="inexistentJournalBean" name="inexistentJournalBean"  schema="<%= schema  %>">
					<fr:destination name="invalid" path="/activities/createScientificJournal.do?method=prepareCreateScientificJournalParticipation"/>
					<fr:destination name="cancel" path="/activities/activitiesManagement.do?method=listActivities"/>
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight thleft thtop mtop05"/>
						<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
				</fr:edit>
				<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.finish"/></html:submit>
				<html:cancel><bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/></html:cancel>	
			</fr:form>
			<fr:form action="/activities/createScientificJournal.do?method=prepareJournalSearch">
				<fr:edit id="stateBean" name="inexistentJournalBean" visible="false"></fr:edit>
				<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.back"/></html:submit>
			</fr:form>	
		</div>
	</logic:present>
	
	<logic:messagesPresent message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error0"><bean:write name="messages" /></span></p>
		</html:messages>
	</logic:messagesPresent>
	
</logic:present>
		
<br/>