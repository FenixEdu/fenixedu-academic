<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="academic.calendars.management.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="MANAGER">
	
	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>
			
	<logic:empty name="calendarEntry">					
		<logic:notEmpty name="calendarEntryBean">
									
			<p class="mtop05"><b><bean:message key="label.create.academic.entry" bundle="MANAGER_RESOURCES"/></b></p>												
			<bean:define id="goBackToPrepareCreateEntryURL">/academicCalendarsManagement.do?method=chooseCalendarEntryTypePostBack</bean:define>
			<fr:form>
				<fr:edit id="calendarEntryBeanWithType" name="calendarEntryBean" schema="ChooseAcademicCalendarEntryType" type="net.sourceforge.fenixedu.dataTransferObject.manager.academicCalendarManagement.CalendarEntryBean">
					<fr:destination name="postBack" path="<%= goBackToPrepareCreateEntryURL %>"/>
					<fr:layout name="tabular">      										  
			   			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
		     		</fr:layout> 
				</fr:edit>		
			</fr:form>
			
			<logic:notEmpty name="calendarEntryBean" property="type">									
				<bean:define id="schemaName">Create<bean:write name="calendarEntryBean" property="type.simpleName"/>CalendarEntryType</bean:define>	
				<bean:define id="entryType" name="calendarEntryBean" property="type.name"/>				
								
				<logic:notEmpty name="calendarEntryBean" property="academicCalendar">				
					<b><bean:message key="label.create.where" bundle="MANAGER_RESOURCES"/>:</b> <bean:write name="calendarEntryBean" property="academicCalendar.title.content"/>				
					<bean:define id="createCalendarEntryURL">/academicCalendarsManagement.do?method=viewAcademicCalendar&amp;academicCalendarID=<bean:write name="calendarEntryBean" property="academicCalendar.idInternal"/></bean:define>					
					<fr:form action="<%= createCalendarEntryURL %>">
						<fr:edit id="calendarEntryBeanWithType1" visible="false" name="calendarEntryBean" />	
						<fr:create id="calendarEntryBeanWithInfo" type="<%= entryType.toString() %>" schema="<%= schemaName.toString() %>">													
							<fr:hidden slot="academicCalendar" name="calendarEntryBean" property="academicCalendar"/>																																		
							<fr:layout name="tabular">      										  
					   			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
					   			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				   			</fr:layout> 
						</fr:create>
						<p>
							<html:submit>
								<bean:message key="label.create" bundle="MANAGER_RESOURCES"/>
							</html:submit>
							<html:cancel>
								<bean:message key="label.cancel" bundle="MANAGER_RESOURCES"/>
							</html:cancel>
						</p>											     				
					</fr:form>				
				</logic:notEmpty>
				
				<logic:notEmpty name="calendarEntryBean" property="parentEntry">											
					<b><bean:message key="label.create.where" bundle="MANAGER_RESOURCES"/>:</b> <bean:write name="calendarEntryBean" property="parentEntry.title.content"/>									
					<bean:define id="createCalendarEntryURL">/academicCalendarsManagement.do?method=viewAcademicCalendarEntry&amp;academicCalendarEntryID=<bean:write name="calendarEntryBean" property="parentEntry.idInternal"/></bean:define>										
					<fr:form action="<%= createCalendarEntryURL %>">
						<fr:edit id="calendarEntryBeanWithType2" visible="false" name="calendarEntryBean" />	
						<fr:create id="calendarEntryBeanWithInfo" type="<%= entryType.toString() %>" schema="<%= schemaName.toString() %>">								
							<fr:hidden slot="parentEntry" name="calendarEntryBean" property="parentEntry"/>																		
				
							<fr:layout name="tabular">      										  
					   			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
					   			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				   			</fr:layout> 
						</fr:create>				
						<p>
							<html:submit>
								<bean:message key="label.create" bundle="MANAGER_RESOURCES"/>
							</html:submit>
							<html:cancel>
								<bean:message key="label.cancel" bundle="MANAGER_RESOURCES"/>
							</html:cancel>
						</p>		
					</fr:form>				
				</logic:notEmpty>											
				
			</logic:notEmpty>			
		</logic:notEmpty>
	</logic:empty>	
	
	<logic:notEmpty name="calendarEntry">
		<p class="mtop05"><b><bean:message key="label.edit.academic.entry" bundle="MANAGER_RESOURCES"/></b></p>		
		<bean:define id="createCalendarEntryURL">/academicCalendarsManagement.do?method=viewAcademicCalendarEntry&amp;academicCalendarEntryID=<bean:write name="calendarEntry" property="idInternal"/></bean:define>			
		<fr:edit name="calendarEntry" schema="EditAcademicCalendarEntryType" type="net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry"
			action="<%= createCalendarEntryURL %>">
			<fr:layout name="tabular">      										  
	   			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
	   			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
   			</fr:layout> 
		</fr:edit>		
	</logic:notEmpty>
	
</logic:present>	