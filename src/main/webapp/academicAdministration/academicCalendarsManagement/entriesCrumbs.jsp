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