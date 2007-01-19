<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="org.joda.time.Days"%>
<%@page import="org.joda.time.DateTime"%>
<%@page import="net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry"%>
<%@page import="java.util.Locale"%>

<html:xhtml/>

<h2><bean:message key="academic.calendars.management.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="MANAGER">
	
	<style>
		.tcalendar {
		border-collapse: collapse;
		}
		.tcalendar th, .tcalendar td {
		border: 1px solid #ddd;	
		}
		.tcalendar th {
		text-align: center;
		background-color: #f5f5f5;
		padding: 3px 4px;
		}
		.tcalendar td {
		background-color: #fff;
		padding: 0;
		}
		.tcalendar td.padded {
		padding: 2px 6px;
		}
		.tdbar {
		background-color: #fed;
		border-top: 2px solid #fed;
		border-bottom: 2px solid #fed;
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
	<logic:notEmpty name="entries">
		
		<table class="tcalendar thlight">
			<tr>
				<bean:size name="months" id="monthsSize"/>
				<th rowspan="2"><bean:message key="label.type" bundle="MANAGER_RESOURCES"/></th>
				<th rowspan="2"><bean:message key="label.academicCalendar.title" bundle="MANAGER_RESOURCES"/> </th>
				<th colspan="<%= monthsSize.toString() %>"><bean:message key="label.year" bundle="MANAGER_RESOURCES"/></th>
			</tr>
			<tr>
				<%
					Locale locale = new Locale("pt", "PT");
				%>
				<logic:iterate id="month" name="months" type="org.joda.time.DateTime">										
					<th><%= month.toString("MMM", locale) %></th>		
				</logic:iterate>	
			</tr>	
			
			<logic:iterate id="entryInfo" name="entries">										    				
			    <bean:define id="entry" name="entryInfo" property="value" type="net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry"/>
			    <bean:define id="entryIndex" name="entryInfo" property="key" type="java.lang.Integer"/>			    
				<bean:define id="title" type="java.lang.String"><bean:write name="entry" property="presentationTimeInterval"/></bean:define>					
				<bean:define id="paddingStyle" type="java.lang.String">padding-left:<%= (entryIndex.intValue() * 15) %>px;</bean:define>
				<bean:define id="entryURL">/academicCalendarsManagement.do?method=viewAcademicCalendarEntry&amp;academicCalendarEntryID=<bean:write name="entry" property="idInternal"/></bean:define>
				
				<logic:notEmpty name="calendarEntry">					
					<%
						if(entry.equals(pageContext.findAttribute("calendarEntrySelected"))){						    												    
					%>				
						<tr title="<%= title %>" class="selected">				
					<%
						} else { 
					%>
						<tr title="<%= title %>">				
					<%
						}
					%>
				</logic:notEmpty>																								
				<logic:notEmpty name="academicCalendar">
					<tr title="<%= title %>">
				</logic:notEmpty>
										
					<td class="padded">						
						<span style="<%= paddingStyle %>">														
							<bean:write name="entry" property="type"/>							
						</span>
					</td>
											
					<td class="padded">
						<html:link page="<%= entryURL %>">
							<bean:write name="entry" property="title.content"/>
						</html:link>		
					</td>																				
																							
					<logic:iterate id="month" name="months" type="org.joda.time.DateTime">					
						
						<%							
							String styleString = "", classString = "", spaceString = "";		
							DateTime firstDayOfMonth = month.withDayOfMonth(1);
							DateTime lastDayOfMonth = month.plusMonths(1).withDayOfMonth(1).minusDays(1);										
							int monthNumberOfDays = Days.daysBetween(firstDayOfMonth, lastDayOfMonth).getDays() + 1;
							int entryDays = 0, remainingDays = 0;
							
							
							// Started in this month
							if(entry.getBegin().getMonthOfYear() == month.getMonthOfYear() && 
								entry.getBegin().getYear() == month.getYear()){																							    
							    
							    // Ended in this month
							    if(entry.getEnd().getMonthOfYear() == month.getMonthOfYear() && 
									entry.getEnd().getYear() == month.getYear()){										
									
									// Started in first day of this month
									if(entry.getBegin().getDayOfMonth() == 1){									    
									  
									    // Ended in the last day of this month
									    if(entry.getEnd().getDayOfMonth() == monthNumberOfDays){										
											classString = "tdbar";
											spaceString = "&nbsp;";
											styleString = "margin: 0 0 0 0; width: " + monthNumberOfDays + "px;";
									    }									    
									    
									    // Ended before last day of this month
									    else {		
											entryDays = Days.daysBetween(entry.getBegin(), entry.getEnd()).getDays() + 1;
											remainingDays = monthNumberOfDays - entryDays;
											classString = "tdbar";
											spaceString = "&nbsp;";
											styleString = "margin: 0 " + remainingDays + "px 0 0; width: " + entryDays + "px;";											
									    }																		
									}									
									
									// Started after first day of this month    								
									else {									    
									
									    // Ended in the last day of this month
									    if(entry.getEnd().getDayOfMonth() == monthNumberOfDays){		
										
											entryDays = Days.daysBetween(entry.getBegin(), lastDayOfMonth).getDays() + 1;
											remainingDays = monthNumberOfDays - entryDays;	
											classString = "tdbar";
											spaceString = "&nbsp;";
											styleString = "margin: 0 0 0 " + remainingDays + "px; width: " + entryDays + "px;";
									    }									    
									    
									    // Ended before last day of this month
									    else {	
											entryDays = Days.daysBetween(entry.getBegin(), entry.getEnd()).getDays() + 1;											
											classString = "tdbar";
											spaceString = "&nbsp;";
											styleString = "margin: 0 " + entryDays + "px 0 " + entryDays + "px; width: " + entryDays + "px;";											
									    }									    									    
									}																																
							    } 							    
								
							    // Ended after this month
							    else { 									
									
									// Started in first day of this month
									if(entry.getBegin().getDayOfMonth() == 1) {	
									    
									    classString = "tdbar";
									    spaceString = "&nbsp;";
										styleString = "margin: 0 0 0 0; width: " + monthNumberOfDays + "px;";									    									
									}									
									
									// Started after first day of this month      
									else {	
									    entryDays = Days.daysBetween(entry.getBegin(), lastDayOfMonth).getDays() + 1;
										remainingDays = monthNumberOfDays - entryDays;	
									    spaceString = "&nbsp;";
										classString = "tdbar";
										styleString = "margin: 0 0 0 " + remainingDays + "px; width: " + entryDays + "px;";									    
									}																
							    }							 
							
							// Not Started in this month    
							} else { 
							    
							    // Started before this month
							    if(entry.getBegin().getYear() < month.getYear() || 
								    (entry.getBegin().getYear() == month.getYear() && 
									    entry.getBegin().getMonthOfYear() < month.getMonthOfYear())) {								
									
									// Ended after this month
									if(entry.getEnd().getYear() > month.getYear() || 
										    (entry.getEnd().getYear() == month.getYear() && 
											    entry.getEnd().getMonthOfYear() > month.getMonthOfYear())){
									    									    
									    classString = "tdbar";
									    spaceString = "&nbsp;";
									    styleString = "margin: 0 0 0 0; width: " + monthNumberOfDays + "px;";
									} 
									
									// Not Ended after this month
									else {	
									    
									    //Ended in this month
									    if(entry.getEnd().getMonthOfYear() == month.getMonthOfYear() && 
										    entry.getEnd().getYear() == month.getYear()){
									
											// Ended in the last day of this month
										    if(entry.getEnd().getDayOfMonth() == monthNumberOfDays){										
												classString = "tdbar";
												spaceString = "&nbsp;";
												styleString = "margin: 0 0 0 0; width: " + monthNumberOfDays + "px;";
										    }									    
										    
										    // Ended before last day of this month
										    else {		
												entryDays = Days.daysBetween(firstDayOfMonth, entry.getEnd()).getDays() + 1;
												remainingDays = monthNumberOfDays - entryDays;
												classString = "tdbar";
												spaceString = "&nbsp;";
												styleString = "margin: 0 " + remainingDays + "px 0 0; width: " + entryDays + "px;";											
										    }													
									    } 
									    
									    // Ended before this month
									    else {
	 										classString = "tdbar";
										    styleString = "margin: 0 0 0 " + monthNumberOfDays + "px; width: 0px;";	
									    }									    									    									   																								    									    									    									    										    
									}																																
								// Started after this month
							    } else {						
									classString = "tdbar";
									styleString = "margin: 0 0 0 " + monthNumberOfDays + "px; width: 0px;";																
							    }
							}						
						%>
																														
						<td><div style="<%= styleString %>" class="<%= classString %>"><%= spaceString %></div></td>											
					</logic:iterate>																		
				</tr>
				
			</logic:iterate>									
		</table>	
	</logic:notEmpty>
	</p>
	
</logic:present>	