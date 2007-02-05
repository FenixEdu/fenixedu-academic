<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.create.room.punctual.scheduling.first" bundle="SOP_RESOURCES"/></h2>

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
	
	<fr:hasMessages for="roomsPunctualSchedulingWithInfo" type="conversion">
		<p>
			<span class="error0">			
				<fr:message for="roomsPunctualSchedulingWithInfo" show="message"/>
			</span>
		</p>
	</fr:hasMessages>
	
	<logic:notEmpty name="roomsPunctualSchedulingBean">
						
		<fr:form>
			<fr:edit nested="true" id="roomsPunctualSchedulingWithPeriodType" name="roomsPunctualSchedulingBean" schema="ChooseRoomsPunctualSchedulingPeriodType">
				<fr:destination name="postBack" path="/roomsPunctualScheduling.do?method=prepareCreate"/>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 vamiddle thlight" />
					<fr:property name="columnClasses" value=",,tdclear tderror1" />
				</fr:layout>						
			</fr:edit>
		</fr:form>
				
		<logic:notEmpty name="roomsPunctualSchedulingBean" property="periodType">
			
			<bean:define id="finalizeCreationUrl">/roomsPunctualScheduling.do?method=prepareFinalizeCreation</bean:define>
			
			<logic:equal name="roomsPunctualSchedulingBean" property="periodType.name" value="DAILY_TYPE">				
				<fr:edit id="roomsPunctualSchedulingWithInfo" name="roomsPunctualSchedulingBean" schema="CreateDailyRoomsPunctualScheduling" action="<%= finalizeCreationUrl %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 vamiddle thlight" />
						<fr:property name="columnClasses" value=",,tdclear tderror1" />
					</fr:layout>
					<fr:destination name="cancel" path="/roomsPunctualScheduling.do?method=prepare"/>
				</fr:edit>										
			</logic:equal>				
			
			
			<logic:equal name="roomsPunctualSchedulingBean" property="periodType.name" value="WITH_FREQUENCY">				
				<fr:edit id="roomsPunctualSchedulingWithInfo" name="roomsPunctualSchedulingBean" schema="CreateFrequencyRoomsPunctualScheduling" action="<%= finalizeCreationUrl %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 vamiddle thlight" />
						<fr:property name="columnClasses" value=",,tdclear tderror1" />
					</fr:layout>
					<fr:destination name="cancel" path="/roomsPunctualScheduling.do?method=prepare"/>	
				</fr:edit>						
			</logic:equal>				
			
			
			<logic:equal name="roomsPunctualSchedulingBean" property="periodType.name" value="CONTINUOUS">				
				<fr:edit id="roomsPunctualSchedulingWithInfo" name="roomsPunctualSchedulingBean" schema="CreateContinuousRoomsPunctualScheduling" action="<%= finalizeCreationUrl %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 vamiddle thlight" />
						<fr:property name="columnClasses" value=",,tdclear tderror1" />
					</fr:layout>
					<fr:destination name="cancel" path="/roomsPunctualScheduling.do?method=prepare"/>	
				</fr:edit>						
			</logic:equal>				
		
		</logic:notEmpty>
		
	</logic:notEmpty>
</logic:present>

