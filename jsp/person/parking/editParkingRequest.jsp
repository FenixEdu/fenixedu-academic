<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>
<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key="label.parking" bundle="PARKING_RESOURCES" /></h2>

<logic:present name="parkingParty">

	<bean:define id="driverLicense">
		<bean:message key="label.driverLicense" bundle="PARKING_RESOURCES" />
	</bean:define>
	<bean:define id="firstCar">
		<bean:message key="label.firstCar" bundle="PARKING_RESOURCES" />
	</bean:define>
	<bean:define id="secondCar">
		<bean:message key="label.secondCar" bundle="PARKING_RESOURCES" />
	</bean:define>
	<bean:define id="submit">
		<bean:message key="button.submit" bundle="PARKING_RESOURCES" />
	</bean:define>

	<logic:notEmpty name="parkingParty" property="parkingRequests">

		<fr:form action="/parking.do?method=editParkingRequest" encoding="multipart/form-data">
			<bean:define id="parkingRequestFactoryEditor" name="parkingParty"
				property="firstRequest.parkingRequestFactoryEditor" />
	<p class="mbottom0"><strong><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES" /></strong>:</p>
			<fr:edit name="parkingRequestFactoryEditor"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor"
				schema="edit.parkingRequestFactory.driverLicense">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1a thright thlight mtop025" />
					<fr:property name="columnClasses" value=",,noborder" />
					<fr:property name="caption" value="<%= driverLicense.toString() %>" />
				</fr:layout>
			</fr:edit>
	<p class="mbottom0"><strong><bean:message key="label.firstCar" bundle="PARKING_RESOURCES" /></strong>:</p>
			<fr:edit name="parkingRequestFactoryEditor"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor"
				schema="edit.parkingRequestFactory.firstCar">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1a thright thlight mtop025" />
					<fr:property name="columnClasses" value=",,noborder" />
					<fr:property name="caption" value="<%= firstCar.toString() %>" />
				</fr:layout>
			</fr:edit>
	<p class="mbottom0"><strong><bean:message key="label.secondCar" bundle="PARKING_RESOURCES" /></strong>:</p>
			<fr:edit name="parkingRequestFactoryEditor"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor"
				schema="edit.parkingRequestFactory.secondCar">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1a thright thlight mtop025" />
					<fr:property name="columnClasses" value=",,noborder" />
					<fr:property name="caption" value="<%= secondCar.toString() %>" />
				</fr:layout>
			</fr:edit>
			<input type="submit" value="<%= submit.toString() %>" />

		</fr:form>
	</logic:notEmpty>

	<logic:empty name="parkingParty" property="parkingRequests">
		<fr:form action="/parking.do?method=createParkingRequest" encoding="multipart/form-data">
			<bean:define id="parkingRequestFactoryCreator" name="parkingParty"
				property="parkingRequestFactoryCreator" />

			<p class="mbottom0"><strong><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES" /></strong>:</p>
			<fr:edit name="parkingRequestFactoryCreator"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryCreator"
				schema="parkingRequestFactory.driverLicense">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1a thright thlight mtop025" />
					<fr:property name="columnClasses" value=",,noborder" />
					<fr:property name="caption" value="<%= driverLicense.toString() %>" />
				</fr:layout>
			</fr:edit>

			<p class="mbottom0"><strong><bean:message key="label.firstCar" bundle="PARKING_RESOURCES" /></strong>:</p>
			<fr:edit name="parkingRequestFactoryCreator"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryCreator"
				schema="parkingRequestFactory.firstCar">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1a thright thlight mtop025" />
					<fr:property name="columnClasses" value=",,noborder" />
					<fr:property name="caption" value="<%= firstCar.toString() %>" />
				</fr:layout>
			</fr:edit>

			<p class="mbottom0"><strong><bean:message key="label.secondCar" bundle="PARKING_RESOURCES" /></strong>:</p>
			<fr:edit name="parkingRequestFactoryCreator"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryCreator"
				schema="parkingRequestFactory.secondCar">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1a thright thlight mtop025" />
					<fr:property name="columnClasses" value=",,noborder" />
					<fr:property name="caption" value="<%= secondCar.toString() %>" />
				</fr:layout>
			</fr:edit>
			<input type="submit" value="<%= submit.toString() %>" />

		</fr:form>
		
	</logic:empty>
</logic:present>
