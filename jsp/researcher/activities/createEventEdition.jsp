<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="schema" name="eventEditionCreationSchema" type="java.lang.String"/>

<logic:notPresent name="eventEditionBean" property="event">
<fr:edit name="eventEditionBean" schema="<%= schema%>" action="/activities/createEventEdition.do?method=prepareCreateEventEditionParticipation"/>
</logic:notPresent>

<logic:present name="eventEditionBean" property="event">
<fr:edit name="eventEditionBean" schema="<%= schema%>" action="/activities/createEventEdition.do?method=createExistentEventEditionParticipation"/>
</logic:present>
