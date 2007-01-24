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
	
	<logic:empty name="academicCalendar">
		<p class="mtop05"><b><bean:message key="label.create.academic.calendar" bundle="MANAGER_RESOURCES"/></b></p>
		<fr:create type="net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendar" schema="CreateAcademicCalendar"
			action="/academicCalendarsManagement.do?method=prepareChooseCalendar" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop0 mbottom1"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>		
			</fr:layout>	
		</fr:create>
	</logic:empty>	
	
	<logic:notEmpty name="academicCalendar">
		<p class="mtop05"><b><bean:message key="label.edit.academic.calendar" bundle="MANAGER_RESOURCES"/></b></p>
		<bean:define id="viewCalendarURL">/academicCalendarsManagement.do?method=viewAcademicCalendar&amp;academicCalendarID=<bean:write name="academicCalendar" property="idInternal"/></bean:define>
		<fr:edit schema="EditAcademicCalendar" type="net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendar" name="academicCalendar"
			action="<%= viewCalendarURL %>">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop0 mbottom1"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>		
			</fr:layout>			
		</fr:edit>
	</logic:notEmpty>
					
</logic:present>	