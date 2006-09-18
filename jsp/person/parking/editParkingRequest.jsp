<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>

<script language="Javascript" type="text/javascript">
<!--

function hide2ndPartForms(){

	document.getElementById('ownCar1').style.display='none';
	document.getElementById('ownCar2').style.display='none';
}

function enableDisableElement(checkbox, elementId){

	if(checkbox.checked){
		document.getElementById(elementId).style.display='none';
		document.getElementById(elementId+'Top').style.display='block';		
	} else {
		document.getElementById(elementId).style.display='block';
		document.getElementById(elementId+'Top').style.display='none';
	}
}
// -->
</script>



<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key="label.parking" bundle="PARKING_RESOURCES" /></h2>

<div class="warning0">
	<p class="mvert025"><bean:message key="title.regulation" bundle="PARKING_RESOURCES"/>:</p>
	<ul>
		<li><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES"/></li>
		
		<li><bean:message key="title.forEachVehicle" bundle="PARKING_RESOURCES"/>
			<ul>
				<li><bean:message key="title.registo" bundle="PARKING_RESOURCES"/></li>
				<li><bean:message key="title.insurance" bundle="PARKING_RESOURCES"/></li>
			</ul>
			<li><bean:message key="title.forEachVehicleNotOwned" bundle="PARKING_RESOURCES"/>
			<ul>
				<li><bean:message key="title.ownerId" bundle="PARKING_RESOURCES"/></li>
				<li><bean:message key="title.anexIV" bundle="PARKING_RESOURCES"/></li>
			</ul>
		</li>
	</ul>
	<p class="mvert025"><bean:message key="title.documentDelivery" bundle="PARKING_RESOURCES"/>:</p>
	<ul>
		<li><bean:message key="title.documentDeliveryOnline" bundle="PARKING_RESOURCES"/></li>
		<li><bean:message key="title.documentDeliveryPaper" bundle="PARKING_RESOURCES"/></li>
	</ul>
	<html:link page="/parking.do?method=downloadParkingRegulamentation">
		<bean:message key="label.parkingRegulation" />
	</html:link>
</div>	

