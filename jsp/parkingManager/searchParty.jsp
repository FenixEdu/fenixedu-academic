<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="label.parking" /></em>
<h2><bean:message key="label.search" /></h2>

<fr:edit id="searchPartyBean" action="/parking.do?method=showParkingPartyRequests" name="searchPartyBean" schema="search.party">
	<fr:destination name="cancel" path="/index.do"/>	
</fr:edit>

<br/>

<bean:define id="url" type="java.lang.String">/externalPerson.do?method=search&amp;name=<bean:write name="searchPartyBean" property="partyName"/></bean:define>
<html:link page="<%= url %>"><bean:message key="link.create.external.person" /></html:link>
<br/>

<logic:present name="searchPartyBean">

	<logic:notEmpty name="searchPartyBean" property="party">
	
		<logic:notEmpty name="searchPartyBean" property="party.parkingParty">
			<bean:define id="parkingParty" name="searchPartyBean" property="party.parkingParty" type="net.sourceforge.fenixedu.domain.parking.ParkingParty"/>
			<bean:define id="personID" name="parkingParty" property="party.idInternal" />
			<h3><bean:message key="label.parkUserInfo" /></h3>
			<p><html:img src="<%= request.getContextPath() +"/parkingManager/parking.do?method=showPhoto&amp;personID="+personID.toString() %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" /></p>
			<logic:iterate id="occupation" name="parkingParty" property="occupations">
				<bean:write name="occupation" filter="false"/><br/>
			</logic:iterate>
			<p><logic:equal name="parkingParty" property="hasFirstCar" value="false">
				<bean:message key="label.newUser" bundle="PARKING_RESOURCES"/>
			</logic:equal></p>
			<p class="mbottom05">
				<html:link page="<%= "/parking.do?method=prepareEditParkingParty&amp;parkingPartyID=" + parkingParty.getIdInternal()%>">
				<bean:message key="title.editUser" bundle="PARKING_RESOURCES"/></html:link>
				<logic:notEmpty name="parkingParty" property="cardNumber">
					<html:link target="printFrame" page="<%= "/parking.do?method=printParkingCard&amp;parkingPartyID=" + parkingParty.getIdInternal()%>">
					<bean:message key="label.printCard" bundle="PARKING_RESOURCES"/></html:link>
				</logic:notEmpty>
			</p>
			<fr:view name="parkingParty" schema="view.parkingParty.personalInfo">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thright thlight mtop025" />
				</fr:layout>
			</fr:view>
			
			<logic:equal name="parkingParty" property="hasFirstCar" value="true">
				<p class="mtop15 mbottom025"><strong><bean:message key="label.driverLicense"
					bundle="PARKING_RESOURCES" /></strong></p>
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
			
			
				<p class="mtop1 mbottom025"><strong><bean:message key="label.firstCar" bundle="PARKING_RESOURCES" /></strong></p>
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
			
				
				<logic:equal name="parkingParty" property="hasSecondCar" value="true">
					<p class="mtop1 mbottom025"><strong><bean:message key="label.secondCar"	bundle="PARKING_RESOURCES" /></strong></p>
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
					</logic:equal>
			
					<p class="mtop05">
						<html:link page="<%= "/parking.do?method=prepareEditParkingParty&amp;parkingPartyID=" + parkingParty.getIdInternal()%>">
						<bean:message key="title.editUser" bundle="PARKING_RESOURCES"/></html:link>
						<logic:notEmpty name="parkingParty" property="cardNumber">
							<html:link target="printFrame" page="<%= "/parking.do?method=printParkingCard&amp;parkingPartyID=" + parkingParty.getIdInternal()%>">
							<bean:message key="label.printCard" bundle="PARKING_RESOURCES"/></html:link>
						</logic:notEmpty>
					</p>
				<h3><bean:message key="label.requestList" /></h3>
			<logic:notEmpty name="parkingRequests">		
				<fr:view name="parkingRequests" schema="show.parkingRequest.noDetail">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1" />
						<fr:property name="link(view)" value="/parking.do?method=showRequest" />
						<fr:property name="key(view)" value="link.viewRequest" />
						<fr:property name="param(view)" value="idInternal" />
						<fr:property name="bundle(view)" value="PARKING_RESOURCES" />
					</fr:layout>
				</fr:view>
			</logic:notEmpty>
			<logic:empty name="parkingRequests">
				<bean:message key="label.userWithoutRequests" bundle="PARKING_RESOURCES"/>
			</logic:empty>
			
		</logic:notEmpty>
		<logic:empty name="searchPartyBean" property="party.parkingParty">
			<p><bean:message key="label.newUser" bundle="PARKING_RESOURCES"/></p>
			<p class="mtop05"><html:link page="">Criar utente</html:link></p>
		</logic:empty>
	</logic:notEmpty>
