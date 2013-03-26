<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.domain.parking.ParkingDocumentType"%>

<h3><bean:message key="label.actualState" /></h3>
<p><fr:view name="parkingParty" schema="view.PersonalInfoAndParkingPartyNotes">
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

<logic:notEmpty name="parkingParty" property="vehicles">
<p class="mtop15 mbottom025"><strong><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES" /></strong></p>		
<table class="tstyle1 thright thlight mtop025 mbottom1">
	<tr>
		<th><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES"/></th>
		<td><bean:write name="parkingParty" property="driverLicenseFileNameToDisplay"/></td>
		<td class="noborder">
		<logic:notEmpty name="parkingParty" property="driverLicenseDocument">			
			<fr:view name="parkingParty" property="driverLicenseDocument.parkingFile">
				<fr:layout name="link">
					<fr:property name="key" value="link.viewDocument"/>
					<fr:property name="bundle" value="PARKING_RESOURCES"/>
				</fr:layout>
			</fr:view>		
		</logic:notEmpty>
		</td>
	</tr>
</table>

<p class="mtop1 mbottom025"><strong><bean:message key="label.vehicles" bundle="PARKING_RESOURCES" /></strong></p>
<table class="tstyle1 thright thlight mtop025 mbottom1">
<logic:iterate id="vehicle" name="parkingParty" property="vehicles">
	<tr>
		<th><bean:message key="label.vehicleMake" bundle="PARKING_RESOURCES"/>:</th>
		<td><bean:write name="vehicle" property="vehicleMake"/></td>
		<td class="noborder"></td>
	</tr>
	<tr>
		<th><bean:message key="label.vehiclePlateNumber" bundle="PARKING_RESOURCES"/>:</th>
		<bean:define id="plateNumber" name="vehicle" property="plateNumber"/>
		<td><bean:write name="plateNumber"/></td>
		<td class="noborder">
			<html:link href="http://www.isp.pt/NR/exeres/019EEB91-E357-4A7C-8BD2-B62293701692.htm" target="_blank"><bean:message key="link.verifyInsurance" bundle="PARKING_RESOURCES"/></html:link>
		</td>
	</tr>
	<tr>
		<th><bean:message key="label.vehiclePropertyRegistry" bundle="PARKING_RESOURCES"/>:</th>
		<td><bean:write name="vehicle" property="propertyRegistryFileNameToDisplay"/></td>
		<td class="noborder">
		<logic:notEmpty name="vehicle" property="propertyRegistryDocument">			
			<fr:view name="vehicle" property="propertyRegistryDocument.parkingFile">
				<fr:layout name="link">
					<fr:property name="key" value="link.viewDocument"/>
					<fr:property name="bundle" value="PARKING_RESOURCES"/>
				</fr:layout>
			</fr:view>		
		</logic:notEmpty>
		</td>
	</tr>	
	<tr>
		<th><bean:message key="label.vehicleInsurance" bundle="PARKING_RESOURCES"/>:</th>
		<td><bean:write name="vehicle" property="insuranceFileNameToDisplay"/></td>
		<td class="noborder">
			<logic:notEmpty name="vehicle" property="insuranceDocument">			
				<fr:view name="vehicle" property="insuranceDocument.parkingFile">
					<fr:layout name="link">
						<fr:property name="key" value="link.viewDocument"/>
						<fr:property name="bundle" value="PARKING_RESOURCES"/>
					</fr:layout>
				</fr:view>		
			</logic:notEmpty>		
		</td>
	</tr>
	<tr>
		<th><bean:message key="label.vehicleOwnerID" bundle="PARKING_RESOURCES"/>:</th>
		<td><bean:write name="vehicle" property="ownerIdFileNameToDisplay"/></td>
		<td class="noborder">
			<logic:notEmpty name="vehicle" property="ownerIdDocument">			
				<fr:view name="vehicle" property="ownerIdDocument.parkingFile">
					<fr:layout name="link">
						<fr:property name="key" value="link.viewDocument"/>
						<fr:property name="bundle" value="PARKING_RESOURCES"/>
					</fr:layout>
				</fr:view>		
			</logic:notEmpty>
		</td>
	</tr>
	<tr>
		<th><bean:message key="label.vehicleAuthorizationDeclaration" bundle="PARKING_RESOURCES"/>:</th>
		<td><bean:write name="vehicle" property="authorizationDeclarationFileNameToDisplay"/></td>
		<td class="noborder">
			<logic:notEmpty name="vehicle" property="declarationDocument">			
				<fr:view name="vehicle" property="declarationDocument.parkingFile">
					<fr:layout name="link">
						<fr:property name="key" value="link.viewDocument"/>
						<fr:property name="bundle" value="PARKING_RESOURCES"/>
					</fr:layout>
				</fr:view>		
			</logic:notEmpty>
		</td>
	</tr>
	<tr>
		<td class="noborder"> </td>
		<td class="noborder"> </td>
		<td class="noborder"> </td>
	</tr>
