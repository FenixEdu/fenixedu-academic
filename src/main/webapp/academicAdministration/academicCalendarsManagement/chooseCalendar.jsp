<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="title.academic.calendars.management" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


	
	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>

	<p class="mtop05"><b><bean:message key="label.academic.calendars" bundle="ACADEMIC_OFFICE_RESOURCES"/></b></p>
	
	<p><html:link page="/academicCalendarsManagement.do?method=prepareCreateAcademicCalendar">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.academic.calendars.insert"/>
	</html:link></p>
	
	<logic:notEmpty name="academicCalendars">
		<fr:view schema="AcademicCalendarInfo" name="academicCalendars">
			<fr:layout name="tabular">      			
	   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>	   			   				   			            	                                                  
	    	</fr:layout>
		</fr:view>					
	</logic:notEmpty>
		
	<logic:empty name="academicCalendars">
		<p class="mtop05"><em><bean:message key="label.empty.academic.calendars" bundle="ACADEMIC_OFFICE_RESOURCES"/>.</em></p>				
	</logic:empty>			
	
	