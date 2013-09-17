<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.teacherPortal" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="create.rooms.reserve.title" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="isTeacher" value="<%= new Boolean(net.sourceforge.fenixedu.injectionCode.AccessControl.getPerson().hasAnyProfessorships()).toString() %>" />


<logic:equal name="isTeacher" value="true">

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>		

	<logic:notEmpty name="roomsReserveBean">		
		<fr:edit id="roomsReserveWithDescriptions" name="roomsReserveBean" schema="CreateRoomsPunctualOccupationRequest"
			action="/roomsReserveManagement.do?method=createRoomsReserve" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>		
			<fr:destination name="cancel" path="/roomsReserveManagement.do?method=viewReserves"/>						
		</fr:edit>
	</logic:notEmpty>
		

	<logic:present name="roomsReserveRequest">
	<logic:notEmpty name="roomsReserveRequest">
		<logic:present name="roomsReserveRequest" property="firstComment">
		<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserve&amp;punctualReserveID=<bean:write name="roomsReserveRequest" property="externalId"/></bean:define>
		<fr:edit id="roomsReserveEdit" name="roomsReserveRequest" property="firstComment" schema="EditRoomsPunctualOccupationRequestFirstComment"
			action="<%= seeReserveURL %>" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>		
			<fr:destination name="cancel" path="<%= seeReserveURL %>"/>						
		</fr:edit>			
		</logic:present>
	</logic:notEmpty>
	</logic:present>	
</logic:equal>

