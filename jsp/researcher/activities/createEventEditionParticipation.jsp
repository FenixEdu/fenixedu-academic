<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em> 
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createEventEdition.useCasetitle"/></h2>

<bean:define id="schema" value="eventEditionCreation.eventName" type="java.lang.String"/>
<logic:present name="eventEditionBean" property="event">
    <bean:define id="schema" value="eventEditionCreation.selectEdition" type="java.lang.String"/>
</logic:present>

<bean:define id="promptForCreation" value="false"/>

<logic:notPresent name="createNewEvent">
	<logic:notPresent name="eventEditionBean" property="event">
		<logic:present name="eventEditionBean" property="eventName">
			<bean:define id="promptForCreation" value="true"/>
		</logic:present>
	</logic:notPresent>
</logic:notPresent>

	<logic:equal name="promptForCreation" value="true">
		<div class="warning0">
			<strong><bean:message key="label.attention" bundle="RESEARCHER_RESOURCES"/>:</strong><br/>
			<bean:message key="label.informationForCreateEvent" bundle="RESEARCHER_RESOURCES"/>
		</div> 
	</logic:equal>

<div class="dinline forminline">
<fr:form action="/activities/createEventEdition.do?method=prepareCreateEventEditionParticipation">
	<logic:notPresent name="createNewEvent">
	<fr:edit name="eventEditionBean" schema="<%= schema %>" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 dinline"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	</fr:edit>
	<br/>
	<html:submit><bean:message key="button.submit" bundle="RESEARCHER_RESOURCES"/></html:submit>
	<logic:equal name="promptForCreation" value="true">
		<html:submit property="prepareCreateEvent"><bean:message key="label.createEvent" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</logic:equal>
	</logic:notPresent>
	
	<logic:present name="createNewEvent">

		<fr:edit id="editionBean" name="eventEditionBean" visible="false"/>
		<fr:edit id="eventData" name="eventEditionBean"  schema="eventCreation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 dinline"/>
		        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/activities/createEventEdition.do?method=invalidCreate"/>
		</fr:edit>
		<br/>
		<br/>
		<fr:edit id="editionData" name="eventEditionBean" schema="eventEditionCreation.fullInfo">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 dinline"/>
		        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/activities/createEventEdition.do?method=invalidCreate"/>
		</fr:edit>
		<br/>
		<html:submit property="newEvent"><bean:message key="button.submit" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</logic:present>
</fr:form>
<fr:form action="/activities/createEventEdition.do?method=prepareEventEditionSearch">
	<html:submit><bean:message key="button.cancel" bundle="RESEARCHER_RESOURCES"/></html:submit>
</fr:form>
</div>


