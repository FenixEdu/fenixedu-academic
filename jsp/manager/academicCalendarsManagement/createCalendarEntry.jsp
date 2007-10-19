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
			
	<logic:empty name="entryBean">					
		
		<logic:notEmpty name="parentEntryBean">
						
			<p class="mtop05"><b><bean:message key="label.create.academic.entry" bundle="MANAGER_RESOURCES"/></b></p>												
			<bean:define id="goBackToPrepareCreateEntryURL">/academicCalendarsManagement.do?method=chooseCalendarEntryTypePostBack</bean:define>
			
			<fr:form action="<%= goBackToPrepareCreateEntryURL %>">
				<fr:edit id="calendarEntryBeanWithType" name="parentEntryBean" schema="ChooseAcademicCalendarEntryType">
					<fr:destination name="postBack" path="<%= goBackToPrepareCreateEntryURL %>"/>
					<fr:layout name="tabular">      										  
			   			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
		     		</fr:layout> 
				</fr:edit>		
			</fr:form>
			
			<logic:notEmpty name="parentEntryBean" property="type">									
			
				<bean:define id="schemaName">Create<bean:write name="parentEntryBean" property="type.simpleName"/>CalendarEntryType</bean:define>	
				<bean:define id="entryType" name="parentEntryBean" property="type.name"/>				
																						
				<logic:notEmpty name="parentEntryBean" property="entry">											
					
					<b><bean:message key="label.create.where" bundle="MANAGER_RESOURCES"/>:</b> <bean:write name="parentEntryBean" property="entry.title.content"/>									
					<bean:define id="createCalendarEntryURL">/academicCalendarsManagement.do?method=viewAcademicCalendarEntry&amp;rootEntryID=<bean:write name="parentEntryBean" property="rootEntry.idInternal"/>&amp;entryID=<bean:write name="parentEntryBean" property="entry.idInternal"/>&amp;begin=<bean:write name="parentEntryBean" property="beginPartialString"/>&amp;end=<bean:write name="parentEntryBean" property="endPartialString"/></bean:define>										
										
					<fr:form action="<%= createCalendarEntryURL %>">
					
						<fr:edit name="parentEntryBean" id="calendarEntryBeanWithType2" visible="false" />	
					
						<fr:create id="calendarEntryBeanWithInfo" type="<%= entryType.toString() %>" schema="<%= schemaName.toString() %>">								
							<fr:hidden slot="parentEntry" name="parentEntryBean" property="entry"/>																		
							<fr:layout name="tabular">      										  
					   			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
					   			<fr:property name="columnClasses" value=",,tdclear tderror1"/>					   								   								   	
				   			</fr:layout> 
						</fr:create>				
					
						<p>
							<html:submit><bean:message key="label.create" bundle="MANAGER_RESOURCES"/></html:submit>
							<html:cancel><bean:message key="label.cancel" bundle="MANAGER_RESOURCES"/></html:cancel>
						</p>		
						
					</fr:form>
									
				</logic:notEmpty>											
				
			</logic:notEmpty>			
		</logic:notEmpty>
	</logic:empty>	
	
	<logic:notEmpty name="entryBean">
		
		<p class="mtop05"><b><bean:message key="label.edit.academic.entry" bundle="MANAGER_RESOURCES"/></b></p>		
		<bean:define id="createCalendarEntryURL">/academicCalendarsManagement.do?method=viewAcademicCalendarEntry&amp;rootEntryID=<bean:write name="entryBean" property="rootEntry.idInternal"/>&amp;entryID=<bean:write name="entryBean" property="entry.idInternal"/>&amp;begin=<bean:write name="entryBean" property="beginPartialString"/>&amp;end=<bean:write name="entryBean" property="endPartialString"/></bean:define>			
		
		<bean:define id="entryInfoSchema" type="java.lang.String" value=""/>			
		<logic:empty name="entryBean" property="entry.parentEntry">
			<bean:define id="entryInfoSchema" value="EditAcademicCalendar"/>
		</logic:empty>
		<logic:notEmpty name="entryBean" property="entry.parentEntry">
			<bean:define id="entryInfoSchema" value="EditAcademicCalendarEntryType"/>
		</logic:notEmpty>
	
		<fr:edit name="entryBean" property="entry" schema="<%= entryInfoSchema %>" type="net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry" 
			action="<%= createCalendarEntryURL %>">
			<fr:layout name="tabular">   
				<fr:hidden slot="rootEntryDestination" name="entryBean" property="rootEntry"/>									  
	   			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
	   			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
   			</fr:layout> 
		</fr:edit>	
				
	</logic:notEmpty>
	
</logic:present>	