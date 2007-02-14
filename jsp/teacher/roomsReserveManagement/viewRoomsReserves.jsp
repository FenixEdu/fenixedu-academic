<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>
<html:xhtml/>

<h2><bean:message key="rooms.reserve.title" bundle="APPLICATION_RESOURCES"/></h2>

<logic:present role="TEACHER">

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>	
	
	<bean:define id="person" name="UserView" property="person" type="net.sourceforge.fenixedu.domain.Person"/>

	<ul class="mvert15">
		<li>
			<html:link page="/roomsReserveManagement.do?method=prepareCreateNewReserve">		
				<bean:message bundle="APPLICATION_RESOURCES" key="label.create.rooms.reserve"/>
			</html:link>
		</li>
	</ul>	

	<logic:notEmpty name="requests">	
				
		<b><bean:message key="label.rooms.reserve.list" bundle="APPLICATION_RESOURCES"/>:</b>
		<logic:notEqual name="numberOfPages" value="1">
			<p>
				<bean:message key="label.pages" bundle="APPLICATION_RESOURCES"/>:
				<cp:collectionPages url="/teacher/roomsReserveManagement.do?method=viewReserves" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>
			</p>
		</logic:notEqual>
		<table class="tstyle1 thlight">			
			<tr>
				<th><bean:message key="label.rooms.reserve.instant" bundle="APPLICATION_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.order" bundle="APPLICATION_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.state" bundle="APPLICATION_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.periods" bundle="APPLICATION_RESOURCES"/></th>	
				<th><bean:message key="label.rooms.reserve.number.of.new.comments" bundle="APPLICATION_RESOURCES"/></th>	
			</tr>
			<logic:iterate id="punctualRequest" name="requests" type="net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest">					
				<tr>
					<td class="nowrap smalltxt">
						<bean:write name="punctualRequest" property="presentationInstant"/>						
					</td>
					<td>
						<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserve&amp;punctualReserveID=<bean:write name="punctualRequest" property="idInternal"/></bean:define>
						<html:link page="<%= seeReserveURL %>">
							<bean:write name="punctualRequest" property="subject"/>
						</html:link>										
					</td>	
					<td class="nowrap smalltxt"><bean:message name="punctualRequest" property="currentState.name" bundle="APPLICATION_RESOURCES"/></td>					
					<td class="aleft smalltxt">					
						<logic:equal name="punctualRequest" property="currentState.name" value="RESOLVED">
							<logic:notEmpty name="punctualRequest" property="genericEvents">
								<logic:iterate id="genericEvent" name="punctualRequest" property="genericEvents">
									<bean:write name="genericEvent" property="eventPeriodForGanttDiagram"/>
									-								
									<bean:write name="genericEvent" property="eventObservationsForGanttDiagram"/>
									<br/>
								</logic:iterate>
							</logic:notEmpty>
							<logic:empty name="punctualRequest" property="genericEvents">
								-
							</logic:empty>
						</logic:equal>
						<logic:notEqual name="punctualRequest" property="currentState.name" value="RESOLVED">						
							-
						</logic:notEqual>						
					</td>	
					<td class="acenter">
						<% Integer numOfNewComments = punctualRequest.getNumberOfNewComments(person);	%>
						<%= numOfNewComments.toString() %>
					</td>					
				</tr>
			</logic:iterate>		
		</table>		
		
		<logic:notEqual name="numberOfPages" value="1">
			<bean:message key="label.pages" bundle="APPLICATION_RESOURCES"/>:
			<cp:collectionPages url="/teacher/roomsReserveManagement.do?method=viewReserves" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>	
		</logic:notEqual>	
			
		<jsp:include page="../../sop/roomsPunctualScheduling/legend.jsp" />
			
	</logic:notEmpty>
		
</logic:present>