<logic:present name="parkingParty">

	<bean:define id="submit">
		<bean:message key="button.submit" bundle="PARKING_RESOURCES" />
	</bean:define>

	<logic:notEmpty name="parkingParty" property="parkingRequests">



	<fr:form action="/parking.do?method=editParkingRequest" encoding="multipart/form-data">
		
		<bean:define id="parkingRequestFactoryEditor" name="parkingParty"
				property="firstRequest.parkingRequestFactoryEditor" />
				
				
	<p class="mbottom0"><strong><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES" /></strong>:</p>	
		<table class="tstyle1a thright thlight mbottom0 tstylepark">
		<tr>
			<th><div id="driverLicenseTop" style="display:none"><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES"/>:</div></th>
			<td>
				<html:checkbox name="parkingForm" property="driverLicense" onclick="enableDisableElement(this,'driverLicense')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox>
			</td>	
		</tr>
		</table>
			<div id="driverLicense">
				<fr:edit name="parkingRequestFactoryEditor" schema="edit.parkingRequestFactory.driverLicense"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>
				</fr:edit>
			</div>
	
			
	<!-- FIRST CAR BEGINING -->
	<p class="mbottom0"><strong><bean:message key="label.firstCar" bundle="PARKING_RESOURCES" /></strong>:</p>
		<table class="tstyle1a thright thlight mtop025 mbottom0 tstylepark">
		<tr>
			<th><bean:message key="label.firstCarMake" bundle="PARKING_RESOURCES"/>:</th>
			<td><fr:edit id="firstCarMake" name="parkingRequestFactoryEditor" slot="firstCarMake"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor">
			</fr:edit></td>
			<td class="noborder"><html:messages id="firstCarMake"/></td>
		</tr>
		<tr>
			<th><bean:message key="label.firstCarPlateNumber" bundle="PARKING_RESOURCES"/>:</th>
			<td><fr:edit id="firstCarPlateNumber" name="parkingRequestFactoryEditor" slot="firstCarPlateNumber"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor">
			</fr:edit> (aa-bb-cc)</td>
			<td class="noborder"><html:messages id="firstCarPlateNumber"/></td>
		</tr>
		<tr>
			<th><div id="registry1Top" style="display:none"><bean:message key="label.firstCarPropertyRegistry" bundle="PARKING_RESOURCES"/>:</div></th>
			<td>
				<html:checkbox name="parkingForm" property="registry1" onclick="enableDisableElement(this,'registry1')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox>
			</td>			
		</tr>
		</table>

			<div id="registry1">
				<fr:edit name="parkingRequestFactoryEditor" schema="edit.parkingRequestFactory.firstCarRegistry"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>
				</fr:edit>
			</div>

		<table class="tstyle1a thright thlight mtop0 mbottom0 tstylepark">
		<tr>
			<th><div id="insurance1Top" style="display:none"><bean:message key="label.firstInsurance" bundle="PARKING_RESOURCES"/>:</div></th>
			<td>
				<html:checkbox name="parkingForm" property="insurance1" onclick="enableDisableElement(this,'insurance1')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox>
			</td>
		</tr>
		</table>
			<div id="insurance1">
				<fr:edit name="parkingRequestFactoryEditor" schema="edit.parkingRequestFactory.firstCarInsurance"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>				
				</fr:edit>
			</div>
			
			
			<p>
				<bean:message key="label.ownVehicle" bundle="PARKING_RESOURCES"/>
				<html:radio name="parkingForm" property="ownVehicle1" value="true" onclick="document.getElementById('ownCar1').style.display='none'"/>
					<bean:message key="label.yes" bundle="PARKING_RESOURCES"/>
				<html:radio name="parkingForm" property="ownVehicle1" value="false" onclick="document.getElementById('ownCar1').style.display='block'"/>
					<bean:message key="label.no" bundle="PARKING_RESOURCES"/>				
			</p>
			<div id="ownCar1">
		<table class="tstyle1a thright thlight mtop025 mbottom0 tstylepark">
		<tr>
			<th><div id="Id1Top" style="display:none"><bean:message key="label.firstCarOwnerId" bundle="PARKING_RESOURCES"/>:</div></th>
			<td><html:checkbox name="parkingForm" property="Id1" onclick="enableDisableElement(this,'Id1')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox></td>
		</tr>
		</table>
			<div id="Id1">
				<fr:edit name="parkingRequestFactoryEditor" schema="edit.parkingRequestFactory.firstCarId.notOwnCar"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>					
				</fr:edit>
			</div>
		<table class="tstyle1a thright thlight mtop0 mbottom0 tstylepark">
		<tr>
			<th><div id="declaration1Top" style="display:none"><bean:message key="label.firstDeclarationAuthorization" bundle="PARKING_RESOURCES"/>:</div></th>
			<td><html:checkbox name="parkingForm" property="declaration1" onclick="enableDisableElement(this,'declaration1')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox></td>
		</tr>
		</table>
			<div id="declaration1">
				<fr:edit name="parkingRequestFactoryEditor" schema="edit.parkingRequestFactory.firstCarAuthorization.notOwnCar"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>				
				</fr:edit>
			</div>
		
			</div>
					
	<!-- SECOND CAR BEGINING -->
			
