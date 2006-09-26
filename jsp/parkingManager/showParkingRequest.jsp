<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<script language="Javascript" type="text/javascript">
<!--

function copy(index, value){
	document.forms[0].elements[index+3].value=value;
}

function copyAll(){
	for(iter2=0; iter2 < document.forms[1].elements.length-2; iter2++){
		document.forms[1].elements[iter2].click();
	}
}

function copySelect(index, value){
	for(iter=0; iter < document.forms[0].elements[index+3].length;iter++){
		if(document.forms[0].elements[index+3].options[iter].text == value){
			document.forms[0].elements[index+3].selectedIndex = iter;
			break;
		}
	}
}
// -->
</script>


<em><bean:message key="label.parking" /></em>
<h2><bean:message key="label.request" /></h2>
<logic:present name="parkingRequest">
	<bean:define id="parkingRequestID" name="parkingRequest"
		property="idInternal" />
<fr:form action="">
<p class="mbottom0"><strong><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES" /></strong>:</p>	
	<table class="tstyle1 thtop thlight printborder">
	<tr>
	<th class="width10em cornerleft"/>
	<th><strong><bean:message key="label.actualState" /></strong></th>
	<td class="noborder"/>
	<th><strong><bean:message key="label.request" /></strong></th>
	</tr>
		<bean:define id="parkingParty" name="parkingRequest"
			property="parkingParty" />
		<tr>
			<th class="width10em" style="text-align: right;"><bean:message key="label.phone" /></th>
			<td><fr:view name="parkingParty" property="party.workPhone" /></td>
			<bean:define id="field" name="parkingRequest" property="phone" />
			<td class="noborder"/>
			<td><fr:view name="parkingRequest" property="phone" /></td>
		</tr>
		<tr>
			<th class="width10em" style="text-align: right;"><bean:message key="label.mobile" /></th>
			<td><fr:view name="parkingParty" property="party.mobile" /></td>
			<bean:define id="field" name="parkingRequest" property="mobile" />
			<td class="noborder"/>
			<td><fr:view name="parkingRequest" property="mobile" /></td>
		</tr>
		<tr>
			<th class="width10em" style="text-align: right;"><bean:message key="label.email" /></th>
			<td><fr:view name="parkingParty" property="party.email" /></td>
			<bean:define id="field" name="parkingRequest" property="email" />
			<td class="noborder"/>
			<td><fr:view name="parkingRequest" property="email" /></td>
		</tr>
	<tr><td class="noborder" colspan="4" height="40"><p class="mbottom0"><strong><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES" /></strong>:</p></td></tr>
		<logic:equal name="parkingRequest" property="parkingRequestState"
			value="PENDING">
			<tr>
				<th class="width10em" style="text-align: right;"><bean:message key="label.driverLicense" /></th>
				<td><fr:edit name="parkingParty" slot="driverLicenseFileNameToDisplay" /></td>
				<bean:define id="field" name="parkingRequest"
					property="driverLicenseFileNameToDisplay" />
				<td class="noborder acenter"><html:button property="" onclick="<%= "copy(1, '"+field+"')"%>">
					<bean:message key="button.copy" />
				</html:button></td>
				<td><fr:view name="parkingRequest" property="driverLicenseFileNameToDisplay" /></td>
			</tr>
	<tr><td class="noborder" colspan="4" height="40"><p class="mbottom0"><strong><bean:message key="label.firstCar" bundle="PARKING_RESOURCES" /></strong>:</p></td></tr>
			<tr>
				<th class="width10em" style="text-align: right;"><bean:message key="label.firstCarMake" /></th>
				<td><fr:edit name="parkingParty" slot="firstCarMake" /></td>

				<bean:define id="field" name="parkingRequest"
					property="firstCarMake" />
				<td class="noborder acenter"><html:button property="" onclick="<%= "copy(2, '"+field+"')"%>">
					<bean:message key="button.copy" />
				</html:button></td>
				<td><fr:view name="parkingRequest" property="firstCarMake" /></td>
			</tr>
			<tr>
				<th class="width10em" style="text-align: right;"><bean:message key="label.firstCarPlateNumber" /></th>
				<td><fr:edit name="parkingParty" slot="firstCarPlateNumber" /></td>

				<bean:define id="field" name="parkingRequest"
					property="firstCarPlateNumber" />
				<td class="noborder acenter"><html:button property="" onclick="<%= "copy(3, '"+field+"')"%>">
					<bean:message key="button.copy" />
				</html:button></td>
				<td><fr:view name="parkingRequest" property="firstCarPlateNumber" /></td>
			</tr>
			<tr>
				<th class="width10em" style="text-align: right;"><bean:message key="label.firstCarPropertyRegistry" /></th>
				<td><fr:edit name="parkingParty"
					slot="firstCarPropertyRegistryFileNameToDisplay" /></td>

				<bean:define id="field" name="parkingRequest"
					property="firstCarPropertyRegistryFileNameToDisplay" />
				<td class="noborder acenter"><html:button property="" onclick="<%= "copy(4, '"+field+"')"%>">
					<bean:message key="button.copy" />
				</html:button></td>
				<td><fr:view name="parkingRequest"
					property="firstCarPropertyRegistryFileNameToDisplay" /></td>
			</tr>
			<tr>
				<th class="width10em" style="text-align: right;"><bean:message key="label.firstInsurance" /></th>
				<td><fr:edit name="parkingParty" slot="firstCarInsuranceFileNameToDisplay" /></td>

				<bean:define id="field" name="parkingRequest"
					property="firstCarInsuranceFileNameToDisplay" />
				<td class="noborder acenter"><html:button property="" onclick="<%= "copy(5, '"+field+"')"%>">
					<bean:message key="button.copy" />
				</html:button></td>
				<td><fr:view name="parkingRequest"
					property="firstCarInsuranceFileNameToDisplay" /></td>
			</tr>
			<tr>
				<th class="width10em" style="text-align: right;"><bean:message key="label.firstCarOwnerId" /></th>
				<td><fr:edit name="parkingParty" slot="firstCarOwnerIdFileNameToDisplay" /></td>
				<logic:notEmpty name="parkingRequest"
					property="firstCarOwnerIdFileNameToDisplay">
					<bean:define id="field" name="parkingRequest"
						property="firstCarOwnerIdFileNameToDisplay" />
					<td class="noborder acenter"><html:button property="" onclick="<%= "copy(6, '"+field+"')"%>">
						<bean:message key="button.copy" />
					</html:button></td>
					<td><fr:view name="parkingRequest"
						property="firstCarOwnerIdFileNameToDisplay" /></td>
				</logic:notEmpty>
				<logic:empty name="parkingRequest"
					property="firstCarOwnerIdFileNameToDisplay">
					<td class="noborder"/><td />
				</logic:empty>
			</tr>
			<tr>
				<th class="width10em" style="text-align: right;"><bean:message key="label.firstDeclarationAuthorization" /></th>
				<td><fr:edit name="parkingParty"
					slot="firstDeclarationAuthorizationFileNameToDisplay" /></td>
				<logic:notEmpty name="parkingRequest"
					property="firstDeclarationAuthorizationFileNameToDisplay">
					<bean:define id="field" name="parkingRequest"
						property="firstDeclarationAuthorizationFileNameToDisplay" />
					<td class="noborder acenter"><html:button property="" onclick="<%= "copy(7, '"+field+"')"%>">
						<bean:message key="button.copy" />
					</html:button></td>
					<td><fr:view name="parkingRequest"
						property="firstDeclarationAuthorizationFileNameToDisplay" /></td>
				</logic:notEmpty>
				<logic:empty name="parkingRequest"
					property="firstDeclarationAuthorizationFileNameToDisplay">
					<td class="noborder"/><td />
				</logic:empty>
			</tr>
			
		</logic:equal>
	</table>
	</fr:form>
