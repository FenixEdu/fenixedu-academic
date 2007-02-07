<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.create.room.punctual.scheduling.second" bundle="SOP_RESOURCES"/></h2>

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
		<logic:notEmpty name="roomsPunctualSchedulingBean" property="periodType">					
			
			<p><b><bean:message key="label.scheduling.period" bundle="SOP_RESOURCES"/></b></p>
														
			<logic:notEmpty name="roomsPunctualSchedulingBean" property="frequency">				
				<table>
					<tr>
						<td><bean:message key="label.first.day" bundle="SOP_RESOURCES"/>:</td>
						<td><bean:write name="roomsPunctualSchedulingBean" property="presentationBeginDate"/></td>
					</tr>									
					<tr>
						<td><bean:message key="label.last.day" bundle="SOP_RESOURCES"/>:</td>
						<td><bean:write name="roomsPunctualSchedulingBean" property="presentationEndDate"/></td>
					</tr>									
					<tr>
						<td><bean:message key="label.begin.hour" bundle="SOP_RESOURCES"/>:</td>
						<td><bean:write name="roomsPunctualSchedulingBean" property="presentationBeginTime"/></td>
					</tr>
					<tr>
						<td><bean:message key="label.end.hour" bundle="SOP_RESOURCES"/>:</td>
						<td><bean:write name="roomsPunctualSchedulingBean" property="presentationEndTime"/></td>
					</tr>								
					<tr>
						<td>
							<bean:message key="label.frequency" bundle="SOP_RESOURCES"/>:
							<bean:message name="roomsPunctualSchedulingBean" property="frequency.name" bundle="ENUMERATION_RESOURCES"/>
						</td>				
					</tr>										
				</table>						
			</logic:notEmpty>				
						
			<logic:empty name="roomsPunctualSchedulingBean" property="frequency">
				<table>
					<tr>
						<td><bean:message key="property.startDate" bundle="SOP_RESOURCES"/>:</td>
						<td>
							<bean:write name="roomsPunctualSchedulingBean" property="presentationBeginDate"/>
							<bean:write name="roomsPunctualSchedulingBean" property="presentationBeginTime"/>
						</td>
					</tr>
					<tr>
						<td><bean:message key="property.endDate" bundle="SOP_RESOURCES"/>:</td>
						<td>
							<bean:write name="roomsPunctualSchedulingBean" property="presentationEndDate"/>
							<bean:write name="roomsPunctualSchedulingBean" property="presentationEndTime"/>
						</td>
					</tr>								
				</table>	
			</logic:empty>		
		
			<br/>
			<b><bean:message key="label.choose.rooms" bundle="SOP_RESOURCES"/></b>
			<fr:form action="/roomsPunctualScheduling.do">				
				<html:hidden property="method" value="associateNewRoom"/>							
				<fr:edit id="roomsPunctualSchedulingWithNewRoom" name="roomsPunctualSchedulingBean" schema="AddNewRoomsToPunctualScheduling">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 vamiddle thlight" />
						<fr:property name="columnClasses" value=",,tdclear tderror1" />
					</fr:layout>				
				</fr:edit>			
				<html:submit><bean:message key="label.add.new.room" bundle="SOP_RESOURCES"/></html:submit>
				<logic:empty name="roomsPunctualSchedulingBean" property="rooms">
					<html:cancel onclick="this.form.method.value='prepare';this.form.submit();"><bean:message key="label.cancel" bundle="SOP_RESOURCES"/></html:cancel>			
				</logic:empty>
			</fr:form>
	
			<br/>
			<logic:notEmpty name="roomsPunctualSchedulingBean" property="rooms">
				<b><bean:message key="label.choosed.rooms" bundle="SOP_RESOURCES"/></b>
				<fr:form action="/roomsPunctualScheduling.do?method=removeRoom">
					<fr:edit id="roomsPunctualSchedulingBeanHidden" name="roomsPunctualSchedulingBean" nested="true" visible="false" />
					<fr:view name="roomsPunctualSchedulingBean" property="rooms" schema="ViewSelectedRooms">
						<fr:layout name="tabular">							
							<fr:property name="checkable" value="true"/>
							<fr:property name="checkboxName" value="selectedRoom"/>
							<fr:property name="checkboxValue" value="idInternal"/>
							<fr:property name="classes" value="tstyle5 vamiddle thlight"/>
						</fr:layout>							
					</fr:view>
					
					<bean:message key="label.available.room.space" bundle="SOP_RESOURCES"/> 
					<bean:write name="roomsPunctualSchedulingBean" property="totalAvailableRoomSpace.key"/><bean:message key="label.normal.capacity.abbreviation" bundle="SOP_RESOURCES"/> 
					/ <bean:write name="roomsPunctualSchedulingBean" property="totalAvailableRoomSpace.value"/><bean:message key="label.exams.capacity.abbreviation" bundle="SOP_RESOURCES"/> 
					
					<p><html:submit><bean:message key="label.remove.room"/></html:submit></p>
					
				</fr:form>																															
			</logic:notEmpty>
	
			<br/>
			<logic:notEmpty name="roomsPunctualSchedulingBean" property="rooms">
				<b><bean:message key="label.choose.descriptions" bundle="SOP_RESOURCES"/></b>	
				<fr:edit id="roomsPunctualSchedulingWithDescriptions" name="roomsPunctualSchedulingBean" schema="FinalizeCreationOfRoomsPunctualScheduling"
					action="/roomsPunctualScheduling.do?method=createRoomsPunctualScheduling">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 vamiddle thlight" />
						<fr:property name="columnClasses" value=",,tdclear tderror1" />
					</fr:layout>
					<fr:destination name="cancel" path="/roomsPunctualScheduling.do?method=prepare"/>					
				</fr:edit>
			</logic:notEmpty>			
		</logic:notEmpty>		
	</logic:notEmpty>
	
</logic:present>

