<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>
<html:xhtml/>

<em><bean:message key="link.rooms.reserve.management" bundle="SOP_RESOURCES"/></em>
<h2><bean:message key="view.rooms.reserve.request.title" bundle="SOP_RESOURCES"/></h2>

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
	
	<logic:notEmpty name="roomsReserveBean">
	
		<bean:define id="punctualRequest" name="roomsReserveBean" property="reserveRequest" />	
		<ul class="mvert15">
			<li>
				<html:link page="/roomsReserveManagement.do?method=seeRoomsReserveRequests">		
					<bean:message bundle="SOP_RESOURCES" key="label.return"/>
				</html:link>
			</li>
			<logic:equal name="punctualRequest" property="currentState.name" value="RESOLVED">
				<li>
					<bean:define id="createNewPunctualRoomsScheduling">/roomsReserveManagement.do?method=openRequestAndReturnToSeeRequest&amp;reserveRequestID=<bean:write name="punctualRequest" property="idInternal"/></bean:define>
					<html:link page="<%= createNewPunctualRoomsScheduling %>">		
						<bean:message bundle="SOP_RESOURCES" key="label.reopen.rooms.reserve.request"/>
					</html:link>
				</li>			
			</logic:equal>
			<logic:equal name="punctualRequest" property="currentState.name" value="NEW">
				<li>
					<bean:define id="createNewPunctualRoomsScheduling">/roomsReserveManagement.do?method=openRequestAndReturnToSeeRequest&amp;reserveRequestID=<bean:write name="punctualRequest" property="idInternal"/></bean:define>
					<html:link page="<%= createNewPunctualRoomsScheduling %>">		
						<bean:message bundle="SOP_RESOURCES" key="label.open.rooms.reserve.request"/>
					</html:link>
				</li>			
			</logic:equal>	
			<logic:equal name="punctualRequest" property="currentState.name" value="OPEN">
				<li>
					<bean:define id="createNewPunctualRoomsScheduling">/roomsReserveManagement.do?method=closeRequestAndReturnToSeeRequest&amp;reserveRequestID=<bean:write name="punctualRequest" property="idInternal"/></bean:define>
					<html:link page="<%= createNewPunctualRoomsScheduling %>">		
						<bean:message bundle="SOP_RESOURCES" key="label.resolve.rooms.reserve.request"/>
					</html:link>
				</li>
			</logic:equal>				
			<logic:equal name="punctualRequest" property="currentState.name" value="OPEN">
				<li>
					<bean:define id="createNewPunctualRoomsScheduling">/roomsReserveManagement.do?method=prepareCreate&amp;reserveRequestID=<bean:write name="punctualRequest" property="idInternal"/></bean:define>
					<html:link page="<%= createNewPunctualRoomsScheduling %>">		
						<bean:message bundle="SOP_RESOURCES" key="label.create.new.punctual.rooms.scheduling"/>
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
						<ul style="padding-left: 1.5em;">
							<logic:iterate id="genericEvent" name="punctualRequest" property="genericEvents">
								<li>									
									<logic:equal name="punctualRequest" property="currentState.name" value="OPEN">
										<bean:define id="viewGenericEventURL">/roomsReserveManagement.do?method=prepareView&amp;genericEventID=<bean:write name="genericEvent" property="idInternal"/>&amp;reserveRequestID=<bean:write name="punctualRequest" property="idInternal"/></bean:define>
										<html:link page="<%= viewGenericEventURL %>">
											<bean:write name="genericEvent" property="eventPeriodForGanttDiagram"/>
											-
											<bean:write name="genericEvent" property="eventObservationsForGanttDiagram"/>
										</html:link>
									</logic:equal>
									<logic:notEqual name="punctualRequest" property="currentState.name" value="OPEN">
										<bean:write name="genericEvent" property="eventPeriodForGanttDiagram"/>
										-
										<bean:write name="genericEvent" property="eventObservationsForGanttDiagram"/>
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
				<th><bean:message key="label.rooms.reserve.description" bundle="SOP_RESOURCES"/></th>
				<td><fr:view name="punctualRequest" property="description"/></td>
			</tr>													
		</table>
		
		<bean:define id="comments" name="punctualRequest" property="commentsWithoutFirstCommentOrderByDate" />
		<logic:empty name="comments">
			<em><bean:message key="label.rooms.reserve.empty.comments" bundle="SOP_RESOURCES"/></em>		
		</logic:empty>
		<logic:notEmpty name="comments">
			<logic:iterate id="comment" name="comments">
				<p class="color888 mtop15 mbottom025"><bean:write name="comment" property="presentationInstant"/></p>
				<p class="mvert025"><strong><bean:write name="comment" property="owner.name"/> (<bean:write name="comment" property="owner.username"/>)</strong></p>
				<p class="mvert025"><fr:view name="comment" property="description"/></p>
				<logic:notEmpty name="comment" property="state">
					<logic:equal name="comment" property="state.name" value="RESOLVED">
						<p class="mvert025"><strong><bean:message key="label.rooms.reserve.resolved" bundle="SOP_RESOURCES"/></strong></p>		
					</logic:equal>
				</logic:notEmpty>		
			</logic:iterate>
		</logic:notEmpty>	
		
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
			<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserveRequest&punctualReserveID=<bean:write name="punctualRequest" property="idInternal"/></bean:define>
			<fr:edit id="roomsReserveNewComment" name="roomsReserveBean" slot="description" 
				validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredMultiLanguageStringValidator"
				type="net.sourceforge.fenixedu.dataTransferObject.teacher.RoomsReserveBean">

				<fr:edit name="roomsReserveBean" id="roomsReserveBeanWithNewComment" nested="true" visible="false"/>
				<fr:layout name="area">
					<fr:property name="rows" value="8" />
					<fr:property name="columns" value="55"/>										
				</fr:layout>
				<fr:destination name="input" path="<%= seeReserveURL %>"/>
			</fr:edit>		
			
			<p>
				<html:submit><bean:message key="label.send" bundle="SOP_RESOURCES"/></html:submit>
				<logic:equal name="punctualRequest" property="currentState.name" value="OPEN">
					<html:submit onclick="this.form.method.value='createNewRoomsReserveCommentAndMakeRequestResolved';this.form.sumit();">
						<bean:message key="label.submit.and.make.request.resolved" bundle="SOP_RESOURCES"/>
					</html:submit>
				</logic:equal>
			</p>
		</fr:form>
		
		<jsp:include page="legend.jsp" />
		
	</logic:notEmpty>

</logic:present>
