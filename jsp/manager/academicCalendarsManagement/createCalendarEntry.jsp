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
	
	
	<%-- Create Entry --%>		
	<logic:empty name="entryBean">							
		<logic:notEmpty name="parentEntryBean">
				
			<logic:notEmpty name="parentEntryBean" property="rootEntry">	
				<p class="mtop15"><b><bean:message key="label.create.academic.entry" bundle="MANAGER_RESOURCES"/></b></p>
			</logic:notEmpty>
			<logic:empty name="parentEntryBean" property="rootEntry">
				<p class="mtop15"><b><bean:message key="label.insert.academic.calendar" bundle="MANAGER_RESOURCES"/></b></p>	
			</logic:empty>
										
			<%-- Choose Entry Type --%>																								
			<logic:notEmpty name="parentEntryBean" property="rootEntry">													
																			
				<bean:define id="goBackToPrepareCreateEntryURL">/academicCalendarsManagement.do?method=chooseCalendarEntryTypePostBack</bean:define>
				
				<fr:form action="<%= goBackToPrepareCreateEntryURL %>">
					<fr:edit id="calendarEntryBeanWithType" name="parentEntryBean" schema="ChooseAcademicCalendarEntryType">
						<fr:destination name="postBack" path="<%= goBackToPrepareCreateEntryURL %>"/>
						<fr:layout name="tabular">      										  
				   			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
			     		</fr:layout> 
					</fr:edit>						
				</fr:form>
							
			</logic:notEmpty>
			
			<%-- Fill Info --%>
			<logic:notEmpty name="parentEntryBean" property="type">																		
																						
				<logic:notEmpty name="parentEntryBean" property="entry">																						
					<p class="mtop15"><b><bean:message key="label.create.where" bundle="MANAGER_RESOURCES"/>:</b> <bean:write name="parentEntryBean" property="entry.title.content"/></p>									
				</logic:notEmpty>
				
				<bean:define id="createCalendarEntryURL">/academicCalendarsManagement.do?method=createEntry</bean:define>
				<bean:define id="schemaName">Create<bean:write name="parentEntryBean" property="type.simpleName"/>CalendarEntryType</bean:define>										
					
				<fr:form action="<%= createCalendarEntryURL %>">						
					<fr:edit id="createdEntryBeanID" name="parentEntryBean" schema="<%= schemaName %>">
						<fr:layout name="tabular">      										  
					   		<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
					   		<fr:property name="columnClasses" value=",,tdclear tderror1"/>					   								   								   	
				   		</fr:layout> 
					</fr:edit>
					<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES"/></html:submit>
				</fr:form>
																													
			</logic:notEmpty>			
		</logic:notEmpty>
	</logic:empty>	
	
	
	<%-- Edit Entry --%>
	<logic:notEmpty name="entryBean">
		
		<p class="mtop05"><b><bean:message key="label.edit.academic.entry" bundle="MANAGER_RESOURCES"/></b></p>		
		
		<bean:define id="editCalendarEntryURL">/academicCalendarsManagement.do?method=editEntry</bean:define>			
		<bean:define id="schemaName">Edit<bean:write name="entryBean" property="type.simpleName"/>CalendarEntryType</bean:define>			
				
		<fr:edit id="editedEntryBeanID" name="entryBean" schema="<%= schemaName %>" action="<%= editCalendarEntryURL %>">
			<fr:layout name="tabular">   													 
	   			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
	   			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
   			</fr:layout> 
		</fr:edit>	
				
	</logic:notEmpty>
	
</logic:present>	