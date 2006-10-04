<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="label.parking" /></em>
<h2><bean:message key="title.editUser" /></h2>

<span class="error0"><html:errors/></span>
<html:form action="/parking.do?method=editParkingParty">
	<fr:edit id="user" name="parkingParty" schema="edit.parkingParty.user">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle8 thright thlight"/>
			<fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
	</fr:edit>
	
	<p class="mbottom05"><strong><bean:message key="label.firstCar" bundle="PARKING_RESOURCES" /></strong></p>
	<html:checkbox name="parkingForm" property="deleteFirstCar"><bean:message key="label.deleteCar" bundle="PARKING_RESOURCES"/></html:checkbox>
	<fr:edit id="firstCar" name="parkingParty" schema="edit.parkingParty.firstCar">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle8 thright thlight"/>
			<fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
	</fr:edit>
	
	<p class="mbottom05"><strong><bean:message key="label.secondCar" bundle="PARKING_RESOURCES" /></strong></p>
	<html:checkbox name="parkingForm" property="deleteSecondCar"><bean:message key="label.deleteCar" bundle="PARKING_RESOURCES"/></html:checkbox>
	<fr:edit id="secondCar" name="parkingParty" schema="edit.parkingParty.secondCar">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle8 thright thlight"/>
			<fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
	</fr:edit>
	
	<html:submit value="Confirmar"/>
</html:form>