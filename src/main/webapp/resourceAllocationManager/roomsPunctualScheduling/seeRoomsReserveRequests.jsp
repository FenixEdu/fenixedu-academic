<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>
<html:xhtml/>

<em><bean:message key="link.rooms.reserve.management" bundle="SOP_RESOURCES"/></em>
<h2><bean:message key="rooms.reserve.title" bundle="SOP_RESOURCES"/></h2>

<logic:present role="RESOURCE_ALLOCATION_MANAGER">

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="SOP_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>	
	<bean:define id="person" name="USER_SESSION_ATTRIBUTE" property="user.person" type="net.sourceforge.fenixedu.domain.Person"/>
	<html:form action="roomsReserveManagement.do?method=seeSpecificRequest" >
		<table>
			<tr>
				<td><bean:message key="label.search.request" bundle="SOP_RESOURCES"/>:</td>
				<td><input type="text" name="requestID" size="6"/></td>
				<td><input type="submit" value="Procurar"/></td>
			</tr>
		</table>
	</html:form>

	<logic:present name="specificRequest">
			<p class="mtop15 mbottom05"><b><bean:message key="label.search.request.result" bundle="SOP_RESOURCES"/></b></p>		
			<table class="tstyle1 thlight mtop025 mbottom05">			
			<tr>
				<th><bean:message key="label.rooms.reserve.identification" bundle="SOP_RESOURCES"/></th>		
				<th><bean:message key="label.rooms.reserve.instant" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.order" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.requestor" bundle="SOP_RESOURCES"/></th>	
				<th><bean:message key="label.rooms.reserve.number.of.new.comments" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.periods" bundle="SOP_RESOURCES"/></th>					
				<th><bean:message key="label.rooms.reserve.action" bundle="SOP_RESOURCES"/></th>	
			</tr>
			<bean:define id="myRequest" name="specificRequest" type="net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest"/>
			<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserveRequest&amp;reserveRequestID=<bean:write name="myRequest" property="externalId"/></bean:define>
				<tr>
					<td class="acenter">
						<html:link page="<%= seeReserveURL %>">
							<bean:write name="myRequest" property="identification"/>
						</html:link>						
					</td>
					<td class="nowrap smalltxt">
						<bean:write name="myRequest" property="presentationInstant"/>						
					</td>
					<td style="width: 250px;">						
						<html:link page="<%= seeReserveURL %>">
							<bean:write name="myRequest" property="subject"/>
						</html:link>								
					</td>						
					<td class="acenter">
						<bean:define id="requestorName" name="myRequest" property="requestor.name" type="java.lang.String"/>						
						<acronym title="<%= requestorName %>"><bean:write name="myRequest" property="requestor.username"/></acronym>
					</td>
					<td class="acenter">	
						<% Integer numOfNewComments = myRequest.getNumberOfNewComments(person);	%>
						<%= numOfNewComments.toString() %>
					</td>

					<td class="acenter smalltxt nowrap">					
						<logic:notEmpty name="myRequest" property="genericEvents">
							<bean:message key="label.yes.capitalized" bundle="SOP_RESOURCES"/>
						</logic:notEmpty>
						<logic:empty name="myRequest" property="genericEvents">
							-
						</logic:empty>
					</td>

					<td class="nowrap">
						<bean:define id="closeRequestURL">/roomsReserveManagement.do?method=closeRequest&amp;reserveRequestID=<bean:write name="myRequest" property="externalId"/></bean:define>
						<html:link page="<%= closeRequestURL %>">
							<bean:message key="label.resolve.rooms.reserve.request" bundle="SOP_RESOURCES"/>
						</html:link>											
					</td>					
				</tr>
		</table>
	</logic:present>
	<p class="mtop2 "><b>
	<bean:message key="label.filter.request.by.campus" bundle="SOP_RESOURCES" />
	</b></p>
	<p >
	<bean:message key="label.filter.request.by.campus.note" bundle="SOP_RESOURCES" />
	</p>
	<fr:form action="/roomsReserveManagement.do?method=seeFilteredRoomsReserveRequests" >
	<fr:edit id="filterRoomRequestByCampus" name="campusBean" >
		<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.spaceManager.CampusBean" bundle="APPLICATION_RESOURCES">
			<fr:slot name="campus" key="label.find.spaces.campus" layout="menu-select-postback" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">	
				<fr:property name="size" value="30"/>
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.spaceManager.CampusProvider"/>		
				<fr:property name="format"	value="${spaceInformation.presentationName}" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>		
		<fr:destination name="post-back" path="/roomsReserveManagement.do?method=seeFilteredRoomsReserveRequests" />
	</fr:edit>
	</fr:form>
	<p class="mtop2 mbottom05"><b><bean:message key="label.my.rooms.reserve.requests" bundle="SOP_RESOURCES"/></b></p>
	
	<logic:empty name="personRequests">
		<p class="mtop05"><em><bean:message key="label.empty.rooms.reserves.requests" bundle="SOP_RESOURCES"/></em></p>
	</logic:empty>	
	
	<logic:notEmpty name="personRequests">
		<table class="tstyle1 thlight mtop025 mbottom05">			
			<tr>
				<th><bean:message key="label.rooms.reserve.identification" bundle="SOP_RESOURCES"/></th>		
				<th><bean:message key="label.rooms.reserve.instant" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.order" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.requestor" bundle="SOP_RESOURCES"/></th>	
				<th><bean:message key="label.rooms.reserve.number.of.new.comments" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.periods" bundle="SOP_RESOURCES"/></th>					
				<th><bean:message key="label.rooms.reserve.action" bundle="SOP_RESOURCES"/></th>	
			</tr>
			<logic:iterate id="myRequest" name="personRequests" type="net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest">
				<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserveRequest&amp;reserveRequestID=<bean:write name="myRequest" property="externalId"/></bean:define>					
				<tr>
					<td class="acenter">						
						<html:link page="<%= seeReserveURL %>">
							<bean:write name="myRequest" property="identification"/>
						</html:link>						
					</td>
					<td class="nowrap smalltxt">
						<bean:write name="myRequest" property="presentationInstant"/>						
					</td>
					<td style="width: 250px;">						
						<html:link page="<%= seeReserveURL %>">
							<bean:write name="myRequest" property="subject"/>
						</html:link>								
					</td>						
					<td class="acenter">
						<bean:define id="requestorName" name="myRequest" property="requestor.name" type="java.lang.String"/>						
						<acronym title="<%= requestorName %>"><bean:write name="myRequest" property="requestor.username"/></acronym>
					</td>
					<td class="acenter">	
						<% Integer numOfNewComments = myRequest.getNumberOfNewComments(person);	%>
						<%= numOfNewComments.toString() %>
					</td>

					<td class="acenter smalltxt nowrap">					
						<logic:notEmpty name="myRequest" property="genericEvents">
							<bean:message key="label.yes.capitalized" bundle="SOP_RESOURCES"/>
						</logic:notEmpty>
						<logic:empty name="myRequest" property="genericEvents">
							-
						</logic:empty>
					</td>

					<td class="nowrap">
						<bean:define id="closeRequestURL">/roomsReserveManagement.do?method=closeRequest&amp;reserveRequestID=<bean:write name="myRequest" property="externalId"/></bean:define>
						<html:link page="<%= closeRequestURL %>">
							<bean:message key="label.resolve.rooms.reserve.request" bundle="SOP_RESOURCES"/>
						</html:link>											
					</td>					
				</tr>
			</logic:iterate>		
		</table>
	</logic:notEmpty>



	<p class="mtop2 mbottom05"><b><bean:message key="label.new.rooms.reserve.requests" bundle="SOP_RESOURCES"/></b></p>
	
	<logic:empty name="newRequests">
		<p class="mtop05"><em><bean:message key="label.empty.rooms.reserves.requests" bundle="SOP_RESOURCES"/></em></p>
	</logic:empty>
	
	<logic:notEmpty name="newRequests">
		<table class="tstyle1 thlight mtop025 mbottom05">			
			<tr>
				<th><bean:message key="label.rooms.reserve.identification" bundle="SOP_RESOURCES"/></th>		
				<th><bean:message key="label.rooms.reserve.instant" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.order" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.requestor" bundle="SOP_RESOURCES"/></th>								
				<th><bean:message key="label.rooms.reserve.action" bundle="SOP_RESOURCES"/></th>	
			</tr>
			<logic:iterate id="newRequest" name="newRequests">
				<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserveRequest&amp;reserveRequestID=<bean:write name="newRequest" property="externalId"/></bean:define>
				<tr>
					<td class="acenter">
						<html:link page="<%= seeReserveURL %>">
							<bean:write name="newRequest" property="identification"/>
						</html:link>												
					</td>				
					<td class="nowrap smalltxt">
						<bean:write name="newRequest" property="presentationInstant"/>						
					</td>
					<td style="width: 250px;">						
						<html:link page="<%= seeReserveURL %>">
							<bean:write name="newRequest" property="subject"/>
						</html:link>										
					</td>	
					<td class="acenter">
						<bean:define id="requestorName" name="newRequest" property="requestor.name" type="java.lang.String"/>						
						<acronym title="<%= requestorName %>"><bean:write name="newRequest" property="requestor.username"/></acronym>
					</td>															
					<td>
						<bean:define id="openRequestURL">/roomsReserveManagement.do?method=openRequest&amp;reserveRequestID=<bean:write name="newRequest" property="externalId"/></bean:define>
						<html:link page="<%= openRequestURL %>">
							<bean:message key="label.open.rooms.reserve.request" bundle="SOP_RESOURCES"/>
						</html:link>											
					</td>						
				</tr>
			</logic:iterate>		
		</table>
	</logic:notEmpty>


	<p class="mtop2 mbottom05"><b><bean:message key="label.opened.rooms.reserve.requests" bundle="SOP_RESOURCES"/></b></p>
	
	<logic:empty name="openedRequests">
		<p class="mtop05"><em><bean:message key="label.empty.rooms.reserves.requests" bundle="SOP_RESOURCES"/></em></p>
	</logic:empty>
	<logic:notEmpty name="openedRequests">
		<table class="tstyle1 thlight mtop025 mbottom05">			
			<tr>
				<th><bean:message key="label.rooms.reserve.identification" bundle="SOP_RESOURCES"/></th>		
				<th><bean:message key="label.rooms.reserve.instant" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.order" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.requestor" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.employee" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.periods" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.action" bundle="SOP_RESOURCES"/></th>	
			</tr>
			<logic:iterate id="openedRequest" name="openedRequests">
				<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserveRequest&amp;reserveRequestID=<bean:write name="openedRequest" property="externalId"/></bean:define>
				<tr>
					<td class="acenter">
						<html:link page="<%= seeReserveURL %>">
							<bean:write name="openedRequest" property="identification"/>
						</html:link>											
					</td>				
					<td class="nowrap smalltxt">
						<bean:write name="openedRequest" property="presentationInstant"/>						
					</td>
					<td style="width: 250px;">						
						<html:link page="<%= seeReserveURL %>">
							<bean:write name="openedRequest" property="subject"/>
						</html:link>										
					</td>	
					<td>
						<bean:define id="requestorName" name="openedRequest" property="requestor.name" type="java.lang.String"/>						
						<acronym title="<%= requestorName %>"><bean:write name="openedRequest" property="requestor.username"/></acronym>
					</td>					
					<td class="acenter">
						<logic:notEmpty name="openedRequest" property="owner">
							<bean:define id="ownerName" name="openedRequest" property="owner.name" type="java.lang.String"/>						
							<span title="<%= ownerName %>"><bean:write name="openedRequest" property="owner.username"/></span>	
						</logic:notEmpty>
						<logic:empty name="openedRequest" property="owner">
						-
						</logic:empty>	
					</td>					
					<td class="acenter smalltxt">					
						<logic:notEmpty name="openedRequest" property="genericEvents">
							<bean:message key="label.yes.capitalized" bundle="SOP_RESOURCES"/>
						</logic:notEmpty>
						<logic:empty name="openedRequest" property="genericEvents">
							-
						</logic:empty>
					</td>	
					<td>
						<bean:define id="closeRequestURL">/roomsReserveManagement.do?method=closeRequest&amp;reserveRequestID=<bean:write name="openedRequest" property="externalId"/></bean:define>
						<html:link page="<%= closeRequestURL %>">
							<bean:message key="label.resolve.rooms.reserve.request" bundle="SOP_RESOURCES"/>
						</html:link>											
					</td>						
				</tr>
			</logic:iterate>		
		</table>
	</logic:notEmpty>


	<p class="mtop2 mbottom05"><b><bean:message key="label.resolved.rooms.reserve.requests" bundle="SOP_RESOURCES"/></b></p>
	<logic:empty name="resolvedRequests">
		<p class="mtop05"><em><bean:message key="label.empty.rooms.reserves.requests" bundle="SOP_RESOURCES"/></em></p>
	</logic:empty>
	<logic:notEmpty name="resolvedRequests">						
		<logic:notEqual name="numberOfPages" value="1">
			<p>
				<bean:message key="label.pages" bundle="SOP_RESOURCES"/>:
				<cp:collectionPages url="/resourceAllocationManager/roomsReserveManagement.do?method=seeRoomsReserveRequests" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>
			</p>
		</logic:notEqual>
		<table class="tstyle1 thlight mtop025 mbottom05">					
			<tr>
				<th><bean:message key="label.rooms.reserve.identification" bundle="SOP_RESOURCES"/></th>		
				<th><bean:message key="label.rooms.reserve.instant" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.order" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.requestor" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.employee" bundle="SOP_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.number.of.new.comments" bundle="SOP_RESOURCES"/></th>	
				<th><bean:message key="label.rooms.reserve.periods" bundle="SOP_RESOURCES"/></th>				
				<th><bean:message key="label.rooms.reserve.action" bundle="SOP_RESOURCES"/></th>	
			</tr>
			<logic:iterate id="resolvedRequest" name="resolvedRequests" type="net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest">
				<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserveRequest&amp;reserveRequestID=<bean:write name="resolvedRequest" property="externalId"/></bean:define>					
				<tr>
					<td class="acenter">
						<html:link page="<%= seeReserveURL %>">
							<bean:write name="resolvedRequest" property="identification"/>
						</html:link>												
					</td>					
					<td class="nowrap smalltxt">
						<bean:write name="resolvedRequest" property="presentationInstant"/>						
					</td>
					<td style="width: 250px;">						
						<html:link page="<%= seeReserveURL %>">
							<bean:write name="resolvedRequest" property="subject"/>
						</html:link>										
					</td>	
					<td class="acenter">
						<bean:define id="requestorName" name="resolvedRequest" property="requestor.name" type="java.lang.String"/>						
						<acronym title="<%= requestorName %>"><bean:write name="resolvedRequest" property="requestor.username"/></acronym>
					</td>					
					<td class="acenter">
						<logic:notEmpty name="resolvedRequest" property="owner">
							<bean:define id="ownerName" name="resolvedRequest" property="owner.name" type="java.lang.String"/>						
							<span title="<%= ownerName %>"><bean:write name="resolvedRequest" property="owner.username"/></span>	
						</logic:notEmpty>
						<logic:empty name="resolvedRequest" property="owner">
						-
						</logic:empty>			
					</td>
					<td class="acenter">
						<% 	String numOfNewComments = "-";
							if(resolvedRequest.getOwner() != null && resolvedRequest.getOwner().equals(person)){
							    numOfNewComments = resolvedRequest.getNumberOfNewComments(person).toString();
							}														
						%>
						<%= numOfNewComments %>						
					</td>
					<td class="acenter smalltxt">
						<logic:notEmpty name="resolvedRequest" property="genericEvents">
							<bean:message key="label.yes.capitalized" bundle="SOP_RESOURCES"/>
						</logic:notEmpty>
						<logic:empty name="resolvedRequest" property="genericEvents">
							-
						</logic:empty>
					</td>						
					<td>
						<bean:define id="openRequestURL">/roomsReserveManagement.do?method=openRequest&amp;reserveRequestID=<bean:write name="resolvedRequest" property="externalId"/></bean:define>
						<html:link page="<%= openRequestURL %>">
							<bean:message key="label.reopen.rooms.reserve.request" bundle="SOP_RESOURCES"/>
						</html:link>											
					</td>							
				</tr>
			</logic:iterate>		
		</table>
	</logic:notEmpty>
			
</logic:present>

