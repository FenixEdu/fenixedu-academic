<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants"%>

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key="label.parking" bundle="PARKING_RESOURCES" /></h2>


<logic:present name="parkingParty">

	<p class="mtop15 mbottom025"><strong><bean:message	key="label.person.title.personal.info" /></strong></p>

	<fr:view name="parkingParty" property="party" schema="viewPersonInfo">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thright thlight mtop025" />
		</fr:layout>
	</fr:view>
	<logic:empty name="parkingParty" property="parkingRequests">
		<logic:equal name="parkingParty" property="hasAllNecessaryPersonalInfo" value="false">
			<p class="infoop2">
				<bean:message key="message.personalDataCondition" bundle="PARKING_RESOURCES" /><br/>
				<bean:message key="message.no.necessaryPersonalInfo" bundle="PARKING_RESOURCES" />
			</p>
		</logic:equal>
		<logic:notEqual name="parkingParty"	property="hasAllNecessaryPersonalInfo" value="false">
			<p>
				<bean:message key="label.read.parkingRegulation" bundle="PARKING_RESOURCES" />: 
				<html:link page="/parking.do?method=downloadParkingRegulamentation">
					<bean:message key="label.parkingRegulation" bundle="PARKING_RESOURCES" />
					<bean:message key="label.parkingRegulation.pdf" bundle="PARKING_RESOURCES" />
				</html:link>
			</p>


			<logic:equal name="parkingParty" property="acceptedRegulation" value="false">				
				<div class="mvert1 infoop2">
					<p class="mvert05"><bean:message key="message.acceptRegulationCondition" bundle="PARKING_RESOURCES" /></p>
					<p class="mvert05"><bean:message key="message.acceptRegulation" bundle="PARKING_RESOURCES" /></p>
					<p class="mvert05">
						<strong>
							<html:link page="/parking.do?method=acceptRegulation">
								<bean:message key="button.acceptRegulation" bundle="PARKING_RESOURCES" /> &gt;&gt;
							</html:link>
						</strong>
					</p>
				</div>
			</logic:equal>

			<logic:notEqual name="parkingParty" property="acceptedRegulation" value="false">
				
				<div class="infoop2 mtop15"> <%-- message.acceptedRegulation --%>
					<div style="padding-bottom: 0.25em;"><bean:write name="parkingParty" property="parkingAcceptedRegulationMessage" filter="false"/></div>
					<p>
						<strong>
							<html:link page="/parking.do?method=prepareEditParking">
								<bean:message key="label.insertParkingDocuments" bundle="PARKING_RESOURCES" /> &gt;&gt;
							</html:link>
						</strong>
					</p>
				</div>
				

				
			</logic:notEqual>
		</logic:notEqual>
	</logic:empty>
	<logic:notEmpty name="parkingParty" property="parkingRequests">

			<p>
				<bean:message key="label.read.parkingRegulation" bundle="PARKING_RESOURCES" />: 
				<html:link page="/parking.do?method=downloadParkingRegulamentation">
					<bean:message key="label.parkingRegulation" bundle="PARKING_RESOURCES" />
					<bean:message key="label.parkingRegulation.pdf" bundle="PARKING_RESOURCES" />
				</html:link>
			</p>
			<%-- editar --%>
			<logic:equal name="canEdit" value="true">
				<p>
					<html:link page="/parking.do?method=prepareEditParking">
						<bean:message key="label.editParkingDocuments"
							bundle="PARKING_RESOURCES" />
					</html:link>
				</p>
			</logic:equal>

		
		<logic:equal name="canEdit" value="false">
			<div class="mvert15">
				<logic:equal name="parkingParty" property="canRequestUnlimitedCardAndIsInAnyRequestPeriod" value="true">
					<logic:empty name="parkingParty" property="lastRequest" >
							<div class="infoop2">
								<bean:message key="message.canRenewToUnlimitedCard" bundle="PARKING_RESOURCES"/>
							</div>
							<ul class="mvert05">
								<li>
									<html:link page="/parking.do?method=renewUnlimitedParkingRequest">
										<bean:message key="label.renewToUnlimitedCard" bundle="PARKING_RESOURCES" />
									</html:link>
								</li>
							</ul>
					</logic:empty>
				</logic:equal>
				<logic:notEmpty name="parkingParty" property="lastRequest" >
					<p class="mbottom2">
						<span class="infoop2">
							<bean:message key="message.renewToUnlimitedCardRequested" bundle="PARKING_RESOURCES"/>
						</span>
					</p>
				</logic:notEmpty>
			</div>
		</logic:equal>

		<logic:notEmpty name="parkingParty" property="firstRequest.requestedAs">
			<p>
				<span class="infoop2"><bean:message key="message.userRequestedAs" bundle="PARKING_RESOURCES"/>
					<strong><bean:message name="parkingParty" property="firstRequest.requestedAs.name" bundle="ENUMERATION_RESOURCES"/></strong>
				</span>
			</p>
		</logic:notEmpty>
		
		<logic:notEmpty name="parkingParty" property="firstRequest.note">
			<p>
				<span class="infoop2"><bean:message key="label.note" bundle="PARKING_RESOURCES"/>:
					<bean:write name="parkingParty" property="firstRequest.note"/>
				</span>
			</p>
		</logic:notEmpty>

		<logic:equal name="parkingParty" property="firstRequest.limitlessAccessCard" value="true">
			<p>
				<span class="infoop2">
					<bean:message key="label.requestedLimitlessAccessCard" bundle="PARKING_RESOURCES"/>						 
					<strong><bean:message key="label.limitlessAccessCard" bundle="PARKING_RESOURCES"/></strong>
					<bean:message key="label.requestedLimitlessAccessCard.done" bundle="PARKING_RESOURCES"/>
				</span>
			</p>
		</logic:equal>
	
		<bean:define id="parkingRequest" name="parkingParty"/>
		<logic:notEmpty name="parkingParty" property="firstRequest">
			<bean:define id="parkingRequest" name="parkingParty" property="firstRequest"/>
		</logic:notEmpty>
	
		<logic:notEmpty name="parkingRequest" property="vehicles">
		<p class="mtop15 mbottom025"><strong><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES" /></strong></p>		
		<table class="tstyle1 thright thlight mtop025 mbottom1">
			<tr>
				<th><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES"/></th>
				<td><bean:write name="parkingRequest" property="driverLicenseFileNameToDisplay"/></td>
				<td class="noborder">
				<bean:define id="partyDriverLicenselink" name="parkingRequest" property="declarationDocumentLink" type="java.lang.String"/>
				<logic:notEqual name="partyDriverLicenselink" value="">							
					<html:link href="<%= partyDriverLicenselink %>" target="_blank">
						<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
					</html:link>		
				</logic:notEqual>		
				</td>
			</tr>
		</table>
		
		<p class="mtop1 mbottom025"><strong><bean:message key="label.vehicles" bundle="PARKING_RESOURCES" /></strong></p>
		<table class="tstyle1 thright thlight mtop025 mbottom1">
		<logic:iterate id="vehicle" name="parkingRequest" property="vehicles">
			<tr>
				<th><bean:message key="label.vehicleMake" bundle="PARKING_RESOURCES"/>:</th>
				<td><bean:write name="vehicle" property="vehicleMake"/></td>
				<td class="noborder"></td>
			</tr>
			<tr>
				<th><bean:message key="label.vehiclePlateNumber" bundle="PARKING_RESOURCES"/>:</th>
				<td><bean:write name="vehicle" property="plateNumber"/></td>
				<td class="noborder"></td>
			</tr>
			<tr>
				<th><bean:message key="label.vehiclePropertyRegistry" bundle="PARKING_RESOURCES"/>:</th>
				<td><bean:write name="vehicle" property="propertyRegistryFileNameToDisplay"/></td>
				<td class="noborder">
				<bean:define id="vehiclePropertyRegisterLink" name="vehicle" property="propertyRegistryDocumentLink" type="java.lang.String"/>
				<logic:notEqual name="vehiclePropertyRegisterLink" value="">							
					<html:link href="<%= vehiclePropertyRegisterLink %>" target="_blank">
						<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
					</html:link>		
				</logic:notEqual>		
				</td>
			</tr>	
			<tr>
				<th><bean:message key="label.vehicleInsurance" bundle="PARKING_RESOURCES"/>:</th>
				<td><bean:write name="vehicle" property="insuranceFileNameToDisplay"/></td>
				<td class="noborder">
				<bean:define id="vehicleInsuranceLink" name="vehicle" property="insuranceDocumentLink" type="java.lang.String"/>
				<logic:notEqual name="vehicleInsuranceLink" value="">							
					<html:link href="<%= vehicleInsuranceLink %>" target="_blank">
						<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
					</html:link>		
				</logic:notEqual>		
				</td>
			</tr>
			<tr>
				<th><bean:message key="label.vehicleOwnerID" bundle="PARKING_RESOURCES"/>:</th>
				<td><bean:write name="vehicle" property="ownerIdFileNameToDisplay"/></td>
				<td class="noborder">
				<bean:define id="vehicleOwnerIDLink" name="vehicle" property="ownerIdDocumentLink" type="java.lang.String"/>
				<logic:notEqual name="vehicleOwnerIDLink" value="">							
					<html:link href="<%= vehicleOwnerIDLink %>" target="_blank">
						<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
					</html:link>		
				</logic:notEqual>		
				</td>
			</tr>
			<tr>
				<th><bean:message key="label.vehicleAuthorizationDeclaration" bundle="PARKING_RESOURCES"/>:</th>
				<td><bean:write name="vehicle" property="authorizationDeclarationFileNameToDisplay"/></td>
				<td class="noborder">
				<bean:define id="vehicleAuthorizationDeclarationLink" name="vehicle" property="declarationDocumentLink" type="java.lang.String"/>
				<logic:notEqual name="vehicleAuthorizationDeclarationLink" value="">							
					<html:link href="<%= vehicleAuthorizationDeclarationLink %>" target="_blank">
						<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
					</html:link>		
				</logic:notEqual>		
				</td>
			</tr>
			<tr>
				<td class="noborder"> </td>
				<td class="noborder"> </td>
				<td class="noborder"> </td>
		</logic:iterate>
		</table>
		</logic:notEmpty>				

	</logic:notEmpty>
</logic:present>
