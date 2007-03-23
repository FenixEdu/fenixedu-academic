<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.research"/></em>
<h2><bean:message key="label.editEvent" bundle="RESEARCHER_RESOURCES"/></h2>


<bean:define id="publicationBean" name="publicationBean" type="net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultPublicationBean"/>
<bean:define id="parameters" value="<%= "resultId=" + publicationBean.getIdInternal().toString() %>"/>

<logic:messagesPresent message="true">
	<p>
	<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
		<span class="error0"><!-- Error messages go here --><bean:write name="messages" /></span>
	</html:messages>
	</p>
</logic:messagesPresent>

<logic:notPresent name="eventEditionBean">		
	<!-- Choose Edition -->
	<logic:present name="publicationBean" property="event">
		<div class="infoop2"><bean:message key="label.chooseEventEdition.instructions" bundle="RESEARCHER_RESOURCES"/></div>
		<div class="dinline forminline">	
			<fr:form action="/resultPublications/editData.do">
				<fr:edit id="publicationBean" name="publicationBean" visible="false"/>
				<fr:edit id="selectPublication" name="publicationBean" schema="result.publication.create.ConferenceArticle.selectEdition">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thright thlight thtop mtop1"/>
		        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
				</fr:edit>
				<html:submit property="confirm"><bean:message key="label.chooseEventEditionFromList" bundle="RESEARCHER_RESOURCES"/></html:submit>
			</fr:form>
			<fr:form action="/resultPublications/createEventToAssociate.do">
				<fr:edit id="publicationBean" name="publicationBean" visible="false"/>
				<html:submit property="createNewEventEdition"><bean:message key="label.createEventEdition" bundle="RESEARCHER_RESOURCES"/></html:submit>	
			</fr:form>
			<fr:form action="/resultPublications/prepareSelectEventToAssociate.do">					
				<fr:edit id="publicationBean" name="publicationBean" visible="false"/>
				<html:submit property="goToNextStep"><bean:message key="label.chooseNewEvent" bundle="RESEARCHER_RESOURCES"/></html:submit>	
			</fr:form>
		    <fr:form action="<%= "/resultPublications/showPublication.do?" + parameters%>">
			<html:submit><bean:message key="button.cancel"/></html:submit>
			</fr:form>
		</div>
	</logic:present>					
</logic:notPresent>					
		
