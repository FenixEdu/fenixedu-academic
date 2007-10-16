<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:notEmpty name="nextEntry">
	
	<bean:define id="currentEntry" name="nextEntry"/>
	
	<logic:empty name="beginDate">
		<bean:define id="beginDate" name="datesToDisplayBean" property="beginPartialString"/>
	</logic:empty>
	<logic:empty name="endDate">
		<bean:define id="endDate" name="datesToDisplayBean" property="endPartialString"/>	
	</logic:empty>
	
	<logic:empty name="nextEntry" property="parentEntry">
		<bean:define id="calendarURL">/academicCalendarsManagement.do?method=viewAcademicCalendar&amp;begin=<bean:write name="beginDate"/>&amp;end=<bean:write name="endDate"/></bean:define>		
		<html:link page="<%= calendarURL %>" paramId="academicCalendarID" paramName="nextEntry" paramProperty="academicCalendar.idInternal">
			<bean:write name="nextEntry" property="academicCalendar.title.content"/>
		</html:link>	
	</logic:empty>
	
	<logic:notEmpty name="nextEntry" property="parentEntry">
		<bean:define id="nextEntry" name="nextEntry" property="parentEntry" toScope="request"></bean:define>
		<jsp:include page="entriesCrumbs.jsp"/>
	</logic:notEmpty>
				
	>
	<%
		if (pageContext.findAttribute("calendarEntrySelected").equals(pageContext.findAttribute("currentEntry"))) { 	
	%>	
		<bean:write name="currentEntry" property="title.content"/>		
	<%
		} else {
	%>
		<bean:define id="entryURL">/academicCalendarsManagement.do?method=viewAcademicCalendarEntry&amp;begin=<bean:write name="beginDate"/>&amp;end=<bean:write name="endDate"/></bean:define>
		<html:link page="<%= entryURL %>" paramId="academicCalendarEntryID" paramName="currentEntry" paramProperty="idInternal">
			<bean:write name="currentEntry" property="title.content"/>
		</html:link>		
	<%
		}
	%>
							
</logic:notEmpty>