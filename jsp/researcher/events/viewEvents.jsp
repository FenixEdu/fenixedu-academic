<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<fr:view name="eventParticipations" layout="tabular" schema="event.participation.view" >
	<fr:layout>
		<fr:property name="link(edit)" value="/events/eventsManagement.do?method=edit"/>
		<fr:property name="param(edit)" value="idInternal/oid"/>
		<fr:property name="key(edit)" value="researcher.events.eventsManagement.edit"/>
		<fr:property name="order(edit)" value="1"/>
		
		<fr:property name="link(delete)" value="/events/eventsManagement.do?method=delete"/>
		<fr:property name="param(delete)" value="idInternal/oid"/>
		<fr:property name="key(delete)" value="researcher.events.eventsManagement.delete"/>
		<fr:property name="order(delete)" value="2"/>
	</fr:layout>
</fr:view>

<html:link page="/events/eventsManagement.do?method=newEvent"><bean:message key="researcher.events.newEvent"/></html:link>
