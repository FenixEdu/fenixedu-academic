<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.parking" /></em>
<h2><bean:message key="link.users"/></h2>

<p class="mtop15 mbottom05"><strong><bean:message key="label.searchUser"/></strong></p>

<fr:form action="/parking.do?method=showParkingPartyRequests">
	<fr:edit id="searchPartyBean" name="searchPartyBean" schema="search.party">
		<fr:destination name="cancel" path="/index.do"/>	
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright mvert05 thmiddle"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>
	<p class="mtop05">
		<html:submit>
			<bean:message key="button.submit" />
		</html:submit>
	</p>
</fr:form>



<bean:define id="url" type="java.lang.String">/externalPerson.do?method=search&amp;name=<bean:write name="searchPartyBean" property="partyName"/></bean:define>

<ul class="mtop15">
	<li>
		<html:link page="<%= url %>"><bean:message key="link.create.external.person" /></html:link>
	</li>
</ul>


<logic:present name="searchPartyBean">

	<logic:notEmpty name="searchPartyBean" property="party">
	
		<logic:notEmpty name="searchPartyBean" property="party.parkingParty">
			<bean:define id="parkingParty" name="searchPartyBean" property="party.parkingParty" type="net.sourceforge.fenixedu.domain.parking.ParkingParty"/>
			<bean:define id="personID" name="parkingParty" property="party.idInternal" />
			
			<h3 class="separator2 mtop2"><bean:message key="label.parkUserInfo"/></h3>
			<p>
				<html:img src="<%= request.getContextPath() +"/parkingManager/parking.do?method=showPhoto&amp;personID="+personID.toString() %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" /></p>
				<logic:iterate id="occupation" name="parkingParty" property="occupations">
					<p><bean:write name="occupation" filter="false"/></p>
				</logic:iterate>
			</p>
			<%--
			<p class="mtop15 mbottom05">
				<logic:empty name="parkingParty" property="vehicles">
					<strong><bean:message key="label.user" bundle="PARKING_RESOURCES"/></strong>
				</logic:empty>
			</p>
			--%>
			<p class="mtop05">
				<html:link page="<%= "/parking.do?method=prepareEditParkingParty&amp;addVehicle=false&amp;parkingPartyID=" + parkingParty.getIdInternal()%>">
					<bean:message key="link.editUserCard" bundle="PARKING_RESOURCES"/>
				</html:link>
				<logic:notEmpty name="parkingParty" property="cardNumber">
					<%--
					<html:link target="printFrame" page="<%= "/parking.do?method=printParkingCard&amp;parkingPartyID=" + parkingParty.getIdInternal()%>">
					<bean:message key="label.printCard" bundle="PARKING_RESOURCES"/>
					</html:link>, 
					--%>
					 | <html:link target="printFrame" page="<%= "/parking.do?method=exportToPDFParkingCard&amp;parkingPartyID=" +  parkingParty.getIdInternal()%>">
					<bean:message key="label.exportToPDF" bundle="PARKING_RESOURCES"/></html:link>
				</logic:notEmpty>
			</p>
			
			<fr:view name="parkingParty" schema="view.parkingParty.personalInfo">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thright thlight mtop025 mbottom05" />
					<fr:property name="rowClasses" value="trhighlight2,,,,,,," />
				</fr:layout>
			</fr:view>
			
			<logic:notEmpty name="parkingParty" property="vehicles">
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
				
				
			</logic:notEmpty>
			
			
			<%--
			<p class="mtop025">
				<html:link page="<%= "/parking.do?method=prepareEditParkingParty&amp;parkingPartyID=" + parkingParty.getIdInternal()%>">
					<bean:message key="title.editUser" bundle="PARKING_RESOURCES"/>
				</html:link>
				<logic:notEmpty name="parkingParty" property="cardNumber">
					| <html:link target="printFrame" page="<%= "/parking.do?method=exportToPDFParkingCard&amp;parkingPartyID=" + parkingParty.getIdInternal()%>">
						<bean:message key="label.exportToPDF" bundle="PARKING_RESOURCES"/>
					</html:link>
				</logic:notEmpty>
			</p>
			--%>

					
			<h3 class="separator2 mtop2"><bean:message key="label.requestList" /></h3>
			<logic:notEmpty name="parkingRequests">		
				<fr:view name="parkingRequests" schema="show.parkingRequest.noDetail">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thlight mtop05" />
						<fr:property name="columnClasses" value="acenter,,,," />
						<fr:property name="link(view)" value="/parking.do?method=showRequest" />
						<fr:property name="key(view)" value="link.viewRequest" />
						<fr:property name="param(view)" value="idInternal" />
						<fr:property name="bundle(view)" value="PARKING_RESOURCES" />
					</fr:layout>
				</fr:view>
			</logic:notEmpty>

			<logic:empty name="parkingRequests">
				<p>
					<em><bean:message key="label.userWithoutRequests" bundle="PARKING_RESOURCES"/>.</em>
				</p>
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

	<table class="tstyle1 thlight">
		<tr>
			<th scope="col"><bean:message key="label.name" bundle="MANAGER_RESOURCES"/></th>
			<th class="width8em" scope="col"><bean:message key="label.identification" bundle="MANAGER_RESOURCES"/></th>
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
				<td class="acenter" rowspan="<%= numberUnits %>" title="<%= docIDTitle %>"><bean:write name="person" property="documentIdNumber"/></td>
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
				<td class="nowrap" rowspan="<%= numberUnits %>">
					<bean:define id="searchPartyBean" name="searchPartyBean" type="net.sourceforge.fenixedu.dataTransferObject.parking.SearchPartyBean"/>
					<bean:define id="url" type="java.lang.String">/parking.do?method=showParkingPartyRequests&amp;partyID=<bean:write name="person" property="idInternal"/>&amp;plateNumber=<bean:write name="searchPartyBean" property="carPlateNumber"/></bean:define>
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
	</table>
		
	</logic:notEmpty>
	<logic:empty name="partyList">
		<p class="mtop2">
			<em><bean:message key="label.noRecordFound"/></em>
		</p>
	</logic:empty>
</logic:present>

<iframe style="display:none;" name="printFrame" src="" height="0" width="0">
</iframe>

