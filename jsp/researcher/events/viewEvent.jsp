<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<bean:define id="eventId" name="selectedEvent" property="idInternal" />
	
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.eventsManagement.superUseCaseTitle"/></em>
		
	<h2/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.viewEvent.useCasetitle"/> </h2>
  	
  	<br/>

	<%-- PARTICIPATIONS --%>	
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.participants"/> </h3>
	<fr:view name="participations" layout="tabular" schema="eventParticipation.summary"/>
	<html:link page="<%="/events/editEvent.do?method=prepareEditParticipants&eventId="+eventId%>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.editParticipants" />
	</html:link>
	<br/>	
 	<br/>
	
	<%-- DATA --%>		
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.data"/> </h3>
	<fr:view name="selectedEvent" schema="event.view-defaults">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	</fr:view>
	<html:link page="<%="/events/editEvent.do?method=prepareEditData&eventId="+eventId%>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.editData" />
	</html:link>
	<br/>
 	<br/>
 	
	<%-- PROJECTS --%>		
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.projects"/> </h3>
	<fr:view name="selectedEvent" property="associatedProjects" schema="eventProjectAssociation.project-summary">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	</fr:view>
	<html:link page="<%="/events/editEvent.do?method=prepareEditProjectAssociations&eventId="+eventId%>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.editAssociatedProjects" />
	</html:link>
	<br/>
 	<br/>
	
	<%-- UNITS --%>		
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.units"/> </h3>
	<fr:view name="unitParticipations" layout="tabular" schema="eventUnitParticipation.summary"/>
	<html:link page="<%="/events/editEvent.do?method=prepareEditParticipantUnits&eventId="+eventId%>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.editEvent.editAssociatedUnits" />
	</html:link>
	<br/>
	<br/>		
	
	
</logic:present>
		
<br/>