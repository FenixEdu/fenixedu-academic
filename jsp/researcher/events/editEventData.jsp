<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<bean:define id="eventId" name="selectedEvent" property="idInternal" />

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.eventsManagement.superUseCaseTitle"/></em>
		
	<h2/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.data.useCaseTitle"/> </h2>
  	
	<br/>

	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.data.explanation"/>
  	
  	<br/>
		
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.data"/> </h3>
	
	<fr:edit name="selectedEvent" schema="event.edit-defaults" action="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	    <fr:destination name="cancel" path="<%="/events/viewEvent.do?method=prepare&eventId="+eventId%>"/>
	</fr:edit>
	
</logic:present>
		
<br/>