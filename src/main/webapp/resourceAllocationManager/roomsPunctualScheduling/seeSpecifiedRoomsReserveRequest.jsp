<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp"%>
<html:xhtml/>

<em><bean:message key="link.rooms.reserve.management" bundle="SOP_RESOURCES"/></em>
<h2><bean:message key="view.rooms.reserve.request.title" bundle="SOP_RESOURCES"/></h2>

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
	
	<logic:notEmpty name="roomsReserveBean">
	
		<bean:define id="punctualRequest" name="roomsReserveBean" property="reserveRequest" />
		<bean:define id="currentStateName" name="punctualRequest" property="currentState.name" />	
		<ul class="mvert15">
			<li>
				<html:link page="/roomsReserveManagement.do?method=seeRoomsReserveRequests">		
					<bean:message bundle="SOP_RESOURCES" key="label.return"/>
				</html:link>
			</li>
			<logic:equal name="currentStateName" value="RESOLVED">
				<li>
					<bean:define id="createNewPunctualRoomsScheduling">/roomsReserveManagement.do?method=openRequestAndReturnToSeeRequest&amp;reserveRequestID=<bean:write name="punctualRequest" property="externalId"/></bean:define>
					<html:link page="<%= createNewPunctualRoomsScheduling %>">		
						<bean:message bundle="SOP_RESOURCES" key="label.reopen.rooms.reserve.request"/>
					</html:link>
				</li>			
			</logic:equal>
			<logic:equal name="currentStateName" value="NEW">
				<li>
					<bean:define id="createNewPunctualRoomsScheduling">/roomsReserveManagement.do?method=openRequestAndReturnToSeeRequest&amp;reserveRequestID=<bean:write name="punctualRequest" property="externalId"/></bean:define>
					<html:link page="<%= createNewPunctualRoomsScheduling %>">		
						<bean:message bundle="SOP_RESOURCES" key="label.open.rooms.reserve.request"/>
					</html:link>
				</li>			
			</logic:equal>								
			<logic:equal name="currentStateName" value="OPEN">
				<li>
					<bean:define id="createNewPunctualRoomsScheduling">/roomsReserveManagement.do?method=prepareCreate&amp;reserveRequestID=<bean:write name="punctualRequest" property="externalId"/></bean:define>
					<html:link page="<%= createNewPunctualRoomsScheduling %>">		
						<bean:message bundle="SOP_RESOURCES" key="label.create.new.punctual.rooms.scheduling"/>
					</html:link>
				</li>
			</logic:equal>	
			<logic:equal name="currentStateName" value="OPEN">
				<li>
					<bean:define id="createNewPunctualRoomsScheduling">/roomsReserveManagement.do?method=closeRequestAndReturnToSeeRequest&amp;reserveRequestID=<bean:write name="punctualRequest" property="externalId"/></bean:define>
					<html:link page="<%= createNewPunctualRoomsScheduling %>">		
						<bean:message bundle="SOP_RESOURCES" key="label.resolve.rooms.reserve.request"/>
					</html:link>
				</li>
			</logic:equal>									
		</ul>
			
		<table class="tstyle1 thlight thright">	
			<tr>
				<th><bean:message key="label.rooms.reserve.identification" bundle="SOP_RESOURCES"/>:</th>
				<td><bean:write name="punctualRequest" property="identification"/></td>				
			</tr>
			<tr>
				<th><bean:message key="label.rooms.reserve.order" bundle="SOP_RESOURCES"/>:</th>				
				<td><fr:view name="punctualRequest" property="subject"/></td>
			</tr>
			<tr>
				<th><bean:message key="label.rooms.reserve.requestor" bundle="SOP_RESOURCES"/>:</th>
				<td>
					<bean:write name="punctualRequest" property="requestor.name"/>&nbsp;
					(<bean:write name="punctualRequest" property="requestor.username"/>)
				</td>
			</tr>
			<tr>
				<th><bean:message key="label.rooms.reserve.requestor.mail" bundle="SOP_RESOURCES"/>:</th>
				<td>
					<logic:notEmpty name="punctualRequest" property="requestor.email">
						<bean:write name="punctualRequest" property="requestor.email"/>
					</logic:notEmpty>
					<logic:empty name="punctualRequest" property="requestor.email">
						-
					</logic:empty>
				</td>
			</tr>
			<tr>
				<th><bean:message key="label.rooms.reserve.requestor.work.phone" bundle="SOP_RESOURCES"/>:</th>
				<td>
					<logic:notEmpty name="punctualRequest" property="requestor.workPhone">
						<bean:write name="punctualRequest" property="requestor.workPhone"/>
					</logic:notEmpty>
					<logic:empty name="punctualRequest" property="requestor.workPhone">
						-
					</logic:empty>
				</td>
			</tr>			
			<tr>
				<th><bean:message key="label.rooms.reserve.instant" bundle="SOP_RESOURCES"/>:</th>
				<td><bean:write name="punctualRequest" property="presentationInstant"/></td>
			</tr>	
			<tr>
				<th><bean:message key="label.rooms.reserve.state" bundle="SOP_RESOURCES"/>:</th>
				<td><bean:message name="punctualRequest" property="currentState.name" bundle="ENUMERATION_RESOURCES"/></td>
			</tr>			
			<tr>
				<th><bean:message key="label.rooms.reserve.periods" bundle="SOP_RESOURCES"/>:</th>	
				<td>
					<logic:notEmpty name="punctualRequest" property="genericEvents">
						<ul class="list6 nobullet">
							<logic:iterate id="genericEvent" name="punctualRequest" property="genericEvents">
								<li>									
									<logic:equal name="punctualRequest" property="currentState.name" value="OPEN">
										<bean:define id="viewGenericEventURL">/roomsReserveManagement.do?method=prepareView&amp;genericEventID=<bean:write name="genericEvent" property="externalId"/>&amp;reserveRequestID=<bean:write name="punctualRequest" property="externalId"/></bean:define>
										<html:link page="<%= viewGenericEventURL %>">
											<bean:write name="genericEvent" property="ganttDiagramEventPeriod"/>
											-
											<bean:write name="genericEvent" property="ganttDiagramEventObservations"/>
										</html:link>
									</logic:equal>
									<logic:notEqual name="currentStateName" value="OPEN">
										<bean:write name="genericEvent" property="ganttDiagramEventPeriod"/>
										-
										<bean:write name="genericEvent" property="ganttDiagramEventObservations"/>
									</logic:notEqual>
								</li>
							</logic:iterate>
						</ul>
					</logic:notEmpty>
					<logic:empty name="punctualRequest" property="genericEvents">
						-
					</logic:empty>
				</td>						
			</tr>	
			<tr>
				<th><bean:message key="label.find.spaces.campus" bundle="APPLICATION_RESOURCES"/></th>
				<logic:notEmpty name="punctualRequest" property="campus">
					<td><fr:view name="punctualRequest" property="campus.spaceInformation.presentationName"/></td>
				</logic:notEmpty>
				<logic:empty name="punctualRequest" property="campus">
					<td>-</td>
				</logic:empty>
			</tr>
			<tr>
				<th><bean:message key="label.rooms.reserve.description" bundle="SOP_RESOURCES"/></th>
				<td><fr:view name="punctualRequest" property="description"/></td>
			</tr>													
		</table>
		
		<bean:define id="comments" name="punctualRequest" property="commentsWithoutFirstCommentOrderByDate" />
		<logic:empty name="comments">
			<em><bean:message key="label.rooms.reserve.empty.comments" bundle="SOP_RESOURCES"/></em>		
		</logic:empty>
		<logic:notEmpty name="comments">					
			<bean:define id="punctualRequestObject" name="punctualRequest" type="net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest" />		
			<logic:iterate id="comment" name="comments" type="net.sourceforge.fenixedu.domain.PunctualRoomsOccupationComment">
						
				<%
					if(punctualRequestObject.getRequestor().equals(comment.getOwner())) {
				%>					
					<div style="border: 1px solid #ddc; padding: 0.5em; background: #fafaea;" class="mtop15">			
						<p class="color888 mvert025"><bean:write name="comment" property="presentationInstant"/></p>
						<p class="mvert025"><strong><bean:write name="comment" property="owner.name"/> (<bean:write name="comment" property="owner.username"/>)</strong></p>
						<p class="mvert025"><fr:view name="comment" property="description"/></p>
						<logic:notEmpty name="comment" property="state">
							<logic:equal name="comment" property="state.name" value="RESOLVED">
								<p class="mvert05"><strong><bean:message key="label.rooms.reserve.resolved" bundle="SOP_RESOURCES"/></strong></p>		
							</logic:equal>
						</logic:notEmpty>		
					</div>
				<%
					} else {
				%>								
					<div style="border: 1px solid #ddd; padding: 0.5em; background: #fafafa;" class="mtop15">			
						<p class="color888 mvert025"><bean:write name="comment" property="presentationInstant"/></p>
						<p class="mvert025"><strong><bean:write name="comment" property="owner.name"/> (<bean:write name="comment" property="owner.username"/>)</strong></p>
						<p class="mvert025"><fr:view name="comment" property="description"/></p>
						<logic:notEmpty name="comment" property="state">
							<logic:equal name="comment" property="state.name" value="RESOLVED">
								<p class="mvert05"><strong><bean:message key="label.rooms.reserve.resolved" bundle="SOP_RESOURCES"/></strong></p>		
							</logic:equal>
						</logic:notEmpty>		
					</div>			
				<%
					}
				%>
				
			</logic:iterate>
		</logic:notEmpty>	
		
		<logic:notEqual name="currentStateName" value="NEW">
		
			<p class="mtop15"><bean:message key="label.rooms.reserve.new.comment" bundle="SOP_RESOURCES"/>:</p>
			<fr:form action="/roomsReserveManagement.do">
				<html:hidden property="method" value="createNewRoomsReserveComment"/>
				
				<fr:hasMessages for="roomsReserveNewComment">
					<p>
						<span class="error0">			
							<fr:message for="roomsReserveNewComment" show="message"/>
						</span>
					</p>
				</fr:hasMessages>
				<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserveRequest&reserveRequestID=<bean:write name="punctualRequest" property="externalId"/></bean:define>
	
				<fr:edit id="roomsReserveNewComment" name="roomsReserveBean" slot="description" 
					validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredMultiLanguageStringValidator"
					type="net.sourceforge.fenixedu.dataTransferObject.teacher.RoomsReserveBean">
					
					<fr:destination name="input" path="<%= seeReserveURL %>"/>
					<fr:destination name="invalid" path="<%= seeReserveURL %>"/>
					<fr:edit name="roomsReserveBean" id="roomsReserveBeanWithNewComment" nested="true" visible="false"/>
					<fr:layout name="area">
						<fr:property name="rows" value="8" />
						<fr:property name="columns" value="55"/>										
					</fr:layout>				
				</fr:edit>		
				
				<p>
					<html:submit><bean:message key="label.send" bundle="SOP_RESOURCES"/></html:submit>
					<logic:equal name="currentStateName" value="OPEN">
						<html:submit onclick="this.form.method.value='createNewRoomsReserveCommentAndMakeRequestResolved';this.form.sumit();">
							<bean:message key="label.submit.and.make.request.resolved" bundle="SOP_RESOURCES"/>
						</html:submit>
					</logic:equal>
				</p>
			</fr:form>
			
			<logic:notEmpty name="punctualRequest" property="genericEvents">
				<jsp:include page="legend.jsp" />
			</logic:notEmpty>	

		</logic:notEqual>
		
	</logic:notEmpty>

</logic:present>
