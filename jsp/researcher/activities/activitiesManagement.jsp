<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em> 
  	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.mainTitle"/></h2>


	<script language="JavaScript">	
	function check(e,v){
		if (e.className == "dnone")
	  	{
		  e.className = "dblock";
		  v.value = "-";
		}
		else {
		  e.className = "dnone";
	  	  v.value = "+";
		}
	}
	</script>


	<p class="dblock" id="instructionsButton"><a href="#"  onclick="javascript:check(document.getElementById('instructions'), document.getElementById('instructionsButton')); return false;">Ver tipos de actividades existentes no sistema</a></p>
	<div id="instructions" class="dblock">
		<div class="mtop025">
			<table class="tstyle1 thlight mtop05">
				<tr><th class="width12em"><bean:message bundle="RESEARCHER_RESOURCES" key="label.types" /></th><th><bean:message bundle="RESEARCHER_RESOURCES" key="label.subTypes" /></th><th><bean:message bundle="RESEARCHER_RESOURCES" key="label.roles" /></th></tr>
				<tr><td class="acenter highlight5"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventEditions" /></td><td><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventEditions.types" /></td><td><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventEditions.roles" /></td></tr>
				<tr><td class="acenter highlight5"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournals" /></td><td style="text-align: center;">-</th><td><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournals.roles" /></td></tr>
				<tr><td class="acenter highlight5"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.journalIssues" /></td><td style="text-align: center;">-</td><td><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.journalIssues.roles" /></td></tr>
				<tr><td class="acenter highlight5"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.cooperations" /></td><td style="text-align: center;">-</th><td><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.cooperations.roles" /></td></tr>
			</table>
		</div>

	</div>

	<script>
		check(document.getElementById('instructions'), document.getElementById('instructionsButton'));
		document.getElementById('instructionsButton').className="dblock";
	</script>




	<ul class="mtop15 mbottom1 list5">
		<li> <bean:message key="researcher.activity.activitiesManagement.createParticipation" bundle="RESEARCHER_RESOURCES"/>: 
			<%-- <html:link page="/activities/createEvent.do?method=prepareEventSearch"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.events" /></html:link>,  --%>
			<html:link page="/activities/createEventEdition.do?method=prepareEventEditionSearch"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventEditions" /></html:link>, 
			<html:link page="/activities/createScientificJournal.do?method=prepareJournalSearch"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournals" /></html:link>,
			<html:link page="/activities/createJournalIssue.do?method=prepareJournalIssueSearch"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.journalIssues" /></html:link>,
			<html:link page="/activities/createCooperation.do?method=prepareCreateCooperationParticipation"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.cooperations" /></html:link>
		</li>
	</ul>
	
	<logic:notEmpty name="international-events">
		<p id='events' class="mtop3 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventsTitle.international" /></span></strong>
		</p>
		<bean:define id="participations" name="international-events" toScope="request"/>
		<bean:define id="forwardTo" value="EditEvent" toScope="request"/>
		<bean:define id="schema" value="researchEventParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>

	<logic:notEmpty name="national-events">
		<p id='events' class="mtop3 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventsTitle.national" /></span></strong>
		</p>
		<bean:define id="participations" name="national-events" toScope="request"/>
		<bean:define id="forwardTo" value="EditEvent" toScope="request"/>
		<bean:define id="schema" value="researchEventParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
		
	</logic:notEmpty>
	
	<logic:notEmpty name="international-eventEditions">
		<p id='events' class="mtop3 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventEditionsTitle.international" /></span></strong>
		</p>
		<bean:define id="participations" name="international-eventEditions" toScope="request"/>
		<bean:define id="forwardTo" value="EditEventEdition" toScope="request"/>
		<bean:define id="schema" value="researchEventEditionParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>
		
	<logic:notEmpty name="national-eventEditions">
		<p id='events' class="mtop3 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventEditionsTitle.national" /></span></strong>
		</p>
		<bean:define id="participations" name="national-eventEditions" toScope="request"/>
		<bean:define id="forwardTo" value="EditEventEdition" toScope="request"/>
		<bean:define id="schema" value="researchEventEditionParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
		
	</logic:notEmpty>

	<logic:notEmpty name="international-journals">
		<p id='scientificJournals' class="mtop3 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournalsTitle.international" /></span></strong>
		</p>
		<bean:define id="participations" name="international-journals" toScope="request"/>
		<bean:define id="forwardTo" value="EditScientificJournal" toScope="request"/>
		<bean:define id="schema" value="researchScientificJournalParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>

	<logic:notEmpty name="national-journals">
		<p id='scientificJournals' class="mtop3 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournalsTitle.national" /></span></strong>
		</p>
		<bean:define id="participations" name="national-journals" toScope="request"/>
		<bean:define id="forwardTo" value="EditScientificJournal" toScope="request"/>
		<bean:define id="schema" value="researchScientificJournalParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="international-issues">
		<p id='issues' class="mtop3 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.issueTitle.international" /></span></strong>
		</p>
		<bean:define id="participations" name="international-issues" toScope="request"/>
		<bean:define id="forwardTo" value="EditJournalIssue" toScope="request"/>
		<bean:define id="schema" value="researchJournalIssueParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>
		
	<logic:notEmpty name="national-issues">
		<p id='issues' class="mtop3 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.issueTitle.national" /></span></strong>
		</p>
		<bean:define id="participations" name="national-issues" toScope="request"/>
		<bean:define id="forwardTo" value="EditJournalIssue" toScope="request"/>
		<bean:define id="schema" value="researchJournalIssueParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="cooperations">
		<p id='cooperations' class="mtop3 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.cooperationsTitle" /></span></strong>
		</p>
		<bean:define id="participations" name="cooperations" toScope="request"/>
		<bean:define id="forwardTo" value="EditCooperation" toScope="request"/>
		<bean:define id="schema" value="researchCooperationParticipation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>
	
</logic:present>
		
