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

// -->
</script>

<em><bean:message key="label.parking" /></em>
<h2><bean:message key="title.editUser" /></h2>

<span class="error0"><html:errors/></span>
<html:form action="/parking.do?method=editParkingParty">
<fr:context>
	<html:messages id="message" property="cardNumber" message="true" bundle="PARKING_RESOURCES"><span class="error0"><bean:write name="message"/></span></html:messages>
	<fr:edit id="user" name="parkingPartyBean" schema="edit.parkingParty.user">
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
	<fr:edit id="cardValidPeriod" name="parkingPartyBean" schema="edit.parkingParty.cardValidPeriod">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle8 thright thlight"/>
			<fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
	</fr:edit>
	</div>
	
	<p class="mbottom05"><strong><bean:message key="label.firstCar" bundle="PARKING_RESOURCES" /></strong></p>
	<fr:edit id="firstCarDelete" name="parkingPartyBean" slot="deleteFirstCar" type="net.sourceforge.fenixedu.dataTransferObject.parking.ParkingPartyBean"/><bean:message key="label.deleteCar" bundle="PARKING_RESOURCES"/>
	<fr:edit id="firstCar" name="parkingPartyBean" schema="edit.parkingParty.firstCar">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle8 thright thlight"/>
			<fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
	</fr:edit>
	
	<p class="mbottom05"><strong><bean:message key="label.secondCar" bundle="PARKING_RESOURCES" /></strong></p>
	<fr:edit id="secondCarDelete" name="parkingPartyBean" slot="deleteSecondCar" type="net.sourceforge.fenixedu.dataTransferObject.parking.ParkingPartyBean"/><bean:message key="label.deleteCar" bundle="PARKING_RESOURCES"/>
	<fr:edit id="secondCar" name="parkingPartyBean" schema="edit.parkingParty.secondCar">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle8 thright thlight"/>
			<fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
	</fr:edit>
	
	<html:submit value="Confirmar"/>
</fr:context>	
</html:form>

<script type="text/javascript">
	displayCardValidPeriod();
</script>