<p class="mbottom0"><strong><bean:message key="label.secondCar" bundle="PARKING_RESOURCES" /></strong>:</p>
		<table class="tstyle1a thright thlight mtop025 mbottom0 tstylepark">
		<tr>
			<th><bean:message key="label.secondCarMake" bundle="PARKING_RESOURCES"/>:</th>
			<td><fr:edit name="parkingRequestFactoryEditor" slot="secondCarMake" validator="net.sourceforge.fenixedu.renderers.validators.RequiredValidator"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor">
			</fr:edit></td>
		</tr>
		<tr>
			<th><bean:message key="label.secondCarPlateNumber" bundle="PARKING_RESOURCES"/>:</th>
			<td><fr:edit name="parkingRequestFactoryEditor" slot="secondCarPlateNumber" validator="net.sourceforge.fenixedu.renderers.validators.RequiredValidator"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor">
			</fr:edit> (aa-bb-cc)</td>			
		</tr>
		<tr>
			<th><div id="registry2Top" style="display:none"><bean:message key="label.secondCarPropertyRegistry" bundle="PARKING_RESOURCES"/>:</div></th>
			<td>
				<html:checkbox name="parkingForm" property="registry2" onclick="enableDisableElement(this,'registry2')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox>
			</td>			
		</tr>
		</table>

			<div id="registry2">
				<fr:edit name="parkingRequestFactoryEditor" schema="edit.parkingRequestFactory.secondCarRegistry"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>
				</fr:edit>
			</div>

		<table class="tstyle1a thright thlight mtop0 mbottom0 tstylepark">
		<tr>
			<th><div id="insurance2Top" style="display:none"><bean:message key="label.secondInsurance" bundle="PARKING_RESOURCES"/>:</div></th>
			<td>
				<html:checkbox name="parkingForm" property="insurance2" onclick="enableDisableElement(this,'insurance2')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox>
			</td>
		</tr>
		</table>
			<div id="insurance2">
				<fr:edit name="parkingRequestFactoryEditor" schema="edit.parkingRequestFactory.secondCarInsurance"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>				
				</fr:edit>
			</div>
			
			
			<p>
				<bean:message key="label.ownVehicle" bundle="PARKING_RESOURCES"/>
				<html:radio name="parkingForm" property="ownVehicle2" value="true" onclick="document.getElementById('ownCar2').style.display='none'"/>
					<bean:message key="label.yes" bundle="PARKING_RESOURCES"/>
				<html:radio name="parkingForm" property="ownVehicle2" value="false" onclick="document.getElementById('ownCar2').style.display='block'"/>
					<bean:message key="label.no" bundle="PARKING_RESOURCES"/>	
			</p>
			<div id="ownCar2">
		<table class="tstyle1a thright thlight mtop025 mbottom0 tstylepark">
		<tr>
			<th><div id="Id2Top" style="display:none"><bean:message key="label.secondCarOwnerId" bundle="PARKING_RESOURCES"/>:</div></th>
			<td><html:checkbox name="parkingForm" property="Id2" onclick="enableDisableElement(this,'Id2')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox></td>
		</tr>
		</table>
			<div id="Id2">
				<fr:edit name="parkingRequestFactoryEditor" schema="edit.parkingRequestFactory.secondCarId.notOwnCar"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>					
				</fr:edit>
			</div>
		<table class="tstyle1a thright thlight mtop0 mbottom0 tstylepark">
		<tr>
			<th><div id="declaration2Top" style="display:none"><bean:message key="label.firstDeclarationAuthorization" bundle="PARKING_RESOURCES"/>:</div></th>
			<td><html:checkbox name="parkingForm" property="declaration2" onclick="enableDisableElement(this,'declaration2')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox></td>
		</tr>
		</table>
			<div id="declaration2">
				<fr:edit name="parkingRequestFactoryEditor" schema="edit.parkingRequestFactory.secondCarAuthorization.notOwnCar"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>				
				</fr:edit>
			</div>
		
			</div>
			
		<input type="submit" value="<%= submit.toString() %>" />		

		</fr:form>
	</logic:notEmpty>


	<!-- CREATE THE FIRST REQUEST -->


	<logic:empty name="parkingParty" property="parkingRequests">
		<fr:form action="/parking.do?method=createParkingRequest" encoding="multipart/form-data">
			<bean:define id="parkingRequestFactoryCreator" name="parkingParty"
				property="parkingRequestFactoryCreator" />

				<p class="mbottom0"><strong><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES" /></strong>:</p>	
		<table class="tstyle1a thright thlight mbottom0 tstylepark">
		<tr>
			<th><div id="driverLicenseTop" style="display:none"><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES"/>:</div></th>
			<td>
				<html:checkbox name="parkingForm" property="driverLicense" onclick="enableDisableElement(this,'driverLicense')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox>
			</td>	
		</tr>
		</table>
			<div id="driverLicense">
				<fr:edit name="parkingRequestFactoryCreator" schema="create.parkingRequestFactory.driverLicense"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$parkingRequestFactoryCreator">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>
				</fr:edit>
			</div>
	
			
	<!-- FIRST CAR BEGINING -->
	<p class="mbottom0"><strong><bean:message key="label.firstCar" bundle="PARKING_RESOURCES" /></strong>:</p>
		<table class="tstyle1a thright thlight mtop025 mbottom0 tstylepark">
		<tr>
			<th><bean:message key="label.firstCarMake" bundle="PARKING_RESOURCES"/>:</th>
			<td><fr:edit name="parkingRequestFactoryCreator" slot="firstCarMake" validator="net.sourceforge.fenixedu.renderers.validators.RequiredValidator"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$parkingRequestFactoryCreator">
			</fr:edit></td>
		</tr>
		<tr>
			<th><bean:message key="label.firstCarPlateNumber" bundle="PARKING_RESOURCES"/>:</th>
			<td><fr:edit name="parkingRequestFactoryCreator" slot="firstCarPlateNumber" validator="net.sourceforge.fenixedu.renderers.validators.RequiredValidator"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$parkingRequestFactoryCreator">
			</fr:edit></td>
		</tr>
		<tr>
			<th><div id="registry1Top" style="display:none"><bean:message key="label.firstCarPropertyRegistry" bundle="PARKING_RESOURCES"/>:</div></th>
			<td>
				<html:checkbox name="parkingForm" property="registry1" onclick="enableDisableElement(this,'registry1')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox>
			</td>			
		</tr>
		</table>

			<div id="registry1">
				<fr:edit name="parkingRequestFactoryCreator" schema="create.parkingRequestFactory.firstCarRegistry"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$parkingRequestFactoryCreator">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>
				</fr:edit>
			</div>

		<table class="tstyle1a thright thlight mtop0 mbottom0 tstylepark">
		<tr>
			<th><div id="insurance1Top" style="display:none"><bean:message key="label.firstInsurance" bundle="PARKING_RESOURCES"/>:</div></th>
			<td>
				<html:checkbox name="parkingForm" property="insurance1" onclick="enableDisableElement(this,'insurance1')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox>
			</td>
		</tr>
		</table>
			<div id="insurance1">
				<fr:edit name="parkingRequestFactoryCreator" schema="create.parkingRequestFactory.firstCarInsurance"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$parkingRequestFactoryCreator">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>				
				</fr:edit>
			</div>
			
			
			<p>
				<bean:message key="label.ownVehicle" bundle="PARKING_RESOURCES"/>
				<html:radio name="parkingForm" property="ownVehicle1" value="true" onclick="document.getElementById('ownCar1').style.display='none'"/>
					<bean:message key="label.yes" bundle="PARKING_RESOURCES"/>
				<html:radio name="parkingForm" property="ownVehicle1" value="false" onclick="document.getElementById('ownCar1').style.display='block'"/>
					<bean:message key="label.no" bundle="PARKING_RESOURCES"/>				
			</p>
			<div id="ownCar1">
		<table class="tstyle1a thright thlight mtop025 mbottom0 tstylepark">
		<tr>
			<th><div id="Id1Top" style="display:none"><bean:message key="label.firstCarOwnerId" bundle="PARKING_RESOURCES"/>:</div></th>
			<td><html:checkbox name="parkingForm" property="Id1" onclick="enableDisableElement(this,'Id1')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox></td>
		</tr>
		</table>
			<div id="Id1">
				<fr:edit name="parkingRequestFactoryCreator" schema="create.parkingRequestFactory.firstCarId.notOwnCar"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$parkingRequestFactoryCreator">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>					
				</fr:edit>
			</div>
		<table class="tstyle1a thright thlight mtop0 mbottom0 tstylepark">
		<tr>
			<th><div id="declaration1Top" style="display:none"><bean:message key="label.firstDeclarationAuthorization" bundle="PARKING_RESOURCES"/>:</div></th>
			<td><html:checkbox name="parkingForm" property="declaration1" onclick="enableDisableElement(this,'declaration1')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox></td>
		</tr>
		</table>
			<div id="declaration1">
				<fr:edit name="parkingRequestFactoryCreator" schema="create.parkingRequestFactory.firstCarAuthorization.notOwnCar"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$parkingRequestFactoryCreator">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>				
				</fr:edit>
			</div>
		
			</div>
					
	<!-- SECOND CAR BEGINING -->
			
