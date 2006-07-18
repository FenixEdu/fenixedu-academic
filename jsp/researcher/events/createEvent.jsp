<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.eventsManagement.superUseCaseTitle"/></em>
		
	<h2/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.createEvent.useCasetitle"/> </h2>
  	
 	<strong>
 		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.createEventUseCase.step.title"/> 1 :
 	</strong>
 	<u><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.createEventUseCase.step.insertData"/></u>
		
	<br/>
	<br/>
	
	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.createEventUseCase.step.insertDataExplanation"/>
  	
   	<br/>
 	<br/>
		
	
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.createEvent.data"/> </h3>
	
	<fr:create type="net.sourceforge.fenixedu.domain.research.event.EventParticipation" schema="eventParticipation.create-defaults" action="/events/eventsManagement.do?method=listEvents">
		<fr:hidden slot="party" name="party"/>
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
   		<fr:destination name="cancel" path="/events/eventsManagement.do?method=listEvents"/>
	</fr:create>
	
</logic:present>
		
<br/>