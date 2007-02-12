<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		
	<%--
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.eventsManagement.superUseCaseTitle"/></em>
	--%>
  	<h2 id='pageTitle'/><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.mainTitle"/></h2>
		
	<ul class="mtop15 mbottom1 list5">
		<li>
			<html:link page="/activities/createEvent.do?method=prepareEventSearch"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.createEvent" /></html:link>
		</li>
		<li>
			<html:link page="/activities/createScientificJournal.do?method=prepareJournalSearch"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.createScientificJournal" /></html:link>	
		</li>
		<li>
			<html:link page="/activities/createCooperation.do?method=prepareCreateCooperationParticipation"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.createCooperation" /></html:link>
		</li>
	</ul>

	<logic:notEmpty name="events">
		<p id='events' class="mtop2 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.eventsTitle" /></span></strong>
		</p>
		<bean:define id="activities" name="events" toScope="request"/>
		<bean:define id="forwardTo" value="EditEvent" toScope="request"/>
		<bean:define id="schema" value="researchEvent.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="journals">
		<p id='scientificJournals' class="mtop2 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.scientificJournalsTitle" /></span></strong>
		</p>
		<bean:define id="activities" name="journals" toScope="request"/>
		<bean:define id="forwardTo" value="EditScientificJournal" toScope="request"/>
		<bean:define id="schema" value="researchScientificJournal.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="cooperations">
		<p id='cooperations' class="mtop2 mbottom0">
			<strong><span><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.cooperationsTitle" /></span></strong>
		</p>
		<bean:define id="activities" name="cooperations" toScope="request"/>
		<bean:define id="forwardTo" value="EditCooperation" toScope="request"/>
		<bean:define id="schema" value="researchCooperation.summary" toScope="request"/>
		<jsp:include page="researchActivitiesList.jsp"/>
	</logic:notEmpty>
	
</logic:present>
		
