<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="see.specified.rooms.reserve.title" bundle="APPLICATION_RESOURCES"/></h2>

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
		
	<logic:notEmpty name="roomsReserveBean">
	
		<bean:define id="punctualRequest" name="roomsReserveBean" property="reserveRequest" />	
		<ul>
			<li>
				<html:link page="/roomsReserveManagement.do?method=viewReserves">		
					<bean:message bundle="APPLICATION_RESOURCES" key="label.return"/>
				</html:link>
			</li>
		</ul>
			
		<table class="tstyle1 thlight thright">			
			<tr>
				<th><bean:message key="label.rooms.reserve.order" bundle="APPLICATION_RESOURCES"/>:</th>
				<td><bean:write name="punctualRequest" property="subject"/></td>				
			</tr>	
			<tr>
				<th><bean:message key="label.rooms.reserve.state" bundle="APPLICATION_RESOURCES"/>:</th>
				<td><bean:message name="punctualRequest" property="currentState.name" bundle="APPLICATION_RESOURCES"/></td>
			</tr>
			<tr>
				<th><bean:message key="label.rooms.reserve.periods" bundle="APPLICATION_RESOURCES"/>:</th>	
				<td>					
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
				</td>						
			</tr>	
			<tr>
				<th><bean:message key="label.rooms.reserve.description" bundle="APPLICATION_RESOURCES"/>:</th>
				<td><bean:write name="punctualRequest" property="description"/></td>
			</tr>													
		</table>
		
		<bean:define id="comments" name="punctualRequest" property="commentsWithoutFirstCommentOrderByDate" />
		<logic:empty name="comments">
			<p class="mtop1 mbottom15">
				<em><bean:message key="label.rooms.reserve.empty.comments" bundle="APPLICATION_RESOURCES"/></em>		
			</p>
		</logic:empty>
		<logic:notEmpty name="comments">
			<logic:iterate id="comment" name="comments">
				<p class="color888 mtop15 mbottom025"><bean:write name="comment" property="presentationInstant"/></p>
				<p class="mvert025"><strong><bean:write name="comment" property="owner.name"/> (<bean:write name="comment" property="owner.username"/>)</strong></p>
				<p class="mvert025"><fr:view name="comment" property="description"/></p>
				<logic:notEmpty name="comment" property="state">
					<logic:equal name="comment" property="state.name" value="RESOLVED">
						<p class="mvert025"><strong><bean:message key="label.rooms.reserve.resolved" bundle="APPLICATION_RESOURCES"/></strong></p>		
					</logic:equal>
				</logic:notEmpty>		
			</logic:iterate>
		</logic:notEmpty>
		
		<p class="mtop25"><bean:message key="label.rooms.reserve.new.comment" bundle="APPLICATION_RESOURCES"/>:</p>
		<fr:form action="/roomsReserveManagement.do">
			<html:hidden property="method" value="createNewComment"/>
			<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserve&amp;punctualReserveID=<bean:write name="punctualRequest" property="idInternal"/></bean:define>
			
			<fr:hasMessages for="roomsReserveNewComment">
				<p>
					<span class="error0">			
						<fr:message for="roomsReserveNewComment" show="message"/>
					</span>
				</p>
			</fr:hasMessages>
			<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserve&punctualReserveID=<bean:write name="punctualRequest" property="idInternal"/></bean:define>
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
			
			<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
			<logic:equal name="punctualRequest" property="currentState.name" value="RESOLVED">
				<html:submit onclick="this.form.method.value='createNewCommentAndReOpenRequest';this.form.sumit();">
					<bean:message key="label.submit.and.reopen" bundle="APPLICATION_RESOURCES"/>
				</html:submit>
			</logic:equal>			
		</fr:form>
		
	</logic:notEmpty>

</logic:present>

