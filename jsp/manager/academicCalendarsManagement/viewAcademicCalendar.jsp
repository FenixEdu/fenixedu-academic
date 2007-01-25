<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/ganttDiagrams.tld" prefix="gd" %>

<html:xhtml/>

<h2><bean:message key="academic.calendars.management.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="MANAGER">
	
	
	<style>
	.tcalendar {
	border-collapse: collapse;
	border: 1px solid #ccc;
	}
	.tcalendar th {
	border: 1px solid #ccc;
	overflow: hidden;
	}
	.tcalendar td {
	border: 1px solid #ccc;
	}
	
	.tcalendar th {
	text-align: center;
	background-color: #f5f5f5;
	background-color: #f5f5f5;
	padding: 3px 4px;
	}
	.tcalendar td {
	background-color: #fff;
	padding: 0;
	}
	.tcalendar td.padded {
	padding: 2px 6px;
	border: 1px solid #ccc;
	}
	td.padded { }
	.tdbar {
	background-color: #fed;
	}
	tr.active td {
	background-color: #fefeea;
	}
	.color555 {
	color: #555;
	}
	tr.selected td {
	background-color: #fdfdde;
	}
	</style>
	
	
	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>
	
	<logic:notEmpty name="calendarEntry">
		<bean:define id="nextEntry" name="calendarEntry" toScope="request" />
		<bean:define id="calendarEntrySelected" name="calendarEntry" type="net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry" toScope="request"/>
	</logic:notEmpty>
	<div class="mbottom2">
		<jsp:include page="entriesCrumbs.jsp"/>
	</div>
		
	<logic:notEmpty name="academicCalendar">
		
		<fr:view name="academicCalendar" schema="AcademicCalendarInfoWithoutLinks" layout="tabular">
			<fr:layout name="tabular">      			
   				<fr:property name="classes" value="tstyle4 thlight thright mvert0"/>
   			</fr:layout>
		</fr:view>		
		<p><html:link page="/academicCalendarsManagement.do?method=prepareCreateEntryForAcademicCalendar" paramId="academicCalendarID" paramName="academicCalendar" paramProperty="idInternal">
			<bean:message bundle="MANAGER_RESOURCES" key="label.insert.calendar.entry"/>
		</html:link>,	
		<html:link page="/academicCalendarsManagement.do?method=prepareEditAcademicCalendar" paramId="academicCalendarID" paramName="academicCalendar" paramProperty="idInternal">
			<bean:message bundle="MANAGER_RESOURCES" key="label.edit.academic.calendar.entry"/>
		</html:link>,		
		<html:link page="/academicCalendarsManagement.do?method=deleteAcademicCalendar" paramId="academicCalendarID" paramName="academicCalendar" paramProperty="idInternal" onclick="return confirm('Tem a certeza que deseja apagar o calendário?')">
			<bean:message bundle="MANAGER_RESOURCES" key="label.delete.academic.calendar"/>
		</html:link></p>
		
	</logic:notEmpty>		
	
	<logic:notEmpty name="calendarEntry">
		
		<fr:view name="calendarEntry" schema="AcademicCalendarEntryInfo" layout="tabular">
			<fr:layout name="tabular">      			
   				<fr:property name="classes" value="tstyle4 thlight thright mvert0"/>
   			</fr:layout>
		</fr:view>
		<p>
		<html:link page="/academicCalendarsManagement.do?method=prepareCreateEntryForAcademicCalendarEntry" paramId="academicCalendarEntryID" paramName="calendarEntry" paramProperty="idInternal">		
			<bean:message bundle="MANAGER_RESOURCES" key="label.insert.calendar.entry"/>
		</html:link>,	
		<html:link page="/academicCalendarsManagement.do?method=prepareEditAcademicCalendarEntry" paramId="academicCalendarEntryID" paramName="calendarEntry" paramProperty="idInternal">		
			<bean:message bundle="MANAGER_RESOURCES" key="label.edit.academic.calendar.entry"/>
		</html:link>,
		<html:link page="/academicCalendarsManagement.do?method=deleteAcademicCalendarEntry" paramId="academicCalendarEntryID" paramName="calendarEntry" paramProperty="idInternal" onclick="return confirm('Tem a certeza que deseja apagar a entrada?')">
			<bean:message bundle="MANAGER_RESOURCES" key="label.delete.academic.calendar"/>
		</html:link>
		</p>	
			
	</logic:notEmpty>		
	
	<p>
		<gd:ganttDiagram ganttDiagram="ganttDiagram" eventParameter="academicCalendarEntryID" eventUrl="/manager/academicCalendarsManagement.do?method=viewAcademicCalendarEntry" bundle="MANAGER_RESOURCES"/>		
	</p>
	
</logic:present>	