</logic:iterate>
</table>
</logic:notEmpty>			

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

<logic:notEmpty name="parkingRequest" property="vehicles">
<p class="mtop15 mbottom025"><strong><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES" /></strong></p>		
<table class="tstyle1 thright thlight mtop025 mbottom1">
	<tr>
		<th><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES"/></th>
		<td><bean:write name="parkingRequest" property="driverLicenseFileNameToDisplay"/></td>
		<td class="noborder">
		<logic:notEmpty name="parkingRequest" property="driverLicenseDocument">			
			<fr:view name="parkingRequest" property="driverLicenseDocument.parkingFile">
				<fr:layout name="link">
					<fr:property name="key" value="link.viewDocument"/>
					<fr:property name="bundle" value="PARKING_RESOURCES"/>
				</fr:layout>
			</fr:view>		
		</logic:notEmpty>
			
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
		<bean:define id="plateNumber" name="vehicle" property="plateNumber"/>
		<td><bean:write name="plateNumber"/></td>
		<td class="noborder">
			<html:link href="http://www.isp.pt/NR/exeres/019EEB91-E357-4A7C-8BD2-B62293701692.htm" target="_blank"><bean:message key="link.verifyInsurance" bundle="PARKING_RESOURCES"/></html:link>
		</td>
	</tr>
	<tr>
		<th><bean:message key="label.vehiclePropertyRegistry" bundle="PARKING_RESOURCES"/>:</th>
		<td><bean:write name="vehicle" property="propertyRegistryFileNameToDisplay"/></td>
		<td class="noborder">
		<logic:notEmpty name="vehicle" property="propertyRegistryDocument">			
			<fr:view name="vehicle" property="propertyRegistryDocument.parkingFile">
				<fr:layout name="link">
					<fr:property name="key" value="link.viewDocument"/>
					<fr:property name="bundle" value="PARKING_RESOURCES"/>
				</fr:layout>
			</fr:view>		
		</logic:notEmpty>
		</td>
	</tr>	
	<tr>
		<th><bean:message key="label.vehicleInsurance" bundle="PARKING_RESOURCES"/>:</th>
		<td><bean:write name="vehicle" property="insuranceFileNameToDisplay"/></td>
		<td class="noborder">
			<logic:notEmpty name="vehicle" property="insuranceDocument">			
				<fr:view name="vehicle" property="insuranceDocument.parkingFile">
					<fr:layout name="link">
						<fr:property name="key" value="link.viewDocument"/>
						<fr:property name="bundle" value="PARKING_RESOURCES"/>
					</fr:layout>
				</fr:view>		
			</logic:notEmpty>	
		</td>
	</tr>
	<tr>
		<th><bean:message key="label.vehicleOwnerID" bundle="PARKING_RESOURCES"/>:</th>
		<td><bean:write name="vehicle" property="ownerIdFileNameToDisplay"/></td>
		<td class="noborder">
			<logic:notEmpty name="vehicle" property="ownerIdDocument">			
				<fr:view name="vehicle" property="ownerIdDocument.parkingFile">
					<fr:layout name="link">
						<fr:property name="key" value="link.viewDocument"/>
						<fr:property name="bundle" value="PARKING_RESOURCES"/>
					</fr:layout>
				</fr:view>		
			</logic:notEmpty>
		</td>
	</tr>
	<tr>
		<th><bean:message key="label.vehicleAuthorizationDeclaration" bundle="PARKING_RESOURCES"/>:</th>
		<td><bean:write name="vehicle" property="authorizationDeclarationFileNameToDisplay"/></td>
		<td class="noborder">
			<logic:notEmpty name="vehicle" property="declarationDocument">			
				<fr:view name="vehicle" property="declarationDocument.parkingFile">
					<fr:layout name="link">
						<fr:property name="key" value="link.viewDocument"/>
						<fr:property name="bundle" value="PARKING_RESOURCES"/>
					</fr:layout>
				</fr:view>		
			</logic:notEmpty>	
		</td>
	</tr>
	<tr>
		<td class="noborder"> </td>
		<td class="noborder"> </td>
		<td class="noborder"> </td>
	</tr>
</logic:iterate>
</table>
</logic:notEmpty>