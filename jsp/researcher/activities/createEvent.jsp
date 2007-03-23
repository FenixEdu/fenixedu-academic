<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="schema" name="eventCreationSchema" type="java.lang.String" />	

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createEvent.mainTitle"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createEvent.useCasetitle"/></h2>
	
	<logic:present name="eventBean">
		<p class="mvert1 breadcumbs">
			<span class="actual"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 1 : </strong>
			<bean:message key="researcher.activity.createEvent.searchEvent" bundle="RESEARCHER_RESOURCES"/></span>
				 > 
			<span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 2 : </strong>
			<bean:message key="researcher.activity.createEvent.createParticipation" bundle="RESEARCHER_RESOURCES"/></span>
	 	</p>
		<p class="mtop2 mbottom1">
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createEventUseCase.searchEvent"/></strong>
		</p>
		<fr:form action="/activities/createEvent.do?method=prepareCreateEventParticipation">
			<fr:edit id="eventBean" name="eventBean"  schema="<%= schema  %>">
				<fr:destination name="invalid" path="/activities/createEvent.do?method=prepareEventSearch"/>			
				<fr:destination name="cancel" path="/activities/activitiesManagement.do?method=listActivities"/>				
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thleft mtop0"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
			</fr:edit>
			<html:submit><bean:message key="button.continue" bundle="RESEARCHER_RESOURCES"/></html:submit>
			<html:cancel><bean:message key="button.cancel" bundle="RESEARCHER_RESOURCES"/></html:cancel>
		</fr:form>
	</logic:present>
	
	<logic:present name="existentEventBean">
		<p class="mvert1 breadcumbs">
			<span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 1 : </strong>
			<bean:message key="researcher.activity.createEvent.searchEvent" bundle="RESEARCHER_RESOURCES"/></span>
				 > 
			<span class="actual"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 2 : </strong>
			<bean:message key="researcher.activity.createEvent.createParticipation" bundle="RESEARCHER_RESOURCES"/></span>
	 	</p>
		<p class="mtop2 mbottom025">
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createEventUseCase.insertEventParticipationRole"/></strong>
		</p>
		<div class="dinline forminline">
			<fr:form action="/activities/createEvent.do?method=createExistentEventParticipation">
				<fr:edit id="existentEventBean" name="existentEventBean"  schema="<%= schema  %>">
					<fr:destination name="invalid" path="/activities/createEvent.do?method=prepareCreateEventParticipation"/>
					<fr:destination name="cancel" path="/activities/activitiesManagement.do?method=listActivities"/>
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight thleft"/>
						<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
				</fr:edit>
				<br/>
				<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.finish"/></html:submit>
				<html:cancel><bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/></html:cancel>	
			</fr:form>
			<fr:form action="/activities/createEvent.do?method=prepareEventSearch">
				<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.back"/></html:submit>
			</fr:form>
		</div>
	</logic:present>

	<logic:present name="inexistentEventBean">
		<p class="mvert1 breadcumbs">
			<span><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 1 : </strong>
			<bean:message key="researcher.activity.createEvent.searchEvent" bundle="RESEARCHER_RESOURCES"/></span>
				 > 
			<span class="actual"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.step"/> 2 : </strong>
			<bean:message key="researcher.activity.createEvent.createParticipation" bundle="RESEARCHER_RESOURCES"/></span>
	 	</p>
		<p class="mtop2 mbottom025">
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createEventUseCase.insertEventData"/></strong>
		</p>
		<div class="dinline forminline">
			<fr:form action="/activities/createEvent.do?method=createInexistentEventParticipation">
				<fr:edit id="inexistentEventBean" name="inexistentEventBean"  schema="<%= schema  %>">
					<fr:destination name="invalid" path="/activities/createEvent.do?method=prepareCreateEventParticipation"/>
					<fr:destination name="cancel" path="/activities/activitiesManagement.do?method=listActivities"/>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thleft thtop mtop05"/>
						<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
				</fr:edit>
				<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.finish"/></html:submit>
				<html:cancel><bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/></html:cancel>	
			</fr:form>
			<fr:form action="/activities/createEvent.do?method=prepareEventSearch">
				<fr:edit id="stateBean" name="inexistentEventBean" visible="false"></fr:edit>
				<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.back"/></html:submit>
			</fr:form>	
		</div>
	</logic:present>
	
	<logic:messagesPresent message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error0"><bean:write name="messages" /></span></p>
		</html:messages>
	</logic:messagesPresent>
</logic:present>
		
<br/>