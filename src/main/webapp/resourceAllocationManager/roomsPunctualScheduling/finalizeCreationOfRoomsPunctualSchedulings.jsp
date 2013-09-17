<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="link.rooms.reserve.management" bundle="SOP_RESOURCES"/></em>
<h2><bean:message key="label.create.room.punctual.scheduling.title" bundle="SOP_RESOURCES"/></h2>

<p class="breadcumbs"><span>Passo1: <bean:message key="label.create.room.punctual.scheduling.choosePeriod" bundle="SOP_RESOURCES"/></span> &gt; <span class="actual">Passo 2: <bean:message key="label.create.room.punctual.scheduling.chooseRoom" bundle="SOP_RESOURCES"/></span></p>

<logic:present role="RESOURCE_ALLOCATION_MANAGER">

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="SOP_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>	
	
	<logic:notEmpty name="roomsPunctualSchedulingBean">				
		<logic:notEmpty name="roomsPunctualSchedulingBean" property="periodType">					
			
			<p class="mbottom05"><strong><bean:message key="label.scheduling.period" bundle="SOP_RESOURCES"/></strong></p>
														
			<logic:notEmpty name="roomsPunctualSchedulingBean" property="frequency">				
				<table class="tstyle5 thright thlight mtop025">
					<logic:notEmpty name="roomsPunctualSchedulingBean" property="roomsReserveRequest">
						<tr>
							<th><bean:message key="label.rooms.reserve.identification" bundle="SOP_RESOURCES"/>:</th>
							<td><bean:write name="roomsPunctualSchedulingBean" property="roomsReserveRequest.identification"/></td>						
						</tr>			
					</logic:notEmpty>
					<tr>
						<th><bean:message key="label.first.day" bundle="SOP_RESOURCES"/>:</th>
						<td><bean:write name="roomsPunctualSchedulingBean" property="presentationBeginDate"/></td>
					</tr>									
					<tr>
						<th><bean:message key="label.last.day" bundle="SOP_RESOURCES"/>:</th>
						<td><bean:write name="roomsPunctualSchedulingBean" property="presentationEndDate"/></td>
					</tr>									
					<tr>
						<td><bean:message key="label.begin.hour" bundle="SOP_RESOURCES"/>:</td>
						<td><bean:write name="roomsPunctualSchedulingBean" property="presentationBeginTime"/></td>
					</tr>
					<tr>
						<th><bean:message key="label.end.hour" bundle="SOP_RESOURCES"/>:</th>
						<td><bean:write name="roomsPunctualSchedulingBean" property="presentationEndTime"/></td>
					</tr>								
					<tr>
						<th><bean:message key="label.frequency" bundle="SOP_RESOURCES"/>:</th>
						<td><bean:message name="roomsPunctualSchedulingBean" property="frequency.name" bundle="ENUMERATION_RESOURCES"/></td>										
					</tr>												
				</table>						
			</logic:notEmpty>				
						
			<logic:empty name="roomsPunctualSchedulingBean" property="frequency">
				<table class="tstyle5 thright thlight mtop025">
					<logic:notEmpty name="roomsPunctualSchedulingBean" property="roomsReserveRequest">
						<tr>
							<th><bean:message key="label.rooms.reserve.identification" bundle="SOP_RESOURCES"/>:</th>
							<td><bean:write name="roomsPunctualSchedulingBean" property="roomsReserveRequest.identification"/></td>						
						</tr>			
					</logic:notEmpty>	
					<tr>
						<th><bean:message key="property.startDate" bundle="SOP_RESOURCES"/>:</th>
						<td>
							<bean:write name="roomsPunctualSchedulingBean" property="presentationBeginDate"/>
							<bean:write name="roomsPunctualSchedulingBean" property="presentationBeginTime"/>
						</td>
					</tr>
					<tr>
						<th><bean:message key="property.endDate" bundle="SOP_RESOURCES"/>:</th>
						<td>
							<bean:write name="roomsPunctualSchedulingBean" property="presentationEndDate"/>
							<bean:write name="roomsPunctualSchedulingBean" property="presentationEndTime"/>
						</td>
					</tr>													
				</table>	
			</logic:empty>		

			<p class="mbottom05"><b><bean:message key="label.choose.rooms" bundle="SOP_RESOURCES"/></b></p>
			<fr:form action="/roomsPunctualScheduling.do">				
				<html:hidden property="method" value="associateNewRoom"/>							
				<fr:edit id="roomsPunctualSchedulingWithNewRoom" name="roomsPunctualSchedulingBean" schema="AddNewRoomsToPunctualScheduling">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 vamiddle thlight thright mtop025 mbottom0" />
						<fr:property name="columnClasses" value="width5em,width40em,tdclear tderror1" />
					</fr:layout>				
				</fr:edit>
				<table class="tstyle5 thlight thright mtop0">
					<tr>
						<td class="width5em"></td>
						<td class="width40em">
							<html:submit><bean:message key="label.add.new.room" bundle="SOP_RESOURCES"/></html:submit>
							<logic:empty name="roomsPunctualSchedulingBean" property="rooms">								
								<logic:empty name="roomsPunctualSchedulingBean" property="roomsReserveRequest">									
									<html:cancel onclick="this.form.method.value='prepare';this.form.submit();"><bean:message key="label.cancel" bundle="SOP_RESOURCES"/></html:cancel>												
								</logic:empty>						
							</logic:empty>
						</td>
					</tr>
				</table>
			</fr:form>
	
			<logic:notEmpty name="roomsPunctualSchedulingBean" property="rooms">
				<p class="mbottom025"><b><bean:message key="label.choosed.rooms" bundle="SOP_RESOURCES"/></b></p>
				<fr:form action="/roomsPunctualScheduling.do?method=removeRoom">
					<fr:edit id="roomsPunctualSchedulingBeanHidden" name="roomsPunctualSchedulingBean" nested="true" visible="false" />
					<fr:view name="roomsPunctualSchedulingBean" property="rooms" schema="ViewSelectedRooms">
						<fr:layout name="tabular">							
							<fr:property name="checkable" value="true"/>
							<fr:property name="checkboxName" value="selectedRoom"/>
							<fr:property name="checkboxValue" value="externalId"/>
							<fr:property name="classes" value="tstyle1 vamiddle thlight thcenter mtop025 mbottom0"/>
							<fr:property name="columnClasses" value="width2em,width8em acenter,width15em acenter,width15em acenter"/>
						</fr:layout>							
					</fr:view>
				<table class="tstyle1 thlight thcenter mtop0">
					<tr>
						<th class="width2em"></th>
						<th class="width8em"></th>
						<th class="width15em acenter"><bean:write name="roomsPunctualSchedulingBean" property="totalAvailableRoomSpace.key"/></th>
						<th class="width15em acenter"><bean:write name="roomsPunctualSchedulingBean" property="totalAvailableRoomSpace.value"/></th>
					</tr>
				</table>
					<p class="mtop0"><html:submit><bean:message key="label.remove.room"/></html:submit></p>					
				</fr:form>																															
			</logic:notEmpty>
	

			<logic:notEmpty name="roomsPunctualSchedulingBean" property="rooms">
				<p class="mtop2 mbottom025"><strong><bean:message key="label.choose.descriptions" bundle="SOP_RESOURCES"/></strong></p>
				
				
				<bean:define id="CreateNewRoomsPunctualSchedulingURL" value=""/>
				<logic:empty name="roomsPunctualSchedulingBean" property="roomsReserveRequest">
					<bean:define id="CreateNewRoomsPunctualSchedulingURL" value="/roomsPunctualScheduling.do?method=createRoomsPunctualScheduling"/>
				</logic:empty>
				<logic:notEmpty name="roomsPunctualSchedulingBean" property="roomsReserveRequest">						
					<bean:define id="CreateNewRoomsPunctualSchedulingURL" value="/roomsReserveManagement.do?method=createRoomsPunctualScheduling"/>
				</logic:notEmpty>

				<fr:edit id="roomsPunctualSchedulingWithDescriptions" name="roomsPunctualSchedulingBean" schema="FinalizeCreationOfRoomsPunctualScheduling"
					action="<%= CreateNewRoomsPunctualSchedulingURL %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 vamiddle thlight mtop025" />
						<fr:property name="columnClasses" value=",,tdclear tderror1" />
					</fr:layout>
					<logic:empty name="roomsPunctualSchedulingBean" property="roomsReserveRequest">
						<fr:destination name="cancel" path="/roomsPunctualScheduling.do?method=prepare"/>
					</logic:empty>
					<logic:notEmpty name="roomsPunctualSchedulingBean" property="roomsReserveRequest">						
						<bean:define id="CancelURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserveRequest&reserveRequestID=<bean:write name="roomsPunctualSchedulingBean" property="roomsReserveRequest.externalId"/></bean:define>
						<fr:destination name="cancel" path="<%= CancelURL %>"/>					
					</logic:notEmpty>				
				</fr:edit>
			</logic:notEmpty>			
		</logic:notEmpty>		
	</logic:notEmpty>
	
</logic:present>

