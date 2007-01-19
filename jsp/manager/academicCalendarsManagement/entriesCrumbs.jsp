<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:notEmpty name="nextEntry">
	
	<bean:define id="currentEntry" name="nextEntry"/>
	
	<logic:empty name="nextEntry" property="parentEntry">		
		<html:link page="/academicCalendarsManagement.do?method=viewAcademicCalendar" paramId="academicCalendarID" paramName="nextEntry" paramProperty="academicCalendar.idInternal">
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
		<html:link page="/academicCalendarsManagement.do?method=viewAcademicCalendarEntry" paramId="academicCalendarEntryID" paramName="currentEntry" paramProperty="idInternal">
			<bean:write name="currentEntry" property="title.content"/>
		</html:link>		
	<%
		}
	%>
							
</logic:notEmpty>