<p class="mbottom0"><strong><bean:message key="label.secondCar" bundle="PARKING_RESOURCES" /></strong>:</p>
		<table class="tstyle1a thright thlight mtop025 mbottom0 tstylepark">
		<tr>
			<th><bean:message key="label.secondCarMake" bundle="PARKING_RESOURCES"/>:</th>
			<td><fr:edit name="parkingRequestFactoryCreator" slot="secondCarMake" validator="net.sourceforge.fenixedu.renderers.validators.RequiredValidator"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$parkingRequestFactoryCreator">
			</fr:edit></td>
		</tr>
		<tr>
			<th><bean:message key="label.secondCarPlateNumber" bundle="PARKING_RESOURCES"/>:</th>
			<td><fr:edit name="parkingRequestFactoryCreator" slot="secondCarPlateNumber" validator="net.sourceforge.fenixedu.renderers.validators.RequiredValidator"
				type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$parkingRequestFactoryCreator">
			</fr:edit></td>
		</tr>
		<tr>
			<th><div id="registry2Top" style="display:none"><bean:message key="label.secondCarPropertyRegistry" bundle="PARKING_RESOURCES"/>:</div></th>
			<td>
				<html:checkbox name="parkingForm" property="registry2" onclick="enableDisableElement(this,'registry2')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox>
			</td>			
		</tr>
		</table>

			<div id="registry2">
				<fr:edit name="parkingRequestFactoryCreator" schema="create.parkingRequestFactory.secondCarRegistry"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$parkingRequestFactoryCreator">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>
				</fr:edit>
			</div>

		<table class="tstyle1a thright thlight mtop0 mbottom0 tstylepark">
		<tr>
			<th><div id="insurance2Top" style="display:none"><bean:message key="label.secondInsurance" bundle="PARKING_RESOURCES"/>:</div></th>
			<td>
				<html:checkbox name="parkingForm" property="insurance2" onclick="enableDisableElement(this,'insurance2')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox>
			</td>
		</tr>
		</table>
			<div id="insurance2">
				<fr:edit name="parkingRequestFactoryCreator" schema="create.parkingRequestFactory.secondCarInsurance"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$parkingRequestFactoryCreator">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>				
				</fr:edit>
			</div>
			
			
			<p>
				<bean:message key="label.ownVehicle" bundle="PARKING_RESOURCES"/>
				<html:radio name="parkingForm" property="ownVehicle2" value="true" onclick="document.getElementById('ownCar2').style.display='none'"/>
					<bean:message key="label.yes" bundle="PARKING_RESOURCES"/>
				<html:radio name="parkingForm" property="ownVehicle2" value="false" onclick="document.getElementById('ownCar2').style.display='block'"/>
					<bean:message key="label.no" bundle="PARKING_RESOURCES"/>	
			</p>
			<div id="ownCar2">
		<table class="tstyle1a thright thlight mtop025 mbottom0 tstylepark">
		<tr>
			<th><div id="Id2Top" style="display:none"><bean:message key="label.secondCarOwnerId" bundle="PARKING_RESOURCES"/>:</div></th>
			<td><html:checkbox name="parkingForm" property="Id2" onclick="enableDisableElement(this,'Id2')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox></td>
		</tr>
		</table>
			<div id="Id2">
				<fr:edit name="parkingRequestFactoryCreator" schema="create.parkingRequestFactory.secondCarId.notOwnCar"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$parkingRequestFactoryCreator">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>					
				</fr:edit>
			</div>
		<table class="tstyle1a thright thlight mtop0 mbottom0 tstylepark">
		<tr>
			<th><div id="declaration2Top" style="display:none"><bean:message key="label.firstDeclarationAuthorization" bundle="PARKING_RESOURCES"/>:</div></th>
			<td><html:checkbox name="parkingForm" property="declaration2" onclick="enableDisableElement(this,'declaration2')">
				<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES"/></html:checkbox></td>
		</tr>
		</table>
			<div id="declaration2">
				<fr:edit name="parkingRequestFactoryCreator" schema="create.parkingRequestFactory.secondCarAuthorization.notOwnCar"
					type="net.sourceforge.fenixedu.domain.parking.ParkingRequest$parkingRequestFactoryCreator">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1a thright thlight mtop0 tstylepark"/>
						<fr:property name="columnClasses" value=",,noborder"/>
					</fr:layout>				
				</fr:edit>
			</div>
		
			</div>
			
		<input type="submit" value="<%= submit.toString() %>" />
		</fr:form>
		
	</logic:empty>

</logic:present>

	<script type="text/javascript">
		hide2ndPartForms();
	</script>

