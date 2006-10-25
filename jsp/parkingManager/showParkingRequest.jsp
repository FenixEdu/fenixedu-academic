<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.domain.parking.ParkingDocumentType"%>

<script language="Javascript" type="text/javascript">
<!--

function confirmation(){
	var result = confirm("Os ficheiros submetidos electronicamente vão ser apagados. Deseja continuar ?");
	if( result ) {
		document.forms[0].accepted.value="true";
		document.forms[0].submit();
	}
}
// -->
</script>

<em><bean:message key="label.parking" /></em>
<h2><bean:message key="label.request" /></h2>

<logic:present name="parkingRequest">		
			
	<bean:define id="parkingRequest" name="parkingRequest" toScope="request"/>
	<bean:define id="parkingParty" name="parkingRequest" property="parkingParty" toScope="request"/>	
	<bean:define id="personID" name="parkingParty" property="party.idInternal" />
	
	<h3><bean:message key="label.parkUserInfo" /></h3>
	<p><html:img src="<%= request.getContextPath() +"/parkingManager/parking.do?method=showPhoto&amp;personID="+personID.toString() %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" /></p>
	<logic:iterate id="occupation" name="parkingParty" property="occupations">
		<p><bean:write name="occupation" filter="false"/></p>
	</logic:iterate>
	<logic:notEmpty name="parkingRequest" property="requestedAs">
		<p><span class="infoop2"><bean:message key="message.userRequestedAs" bundle="PARKING_RESOURCES"/> 
		<strong><bean:message name="parkingRequest" property="requestedAs.name" bundle="ENUMERATION_RESOURCES"/></strong></span></p>
	</logic:notEmpty>
	<bean:define id="person" name="parkingParty" property="party" type="net.sourceforge.fenixedu.domain.Person"/>
	<logic:notEqual name="person" property="partyClassification" value="TEACHER">
	<logic:notEqual name="person" property="partyClassification" value="EMPLOYEE">
		<logic:equal name="parkingRequest" property="limitlessAccessCard" value="false">
			<bean:define id="cardTypeRequest"><bean:message key="label.limitedCard" bundle="PARKING_RESOURCES"></bean:message></bean:define>
		</logic:equal>
		<logic:equal name="parkingRequest" property="limitlessAccessCard" value="true">
			<bean:define id="cardTypeRequest"><bean:message key="label.limitlessCard" bundle="PARKING_RESOURCES"></bean:message></bean:define>
		</logic:equal>
		<p><span class="infoop2"><bean:message key="message.userRequestedCardType" bundle="PARKING_RESOURCES"/>
		<strong><bean:write name="cardTypeRequest"/></strong></span></p>
	</logic:notEqual>
	</logic:notEqual>
	<logic:present name="monitor">
		<logic:equal name="parkingRequest" property="limitlessAccessCard" value="false">
			<bean:define id="cardTypeRequest"><bean:message key="label.limitedCard" bundle="PARKING_RESOURCES"></bean:message></bean:define>
		</logic:equal>
		<logic:equal name="parkingRequest" property="limitlessAccessCard" value="true">
			<bean:define id="cardTypeRequest"><bean:message key="label.limitlessCard" bundle="PARKING_RESOURCES"></bean:message></bean:define>
		</logic:equal>
		<p><span class="infoop2"><bean:message key="message.userRequestedCardType" bundle="PARKING_RESOURCES"/>
		<strong><bean:write name="cardTypeRequest"/></strong></span></p>
	</logic:present>
	
	<logic:notEqual name="parkingRequest" property="parkingRequestState" value="PENDING">		
		<jsp:include page="viewParkingPartyAndRequest.jsp"/>
	</logic:notEqual>
	
	<logic:equal name="parkingRequest" property="parkingRequestState" value="PENDING">
		<bean:define id="parkingRequestID" name="parkingRequest" property="idInternal" />			
		<html:form action="/parking">
			<html:hidden property="code" value="<%= parkingRequestID.toString()%>"/>
			<html:hidden property="method" value="editFirstTimeParkingParty"/>
			<html:hidden property="parkingRequestState" value="<%= pageContext.findAttribute("parkingRequestState").toString() %>"/>
			<html:hidden property="parkingPartyClassification" value="<%= pageContext.findAttribute("parkingPartyClassification").toString() %>"/>
			<html:hidden property="personName" value="<%= pageContext.findAttribute("personName").toString() %>"/>
			<html:hidden property="carPlateNumber" value="<%= pageContext.findAttribute("carPlateNumber").toString() %>"/>
			<html:hidden property="accepted" value=""/>			
			
			<p class="mbottom025"><strong><bean:message key="label.cardNumber"/></strong></p>
			<html:text size="12" property="cardNumber"/><span class="error"><!-- Error messages go here --><html:errors /></span>
			<span class="error0 mtop0"><html:messages id="message" property="cardNumber" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>
			
					<p class="mbottom025"><strong><bean:message key="label.group"/></strong></p>
			
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.variationCode" property="group">
				<html:option value="0">
					<bean:message key="label.choose" />
				</html:option>
				<logic:iterate id="groupIt" name="groups" type="net.sourceforge.fenixedu.domain.parking.ParkingGroup">
					<bean:define id="groupId" name="groupIt" property="idInternal"/>
					<html:option value="<%=groupId.toString()%>">
						<bean:write name="groupIt" property="groupName"/>
					</html:option>
				</logic:iterate>
			</html:select>
			<span class="error0 mtop0"><html:messages id="message" property="group" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>			
			
			<jsp:include page="viewParkingPartyAndRequest.jsp"/>
			
			<p class="mbottom025"><strong><bean:message key="label.note"/></strong></p>
			<html:textarea rows="7" cols="45" property="note"/>
			<p class="mtop2">
			<html:button bundle="HTMLALT_RESOURCES" altKey="submit.submit" property="accept" onclick="confirmation();"><bean:message key="button.accept"/></html:button>
			<%--
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" property="acceptPrint"><bean:message key="button.acceptPrint"/></html:submit>
			--%>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" property="notify"><bean:message key="button.notify"/></html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" property="reject"><bean:message key="button.reject"/></html:submit>
			</p>
		</html:form>
		<bean:define id="parkingPartyId" name="parkingRequest" property="parkingParty.idInternal" />	
		<%--
		<html:link target="printFrame" page="<%= "/parking.do?method=printParkingCard&amp;parkingPartyID=" + parkingPartyId.toString()%>">
		<bean:message key="label.printCard" bundle="PARKING_RESOURCES"/></html:link>
		--%>
		<html:link target="printFrame" page="<%= "/parking.do?method=exportToPDFParkingCard&amp;parkingPartyID=" + parkingPartyId.toString()%>">
		<bean:message key="label.exportToPDF" bundle="PARKING_RESOURCES"/></html:link>
	</logic:equal>
	
</logic:present>
<iframe style="display:none;" name="printFrame" src="" height="0" width="0">
</iframe>
