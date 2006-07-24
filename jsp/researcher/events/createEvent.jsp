<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.eventsManagement.superUseCaseTitle"/></em>
		
	<h2/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.createEvent.useCasetitle"/> </h2>
  	
 	<br/>
 		
	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.createEventUseCase.step.insertDataExplanation"/>
  	
   	<br/>
 	<br/>
		
	
	
	<logic:present name="simpleBean">
	 	<br/>
 	 	<br/>
 	 	<br/>
		<fr:edit id="simpleBean" name="simpleBean" action="/events/createEvent.do?method=createSimpleEventParticipation" schema="eventParticipation.simpleCreation">
			<fr:destination name="invalid" path="/events/createEvent.do?method=prepareCreateSimpleEventParticipation"/>			
			<fr:destination name="cancel" path="/events/eventsManagement.do?method=listEvents"/>				
		</fr:edit>
	</logic:present>
	<logic:present name="fullBean">
		<strong>
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.createEventUseCase.fullEventCreationExplanation"/>
		</strong>
		<br/>
		<br/>
		<fr:edit id="fullBean" name="fullBean" action="/events/createEvent.do?method=createFullEventParticipation" schema="eventParticipation.fullCreation">
			<fr:destination name="invalid" path="/events/createEvent.do?method=prepareCreateFullEventParticipation"/>		
			<fr:destination name="cancel" path="/events/eventsManagement.do?method=listEvents"/>	
		</fr:edit>
	</logic:present>
  	<br/>
	
</logic:present>
		
<br/>