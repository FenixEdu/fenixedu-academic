<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<em>Eventos</em> <!-- tobundle -->
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.createEvent.useCasetitle"/></h2>
  	
 	<p class="mtop2 mbottom1">	
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.createEventUseCase.step.insertDataExplanation"/>
	</p>

	
	<logic:present name="simpleBean">
		<fr:edit id="simpleBean" name="simpleBean" action="/events/createEvent.do?method=createSimpleEventParticipation" schema="eventParticipation.simpleCreation">
			<fr:destination name="invalid" path="/events/createEvent.do?method=prepareCreateSimpleEventParticipation"/>			
			<fr:destination name="cancel" path="/events/eventsManagement.do?method=listEvents"/>				
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright mtop0"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
	</logic:present>

	<logic:present name="fullBean">
		<p class="mvert1">
			<span class="warning0">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.createEventUseCase.fullEventCreationExplanation"/>
			</span>
		</p>

		<fr:edit id="fullBean" name="fullBean" action="/events/createEvent.do?method=createFullEventParticipation" schema="eventParticipation.fullCreation">
			<fr:destination name="invalid" path="/events/createEvent.do?method=prepareCreateFullEventParticipation"/>
			<fr:destination name="cancel" path="/events/eventsManagement.do?method=listEvents"/>
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright"/>
			</fr:layout>
		</fr:edit>
	</logic:present>
	
</logic:present>
		
<br/>