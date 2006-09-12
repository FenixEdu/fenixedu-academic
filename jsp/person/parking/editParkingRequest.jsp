<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>

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
		<%--		<fr:edit name="parkingRequest"
			type="net.sourceforge.fenixedu.domain.parking.ParkingRequest"
			schema="input.parkingRequest" id="edit">
			<fr:destination name="success"
				path="/parking.do?method=prepareParking" />
		</fr:edit> --%>

		<fr:form action="/parking.do?method=editParkingRequest">
			<bean:define id="parkingRequestFactoryEditor" name="parkingParty"
				property="firstRequest.parkingRequestFactoryEditor" />
			<fr:edit name="parkingRequestFactoryEditor"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor"
				schema="edit.parkingRequestFactory.driverLicense">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1b aright printborder" />
					<fr:property name="columnClasses" value="aleft" />
					<fr:property name="caption" value="<%= driverLicense.toString() %>" />
				</fr:layout>
			</fr:edit>
			<fr:edit name="parkingRequestFactoryEditor"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor"
				schema="edit.parkingRequestFactory.firstCar">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1b aright printborder" />
					<fr:property name="columnClasses" value="aleft" />
					<fr:property name="caption" value="<%= firstCar.toString() %>" />
				</fr:layout>
			</fr:edit>
			<fr:edit name="parkingRequestFactoryEditor"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor"
				schema="edit.parkingRequestFactory.secondCar">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1b aright printborder" />
					<fr:property name="columnClasses" value="aleft" />
					<fr:property name="caption" value="<%= secondCar.toString() %>" />
				</fr:layout>
			</fr:edit>
			<input type="submit" value="<%= submit.toString() %>" />

		</fr:form>
	</logic:notEmpty>

	<logic:empty name="parkingParty" property="parkingRequests">
		<fr:form action="/parking.do?method=createParkingRequest">
			<bean:define id="parkingRequestFactoryCreator" name="parkingParty"
				property="parkingRequestFactoryCreator" />
			<fr:edit name="parkingRequestFactoryCreator"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryCreator"
				schema="parkingRequestFactory.driverLicense">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1b aright printborder" />
					<fr:property name="columnClasses" value="aleft" />
					<fr:property name="caption" value="<%= driverLicense.toString() %>" />
				</fr:layout>
			</fr:edit>
			<fr:edit name="parkingRequestFactoryCreator"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryCreator"
				schema="parkingRequestFactory.firstCar">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1b aright printborder" />
					<fr:property name="columnClasses" value="aleft" />
					<fr:property name="caption" value="<%= firstCar.toString() %>" />
				</fr:layout>
			</fr:edit>
			<fr:edit name="parkingRequestFactoryCreator"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryCreator"
				schema="parkingRequestFactory.secondCar">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1b aright printborder" />
					<fr:property name="columnClasses" value="aleft" />
					<fr:property name="caption" value="<%= secondCar.toString() %>" />
				</fr:layout>
			</fr:edit>
			<input type="submit" value="<%= submit.toString() %>" />

		</fr:form>
		<%--
		<fr:create type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryCreator"
			schema="input.parkingRequestFactoryCreator"
			action="/parking.do?method=createParkingRequest">
		<fr:hidden slot="parkingParty" name="parkingParty"/>
	</fr:create>
	--%>
		<%--
		<fr:create
			type="net.sourceforge.fenixedu.domain.parking.ParkingRequest"
			schema="input.parkingRequest" id="create">
			<fr:hidden slot="parkingParty" name="parkingParty" />
		</fr:create>
		--%>
	</logic:empty>
</logic:present>
