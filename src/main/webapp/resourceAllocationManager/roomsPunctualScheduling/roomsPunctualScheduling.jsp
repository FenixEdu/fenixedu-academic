<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/gantt-diagrams" prefix="gd" %>
<html:xhtml/>

<em><bean:message key="link.rooms.reserve.management" bundle="SOP_RESOURCES"/></em>
<h2><bean:message key="rooms.punctual.scheduling.title" bundle="SOP_RESOURCES"/></h2>

<logic:present role="role(RESOURCE_ALLOCATION_MANAGER)">

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
				<html:messages id="message" message="true" bundle="SOP_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>	
	
	<ul class="mvert15">
		<li>
			<html:link page="/roomsPunctualScheduling.do?method=prepareCreate">		
				<bean:message bundle="SOP_RESOURCES" key="label.create.room.punctual.scheduling"/>
			</html:link>
		</li>
		<li>
			<html:link page="/roomsPunctualScheduling.do?method=seeRoomsPunctualSchedulingHistory">		
				<bean:message bundle="SOP_RESOURCES" key="link.room.punctual.scheduling.history"/>
			</html:link>
		</li>
	</ul>
		
	<logic:notEmpty name="ganttDiagram">		
		<logic:notEmpty name="ganttDiagram" property="events">
		
			<fr:form action="/roomsPunctualScheduling.do?method=prepare">
				<fr:edit name="roomsPunctualSchedulingBean" schema="GanttDiagramAvailable">
					<fr:destination name="postBack" path="/roomsPunctualScheduling.do?method=prepare"/>
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 vamiddle thlight" />
						<fr:property name="columnClasses" value=",,tdclear tderror1" />
					</fr:layout>				
				</fr:edit>
			</fr:form>
		
			<logic:equal name="roomsPunctualSchedulingBean" property="ganttDiagramAvailable" value="true" >
				<p>
					<gd:ganttDiagram 
						 ganttDiagram="ganttDiagram" 						 
						 eventParameter="genericEventID"			
						 eventUrl="/resourceAllocationManager/roomsPunctualScheduling.do?method=prepareView" 					 						 
						 firstDayParameter="firstDay" 					 						 
						 weeklyViewUrl="/resourceAllocationManager/roomsPunctualScheduling.do?method=prepare" 
						 dailyViewUrl="/resourceAllocationManager/roomsPunctualScheduling.do?method=prepareViewDailyView" 
						 monthlyViewUrl="/resourceAllocationManager/roomsPunctualScheduling.do?method=prepareViewMonthlyView"						 
						 bundle="SOP_RESOURCES" 
					/>		
				</p>
			</logic:equal>
			
			<logic:equal name="roomsPunctualSchedulingBean" property="ganttDiagramAvailable" value="false" >			
				<table class="tcalendar thlight">
					<tr>
						<th><bean:message key="label.ganttDiagram.event" bundle="SOP_RESOURCES"/></th>
						<th><bean:message key="label.ganttDiagram.period" bundle="SOP_RESOURCES"/></th>
						<th><bean:message key="label.ganttDiagram.observations" bundle="SOP_RESOURCES"/></th>
					</tr>				
					<logic:iterate id="event" name="ganttDiagram" property="events">					
						<tr>					
							<td class="padded">
								<bean:define id="genericEventURL">/roomsPunctualScheduling.do?method=prepareView&amp;genericEventID=<bean:write name="event" property="externalId"/></bean:define>
								<html:link page="<%= genericEventURL %>">
									<fr:view name="event" property="title"/>
								</html:link>	
							</td>
							<td class="padded smalltxt"><bean:write name="event" property="ganttDiagramEventPeriod"/></td>
							<td class="padded smalltxt"><bean:write name="event" property="ganttDiagramEventObservations"/></td>						
						</tr>						
					</logic:iterate>				
				</table>			
			</logic:equal>
			
			<jsp:include page="legend.jsp" />
						
		</logic:notEmpty>			
	</logic:notEmpty>	
	
</logic:present>


