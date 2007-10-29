<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.parking" /></em>

<bean:define id="parkingParty" name="parkingParty" type="net.sourceforge.fenixedu.domain.parking.ParkingParty"/>
<bean:define id="personID" name="parkingParty" property="party.idInternal" />

<h3 class="separator2 mtop2"><bean:message key="label.parkUserInfo"/></h3>
<p>
	<html:img src="<%= request.getContextPath() +"/parkingManager/parking.do?method=showPhoto&amp;personID="+personID.toString() %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" /></p>
	<logic:iterate id="occupation" name="parkingParty" property="occupations">
		<p><bean:write name="occupation" filter="false"/></p>
	</logic:iterate>
</p>

<bean:define id="query" value=""/>
<logic:notEmpty name="parkingCardSearchBean" property="parkingCardUserState">
	<bean:define id="parkingCardUserState" name="parkingCardSearchBean" property="parkingCardUserState"/>
	<bean:define id="query" value="<%="&parkingCardUserState="+ parkingCardUserState.toString()%>"/>
</logic:notEmpty>
<logic:notEmpty name="parkingCardSearchBean" property="parkingGroup">
	<bean:define id="parkingGroupID" name="parkingCardSearchBean" property="parkingGroup.idInternal"/>
	<bean:define id="query" value="<%=query+"&parkingGroupID="+ parkingGroupID.toString()%>"/>
</logic:notEmpty>
<logic:notEmpty name="parkingCardSearchBean" property="actualEndDate">
	<bean:define id="actualEndDate" name="parkingCardSearchBean" property="actualEndDate"/>
	<bean:define id="query" value="<%=query+"&actualEndDate="+ actualEndDate.toString()%>"/>
</logic:notEmpty>
<logic:notEmpty name="parkingCardSearchBean" property="parkingCardSearchPeriod">
	<bean:define id="parkingCardSearchPeriod" name="parkingCardSearchBean" property="parkingCardSearchPeriod"/>
	<bean:define id="query" value="<%=query+"&parkingCardSearchPeriod="+ parkingCardSearchPeriod.toString()%>"/>
</logic:notEmpty>
<p class="mtop05">
	<html:link page="<%= "/manageParkingPeriods.do?method=searchCards" + query.toString() %>">
		<bean:message key="link.back" bundle="PARKING_RESOURCES"/>
	</html:link>	
</p>

<fr:view name="parkingParty" schema="view.parkingParty.personalInfo">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thright thlight mtop025 mbottom05" />
		<fr:property name="rowClasses" value="trhighlight2,,,,,,," />
	</fr:layout>
</fr:view>


<p class="mtop15">
	<strong><bean:message key="label.driverLicense"	bundle="PARKING_RESOURCES" /></strong>
</p>

<table class="tstyle1 thright thlight mtop025 mbottom1">
	<tr>
		<th><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES"/></th>
		<td>
			<bean:write name="parkingParty" property="driverLicenseFileNameToDisplay"/>
			<logic:empty name="parkingParty" property="driverLicenseFileNameToDisplay">-</logic:empty>	
		</td>
		<td class="noborder">
		<bean:define id="partyDriverLicenselink" name="parkingParty" property="declarationDocumentLink" type="java.lang.String"/>
		<logic:notEqual name="partyDriverLicenselink" value="">							
			<html:link href="<%= partyDriverLicenselink %>" target="_blank">
				<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
			</html:link>		
		</logic:notEqual>		
		</td>
	</tr>
</table>

<p class="mtop1 mbottom025"><strong><bean:message key="label.vehicles" bundle="PARKING_RESOURCES" /></strong></p>

<logic:iterate id="vehicle" name="parkingParty" property="vehicles">
<table class="tstyle1 thright thlight mvert025 mbottom1">
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
		<td>
			<bean:write name="vehicle" property="propertyRegistryFileNameToDisplay"/>
			<logic:empty name="vehicle" property="propertyRegistryFileNameToDisplay">-</logic:empty>
		</td>
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
		<td>
			<bean:write name="vehicle" property="insuranceFileNameToDisplay"/>
			<logic:empty name="vehicle" property="insuranceFileNameToDisplay">-</logic:empty>
		</td>
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
		<td>
			<bean:write name="vehicle" property="ownerIdFileNameToDisplay"/>
			<logic:empty name="vehicle" property="ownerIdFileNameToDisplay">-</logic:empty>
		</td>
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
		<td>
			<bean:write name="vehicle" property="authorizationDeclarationFileNameToDisplay"/>
			<logic:empty name="vehicle" property="authorizationDeclarationFileNameToDisplay">-</logic:empty>
		</td>
		<td class="noborder">
		<bean:define id="vehicleAuthorizationDeclarationLink" name="vehicle" property="declarationDocumentLink" type="java.lang.String"/>
		<logic:notEqual name="vehicleAuthorizationDeclarationLink" value="">							
			<html:link href="<%= vehicleAuthorizationDeclarationLink %>" target="_blank">
				<bean:message key="link.viewDocument" bundle="PARKING_RESOURCES" />
			</html:link>		
		</logic:notEqual>		
		</td>
	</tr>
</table>
</logic:iterate>

<p class="mtop05">
	<html:link page="<%= "/manageParkingPeriods.do?method=searchCards" + query.toString() %>">
		<bean:message key="link.back" bundle="PARKING_RESOURCES"/>
	</html:link>	
</p>	