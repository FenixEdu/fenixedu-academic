<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.eventsManagement.superUseCaseTitle"/></em>

  	<h2 id='pageTitle'/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.eventsManagement.title"/> </h2>
		
		
		<fr:view name="events" layout="tabular-list" >
			<fr:layout>
				<fr:property name="subLayout" value="values"/>
				<fr:property name="subSchema" value="event.summary"/>
			
				<fr:property name="link(view)" value="/events/viewEvent.do?method=prepare"/>
				<fr:property name="param(view)" value="idInternal/eventId"/>
				<fr:property name="key(view)" value="researcher.event.eventsManagement.view"/>
				<fr:property name="bundle(view)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(view)" value="1"/>

				<fr:property name="link(delete)" value="/events/eventsManagement.do?method=delete"/>
				<fr:property name="param(delete)" value="idInternal/eventId"/>
				<fr:property name="key(delete)" value="researcher.event.eventsManagement.delete"/>
				<fr:property name="bundle(delete)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(delete)" value="2"/>
			</fr:layout>
		</fr:view>
		
	<html:link page="/events/createEvent.do?method=prepareCreateSimpleEventParticipation"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.event.eventsManagement.createEvent" /></html:link>
</logic:present>
		
<br/>