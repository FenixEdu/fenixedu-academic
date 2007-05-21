<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em> 
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createEventEdition.useCasetitle"/></h2>



<bean:define id="schema" value="eventEditionCreation.eventName" type="java.lang.String"/>


<logic:notPresent name="createNewEvent">
<logic:notPresent name="eventEditionBean" property="event">
	<p class="mvert15 breadcumbs">
		<span class="actual"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 1 : </strong>	<bean:message key="researcher.activity.chooseEvent" bundle="RESEARCHER_RESOURCES"/></span> &gt; 
		<span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 2 : </strong> <bean:message key="researcher.activity.chooseEdition" bundle="RESEARCHER_RESOURCES"/></span>
	</p>
</logic:notPresent>

<logic:present name="eventEditionBean" property="event">
	<p class="mvert15 breadcumbs">
		<span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 1 : </strong>	<bean:message key="researcher.activity.chooseEvent" bundle="RESEARCHER_RESOURCES"/></span> &gt; 
		<span class="actual"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 2 : </strong> <bean:message key="researcher.activity.chooseEdition" bundle="RESEARCHER_RESOURCES"/></span>
	</p>
    <bean:define id="schema" value="eventEditionCreation.selectEdition" type="java.lang.String"/>
</logic:present>
</logic:notPresent>

<logic:present name="createNewEvent">
	<p class="mvert15 breadcumbs">
		<span class="actual"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 1 : </strong>	<bean:message key="researcher.activity.chooseEvent" bundle="RESEARCHER_RESOURCES"/></span> &gt; 
		<span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 2 : </strong> <bean:message key="researcher.activity.chooseEdition" bundle="RESEARCHER_RESOURCES"/></span>
	</p>
</logic:present>


<p class="color555"><bean:message bundle="RESEARCHER_RESOURCES" key="label.chooseEvent.firstInstructions"/></p>


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
	<logic:notPresent name="createNewEdition">
		<fr:edit id="eventEditionBean" name="eventEditionBean" schema="<%= schema %>" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight thmiddle dinline"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="input" path="/activities/createEventEdition.do?method=addNewLanguage"/>
		</fr:edit>
		<br/>
		<html:submit><bean:message key="button.submit" bundle="RESEARCHER_RESOURCES"/></html:submit>
		<logic:equal name="promptForCreation" value="true">
			<html:submit property="prepareCreateEvent"><bean:message key="label.createEvent" bundle="RESEARCHER_RESOURCES"/></html:submit>
		</logic:equal>
		<logic:present name="eventEditionBean" property="event">
			<logic:notPresent name="skipCreateEdition">
			<html:submit property="createEdition"><bean:message key="button.createEventEdition" bundle="RESEARCHER_RESOURCES"/></html:submit>
			</logic:notPresent>
		</logic:present>
	</logic:notPresent>
	</logic:notPresent>
	
	<logic:present name="createNewEvent">
		<p class="mtop2"><strong><bean:message key="label.event" bundle="RESEARCHER_RESOURCES"/>:</strong></p>
		<fr:edit id="editionBean" name="eventEditionBean" visible="false"/>
		<fr:edit id="eventData" name="eventEditionBean"  schema="eventCreation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle dinline"/>
		        <fr:property name="columnClasses" value="width100px,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/activities/createEventEdition.do?method=invalidCreate"/>
		</fr:edit>
		<br/>
		<p class="mtop15"><strong><bean:message key="label.edition" bundle="RESEARCHER_RESOURCES"/>:</strong></p>
		<fr:edit id="editionData" name="eventEditionBean" schema="eventEditionCreation.fullInfo">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle dinline"/>
		        <fr:property name="columnClasses" value="width100px,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/activities/createEventEdition.do?method=invalidCreate&createNewEvent=true"/>
		</fr:edit>
		<br/>
		<html:submit property="newEvent"><bean:message key="button.submit" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</logic:present>
	<logic:present name="createNewEdition">
		<fr:edit id="editionBean" name="eventEditionBean" visible="false"/>
		<strong><bean:message key="label.event" bundle="RESEARCHER_RESOURCES"/>:</strong> <fr:view name="eventEditionBean" property="event.name"/>
		<br/>
		<br/>
		<fr:edit id="editionData" name="eventEditionBean" schema="eventEditionCreation.fullInfo">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle dinline"/>
		        <fr:property name="columnClasses" value="width100px,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/activities/createEventEdition.do?method=invalidCreate&createNewEdition=true"/>
		</fr:edit>
		<br/>
		<html:submit property="newEventEdition"><bean:message key="button.submit" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</logic:present>
</fr:form>

<logic:present name="eventEditionBean" property="event">
	<fr:form action="/activities/createEventEdition.do?method=prepareEventEditionSearch">
		<html:submit><bean:message key="button.back" bundle="RESEARCHER_RESOURCES"/></html:submit>
	</fr:form>
</logic:present>

<fr:form action="/activities/activitiesManagement.do?method=listActivities">
	<html:submit><bean:message key="button.cancel" bundle="RESEARCHER_RESOURCES"/></html:submit>
</fr:form>
</div>


