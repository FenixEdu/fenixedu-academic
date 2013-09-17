<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@page import="net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<logic:notEmpty name="nextEntry">
	
	<bean:define id="currentEntry" name="nextEntry"/>
		
	<logic:notEmpty name="nextEntry" property="parentEntry">
		<bean:define id="nextEntry" name="nextEntry" property="parentEntry" toScope="request"></bean:define>
		<jsp:include page="entriesCrumbs.jsp"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="currentEntry" property="parentEntry">

		>
		
	</logic:notEmpty>
	
	<%
		if (pageContext.findAttribute("calendarEntrySelected").equals(pageContext.findAttribute("currentEntry"))) { 	
	%>	
		  
		  <logic:equal name="currentEntry" property="class.simpleName" value="<%= AcademicCalendarRootEntry.class.getSimpleName() %>">		  
		  	<bean:write name="rootEntry" property="title.content"/>
		  </logic:equal>
		  <logic:notEqual name="currentEntry" property="class.simpleName" value="<%= AcademicCalendarRootEntry.class.getSimpleName() %>">	
			<bean:write name="currentEntry" property="title.content"/>		  	
		  </logic:notEqual>
		  
	<%
		} else {
	%>		
		<bean:define id="entryURL">/academicCalendarsManagement.do?method=viewAcademicCalendarEntry&amp;begin=<bean:write name="entryBean" property="beginPartialString"/>&amp;end=<bean:write name="entryBean" property="endPartialString"/>&amp;rootEntryID=<bean:write name="entryBean" property="rootEntry.externalId"/></bean:define>
		
		<logic:equal name="currentEntry" property="class.simpleName" value="<%= AcademicCalendarRootEntry.class.getSimpleName() %>">		  
		  	<html:link page="<%= entryURL %>" paramId="entryID" paramName="rootEntry" paramProperty="externalId">
				<bean:write name="rootEntry" property="title.content"/>
			</html:link>
	    </logic:equal>
		<logic:notEqual name="currentEntry" property="class.simpleName" value="<%= AcademicCalendarRootEntry.class.getSimpleName() %>">	
		    <html:link page="<%= entryURL %>" paramId="entryID" paramName="currentEntry" paramProperty="externalId">
				<bean:write name="currentEntry" property="title.content"/>
			</html:link>		  	
		</logic:notEqual>
						
	<%
		}
	%>
							
</logic:notEmpty>