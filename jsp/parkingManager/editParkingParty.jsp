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

<span class="error0"><html:errors/></span>
<html:form action="/parking.do" styleId="editUser">
<fr:context>
	<html:hidden name="parkingForm" property="method" value="editParkingParty"/>
	<html:hidden name="parkingForm" property="addVehicle" value="no"/>
	<html:messages id="message" property="cardNumber" message="true" bundle="PARKING_RESOURCES"><span class="error0"><bean:write name="message"/></span></html:messages>
	<fr:edit id="user" name="parkingPartyBean" schema="edit.parkingPartyBean.user">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle8 thright thlight"/>
			<fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
	</fr:edit>
	<bean:message key="label.cardValidPeriod" bundle="PARKING_RESOURCES"/>	
	<html:radio styleId="cardValidPeriodIdYes" name="parkingForm" property="cardAlwaysValid" value="yes" onclick="displayCardValidPeriod(false)">sim</html:radio>
	<html:radio styleId="cardValidPeriodIdNo" name="parkingForm" property="cardAlwaysValid" value="no" onclick="displayCardValidPeriod(true)">não</html:radio>	
	<br/><html:messages id="message" property="mustFillInDates" message="true" bundle="PARKING_RESOURCES"><span class="error0"><bean:write name="message"/></span></html:messages>
	<div id="cardValidPeriodDivId" style="display:block">
	<fr:edit id="cardValidPeriod" name="parkingPartyBean" schema="edit.parkingPartyBean.cardValidPeriod">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle8 thright thlight"/>
			<fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
	</fr:edit>
	</div>
	 
	<p><strong><bean:message key="label.vehicles" bundle="PARKING_RESOURCES"/>:</strong></p>
	<html:messages id="message" property="vehicleMandatoryData" message="true" bundle="PARKING_RESOURCES"><span class="error0"><bean:write name="message"/></span></html:messages>
	<html:messages id="message" property="noVehicles" message="true" bundle="PARKING_RESOURCES"><span class="error0"><bean:write name="message"/></span></html:messages>
	<fr:edit id="vehicle" name="parkingPartyBean" property="vehicles" layout="tabular-editable" schema="edit.vehicleBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle8 thright thlight"/>
		</fr:layout>
	</fr:edit>
	<p><html:link href="javascript:addVehicle()"><bean:message key="link.addVehicle" bundle="PARKING_RESOURCES"/></html:link></p>
	<br/><br/>
	
	<html:submit value="Confirmar"/>	

</fr:context>
</html:form>

<script type="text/javascript">
	displayCardValidPeriod();
</script>