</logic:present>
<logic:present name="partyList">
	<logic:notEmpty name="partyList">
	<table class="tstyle1"><tbody>
		<tr>
			<th scope="col"><bean:message key="label.name" bundle="MANAGER_RESOURCES"/></th>
			<th scope="col"><bean:message key="label.identification" bundle="MANAGER_RESOURCES"/></th>
			<th scope="col"><bean:message key="label.person.unit.info" bundle="MANAGER_RESOURCES"/></th>
			<th scope="col"></th>
		</tr>
		<logic:iterate id="party" name="partyList">
			<bean:define id="person" name="party" type="net.sourceforge.fenixedu.domain.Person"/>
			<bean:define id="organizationalUnitsPresentation" name="person" property="organizationalUnitsPresentation"/>
			<bean:size id="numberUnitsTemp" name="person" property="organizationalUnitsPresentation"/>
			<bean:define id="numberUnits"><bean:write name="numberUnitsTemp"/></bean:define>
			<logic:empty name="organizationalUnitsPresentation">
				<bean:define id="numberUnits" value="1"/>
			</logic:empty>
			<tr>
				<td rowspan="<%= numberUnits %>"><bean:write name="person" property="name"/></td>
				<bean:define id="docIDTitle" type="java.lang.String"><logic:present name="person" property="idDocumentType"><bean:message name="person" property="idDocumentType.name" bundle="ENUMERATION_RESOURCES"/></logic:present></bean:define>
				<td rowspan="<%= numberUnits %>" title="<%= docIDTitle %>"><bean:write name="person" property="documentIdNumber"/></td>
				<logic:empty name="person" property="organizationalUnitsPresentation">
					<td>
					</td>
				</logic:empty>
				<logic:iterate id="unitName" name="organizationalUnitsPresentation" length="1">
					<td>
						<bean:write name="unitName"/>
						<br/>
					</td>
				</logic:iterate>
				<td rowspan="<%= numberUnits %>">
					<bean:define id="searchPartyBean" name="searchPartyBean" type="net.sourceforge.fenixedu.dataTransferObject.parking.SearchPartyBean"/>
					<bean:define id="url" type="java.lang.String">/parking.do?method=showParkingPartyRequests&amp;partyID=<bean:write name="person" property="idInternal"/>
						&amp;plateNumber=<bean:write name="searchPartyBean" property="carPlateNumber"/></bean:define>
					<html:link page="<%= url %>"><bean:message key="link.viewUser" /></html:link>
				</td>
			</tr>
			<logic:iterate id="unitName" name="organizationalUnitsPresentation" offset="1">
				<tr>
					<td>
						<bean:write name="unitName"/>
						<br/>
					</td>
				</tr>
			</logic:iterate>
		</logic:iterate>
		</tbody>
	</table>
		
	</logic:notEmpty>
	<logic:empty name="partyList">
		<bean:message key="label.noRecordFound"/>
	</logic:empty>
</logic:present>

<iframe style="display:none;" name="printFrame" src="" height="0" width="0">
</iframe>

