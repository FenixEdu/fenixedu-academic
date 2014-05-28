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
	
	