</logic:present>




<%-- 
	<logic:present name="parkingParty">
		<fr:edit name="parkingParty" schema="input.parkingParty" id="edit">
			<fr:hidden slot="party" name="parkingRequest" property="party" />
			<fr:destination name="cancel" path="/parking.do?method=showParkingRequests"/>
		</fr:edit>
	</logic:present>
	<logic:notPresent name="parkingParty">
		<fr:create
			action="<%= "/parking.do?method=acceptRequest&parkingRequestID=" + parkingRequestID.toString() %>"
					type="net.sourceforge.fenixedu.domain.parking.ParkingParty"
					schema="input.parkingParty" id="create">
					<fr:hidden slot="party" name="parkingRequest" property="party" />
					<fr:destination name="cancel"
						path="/parking.do?method=showParkingRequests" />
					</fr:create>
					</logic:notPresent>

					<br />
					<br />

					<html:form
						action="<%= "/parking.do?method=rejectRequest&parkingRequestID=" + parkingRequestID.toString() %>">
						<table border="0">
							<logic:notEmpty name="parkingRequest" property="phone">
								<tr>
									<td><strong><bean:message key="label.phone" /></strong></td>
									<td><fr:view name="parkingRequest" property="phone" /></td>
									<bean:define id="field" name="parkingRequest" property="phone" />
									<td><html:button property=""
										onclick="<%= "copy(1, '"+field+"')"%>">
										<bean:message key="button.copy" />
									</html:button></td>
								</tr>
							</logic:notEmpty>
							<logic:notEmpty name="parkingRequest" property="mobile">
								<tr>
									<td><strong><bean:message key="label.mobile" /></strong></td>
									<td><fr:view name="parkingRequest" property="mobile" /></td>
									<bean:define id="field" name="parkingRequest" property="mobile" />
									<td><html:button property=""
										onclick="<%= "copy(2, '"+field+"')"%>">
										<bean:message key="button.copy" />
									</html:button></td>
								</tr>
							</logic:notEmpty>
							<logic:notEmpty name="parkingRequest" property="email">
								<tr>
									<td><strong><bean:message key="label.email" /></strong></td>
									<td><fr:view name="parkingRequest" property="email" /></td>
									<bean:define id="field" name="parkingRequest" property="email" />
									<td><html:button property=""
										onclick="<%= "copy(3, '"+field+"')"%>">
										<bean:message key="button.copy" />
									</html:button></td>
								</tr>
							</logic:notEmpty>
							<logic:notEmpty name="parkingRequest"
								property="firstCarPlateNumber">
								<tr>
									<td><strong><bean:message key="label.firstCarPlateNumber" /></strong></td>
									<td><fr:view name="parkingRequest"
										property="firstCarPlateNumber" /></td>
									<bean:define id="field" name="parkingRequest"
										property="firstCarPlateNumber" />
									<td><html:button property=""
										onclick="<%= "copy(5, '"+field+"')"%>">
										<bean:message key="button.copy" />
									</html:button></td>
								</tr>
							</logic:notEmpty>
							<logic:notEmpty name="parkingRequest" property="firstCarMake">
								<tr>
									<td><strong><bean:message key="label.firstCarMake" /></strong></td>
									<td><fr:view name="parkingRequest" property="firstCarMake" /></td>
									<bean:define id="field" name="parkingRequest"
										property="firstCarMake" />
									<td><html:button property=""
										onclick="<%= "copy(6, '"+field+"')"%>">
										<bean:message key="button.copy" />
									</html:button></td>
								</tr>
							</logic:notEmpty>
							<logic:notEmpty name="parkingRequest"
								property="secondCarPlateNumber">
								<tr>
									<td><strong><bean:message key="label.secondCarPlateNumber" /></strong></td>
									<td><fr:view name="parkingRequest"
										property="secondCarPlateNumber" /></td>
									<bean:define id="field" name="parkingRequest"
										property="secondCarPlateNumber" />
									<td><html:button property=""
										onclick="<%= "copy(7, '"+field+"')"%>">
										<bean:message key="button.copy" />
									</html:button></td>
								</tr>
							</logic:notEmpty>
							<logic:notEmpty name="parkingRequest" property="secondCarMake">
								<tr>
									<td><strong><bean:message key="label.secondCarMake" /></strong></td>
									<td><fr:view name="parkingRequest" property="secondCarMake" /></td>
									<bean:define id="field" name="parkingRequest"
										property="secondCarMake" />
									<td><html:button property=""
										onclick="<%= "copy(8, '"+field+"')"%>">
										<bean:message key="button.copy" />
									</html:button></td>
								</tr>
							</logic:notEmpty>
							<tr>
								<td><strong><bean:message key="label.creationDate" /></strong></td>
								<td><fr:view name="parkingRequest" property="creationDate" /></td>
							</tr>
						</table>
						<html:button property="" onclick="copyAll()">
							<bean:message key="button.copyAll" />
						</html:button>
						<html:submit>
							<bean:message key="button.reject" />
						</html:submit>
					</html:form>
					</logic:present>
		--%>
