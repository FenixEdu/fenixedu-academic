<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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

<em><bean:message key="label.parking" /></em>
<h2><bean:message key="title.editUser" /></h2>

<p>
	<span class="error0"><html:errors/></span>
</p>

<html:form action="/parking.do" styleId="editUser">
<fr:context>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="parkingForm" property="method" value="editParkingParty"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.addVehicle" name="parkingForm" property="addVehicle" value="no"/>
	<html:messages id="message" property="cardNumber" message="true" bundle="PARKING_RESOURCES"><span class="error0"><bean:write name="message"/></span></html:messages>
	<fr:edit id="user" name="parkingPartyBean" schema="edit.parkingPartyBean.user">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<bean:message key="label.cardValidPeriod" bundle="PARKING_RESOURCES"/>	
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.cardAlwaysValid" styleId="cardValidPeriodIdYes" name="parkingForm" property="cardAlwaysValid" value="yes" onclick="displayCardValidPeriod(false)">
		<bean:message key="label.yes" bundle="PARKING_RESOURCES"/></html:radio>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.cardAlwaysValid" styleId="cardValidPeriodIdNo" name="parkingForm" property="cardAlwaysValid" value="no" onclick="displayCardValidPeriod(true)">
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
	 
	<logic:notEmpty name="parkingPartyBean" property="vehicles">
		<p class="mbottom05"><strong><bean:message key="label.vehicles" bundle="PARKING_RESOURCES"/>:</strong></p>
		<html:messages id="message" property="vehicleMandatoryData" message="true" bundle="PARKING_RESOURCES"><span class="error0"><bean:write name="message"/></span></html:messages>
		<html:messages id="message" property="noVehicles" message="true" bundle="PARKING_RESOURCES"><span class="error0"><bean:write name="message"/></span></html:messages>
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