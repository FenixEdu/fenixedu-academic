<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="academic.calendars.management.title" bundle="MANAGER_RESOURCES"/></h2>


	
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
				
			<fr:hasMessages for="createdEntryBeanID" type="conversion">
				<p>
					<span class="error0">			
						<fr:message for="createdEntryBeanID" show="message"/>
					</span>
				</p>
			</fr:hasMessages>						
				
			<logic:notEmpty name="parentEntryBean" property="rootEntry">	
				<p class="mtop15"><b><bean:message key="label.create.academic.entry" bundle="MANAGER_RESOURCES"/></b></p>
			</logic:notEmpty>
			<logic:empty name="parentEntryBean" property="rootEntry">
				<p class="mtop15"><b><bean:message key="label.insert.academic.calendar" bundle="MANAGER_RESOURCES"/></b></p>	
			</logic:empty>
										
			<%-- Choose Entry Type --%>																								
			<logic:notEmpty name="parentEntryBean" property="rootEntry">																																			
				<fr:form action="/academicCalendarsManagement.do?method=chooseCalendarEntryTypePostBack">
					<fr:edit id="calendarEntryBeanWithType" name="parentEntryBean" schema="ChooseAcademicCalendarEntryType">
						<fr:destination name="postBack" path="/academicCalendarsManagement.do?method=chooseCalendarEntryTypePostBack"/>
						<fr:layout name="tabular">      										  
				   			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
			     		</fr:layout> 
					</fr:edit>						
				</fr:form>							
			</logic:notEmpty>
			
			<%-- Fill Info --%>
			<logic:notEmpty name="parentEntryBean" property="type">																		
																						
				<logic:notEmpty name="parentEntryBean" property="entry">																						
					<p class="mtop15"><b><bean:message key="label.create.where" bundle="MANAGER_RESOURCES"/>:</b> <bean:write name="parentEntryBean" property="entry.title.content"/> (<bean:write name="parentEntryBean" property="entry.presentationTimeInterval"/>)</p>													
				</logic:notEmpty>
								
				<bean:define id="schemaName">Create<bean:write name="parentEntryBean" property="type.simpleName"/>CalendarEntryType</bean:define>										
					
				<fr:form action="/academicCalendarsManagement.do">											
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" name="academicCalendarsManagementForm" value="createEntry"/>					
					<fr:edit id="createdEntryBeanID" name="parentEntryBean" schema="<%= schemaName %>">
						<fr:layout name="tabular">      										  
					   		<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
					   		<fr:property name="columnClasses" value=",,tdclear tderror1"/>					   								   								   	
				   		</fr:layout> 				   		
					</fr:edit>												
					<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES"/></html:submit>					
					<logic:notEmpty name="parentEntryBean" property="rootEntry">
						<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='gotBackToViewEntry';this.form.submit();"><bean:message key="button.cancel" bundle="MANAGER_RESOURCES"/></html:cancel>
					</logic:notEmpty>					
					<logic:empty name="parentEntryBean" property="rootEntry">
						<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='prepareChooseCalendar';this.form.submit();"><bean:message key="button.cancel" bundle="MANAGER_RESOURCES"/></html:cancel>
					</logic:empty>
				</fr:form>
																													
			</logic:notEmpty>			
		</logic:notEmpty>
	</logic:empty>	
	
	
	<%-- Edit Entry --%>
	<logic:notEmpty name="entryBean">
		
		<p class="mtop05"><b><bean:message key="label.edit.academic.entry" bundle="MANAGER_RESOURCES"/></b></p>		
						
		<bean:define id="schemaName">Edit<bean:write name="entryBean" property="type.simpleName"/>CalendarEntryType</bean:define>			
		
		<fr:hasMessages for="editedEntryBeanID" type="conversion">
			<p>
				<span class="error0">			
					<fr:message for="editedEntryBeanID" show="message"/>
				</span>
			</p>
		</fr:hasMessages>
		
		<fr:form action="/academicCalendarsManagement.do">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" name="academicCalendarsManagementForm" value="editEntry"/>							
			<fr:edit id="editedEntryBeanID" name="entryBean" schema="<%= schemaName %>">
				<fr:layout name="tabular">   													 
		   			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
		   			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	   			</fr:layout> 
			</fr:edit>	
			<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES"/></html:submit>	
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='gotBackToViewEntry';this.form.submit();"><bean:message key="button.cancel" bundle="MANAGER_RESOURCES"/></html:cancel>				
		</fr:form>		
		
	</logic:notEmpty>
	
	