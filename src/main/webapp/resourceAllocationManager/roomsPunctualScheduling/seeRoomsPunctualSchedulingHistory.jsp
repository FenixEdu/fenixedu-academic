<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/gantt-diagrams" prefix="gd" %>
<html:xhtml/>

<em><bean:message key="link.rooms.reserve.management" bundle="SOP_RESOURCES"/></em>
<h2><bean:message key="label.room.punctual.scheduling.history" bundle="SOP_RESOURCES"/></h2>

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

	<logic:notEmpty name="roomsPunctualSchedulingHistoryBean">
		
		<ul class="mvert15">
			<li>
				<html:link page="/roomsPunctualScheduling.do?method=prepare">		
					<bean:message bundle="SOP_RESOURCES" key="label.back"/>
				</html:link>
			</li>		
		</ul>
		
		<fr:form action="/roomsPunctualScheduling.do?method=seeRoomsPunctualSchedulingHistory">
			<fr:edit id="roomsPunctualSchedulingHistoryWithYearAndMonth" name="roomsPunctualSchedulingHistoryBean" schema="RoomsPunctualSchedulingHistory">
				<fr:destination name="postback" path="/roomsPunctualScheduling.do?method=seeRoomsPunctualSchedulingHistory"/>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 vamiddle thlight" />
					<fr:property name="columnClasses" value=",,tdclear tderror1" />
				</fr:layout>		
			</fr:edit>
		</fr:form>
			
		<logic:empty name="events">
			<em><bean:message key="label.empty.rooms.punctual.scheduling" bundle="SOP_RESOURCES"/></em>			
		</logic:empty>	
				
		<logic:notEmpty name="events">					
			<table class="tcalendar thlight mtop2">
				<tr>
					<th><bean:message key="label.ganttDiagram.event" bundle="SOP_RESOURCES"/></th>
					<th><bean:message key="label.ganttDiagram.period" bundle="SOP_RESOURCES"/></th>
					<th><bean:message key="label.ganttDiagram.observations" bundle="SOP_RESOURCES"/></th> 
				</tr>				
				<logic:iterate id="event" name="events">					
					<tr>
						<bean:define id="genericEventURL">/roomsPunctualScheduling.do?method=prepareView&amp;genericEventID=<bean:write name="event" property="externalId"/></bean:define>																	
						<bean:define id="tdTile" value=""/>					
						<logic:notEmpty name="event" property="description">
							<logic:notEmpty name="event" property="description.content">
								<bean:define id="tdTile" name="event" property="description.content" type="java.lang.String"/>
							</logic:notEmpty>							
						</logic:notEmpty>							
						<td class="padded" title="<%= tdTile %>">
							<html:link page="<%= genericEventURL %>">
								<fr:view name="event" property="title"/>
							</html:link>
						</td>
						<td class="padded smalltxt"><bean:write name="event" property="ganttDiagramEventPeriod"/></td>  
						<td class="padded smalltxt"><bean:write name="event" property="ganttDiagramEventObservations"/></td> 
					</tr>						
				</logic:iterate>				
			</table>						
			<jsp:include page="legend.jsp" />
		</logic:notEmpty>	
		
	</logic:notEmpty>
	
</logic:present>