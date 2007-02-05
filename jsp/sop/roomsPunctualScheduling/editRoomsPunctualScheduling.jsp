<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

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
	
	<bean:define id="genericEvent" name="genericEvent" type="net.sourceforge.fenixedu.domain.GenericEvent"/>
	<logic:notEmpty name="genericEvent">
		<logic:notEmpty name="genericEvent" property="roomOccupations">					
			
			<logic:empty name="genericEvent" property="frequency">
				<table>
					<tr>
						<td><bean:message key="property.startDate" bundle="SOP_RESOURCES"/>:</td>
						<td>
							<bean:write name="genericEvent" property="presentationBeginDate"/>
							<bean:write name="genericEvent" property="presentationBeginTime"/>
						</td>
					</tr>
					<tr>
						<td><bean:message key="property.endDate" bundle="SOP_RESOURCES"/>:</td>
						<td>
							<bean:write name="genericEvent" property="presentationEndDate"/>
							<bean:write name="genericEvent" property="presentationEndTime"/>
						</td>
					</tr>	
					<tr>
						<td>							
							<bean:message key="label.rooms" bundle="SOP_RESOURCES"/>:
							<logic:iterate id="roomOccupation" name="genericEvent" property="roomOccupations">
								[<bean:write name="roomOccupation" property="room.name"/>]
							</logic:iterate>							
						</td>
					</tr>							
				</table>						
			</logic:empty>
			
			<logic:notEmpty name="genericEvent" property="frequency">										
				<table>
					<tr>
						<td><bean:message key="label.first.day" bundle="SOP_RESOURCES"/>:</td>
						<td><bean:write name="genericEvent" property="presentationBeginDate"/></td>
					</tr>									
					<tr>
						<td><bean:message key="label.last.day" bundle="SOP_RESOURCES"/>:</td>
						<td><bean:write name="genericEvent" property="presentationEndDate"/></td>
					</tr>									
					<tr>
						<td><bean:message key="label.begin.hour" bundle="SOP_RESOURCES"/>:</td>
						<td><bean:write name="genericEvent" property="presentationBeginTime"/></td>
					</tr>
					<tr>
						<td><bean:message key="label.end.hour" bundle="SOP_RESOURCES"/>:</td>
						<td><bean:write name="genericEvent" property="presentationEndTime"/></td>
					</tr>								
					<tr>
						<td>							
							<bean:message key="label.frequency" bundle="SOP_RESOURCES"/>:
							<bean:message name="genericEvent" property="frequency.name" bundle="ENUMERATION_RESOURCES"/>
						</td>				
					</tr>
					<tr>
						<td>							
							<bean:message key="label.rooms" bundle="SOP_RESOURCES"/>:
							<logic:iterate id="roomOccupation" name="genericEvent" property="roomOccupations">
								[<bean:write name="roomOccupation" property="room.name"/>]
							</logic:iterate>							
						</td>
					</tr>															
				</table>				
			</logic:notEmpty>					

		</logic:notEmpty>
		
		<bean:define id="deleteURL">/roomsPunctualScheduling.do?method=deleteRoomsPunctualScheduling&amp;genericEventID=<bean:write name="genericEvent" property="idInternal"/></bean:define>
		<p><html:link page="<%= deleteURL %>">		
			<bean:message bundle="SOP_RESOURCES" key="label.delete.room.punctual.scheduling"/>
		</html:link></p>
				
		<fr:edit id="roomsPunctualSchedulingWithDescriptions" name="genericEvent" schema="EditRoomsPunctualSchedulingDescriptions" action="/roomsPunctualScheduling.do?method=prepare">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 vamiddle thlight" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
			<fr:destination name="cancel" path="/roomsPunctualScheduling.do?method=prepare"/>					
		</fr:edit>
						
	</logic:notEmpty>		
</logic:present>	