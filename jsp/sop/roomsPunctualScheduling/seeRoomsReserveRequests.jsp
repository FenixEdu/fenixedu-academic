<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>
<html:xhtml/>

<em><bean:message key="link.rooms.reserve.management" bundle="SOP_RESOURCES"/></em>
<h2><bean:message key="rooms.reserve.title" bundle="SOP_RESOURCES"/></h2>

<logic:present role="TIME_TABLE_MANAGER">

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="SOP_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>	

	<br/>
	<b><bean:message key="label.my.rooms.reserve.requests" bundle="SOP_RESOURCES"/>:</b>
	<logic:empty name="personRequests">
		<p><em><bean:message key="label.empty.rooms.reserves.requests" bundle="SOP_RESOURCES"/></em></p>
	</logic:empty>	
	<logic:notEmpty name="personRequests">
		<table class="tstyle1">			
			<tr>
				<th><bean:message key="label.rooms.reserve.identification" bundle="SOP_RESOURCES"/></th>		
				<th><bean:message key="label.rooms.reserve.instant" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.order" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.requestor" bundle="SOP_RESOURCES"/></th>				
				<th><bean:message key="label.rooms.reserve.periods" bundle="SOP_RESOURCES"/></th>	
			</tr>
			<logic:iterate id="myRequest" name="personRequests">					
				<tr>
					<td>
						<bean:write name="myRequest" property="identification"/>						
					</td>
					<td>
						<bean:write name="myRequest" property="presentationInstant"/>						
					</td>
					<td>
						<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserveRequest&amp;reserveRequestID=<bean:write name="myRequest" property="idInternal"/></bean:define>
						<html:link page="<%= seeReserveURL %>">
							<bean:write name="myRequest" property="subject"/>
						</html:link>										
					</td>						
					<td>
						<bean:define id="requestorName" name="myRequest" property="requestor.name" type="java.lang.String"/>						
						<span title="<%= requestorName %>"><bean:write name="myRequest" property="requestor.username"/></span>						
					</td>										
					<td>					
						<logic:notEmpty name="myRequest" property="genericEvents">
							<logic:iterate id="genericEvent" name="myRequest" property="genericEvents">
								<bean:write name="genericEvent" property="eventPeriodForGanttDiagram"/>
								-								
								<bean:write name="genericEvent" property="eventObservationsForGanttDiagram"/>								
								<br/>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="myRequest" property="genericEvents">
							-
						</logic:empty>
					</td>						
				</tr>
			</logic:iterate>		
		</table>
	</logic:notEmpty>
	
	<b><bean:message key="label.opened.rooms.reserve.requests" bundle="SOP_RESOURCES"/>:</b>
	<logic:empty name="openedRequests">
		<p><em><bean:message key="label.empty.rooms.reserves.requests" bundle="SOP_RESOURCES"/></em></p>
	</logic:empty>
	<logic:notEmpty name="openedRequests">
		<table class="tstyle1">			
			<tr>
				<th><bean:message key="label.rooms.reserve.identification" bundle="SOP_RESOURCES"/></th>		
				<th><bean:message key="label.rooms.reserve.instant" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.order" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.requestor" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.employee" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.periods" bundle="SOP_RESOURCES"/></th>	
			</tr>
			<logic:iterate id="openedRequest" name="openedRequests">					
				<tr>
					<td>
						<bean:write name="openedRequest" property="identification"/>						
					</td>				
					<td>
						<bean:write name="openedRequest" property="presentationInstant"/>						
					</td>
					<td>
						<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserveRequest&amp;reserveRequestID=<bean:write name="openedRequest" property="idInternal"/></bean:define>
						<html:link page="<%= seeReserveURL %>">
							<bean:write name="openedRequest" property="subject"/>
						</html:link>										
					</td>	
					<td>
						<bean:define id="requestorName" name="openedRequest" property="requestor.name" type="java.lang.String"/>						
						<span title="<%= requestorName %>"><bean:write name="openedRequest" property="requestor.username"/></span>						
					</td>					
					<td>
						<logic:notEmpty name="openedRequest" property="owner">
							<bean:define id="ownerName" name="openedRequest" property="owner.name" type="java.lang.String"/>						
							<span title="<%= ownerName %>"><bean:write name="openedRequest" property="owner.username"/></span>	
						</logic:notEmpty>
						<logic:empty name="openedRequest" property="owner">
						-
						</logic:empty>	
					</td>					
					<td>					
						<logic:notEmpty name="openedRequest" property="genericEvents">
							<logic:iterate id="genericEvent" name="openedRequest" property="genericEvents">
								<bean:write name="genericEvent" property="eventPeriodForGanttDiagram"/>
								-								
								<bean:write name="genericEvent" property="eventObservationsForGanttDiagram"/>								
								<br/>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="openedRequest" property="genericEvents">
							-
						</logic:empty>
					</td>						
				</tr>
			</logic:iterate>		
		</table>
	</logic:notEmpty>

	<b><bean:message key="label.new.rooms.reserve.requests" bundle="SOP_RESOURCES"/>:</b>
	<logic:empty name="newRequests">
		<p><em><bean:message key="label.empty.rooms.reserves.requests" bundle="SOP_RESOURCES"/></em></p>
	</logic:empty>
	<logic:notEmpty name="newRequests">
		<table class="tstyle1">			
			<tr>
				<th><bean:message key="label.rooms.reserve.identification" bundle="SOP_RESOURCES"/></th>		
				<th><bean:message key="label.rooms.reserve.instant" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.order" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.requestor" bundle="SOP_RESOURCES"/></th>								
				<th><bean:message key="label.rooms.reserve.action" bundle="SOP_RESOURCES"/></th>	
			</tr>
			<logic:iterate id="newRequest" name="newRequests">					
				<tr>
					<td>
						<bean:write name="newRequest" property="identification"/>						
					</td>				
					<td>
						<bean:write name="newRequest" property="presentationInstant"/>						
					</td>
					<td>
						<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserveRequest&amp;reserveRequestID=<bean:write name="newRequest" property="idInternal"/></bean:define>
						<html:link page="<%= seeReserveURL %>">
							<bean:write name="newRequest" property="subject"/>
						</html:link>										
					</td>	
					<td>
						<bean:define id="requestorName" name="newRequest" property="requestor.name" type="java.lang.String"/>						
						<span title="<%= requestorName %>"><bean:write name="newRequest" property="requestor.username"/></span>						
					</td>															
					<td>
						<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=openRequest&amp;reserveRequestID=<bean:write name="newRequest" property="idInternal"/></bean:define>
						<html:link page="<%= seeReserveURL %>">
							<bean:message key="label.open.rooms.reserve.request" bundle="SOP_RESOURCES"/>
						</html:link>											
					</td>						
				</tr>
			</logic:iterate>		
		</table>
	</logic:notEmpty>
				
	<b><bean:message key="label.resolved.rooms.reserve.requests" bundle="SOP_RESOURCES"/>:</b>	
	<logic:empty name="resolvedRequests">
		<p><em><bean:message key="label.empty.rooms.reserves.requests" bundle="SOP_RESOURCES"/></em></p>
	</logic:empty>
	<logic:notEmpty name="resolvedRequests">						
		<logic:notEqual name="numberOfPages" value="1">
			<p>
				<bean:message key="label.pages" bundle="SOP_RESOURCES"/>:
				<cp:collectionPages url="/sop/roomsReserveManagement.do?method=seeRoomsReserveRequests" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>
			</p>
		</logic:notEqual>
		<table class="tstyle1">					
			<tr>
				<th><bean:message key="label.rooms.reserve.identification" bundle="SOP_RESOURCES"/></th>		
				<th><bean:message key="label.rooms.reserve.instant" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.order" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.requestor" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.employee" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.periods" bundle="SOP_RESOURCES"/></th>	
				<th><bean:message key="label.rooms.reserve.action" bundle="SOP_RESOURCES"/></th>	
			</tr>
			<logic:iterate id="resolvedRequest" name="resolvedRequests">					
				<tr>
					<td>
						<bean:write name="resolvedRequest" property="identification"/>						
					</td>					
					<td>
						<bean:write name="resolvedRequest" property="presentationInstant"/>						
					</td>
					<td>
						<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserveRequest&amp;reserveRequestID=<bean:write name="resolvedRequest" property="idInternal"/></bean:define>
						<html:link page="<%= seeReserveURL %>">
							<bean:write name="resolvedRequest" property="subject"/>
						</html:link>										
					</td>	
					<td>
						<bean:define id="requestorName" name="resolvedRequest" property="requestor.name" type="java.lang.String"/>						
						<span title="<%= requestorName %>"><bean:write name="resolvedRequest" property="requestor.username"/></span>						
					</td>					
					<td>
						<logic:notEmpty name="resolvedRequest" property="owner">
							<bean:define id="ownerName" name="resolvedRequest" property="owner.name" type="java.lang.String"/>						
							<span title="<%= ownerName %>"><bean:write name="resolvedRequest" property="owner.username"/></span>	
						</logic:notEmpty>
						<logic:empty name="resolvedRequest" property="owner">
						-
						</logic:empty>			
					</td>
					<td>					
						<logic:notEmpty name="resolvedRequest" property="genericEvents">
							<logic:iterate id="genericEvent" name="resolvedRequest" property="genericEvents">
								<bean:write name="genericEvent" property="eventPeriodForGanttDiagram"/>
								-								
								<bean:write name="genericEvent" property="eventObservationsForGanttDiagram"/>								
								<br/>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="resolvedRequest" property="genericEvents">
							-
						</logic:empty>
					</td>	
					<td>
						<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=reOpenRequest&amp;reserveRequestID=<bean:write name="resolvedRequest" property="idInternal"/></bean:define>
						<html:link page="<%= seeReserveURL %>">
							<bean:message key="label.reopen.rooms.reserve.request" bundle="SOP_RESOURCES"/>
						</html:link>											
					</td>							
				</tr>
			</logic:iterate>		
		</table>
	</logic:notEmpty>
	
	<jsp:include page="legend.jsp" />
	
</logic:present>