<!-- Create Event or Event Edition  -->			
<logic:present name="eventEditionBean">
	<div class="dinline forminline">	
		<fr:form action="/resultPublications/createEventToAssociate.do">
			<fr:edit id="publicationBean" name="publicationBean" visible="false"/>
			<fr:edit id="eventCreationBean" name="eventEditionBean" visible="false"/>		
			
			<!-- Select event, using autocomplete -->
			<logic:equal name="eventEditionBean" property="selectEventState" value="true">
				<logic:present name="eventEditionBean" property="eventName">
					<div class="warning0">
						<b><bean:message key="label.attention" bundle="RESEARCHER_RESOURCES"/>:</b><br/>
						<bean:message key="label.informationForCreateEvent" bundle="RESEARCHER_RESOURCES"/>
					</div>
				</logic:present>
				
				<p class="color888 mvert05"><bean:message key="label.chooseEvent.instructions" bundle="RESEARCHER_RESOURCES"/></p>
				<fr:edit id="eventEditionBean" name="eventEditionBean" schema="result.publication.select.Event">
					<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle5 thright thlight thtop mtop05 dinline"/>
				        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
						</fr:layout>
					<fr:destination name="invalid" path="/resultPublications/prepareCreateEventToAssociate.do"/>
				</fr:edit>
				<br/>
				<html:submit property="goToNextStep"><bean:message key="label.chooseEventFromList" bundle="RESEARCHER_RESOURCES"/></html:submit>
				<logic:present name="eventEditionBean" property="eventName">
					<html:submit property="createNewEvent"><bean:message key="label.createEvent" bundle="RESEARCHER_RESOURCES"/></html:submit>	
				</logic:present>
			</logic:equal>			
			
			<!-- Select event edition, using autocomplete -->
			 <logic:equal name="eventEditionBean" property="selectEventEditionState" value="true">
			 	<div class="infoop2"><bean:message key="label.chooseEventEdition.instructions" bundle="RESEARCHER_RESOURCES"/></div>
				<br/>
				<fr:edit id="eventEditionBean" name="eventEditionBean" schema="result.publication.select.EventEdition">
					<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle5 thright thlight thtop mtop05 dinline"/>
				        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
						</fr:layout>
					<fr:destination name="invalid" path="/resultPublications/prepareCreateEventToAssociate.do"/>			
				</fr:edit>
				<br/>
				<html:submit property="goToNextStep"><bean:message key="label.chooseEventEditionFromList" bundle="RESEARCHER_RESOURCES"/></html:submit>
			 </logic:equal>
			 
			 <!-- Create new event  -->
			<logic:equal name="eventEditionBean" property="newEventState" value="true">
				<p class="mbottom0"><strong><bean:message key="label.eventData" bundle="RESEARCHER_RESOURCES"/>:</strong></p>
				<fr:edit id="eventInfo" name="eventEditionBean" schema="result.publication.createEvent">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight thtop mtop0"/>
		        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
					<fr:destination name="invalid" path="/resultPublications/prepareCreateEventToAssociate.do"/>
				</fr:edit>
				<br/>
				<p><strong><bean:message key="label.eventEdition" bundle="RESEARCHER_RESOURCES"/></strong>:</p>
				<p class="color888"><bean:message key="label.newEventEdition.instructions" bundle="RESEARCHER_RESOURCES"/></p>
				<fr:edit id="eventEditionBean" name="eventEditionBean" schema="result.publication.create.NewEventEdition">
					<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle5 thright thlight thtop mtop05 dinline"/>
				        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
						</fr:layout>
					<fr:destination name="invalid" path="/resultPublications/prepareCreateEventToAssociate.do"/>			
				</fr:edit>
				<br/>
				<html:submit property="goToNextStep"><bean:message key="label.createEvent" bundle="RESEARCHER_RESOURCES"/></html:submit>
		 	</logic:equal>
			
			<!-- Create new event edition --> 
			<logic:equal name="eventEditionBean" property="newEventEditionState" value="true">
				<div class="mbottom15">
					<p class="mbottom05"><strong><bean:message key="label.event" bundle="RESEARCHER_RESOURCES"/>:</strong></p>
					<fr:view name="eventEditionBean" property="event.name"/>
				</div>
				<p><strong><bean:message key="label.eventEdition" bundle="RESEARCHER_RESOURCES"/></strong>:</p>
				<p class="color888"><bean:message key="label.newEventEdition.instructions" bundle="RESEARCHER_RESOURCES"/></p>
				<fr:edit id="eventEditionBean" name="eventEditionBean" schema="result.publication.create.NewEventEdition">
					<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle5 thright thlight thtop mtop05 dinline"/>
				        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
						</fr:layout>
					<fr:destination name="invalid" path="/resultPublications/prepareCreateEventToAssociate.do"/>			
				</fr:edit>
				<br/>
			 	<html:submit property="goToNextStep"><bean:message key="label.createEventEdition" bundle="RESEARCHER_RESOURCES"/></html:submit>
			</logic:equal>
		</fr:form>
		<!-- Auxiliary form to create event edition -->
		<logic:equal name="eventEditionBean" property="selectEventEditionState" value="true">
			<fr:form action="/resultPublications/createEventToAssociate.do">
				<fr:edit id="publicationBean" name="publicationBean" visible="false"/>
				<fr:edit id="eventEditionBean" name="eventEditionBean" visible="false"/>
				<html:submit property="createNewEventEdition">
					<bean:message key="label.createEventEdition" bundle="RESEARCHER_RESOURCES"/>
				</html:submit>
			</fr:form>
			<fr:form action="/resultPublications/prepareSelectEventToAssociate.do">					
				<fr:edit id="publicationBean" name="publicationBean" visible="false"/>
			<html:submit property="goToNextStep"><bean:message key="label.chooseNewEvent" bundle="RESEARCHER_RESOURCES"/></html:submit>	
		</fr:form>
		</logic:equal>
		<fr:form action="<%= "/resultPublications/showPublication.do?" + parameters%>">
			<html:submit><bean:message key="button.cancel"/></html:submit>
		</fr:form>
	</div>
</logic:present>