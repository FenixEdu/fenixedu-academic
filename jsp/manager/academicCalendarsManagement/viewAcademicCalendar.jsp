<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/ganttDiagrams.tld" prefix="gd" %>

<html:xhtml/>

<h2><bean:message key="academic.calendars.management.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="MANAGER">
		
	<style type="text/css">
	.tcalendar {
	border-collapse: collapse;
	/*border: 1px solid #ccc;*/
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
	background-color: #a3d1d9;
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
	td.tcalendarlinks {
	padding: 0.5em 0;
	border-bottom: none;
	border-left: none;
	border-right: none;
	}
	td.tcalendarlinks span { color: #888; }
	td.tcalendarlinks span a { color: #888; }
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
		
	<logic:notEmpty name="entryBean">
		
		<logic:notEmpty name="entryBean" property="entry">								
			<bean:define id="nextEntry" name="entryBean" property="entry" toScope="request" />
			<bean:define id="rootEntry" name="entryBean" property="rootEntry" toScope="request" />
			<bean:define id="calendarEntrySelected" name="entryBean" property="entry" type="net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry" toScope="request"/>
		</logic:notEmpty>
							
		<div class="mbottom2">
			<jsp:include page="entriesCrumbs.jsp"/>
		</div>		
					
		<bean:define id="rootEntryID" name="rootEntry" property="idInternal" />			
		<bean:define id="beginDate" name="entryBean" property="beginPartialString" />
		<bean:define id="endDate" name="entryBean" property="endPartialString" />												
		
		<fr:edit id="datesToDisplayID" name="entryBean" schema="ChooseDatesToViewAcademicCalendarSchema" action="/academicCalendarsManagement.do?method=viewAcademicCalendar">
			<fr:layout name="tabular">      			
   				<fr:property name="classes" value="tstyle4 thlight thright mvert0"/>   				
   			</fr:layout>
   			<fr:destination name="cancel" path="/academicCalendarsManagement.do?method=prepareChooseCalendar"/>
		</fr:edit>						
				
		
		<logic:notEmpty name="entryBean">
						
			<bean:define id="entryInfoSchema" type="java.lang.String" value=""/>			
			<logic:empty name="entryBean" property="entry.parentEntry">
				<bean:define id="entryInfoSchema" value="AcademicCalendarInfoWithoutLinks"/>
			</logic:empty>
			<logic:notEmpty name="entryBean" property="entry.parentEntry">
				<bean:define id="entryInfoSchema"><bean:write name="entryBean" property="entry.class.simpleName"/>EntryInfo</bean:define>													
			</logic:notEmpty>
						
			<p class="mtop2">
				<fr:view name="entryBean" property="entry" schema="<%= entryInfoSchema %>" layout="tabular">
					<fr:layout name="tabular">      			
		   				<fr:property name="classes" value="tstyle4 thlight thright mvert0"/>
		   			</fr:layout>
				</fr:view>
			</p>		
			
			<p>				
				<html:link page="<%= "/academicCalendarsManagement.do?method=prepareCreateEntry&amp;rootEntryID=" + rootEntryID + "&amp;begin=" + beginDate + "&amp;end=" + endDate %>" paramId="entryID" paramName="entryBean" paramProperty="entry.idInternal">
					<bean:message bundle="MANAGER_RESOURCES" key="label.insert.calendar.entry"/>
				</html:link>,	
				<html:link page="<%= "/academicCalendarsManagement.do?method=prepareEditEntry&amp;rootEntryID=" + rootEntryID + "&amp;begin=" + beginDate + "&amp;end=" + endDate %>" paramId="entryID" paramName="entryBean" paramProperty="entry.idInternal">
					<bean:message bundle="MANAGER_RESOURCES" key="label.edit.academic.calendar.entry"/>
				</html:link>,		
				<html:link page="<%= "/academicCalendarsManagement.do?method=deleteEntry&amp;rootEntryID=" + rootEntryID + "&amp;begin=" + beginDate + "&amp;end=" + endDate %>" paramId="entryID" paramName="entryBean" paramProperty="entry.idInternal" onclick="return confirm('Tem a certeza que deseja apagar o calendário?')">
					<bean:message bundle="MANAGER_RESOURCES" key="label.delete.academic.calendar"/>
				</html:link>
			</p>
			
		</logic:notEmpty>					
		
		<logic:notEmpty name="ganttDiagram">
			<p>										
				<gd:ganttDiagram ganttDiagram="ganttDiagram" eventParameter="entryID" eventUrl="<%= "/manager/academicCalendarsManagement.do?method=viewAcademicCalendarEntry&amp;rootEntryID=" + rootEntryID + "&amp;begin=" + beginDate + "&amp;end=" + endDate %>" bundle="MANAGER_RESOURCES"/>		
			</p>
		</logic:notEmpty>
		
	</logic:notEmpty>	
</logic:present>	