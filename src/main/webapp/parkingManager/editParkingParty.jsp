<%--

    Copyright © 2014 Instituto Superior Técnico

    This file is part of Fenix Parking.

    Fenix Parking is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Fenix Parking is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Fenix Parking.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<script language="Javascript" type="text/javascript">
<!--

function displayCardValidPeriod(){
	if(document.getElementById('cardValidPeriodIdYes').checked){
		document.getElementById('cardValidPeriodDivId').style.display='none';
	} else {
		document.getElementById('cardValidPeriodDivId').style.display='block';
	}
}

function hideCardValidPeriod(toShow){
	if(toShow){
		document.getElementById('cardValidPeriodDivId').style.display='block';
	} else {
		document.getElementById('cardValidPeriodDivId').style.display='none';
	}
}

function addVehicle(){
	document.getElementById('editUser').method.value='prepareEditParkingParty';	
	document.getElementById('editUser').addVehicle.value='yes';	
	document.getElementById('editUser').submit();
	return true;
}
// -->
</script>

<h2><bean:message key="title.editUser" /></h2>

<p>
	<span class="error0"><html:errors/></span>
</p>

<html:form action="/parking.do" styleId="editUser">
<fr:context>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="parkingForm" property="method" value="editParkingParty"/>
	<html:hidden bundle="PARKING_RESOURCES" altKey="hidden.addVehicle" name="parkingForm" property="addVehicle" value="no"/>
	<html:messages id="message" property="cardNumber" message="true" bundle="PARKING_RESOURCES"><span class="error0"><bean:write name="message"/></span></html:messages>
	<fr:edit id="user" name="parkingPartyBean" schema="edit.parkingPartyBean.user">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<bean:message key="label.cardValidPeriod" bundle="PARKING_RESOURCES"/>	
	<html:radio bundle="PARKING_RESOURCES" altKey="radio.cardAlwaysValid" styleId="cardValidPeriodIdYes" name="parkingForm" property="cardAlwaysValid" value="yes" onclick="displayCardValidPeriod(false)">
		<bean:message key="label.yes" bundle="PARKING_RESOURCES"/></html:radio>
	<html:radio bundle="PARKING_RESOURCES" altKey="radio.cardAlwaysValid" styleId="cardValidPeriodIdNo" name="parkingForm" property="cardAlwaysValid" value="no" onclick="displayCardValidPeriod(true)">
		<bean:message key="label.no" bundle="PARKING_RESOURCES"/></html:radio>	
	<html:messages id="message" property="mustFillInDates" message="true" bundle="PARKING_RESOURCES"><span class="error0"><bean:write name="message"/></span></html:messages>
	<div id="cardValidPeriodDivId" style="display:block">
	<fr:edit id="cardValidPeriod" name="parkingPartyBean" schema="edit.parkingPartyBean.cardValidPeriod">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	</div>

	<p class="mtop15"><html:messages id="message" property="noVehicles" message="true" bundle="PARKING_RESOURCES"><span class="error0"><bean:write name="message"/></span></html:messages></p>	 
	<logic:notEmpty name="parkingPartyBean" property="vehicles">
		<p class="mbottom05"><strong><bean:message key="label.vehicles" bundle="PARKING_RESOURCES"/>:</strong></p>
		<html:messages id="message" property="vehicleMandatoryData" message="true" bundle="PARKING_RESOURCES"><span class="error0"><bean:write name="message"/></span></html:messages>		
		<html:messages id="message" property="repeatedPlates" message="true" bundle="PARKING_RESOURCES"><span class="error0"><bean:write name="message"/></span></html:messages>
		<fr:edit id="vehicle" name="parkingPartyBean" property="vehicles" layout="tabular-editable" schema="edit.vehicleBean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>
		</fr:edit>
	</logic:notEmpty>
	
	<logic:empty name="parkingPartyBean" property="vehicles">
		<p class="mvert15"><em><bean:message key="label.vehiclesNotFound" bundle="PARKING_RESOURCES"/>.</em></p>
	</logic:empty>
	
	<p><html:link href="javascript:addVehicle()"><bean:message key="link.addVehicle" bundle="PARKING_RESOURCES"/></html:link></p>

	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Confirmar"/>	
	</p>

</fr:context>
</html:form>

<script type="text/javascript">
	displayCardValidPeriod();
</script>