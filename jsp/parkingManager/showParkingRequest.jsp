<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.domain.parking.ParkingDocumentType"%>
<em><bean:message key="label.parking" /></em>
<h2><bean:message key="label.request" /></h2>

<logic:present name="parkingRequest">
	
	<bean:define id="parkingParty" name="parkingRequest" property="parkingParty" />	
	<bean:define id="personID" name="parkingParty" property="party.idInternal" />
	
	<h3><bean:message key="label.parkUserInfo" /></h3>
	<p><html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/viewPhoto.do?personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" /></p>
	<logic:iterate id="occupation" name="parkingParty" property="occupations">
		<p><bean:write name="occupation" filter="false"/></p>
	</logic:iterate>	
	<h3><bean:message key="label.actualState" /></h3>
	<p><fr:view name="parkingParty" property="party" schema="viewPersonInfo">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thright thlight mtop025" />
		</fr:layout>
	</fr:view></p>
	<p><fr:view name="parkingParty" schema="viewParkingPartyInfo">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight mtop025" />
			</fr:layout>
	</fr:view></p>	
	
	<!--  PARKING PARTY -->
	
	<logic:equal name="parkingParty" property="hasDriverLicense" value="true">
	<table class="tstyle1 thright thlight mtop025 mbottom1">
		<tr>
			<th><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingParty" property="driverLicenseFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="partyDriverLicenselink">
				<bean:define id="partyDriverLicenselink" name="partyDriverLicenselink" type="java.lang.String"/>
				<html:link href="<%= partyDriverLicenselink %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
	</table>		
	</logic:equal>

	<logic:equal name="parkingParty" property="hasFirstCar" value="true">
	<table class="tstyle1 thright thlight mtop025 mbottom1">
		<tr>
			<th><bean:message key="label.firstCarMake" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingParty" property="firstCarMake"/></td>
			<td class="noborder"></td>
		</tr>
		<tr>
			<th><bean:message key="label.firstCarPlateNumber" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingParty" property="firstCarPlateNumber"/></td>
			<td class="noborder"></td>
		</tr>
		<tr>
			<th><bean:message key="label.firstCarPropertyRegistry" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingParty" property="firstCarPropertyRegistryFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="partyRegister1link">
				<bean:define id="partyRegister1link" name="partyRegister1link" type="java.lang.String"/>
				<html:link href="<%= partyRegister1link %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.firstInsurance" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingParty" property="firstCarInsuranceFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="partyInsurance1link">
				<bean:define id="partyInsurance1link" name="partyInsurance1link" type="java.lang.String"/>
				<html:link href="<%= partyInsurance1link %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.firstCarOwnerId" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingParty" property="firstCarOwnerIdFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="partyOwnerID1link">
				<bean:define id="partyOwnerID1link" name="partyOwnerID1link" type="java.lang.String"/>
				<html:link href="<%= partyOwnerID1link %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.firstDeclarationAuthorization" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingParty" property="firstDeclarationAuthorizationFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="partyDeclaration1link">
				<bean:define id="partyDeclaration1link" name="partyDeclaration1link" type="java.lang.String"/>
				<html:link href="<%= partyDeclaration1link %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
	</table>	
	</logic:equal>

	<logic:equal name="parkingParty" property="hasSecondCar" value="true">
	<table class="tstyle1 thright thlight mtop025 mbottom1">
		<tr>
			<th><bean:message key="label.secondCarMake" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingParty" property="secondCarMake"/></td>
			<td class="noborder"></td>
		</tr>
		<tr>
			<th><bean:message key="label.secondCarPlateNumber" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingParty" property="secondCarPlateNumber"/></td>
			<td class="noborder"></td>
		</tr>
		<tr>
			<th><bean:message key="label.secondCarPropertyRegistry" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingParty" property="secondCarPropertyRegistryFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="partyRegister2link">
				<bean:define id="partyRegister2link" name="partyRegister2link" type="java.lang.String"/>
				<html:link href="<%= partyRegister2link %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.secondInsurance" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingParty" property="secondCarInsuranceFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="partyInsurance2link">
				<bean:define id="partyInsurance2link" name="partyInsurance2link" type="java.lang.String"/>
				<html:link href="<%= partyInsurance2link %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.secondCarOwnerId" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingParty" property="secondCarOwnerIdFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="partyOwnerID2link">
				<bean:define id="partyOwnerID2link" name="partyOwnerID2link" type="java.lang.String"/>
				<html:link href="<%= partyOwnerID2link %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.secondDeclarationAuthorization" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingParty" property="secondDeclarationAuthorizationFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="partyDeclaration2link">
				<bean:define id="partyDeclaration2link" name="partyDeclaration2link" type="java.lang.String"/>
				<html:link href="<%= partyDeclaration2link %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
	</table>	
	</logic:equal>
	
	<!-- PARKING REQUEST -->
	
	<bean:define id="parkingRequest" name="parkingRequest" type="net.sourceforge.fenixedu.domain.parking.ParkingRequest"/>
	<h3><bean:message key="label.request" /></h3>
	<p><fr:view name="parkingRequest" schema="viewRequestPersonInfo">
			<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thright thlight mtop025" />
			</fr:layout>
	</fr:view></p>
	<p><fr:view name="parkingRequest" schema="viewParkingRequestInfo">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight mtop025" />
			</fr:layout>
	</fr:view>
	<logic:equal name="parkingRequest" property="hasDriverLicense" value="true">
	<table class="tstyle1 thright thlight mtop025 mbottom1">
		<tr>
			<th><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingRequest" property="driverLicenseFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="requestDriverLicenselink">
				<bean:define id="requestDriverLicenselink" name="requestDriverLicenselink" type="java.lang.String"/>
				<html:link href="<%= requestDriverLicenselink %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
	</table>
	</logic:equal>

	<logic:equal name="parkingRequest" property="hasFirstCar" value="true">
	<p class="mtop1 mbottom025"><strong><bean:message key="label.firstCar" bundle="PARKING_RESOURCES" /></strong></p>
	<table class="tstyle1 thright thlight mtop025 mbottom1">
		<tr>
			<th><bean:message key="label.firstCarMake" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingRequest" property="firstCarMake"/></td>
			<td class="noborder"></td>
		</tr>
		<tr>
			<th><bean:message key="label.firstCarPlateNumber" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingRequest" property="firstCarPlateNumber"/></td>
			<td class="noborder"></td>
		</tr>	
		<tr>
			<th><bean:message key="label.firstCarPropertyRegistry" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingRequest" property="firstCarPropertyRegistryFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="requestRegister1link">
				<bean:define id="requestRegister1link" name="requestRegister1link" type="java.lang.String"/>
				<html:link href="<%= requestRegister1link %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.firstInsurance" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingRequest" property="firstCarInsuranceFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="requestInsurance1link">
				<bean:define id="requestInsurance1link" name="requestInsurance1link" type="java.lang.String"/>
				<html:link href="<%= requestInsurance1link %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.firstCarOwnerId" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingRequest" property="firstCarOwnerIdFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="requestOwnerID1link">
				<bean:define id="requestOwnerID1link" name="requestOwnerID1link" type="java.lang.String"/>
				<html:link href="<%= requestOwnerID1link %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.firstDeclarationAuthorization" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingRequest" property="firstDeclarationAuthorizationFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="requestDeclaration1link">
				<bean:define id="requestDeclaration1link" name="requestDeclaration1link" type="java.lang.String"/>
				<html:link href="<%= requestDeclaration1link %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
	</table>
	</logic:equal>

	<logic:equal name="parkingRequest" property="hasSecondCar" value="true">
	<p class="mtop1 mbottom025"><strong><bean:message key="label.secondCar" bundle="PARKING_RESOURCES" /></strong></p>
	<table class="tstyle1 thright thlight mtop025 mbottom1">
		<tr>
			<th><bean:message key="label.secondCarMake" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingRequest" property="secondCarMake"/></td>
			<td class="noborder"></td>
		</tr>
		<tr>
			<th><bean:message key="label.secondCarPlateNumber" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingRequest" property="secondCarPlateNumber"/></td>
			<td class="noborder"></td>
		</tr>	
		<tr>
			<th><bean:message key="label.secondCarPropertyRegistry" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingRequest" property="secondCarPropertyRegistryFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="requestRegister2link">
				<bean:define id="requestRegister2link" name="requestRegister2link" type="java.lang.String"/>
				<html:link href="<%= requestRegister2link %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.secondInsurance" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingRequest" property="secondCarInsuranceFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="requestInsurance2link">
				<bean:define id="requestInsurance2link" name="requestInsurance2link" type="java.lang.String"/>
				<html:link href="<%= requestInsurance2link %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.secondCarOwnerId" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingRequest" property="secondCarOwnerIdFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="requestOwnerID2link">
				<bean:define id="requestOwnerID2link" name="requestOwnerID2link" type="java.lang.String"/>
				<html:link href="<%= requestOwnerID2link %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.secondDeclarationAuthorization" bundle="PARKING_RESOURCES"/></th>
			<td><bean:write name="parkingRequest" property="secondDeclarationAuthorizationFileNameToDisplay"/></td>
			<td class="noborder">
			<logic:present name="requestDeclaration2link">
				<bean:define id="requestDeclaration2link" name="requestDeclaration2link" type="java.lang.String"/>
				<html:link href="<%= requestDeclaration2link %>" target="_blank">
					<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
				</html:link>		
			</logic:present>		
			</td>
		</tr>
	</table>
	</logic:equal>
	
	<logic:equal name="parkingRequest" property="parkingRequestState" value="PENDING">
		<bean:define id="parkingRequestID" name="parkingRequest" property="idInternal" />
		<html:form action="/parking">
			<html:hidden property="code" value="<%= parkingRequestID.toString()%>"/>
			<html:hidden property="method" value="editParkingParty"/>
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
			
			
			<p class="mbottom025"><strong><bean:message key="label.note"/></strong></p>
			<html:textarea rows="7" cols="45" property="note"/>
			<p class="mtop2">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" property="accept"><bean:message key="button.accept"/></html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" property="notify"><bean:message key="button.notify"/></html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" property="reject"><bean:message key="button.reject"/></html:submit>
			</p>
		</html:form>
	</logic:equal>
</logic:present>
