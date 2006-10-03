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

<logic:present name="searchPartyBean">

	<logic:notEmpty name="searchPartyBean" property="party">
	
		<logic:notEmpty name="searchPartyBean" property="party.parkingParty">
			<bean:define id="parkingParty" name="searchPartyBean" property="party.parkingParty"/>
			
			<h3><bean:message key="label.parkUserInfo" /></h3>
			<logic:iterate id="occupation" name="parkingParty" property="occupations">
				<bean:write name="occupation" filter="false"/><br/>
			</logic:iterate>
			<p><logic:equal name="parkingParty" property="hasFirstCar" value="false">
				<bean:message key="label.newUser" bundle="PARKING_RESOURCES"/>
			</logic:equal></p>
			<p class="mbottom05"><html:link page="">Editar utente</html:link></p>
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
			
					<p class="mtop05"><html:link page="">Editar utente</html:link></p>
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
	<bean:define id="searchPartyBean" name="searchPartyBean" type="net.sourceforge.fenixedu.dataTransferObject.parking.SearchPartyBean"/>
		<fr:view name="partyList" schema="view.partyName">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
				<fr:property name="link(view)" value="/parking.do?method=showParkingPartyRequests" />
				<fr:property name="key(view)" value="link.view" />
				<fr:property name="param(view)" value="<%= "idInternal/partyID,plateNumber=" + searchPartyBean.getCarPlateNumber()%>"/>
				<fr:property name="bundle(view)" value="PARKING_RESOURCES" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="partyList">
		<!-- criar utente -->
		<bean:message key="label.noRecordFound"/>
	</logic:empty>
</logic:present>

