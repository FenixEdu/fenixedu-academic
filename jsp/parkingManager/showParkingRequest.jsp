<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.domain.parking.ParkingDocumentType"%>
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
			
			<p class="mbottom025"><strong><bean:message key="label.cardNumber"/></strong></p>
			<html:text size="10" property="cardNumber"/><span class="error"><!-- Error messages go here --><html:errors /></span>
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
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" property="accept"><bean:message key="button.accept"/></html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" property="acceptPrint"><bean:message key="button.acceptPrint"/></html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" property="notify"><bean:message key="button.notify"/></html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" property="reject"><bean:message key="button.reject"/></html:submit>
			</p>
		</html:form>
	</logic:equal>
	
</logic:present>
