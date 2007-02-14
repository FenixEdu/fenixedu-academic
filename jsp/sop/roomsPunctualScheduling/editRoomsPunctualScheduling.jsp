<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="link.rooms.reserve.management" bundle="SOP_RESOURCES"/></em>
<h2><bean:message key="label.edit.room.punctual.scheduling.second" bundle="SOP_RESOURCES"/></h2>

<logic:present role="TIME_TABLE_MANAGER">

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
		<bean:define id="genericEvent" name="roomsPunctualSchedulingBean" property="genericEvent" type="net.sourceforge.fenixedu.domain.GenericEvent"/>
		<logic:notEmpty name="genericEvent">
			<logic:notEmpty name="genericEvent" property="roomOccupations">					
				
				<logic:empty name="genericEvent" property="frequency">
					<table class="tstyle1 thright thlight mtop025">
						<logic:notEmpty name="roomsPunctualSchedulingBean" property="roomsReserveRequestIdentification">
							<tr>
								<th><bean:message key="label.rooms.reserve.identification" bundle="SOP_RESOURCES"/>:</th>
								<td><bean:write name="roomsPunctualSchedulingBean" property="roomsReserveRequestIdentification"/></td>						
							</tr>			
						</logic:notEmpty>
						<tr>
							<th><bean:message key="property.startDate" bundle="SOP_RESOURCES"/>:</th>
							<td>
								<bean:write name="genericEvent" property="presentationBeginDate"/>
								<bean:write name="genericEvent" property="presentationBeginTime"/>
							</td>
						</tr>
						<tr>
							<th><bean:message key="property.endDate" bundle="SOP_RESOURCES"/>:</th>
							<td>
								<bean:write name="genericEvent" property="presentationEndDate"/>
								<bean:write name="genericEvent" property="presentationEndTime"/>
							</td>
						</tr>														
					</table>						
				</logic:empty>
				
				<logic:notEmpty name="genericEvent" property="frequency">										
					<table class="tstyle1 thright thlight mtop025 mbottom025">
						<logic:notEmpty name="roomsPunctualSchedulingBean" property="roomsReserveRequestIdentification">
							<tr>
								<th><bean:message key="label.rooms.reserve.identification" bundle="SOP_RESOURCES"/>:</th>
								<td><bean:write name="roomsPunctualSchedulingBean" property="roomsReserveRequestIdentification"/></td>						
							</tr>			
						</logic:notEmpty>	
						<tr>
							<th><bean:message key="label.first.day" bundle="SOP_RESOURCES"/>:</th>
							<td><bean:write name="genericEvent" property="presentationBeginDate"/></td>
						</tr>									
						<tr>
							<th><bean:message key="label.last.day" bundle="SOP_RESOURCES"/>:</th>
							<td><bean:write name="genericEvent" property="presentationEndDate"/></td>
						</tr>									
						<tr>
							<th><bean:message key="label.begin.hour" bundle="SOP_RESOURCES"/>:</th>
							<td><bean:write name="genericEvent" property="presentationBeginTime"/></td>
						</tr>
						<tr>
							<th><bean:message key="label.end.hour" bundle="SOP_RESOURCES"/>:</th>
							<td><bean:write name="genericEvent" property="presentationEndTime"/></td>
						</tr>								
						<tr>
							<th><bean:message key="label.frequency" bundle="SOP_RESOURCES"/>:</th>				
							<td><bean:message name="genericEvent" property="frequency.name" bundle="ENUMERATION_RESOURCES"/></td>
						</tr>																				
					</table>				
				</logic:notEmpty>					
			</logic:notEmpty>

			<bean:define id="deleteURL" value="" />
			<logic:empty name="roomsPunctualSchedulingBean" property="roomsReserveRequest">
				<bean:define id="deleteURL">/roomsPunctualScheduling.do?method=deleteRoomsPunctualScheduling&amp;genericEventID=<bean:write name="genericEvent" property="idInternal"/></bean:define>
			</logic:empty>
			<logic:notEmpty name="roomsPunctualSchedulingBean" property="roomsReserveRequest">
				<bean:define id="deleteURL">/roomsReserveManagement.do?method=deleteRoomsPunctualScheduling&amp;genericEventID=<bean:write name="genericEvent" property="idInternal"/>&amp;reserveRequestID=<bean:write name="roomsPunctualSchedulingBean" property="roomsReserveRequest.idInternal"/></bean:define>
			</logic:notEmpty>
			
			<ul class="mtop0 mbottom2">
				<li>
					<html:link page="<%= deleteURL %>" onclick="return confirm('Tem a certeza que deseja apagar a marcação?')">		
						<bean:message bundle="SOP_RESOURCES" key="label.delete.room.punctual.scheduling"/>
					</html:link>
				</li>
			</ul>
				
			<p class="mbottom05"><b><bean:message key="label.choose.rooms" bundle="SOP_RESOURCES"/></b></p>
			<fr:form action="/roomsPunctualScheduling.do">				
				<html:hidden property="method" value="associateNewRoomToEdit"/>							
				<fr:edit id="roomsPunctualSchedulingWithNewRoom" name="roomsPunctualSchedulingBean" schema="AddNewRoomsToPunctualScheduling">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 vamiddle thlight thright mtop025 mbottom0" />
						<fr:property name="columnClasses" value="width5em,width20em,tdclear tderror1" />
					</fr:layout>				
				</fr:edit>
				<table class="tstyle5 thlight thright mtop0">
					<tr>
						<td class="width5em"></td>
						<td class="width20em">
							<html:submit><bean:message key="label.add.new.room" bundle="SOP_RESOURCES"/></html:submit>
							<logic:empty name="roomsPunctualSchedulingBean" property="rooms">
								<html:cancel onclick="this.form.method.value='prepare';this.form.submit();"><bean:message key="label.cancel" bundle="SOP_RESOURCES"/></html:cancel>			
							</logic:empty>
						</td>
					</tr>
				</table>
			</fr:form>
					

			<logic:notEmpty name="roomsPunctualSchedulingBean" property="rooms">
				<p class="mbottom025"><b><bean:message key="label.choosed.rooms" bundle="SOP_RESOURCES"/></b></p>
				<fr:form action="/roomsPunctualScheduling.do?method=removeRoomToEdit">
					<fr:edit id="roomsPunctualSchedulingBeanHidden" name="roomsPunctualSchedulingBean" nested="true" visible="false" />
					<fr:view name="roomsPunctualSchedulingBean" property="rooms" schema="ViewSelectedRooms">
						<fr:layout name="tabular">							
							<fr:property name="checkable" value="true"/>
							<fr:property name="checkboxName" value="selectedRoom"/>
							<fr:property name="checkboxValue" value="idInternal"/>
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
					
			<br/>
			<logic:notEmpty name="roomsPunctualSchedulingBean" property="rooms">
				<b><bean:message key="label.choose.descriptions" bundle="SOP_RESOURCES"/></b>	
				
				<bean:define id="EditNewRoomsPunctualSchedulingURL" value=""/>				
				<logic:empty name="roomsPunctualSchedulingBean" property="roomsReserveRequest">
					<bean:define id="EditNewRoomsPunctualSchedulingURL" value="/roomsPunctualScheduling.do?method=editRoomsPunctualScheduling"/>
				</logic:empty>
				<logic:notEmpty name="roomsPunctualSchedulingBean" property="roomsReserveRequest">						
					<bean:define id="EditNewRoomsPunctualSchedulingURL" value="/roomsReserveManagement.do?method=editRoomsPunctualScheduling"/>
				</logic:notEmpty>	
							
				<fr:edit id="roomsPunctualSchedulingWithDescriptions" name="roomsPunctualSchedulingBean" schema="FinalizeCreationOfRoomsPunctualScheduling"
					action="<%= EditNewRoomsPunctualSchedulingURL %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 vamiddle thlight" />
						<fr:property name="columnClasses" value=",,tdclear tderror1" />
					</fr:layout>
					<logic:empty name="roomsPunctualSchedulingBean" property="roomsReserveRequest">
						<fr:destination name="cancel" path="/roomsPunctualScheduling.do?method=prepare"/>
					</logic:empty>
					<logic:notEmpty name="roomsPunctualSchedulingBean" property="roomsReserveRequest">
						<bean:define id="CancelURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserveRequest&reserveRequestID=<bean:write name="roomsPunctualSchedulingBean" property="roomsReserveRequest.idInternal"/></bean:define>
						<fr:destination name="cancel" path="<%= CancelURL %>"/>					
					</logic:notEmpty>
				</fr:edit>
			</logic:notEmpty>
							
		</logic:notEmpty>	
	</logic:notEmpty>		
</logic:present>	