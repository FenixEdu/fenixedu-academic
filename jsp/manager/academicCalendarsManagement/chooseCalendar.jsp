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

	<p class="mtop05"><b><bean:message key="label.academic.calendars" bundle="MANAGER_RESOURCES"/></b></p>
	
	<p><html:link page="/prepareCreateAcademicCalendar.do">
		<bean:message bundle="MANAGER_RESOURCES" key="label.insert.academic.calendar"/>
	</html:link></p>
	
	<logic:notEmpty name="academicCalendars">
		<fr:view schema="AcademicCalendarInfo" name="academicCalendars">
			<fr:layout name="tabular">      			
	   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>	   			   				   			            	                                                  
	    	</fr:layout>
		</fr:view>					
	</logic:notEmpty>
		
	<logic:empty name="academicCalendars">
		<p class="mtop05"><em><bean:message key="label.empty.academic.calendars" bundle="MANAGER_RESOURCES"/>.</em></p>				
	</logic:empty>			
	
</logic:present>	