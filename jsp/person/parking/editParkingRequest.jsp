<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>

<script language="Javascript" type="text/javascript">
<!--

function hideStudentRadioButtons(){
	document.getElementsByName('driverLicense')[2].checked=true;
	document.getElementById('driverLicenseDiv').style.display='block';
	document.getElementById('driverLicenseRadio').style.display='none';
	document.getElementById('driverLicenseStudent').style.display='block';

	document.getElementsByName('registry1')[2].checked=true;
	document.getElementById('registry1Div').style.display='block';
	document.getElementById('registry1Radio').style.display='none';		
	document.getElementById('registry1Student').style.display='block';

	document.getElementsByName('Id1')[2].checked=true;
	document.getElementById('Id1Div').style.display='block';
	document.getElementById('Id1Radio').style.display='none';	
	document.getElementById('Id1Student').style.display='block';	

	document.getElementsByName('declaration1')[1].checked=true;
	document.getElementById('declaration1Div').style.display='block';
	document.getElementById('declaration1Radio').style.display='none';
	document.getElementById('declaration1Student').style.display='block';				

	document.getElementsByName('registry2')[2].checked=true;
	document.getElementById('registry2Div').style.display='block';
	document.getElementById('registry2Radio').style.display='none';		
	document.getElementById('registry2Student').style.display='block';		

	document.getElementsByName('Id2')[2].checked=true;
	document.getElementById('Id2Div').style.display='block';
	document.getElementById('Id2Radio').style.display='none';
	document.getElementById('Id2Student').style.display='block';		
	
	document.getElementsByName('declaration2')[1].checked=true;
	document.getElementById('declaration2Div').style.display='block';
	document.getElementById('declaration2Radio').style.display='none';
	document.getElementById('declaration2Student').style.display='block';	
}

function hideInputBoxes(){
	if(document.getElementById('ownCar1radio1').checked==true){
		document.getElementById('ownCar1').style.display='none';
	}
	if(document.getElementById('ownCar2radio1').checked==true){
		document.getElementById('ownCar2').style.display='none';
	}
	if(document.getElementById('hasVehicle2radio1').checked!=true){
		document.getElementById('hasVehicle2').style.display='none';
	}
	if(document.getElementsByName('driverLicense')[2].checked==false){
		document.getElementById('driverLicenseDiv').style.display='none';
		document.getElementById('driverLicenseFile').style.display='none';		
		document.getElementById('driverLicenseDivTop').style.display='block';
	}
	if(document.getElementsByName('registry1')[2].checked==false){
		document.getElementById('registry1Div').style.display='none';
		document.getElementById('registry1File').style.display='none';		
		document.getElementById('registry1DivTop').style.display='block';
	}
	if(document.getElementsByName('Id1')[2].checked==false){
		document.getElementById('Id1Div').style.display='none';
		document.getElementById('Id1File').style.display='none';		
		document.getElementById('Id1DivTop').style.display='block';
	}
	if(document.getElementsByName('declaration1')[1].checked==false){	
		document.getElementById('declaration1Div').style.display='none';
		document.getElementById('declaration1File').style.display='none';		
		document.getElementById('declaration1DivTop').style.display='block';
	}
	if(document.getElementsByName('registry2')[2].checked==false){
		document.getElementById('registry2Div').style.display='none';
		document.getElementById('registry2File').style.display='none';		
		document.getElementById('registry2DivTop').style.display='block';
	}
	if(document.getElementsByName('Id2')[2].checked==false){
		document.getElementById('Id2Div').style.display='none';
		document.getElementById('Id2File').style.display='none';		
		document.getElementById('Id2DivTop').style.display='block';
	}
	if(document.getElementsByName('declaration2')[1].checked==false){
		document.getElementById('declaration2Div').style.display='none';
		document.getElementById('declaration2File').style.display='none';		
		document.getElementById('declaration2DivTop').style.display='block';
	}
}

function changeElementsDisplay(elementId, elementId2,elementDisplay, topDisplay){
		document.getElementById(elementId).style.display=elementDisplay;
		document.getElementById(elementId2).style.display=elementDisplay;		
		document.getElementById(elementId+'Top').style.display=topDisplay;
}
// -->
</script>


<style>
.separator1 {
border-bottom: 1px solid #ddd;
margin: 0.5em 0;
}

</style>


<h2><bean:message key="label.parking" bundle="PARKING_RESOURCES" /></h2>

<span class="error0 mtop0"><html:messages id="message" property="timeout" message="true" bundle="PARKING_RESOURCES">
	<bean:write name="message"/><br/>
</html:messages></span>

<div class="infoop2">
	<p class="mvert025"><bean:message key="title.regulation" bundle="PARKING_RESOURCES"/>:</p>
	<ul>
		<li><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES"/></li>		
		<li><bean:message key="title.registo" bundle="PARKING_RESOURCES"/> <span class="greytxt1">(<bean:message key="title.forEachVehicle" bundle="PARKING_RESOURCES"/>)</span></li>
		<li><bean:message key="title.ownerId" bundle="PARKING_RESOURCES"/> <span class="greytxt1">(<bean:message key="title.forEachVehicleNotOwned" bundle="PARKING_RESOURCES"/>)</span></li>
		<bean:define id="url" type="java.lang.String">
			<html:link page="/parking.do?method=downloadAuthorizationDocument">
				<bean:message key="title.anexIV.label" bundle="PARKING_RESOURCES" />
			</html:link>
		</bean:define>
		<li><bean:message key="title.anexIV" bundle="PARKING_RESOURCES" arg0="<%= url %>"/> <span class="greytxt1">(<bean:message key="title.forEachVehicleNotOwned" bundle="PARKING_RESOURCES"/>)</span></li>
		<p class="mtop025"><em><bean:message key="message.forMoreThan2Cars" bundle="PARKING_RESOURCES"/></em></p>
	</ul>		
	
	<p class="mvert025"><bean:message key="title.documentDelivery" bundle="PARKING_RESOURCES"/>:</p>
	<ul>
		<li><bean:message key="title.documentDeliveryOnline" bundle="PARKING_RESOURCES"/></li>
		<li><bean:message key="title.documentDeliveryPaper" bundle="PARKING_RESOURCES"/></li>
	</ul>
	<p class="mvert025"><bean:message key="message.toSubmitDocuments" bundle="PARKING_RESOURCES"/></p>
	<ul>
		<li><bean:message key="message.maxFileSize" bundle="PARKING_RESOURCES"/></li>
		<li><bean:message key="message.fileFormat" bundle="PARKING_RESOURCES"/></li>
		<li><bean:message key="message.howToSubmit" bundle="PARKING_RESOURCES"/></li>
	</ul>
	<bean:message key="message.personalDataUse" bundle="PARKING_RESOURCES"/>
</div>	


<logic:present name="parkingParty">

	<bean:define id="submit">
		<bean:message key="button.submit" bundle="PARKING_RESOURCES" />
	</bean:define>
	<bean:define id="driverLicenseLabel">
		<bean:message  key="label.driverLicense" bundle="PARKING_RESOURCES"/>
	</bean:define>
	<bean:define id="propertyRegisterLabel">
		<bean:message  key="title.registo" bundle="PARKING_RESOURCES"/>
	</bean:define>
	<bean:define id="ownerId">
		<bean:message  key="title.ownerId" bundle="PARKING_RESOURCES"/>
	</bean:define>
	<bean:define id="authorizationDeclaration">
		<bean:message  key="label.firstDeclarationAuthorization" bundle="PARKING_RESOURCES"/>
	</bean:define>
	
	<bean:define id="method" value="createParkingRequest"/>
	<bean:define id="action" value="create"/>
	<bean:define id="factoryName" value="parkingRequestFactoryCreator"/>
	<bean:define id="type" value="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryCreator"/>

	<logic:notEmpty name="parkingParty" property="parkingRequests">
		<bean:define id="method" value="editParkingRequest"/>
		<bean:define id="action" value="edit"/>
		<bean:define id="factoryName" value="parkingRequestFactoryEditor"/>
		<bean:define id="type" value="net.sourceforge.fenixedu.domain.parking.ParkingRequest$ParkingRequestFactoryEditor"/>
	</logic:notEmpty>

	
	<span class="error0 mtop0"><html:messages id="message" property="fileError" message="true" bundle="PARKING_RESOURCES">
		<bean:write name="message"/><br/>
	</html:messages></span>
	<fr:form action="<%= "/parking.do?method="+method%>" encoding="multipart/form-data">
		<p class="mtop2 mbottom025"><strong><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES" /></strong></p>
		<div id="driverLicenseRadio">	
			<div id="driverLicenseDivTop" style="display:none"></div>

				<p>
				<html:radio name="parkingForm" property="driverLicense" value="ALREADY_DELIVERED_HARD_COPY" onclick="changeElementsDisplay('driverLicenseDiv','driverLicenseFile', 'none', 'block')">
					<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES" arg0="<%=driverLicenseLabel%>"/>
				</html:radio>
				</p>
				
				<p>
				<html:radio name="parkingForm" property="driverLicense" value="WILL_DELIVER_HARD_COPY" onclick="changeElementsDisplay('driverLicenseDiv','driverLicenseFile', 'none', 'block')">
					<bean:message key="label.willdeliverDocument" bundle="PARKING_RESOURCES" arg0="<%=driverLicenseLabel%>"/>
				</html:radio>
				</p>
				
				<p>
				<html:radio name="parkingForm" property="driverLicense" value="ELECTRONIC_DELIVERY" onclick="changeElementsDisplay('driverLicenseDiv','driverLicenseFile', 'block', 'none')">
					<bean:message key="label.deliverOnlineDocument" bundle="PARKING_RESOURCES" arg0="<%=driverLicenseLabel%>"/>
				</html:radio>
				</p>
				
				<div id="driverLicenseFile">
				<logic:notEmpty name="<%= factoryName %>" property="driverLicenseFileName">
					<p>
					<span class="warning0 mtop05">
						<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=driverLicenseLabel%>"/>:
						<bean:write name="<%= factoryName %>" property="driverLicenseFileName"/>
					</span>
					</p>
				</logic:notEmpty>
				</div>
			</div>
		<div id="driverLicenseStudent" style="display:none">
		<logic:notEmpty name="<%= factoryName %>" property="driverLicenseFileName">
			<table class="tstyle8 thright thlight mbottom0 mtop0 tstylepark">
				<tr>
					<th class="width150px"/>
					<td>						
						<span class="warning0 mtop025">
							<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=driverLicenseLabel%>"/>:
							<bean:write name="<%= factoryName %>" property="driverLicenseFileName"/>
						</span>											
					</td>
				</tr>
			</table>
		</logic:notEmpty>	
		</div>
			<div id="driverLicenseDiv">
				<fr:edit id="driverLicenseFR" name="<%= factoryName %>" schema="<%= action+".parkingRequestFactory.driverLicense"%>"
					type="<%= type %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle8 thright thlight mvert0"/>
						<fr:property name="columnClasses" value="width150px,,noborder"/>
					</fr:layout>
				</fr:edit>			

				<span class="error0 mtop0"><html:messages id="message" property="driverLicenseMessage" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/>
				</html:messages></span>

			</div>

			<span class="error0 mtop0"><html:messages id="message" property="driverLicenseDeliveryMessage" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>



<p class="mvert2">
	<bean:message key="message.howManyVehicles" bundle="PARKING_RESOURCES"/>
	<html:radio styleId="hasVehicle2radio2" name="parkingForm" property="vehicle2" value="false" onclick="document.getElementById('hasVehicle2').style.display='none'"/>Um 
	<html:radio styleId="hasVehicle2radio1" name="parkingForm" property="vehicle2" value="true" onclick="document.getElementById('hasVehicle2').style.display='block'"/>Dois
</p>
	
	<!-- FIRST CAR BEGINING -->
	<p class="mbottom025"><strong><bean:message key="label.firstCar" bundle="PARKING_RESOURCES" /></strong></p>

	<div class="separator1"></div>

		<table class="tstyle8 thright thlight mtop025 mbottom0">
		<tr>
			<th class="width150px"><bean:message key="label.firstCarMake" bundle="PARKING_RESOURCES"/>:</th>
			<td><fr:edit id="firstCarMakeFR" name="<%= factoryName %>" slot="firstCarMake" 
				type="<%= type %>">
						<fr:layout>
							<fr:property name="size" value="25"/>
							<fr:property name="maxLength" value="20"/>
						</fr:layout>
			</fr:edit>
			<span class="error0 mtop025">
				<html:messages id="message" property="firstCarMakePT" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/>
				</html:messages></span>
			</td>
			<td class="noborder">
			</td>
		</tr>
		<tr>
			<th class="width150px"><bean:message key="label.firstCarPlateNumber" bundle="PARKING_RESOURCES"/>:</th>
			<td><fr:edit id="firstCarPlateNumberFR" name="<%= factoryName %>" slot="firstCarPlateNumber"
				type="<%= type %>">
						<fr:layout>
							<fr:property name="size" value="10"/>
							<fr:property name="maxLength" value="10"/>
						</fr:layout>
			</fr:edit> (aa-bb-cc)
			<span class="error0 mtop025">
				<html:messages id="message" property="firstCarPlateNumberPT" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/>
				</html:messages></span>
			</td>
			<td class="noborder">
			</td>
		</tr>
		</table>
		<div id="registry1Radio">
		<table class="tstyle8 thright thlight mtop0 mbottom0">
		<tr>
			<th class="width150px"><div id="registry1DivTop" style="display:none"><bean:message key="label.firstCarPropertyRegistry" bundle="PARKING_RESOURCES"/>:</div></th>
			<td>
				<p>
				<html:radio name="parkingForm" property="registry1" value="ALREADY_DELIVERED_HARD_COPY" onclick="changeElementsDisplay('registry1Div','registry1File', 'none', 'block')">
					<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>
				</html:radio>
				</p>
				<p>
				<html:radio name="parkingForm" property="registry1" value="WILL_DELIVER_HARD_COPY" onclick="changeElementsDisplay('registry1Div','registry1File', 'none', 'block')">
					<bean:message key="label.willdeliverDocument" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>
				</html:radio>
				</p>
				<p>
				<html:radio name="parkingForm" property="registry1" value="ELECTRONIC_DELIVERY" onclick="changeElementsDisplay('registry1Div','registry1File', 'block', 'none')">
					<bean:message key="label.deliverOnlineDocument" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>
				</html:radio>
				</p>
				<div id="registry1File">
				<logic:notEmpty name="<%= factoryName %>" property="firstCarPropertyRegistryFileName">
					<p>
					<span class="warning0 mtop05">				
						<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>:
						<bean:write name="<%= factoryName %>" property="firstCarPropertyRegistryFileName"/>
					</span>
					</p>
				</logic:notEmpty>
				</div>
			</td>
			<td class="noborder"></td>
		</tr>
		</table>
		</div>
		<div id="registry1Student" style="display:none">
		<logic:notEmpty name="<%= factoryName %>" property="firstCarPropertyRegistryFileName">
			<table class="tstyle8 thright thlight mbottom0 mtop0 tstylepark">
				<tr>
					<th class="width150px"/>
					<td>						
						<span class="warning0 mtop025">
							<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>:
							<bean:write name="<%= factoryName %>" property="firstCarPropertyRegistryFileName"/>
						</span>											
					</td>
				</tr>
			</table>
		</logic:notEmpty>
		</div>
			<div id="registry1Div">
				<fr:edit id="registry1FR" name="<%= factoryName %>"  schema="<%= action+".parkingRequestFactory.firstCarRegistry"%>"
					type="<%= type %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle8 thright thlight mtop0 mbottom0"/>
						<fr:property name="columnClasses" value="width150px,,noborder"/>
					</fr:layout>
				</fr:edit>					

				<span class="error0 mtop025"><html:messages id="message" property="firstCarPropertyRegistryMessage" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/>
				</html:messages></span>

			</div>

			<span class="error0 mtop0"><html:messages id="message" property="firstCarPropertyRegistryDeliveryMessage" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>

	<div class="separator1"></div>

			<p>
				<bean:message key="label.ownVehicle" bundle="PARKING_RESOURCES"/>? 
				<html:radio styleId="ownCar1radio1" name="parkingForm" property="ownVehicle1" value="true" onclick="document.getElementById('ownCar1').style.display='none'"/>
					<bean:message key="label.yes" bundle="PARKING_RESOURCES"/>
				<html:radio styleId="ownCar1radio2" name="parkingForm" property="ownVehicle1" value="false" onclick="document.getElementById('ownCar1').style.display='block'"/>
					<bean:message key="label.no" bundle="PARKING_RESOURCES"/>				
			</p>

		
		<div id="ownCar1">
	
	<div class="separator1"></div>
		<div id="Id1Radio">
		<table class="tstyle8 thright thlight mtop025 mbottom0">
		<tr>
			<th class="width150px"><div id="Id1DivTop" style="display:none"><bean:message key="label.firstCarOwnerId" bundle="PARKING_RESOURCES"/>:</div></th>
			<td>
				<p>
				<html:radio name="parkingForm" property="Id1" value="ALREADY_DELIVERED_HARD_COPY" onclick="changeElementsDisplay('Id1Div','Id1File', 'none', 'block')">
					<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>
				</html:radio>
				</p>
				<p>
				<html:radio name="parkingForm" property="Id1" value="WILL_DELIVER_HARD_COPY" onclick="changeElementsDisplay('Id1Div','Id1File', 'none', 'block')">
					<bean:message key="label.willdeliverDocument" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>
				</html:radio>
				</p>
				<p>				
				<html:radio name="parkingForm" property="Id1" value="ELECTRONIC_DELIVERY" onclick="changeElementsDisplay('Id1Div','Id1File', 'block', 'none')">
					<bean:message key="label.deliverOnlineDocument" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>
				</html:radio>
				</p>
				<div id="Id1File">
				<logic:notEmpty name="<%= factoryName %>" property="firstCarOwnerIdFileName">
					<p>
					<span class="warning0 mtop05">
						<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>:
						<bean:write name="<%= factoryName %>" property="firstCarOwnerIdFileName"/>
					</span>
					</p>
				</logic:notEmpty>
				</div>
			</td>	
		</tr>
		</table>
		</div>
		<div id="Id1Student" style="display:none">
		<logic:notEmpty name="<%= factoryName %>" property="firstCarOwnerIdFileName">
			<table class="tstyle8 thright thlight mbottom0 mtop0 tstylepark">
				<tr>
					<th class="width150px"/>
					<td>						
						<span class="warning0 mtop025">
							<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>:
							<bean:write name="<%= factoryName %>" property="firstCarOwnerIdFileName"/>
						</span>											
					</td>
				</tr>
			</table>
		</logic:notEmpty>
		</div>		
			<div id="Id1Div">
				<fr:edit id="Id1FR" name="<%= factoryName %>" schema="<%= action+".parkingRequestFactory.firstCarId.notOwnCar"%>"
					type="<%= type %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle8 thright thlight mtop0 mbottom0"/>
						<fr:property name="columnClasses" value="width150px,,noborder"/>
					</fr:layout>					
				</fr:edit>

				<span class="error0 mtop025"><html:messages id="message" property="firstCarOwnerIdMessage" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/>
				</html:messages></span>

			</div>

			<span class="error0 mtop0"><html:messages id="message" property="firstCarOwnerIdDeliveryMessage" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>	

	<div class="separator1"></div>
		<div id="declaration1Radio">
		<table class="tstyle8 thright thlight mtop0 mbottom0">
		<tr>
			<th class="width150px"><div id="declaration1DivTop" style="display:none"><bean:message key="label.firstDeclarationAuthorization" bundle="PARKING_RESOURCES"/>:</div></th>
			<td>
				<p>
				<html:radio name="parkingForm" property="declaration1" value="WILL_DELIVER_HARD_COPY" onclick="changeElementsDisplay('declaration1Div','Id1File', 'none', 'block')">
					<bean:message key="label.willdeliverDocument" bundle="PARKING_RESOURCES" arg0="<%=authorizationDeclaration%>"/>
				</html:radio>				
				</p>
				<p>
				<html:radio name="parkingForm" property="declaration1" value="ELECTRONIC_DELIVERY" onclick="changeElementsDisplay('declaration1Div','declaration1File', 'block', 'none')">
					<bean:message key="label.deliverOnlineDocument" bundle="PARKING_RESOURCES" arg0="<%=authorizationDeclaration%>"/>
				</html:radio>
				</p>
				<div id="declaration1File">
				<logic:notEmpty name="<%= factoryName %>" property="firstDeclarationAuthorizationFileName">
					<p>
					<span class="warning0 mtop05">
						<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=authorizationDeclaration%>"/>:
						<bean:write name="<%= factoryName %>" property="firstDeclarationAuthorizationFileName"/>
					</span>	
					</p>
				</logic:notEmpty>				
				</div>
			</td>					
		</tr>
		</table>
		</div>
		<div id="declaration1Student" style="display:none">
		<logic:notEmpty name="<%= factoryName %>" property="firstDeclarationAuthorizationFileName">
			<table class="tstyle8 thright thlight mbottom0 mtop0 tstylepark">
				<tr>
					<th class="width150px"/>
					<td>						
						<span class="warning0 mtop025">
							<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=authorizationDeclaration%>"/>:
							<bean:write name="<%= factoryName %>" property="firstDeclarationAuthorizationFileName"/>
						</span>											
					</td>
				</tr>
			</table>
		</logic:notEmpty>
		</div>
			<div id="declaration1Div">
				<fr:edit id="declaration1FR" name="<%= factoryName %>" schema="<%= action+".parkingRequestFactory.firstCarAuthorization.notOwnCar"%>"
					type="<%= type %>">
				
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle8 thright thlight mtop0 mbottom0"/>
						<fr:property name="columnClasses" value="width150px,,noborder"/>
					</fr:layout>				
				</fr:edit>
	
				<span class="error0 mtop025"><html:messages id="message" property="firstDeclarationAuthorizationMessage" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/>
				</html:messages></span>

			</div>

			<span class="error0 mtop0"><html:messages id="message" property="firstDeclarationAuthorizationDeliveryMessage" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>	

			</div>
	<div class="separator1"></div>
	
	<!-- SECOND CAR BEGINING -->

<div id="hasVehicle2">

		<p class="mtop2"><strong><bean:message key="label.secondCar" bundle="PARKING_RESOURCES" /></strong></p>
		
	<div class="separator1"></div>
		<table class="tstyle8 thright thlight mtop025 mbottom0">
		<tr>
			<th class="width150px"><bean:message key="label.secondCarMake" bundle="PARKING_RESOURCES"/>:</th>
			<td><fr:edit id="secondCarMakeFR" name="<%= factoryName %>" slot="secondCarMake" 
					type="<%= type %>">
						<fr:layout>
							<fr:property name="size" value="25"/>
							<fr:property name="maxLength" value="20"/>
						</fr:layout>					
			</fr:edit>
			<span class="error0 mtop025">
				<html:messages id="message" property="secondCarMakePT" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/>
				</html:messages></span></td>
			<td class="noborder">
			</td>
		</tr>
		<tr>
			<th class="width150px"><bean:message key="label.secondCarPlateNumber" bundle="PARKING_RESOURCES"/>:</th>
			<td><fr:edit id="secondCarPlateNumberFR" name="<%= factoryName %>" slot="secondCarPlateNumber"
					type="<%= type %>">
						<fr:layout>
							<fr:property name="size" value="10"/>
							<fr:property name="maxLength" value="10"/>
						</fr:layout>					
			</fr:edit> (aa-bb-cc)
			<span class="error0 mtop025">
				<html:messages id="message" property="secondCarPlateNumberPT" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/>
				</html:messages></span></td>		
			<td class="noborder">
			</td>	
		</tr>
		</table>
		<div id="registry2Radio">
		<table class="tstyle8 thright thlight mtop0 mbottom0">
		<tr>
			<th class="width150px"><div id="registry2DivTop" style="display:none"><bean:message key="label.secondCarPropertyRegistry" bundle="PARKING_RESOURCES"/>:</div></th>
			<td>
				<p>
				<html:radio name="parkingForm" property="registry2" value="ALREADY_DELIVERED_HARD_COPY" onclick="changeElementsDisplay('registry2Div','registry2File', 'none', 'block')">
					<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>
				</html:radio>
				</p>
				<p>
				<html:radio name="parkingForm" property="registry2" value="WILL_DELIVER_HARD_COPY" onclick="changeElementsDisplay('registry2Div','registry2File', 'none', 'block')">
					<bean:message key="label.willdeliverDocument" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>
				</html:radio>
				</p>						
				<p>
				<html:radio name="parkingForm" property="registry2" value="ELECTRONIC_DELIVERY" onclick="changeElementsDisplay('registry2Div','registry2File', 'block', 'none')">
					<bean:message key="label.deliverOnlineDocument" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>
				</html:radio>
				</p>
				<div id="registry2File">
				<logic:notEmpty name="<%= factoryName %>" property="secondCarPropertyRegistryFileName">
					<p>
					<span class="warning0 mtop05">
						<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>:
						<bean:write name="<%= factoryName %>" property="secondCarPropertyRegistryFileName"/>
					</span>
					</p>
				</logic:notEmpty>				
				</div>
			</td>	
		</tr>
		</table>
		</div>

		<div id="registry2Student" style="display:none">
		<logic:notEmpty name="<%= factoryName %>" property="secondCarPropertyRegistryFileName">
			<table class="tstyle8 thright thlight mbottom0 mtop0 tstylepark">
				<tr>
					<th class="width150px"/>
					<td>						
						<span class="warning0 mtop025">
							<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>:
							<bean:write name="<%= factoryName %>" property="secondCarPropertyRegistryFileName"/>
						</span>											
					</td>
				</tr>
			</table>
		</logic:notEmpty>
		</div>
			<div id="registry2Div">
				<fr:edit id="registry2FR" name="<%= factoryName %>" schema="<%= action+".parkingRequestFactory.secondCarRegistry"%>"
					type="<%= type %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle8 thright thlight mtop0 mbottom0"/>
						<fr:property name="columnClasses" value="width150px,,noborder"/>
					</fr:layout>
				</fr:edit>

				<span class="error0 mtop025"><html:messages id="message" property="secondCarPropertyRegistryMessage" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/>
				</html:messages></span>

			</div>

			<span class="error0 mtop0"><html:messages id="message" property="secondCarPropertyRegistryDeliveryMessage" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>

	<div class="separator1"></div>
			
			<p>
				<bean:message key="label.ownVehicle" bundle="PARKING_RESOURCES"/>? 
				<html:radio styleId="ownCar2radio1" name="parkingForm" property="ownVehicle2" value="true" onclick="document.getElementById('ownCar2').style.display='none'"/>
					<bean:message key="label.yes" bundle="PARKING_RESOURCES"/>
				<html:radio styleId="ownCar2radio2" name="parkingForm" property="ownVehicle2" value="false" onclick="document.getElementById('ownCar2').style.display='block'"/>
					<bean:message key="label.no" bundle="PARKING_RESOURCES"/>	
			</p>
			
		<div id="ownCar2">
	<div class="separator1"></div>
		<div id="Id2Radio">
		<table class="tstyle8 thright thlight mtop025 mbottom0">
		<tr>
			<th class="width150px"><div id="Id2DivTop" style="display:none"><bean:message key="label.secondCarOwnerId" bundle="PARKING_RESOURCES"/>:</div></th>
			<td>
				<p>
				<html:radio name="parkingForm" property="Id2" value="ALREADY_DELIVERED_HARD_COPY" onclick="changeElementsDisplay('Id2Div','Id2File', 'none', 'block')">
					<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>
				</html:radio>
				</p>
				<p>
				<html:radio name="parkingForm" property="Id2" value="WILL_DELIVER_HARD_COPY" onclick="changeElementsDisplay('Id2Div','Id2File', 'none', 'block')">
					<bean:message key="label.willdeliverDocument" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>
				</html:radio>
				</p>
				<p>						
				<html:radio name="parkingForm" property="Id2" value="ELECTRONIC_DELIVERY" onclick="changeElementsDisplay('Id2Div','Id2File', 'block', 'none')">
					<bean:message key="label.deliverOnlineDocument" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>
				</html:radio>
				</p>
				<div id="Id2File">
				<logic:notEmpty name="<%= factoryName %>" property="secondCarOwnerIdFileName">
					<p>
					<span class="warning0 mtop05">				
						<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>:
						<bean:write name="<%= factoryName %>" property="secondCarOwnerIdFileName"/>
					</span>
					</p>
				</logic:notEmpty>				
				</div>
			</td>	
		</tr>
		</table>
		</div>
		<div id="Id2Student" style="display:none">
		<logic:notEmpty name="<%= factoryName %>" property="secondCarOwnerIdFileName">
			<table class="tstyle8 thright thlight mbottom0 mtop0 tstylepark">
				<tr>
					<th class="width150px"/>
					<td>						
						<span class="warning0 mtop025">
							<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>:
							<bean:write name="<%= factoryName %>" property="secondCarOwnerIdFileName"/>
						</span>											
					</td>
				</tr>
			</table>
		</logic:notEmpty>
		</div>	
			<div id="Id2Div">
				<fr:edit id="Id2FR" name="<%= factoryName %>" schema="<%= action+".parkingRequestFactory.secondCarId.notOwnCar"%>"
					type="<%= type %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle8 thright thlight mtop0 mbottom0"/>
						<fr:property name="columnClasses" value="width150px,,noborder"/>
					</fr:layout>					
				</fr:edit>

				<span class="error0 mtop025"><html:messages id="message" property="secondCarOwnerIdMessage" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/>
				</html:messages></span>

			</div>

			<span class="error0 mtop0"><html:messages id="message" property="secondCarOwnerIdDeliveryMessage" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>

	<div class="separator1"></div>	
		<div id="declaration2Radio">
		<table class="tstyle8 thright thlight mtop0 mbottom0">
		<tr>
			<th class="width150px"><div id="declaration2DivTop" style="display:none"><bean:message key="label.firstDeclarationAuthorization" bundle="PARKING_RESOURCES"/>:</div></th>
			<td>
				<p>
				<html:radio name="parkingForm" property="declaration2" value="WILL_DELIVER_HARD_COPY" onclick="changeElementsDisplay('declaration2Div','declaration2File', 'none', 'block')">
					<bean:message key="label.willdeliverDocument" bundle="PARKING_RESOURCES" arg0="<%=authorizationDeclaration%>"/>
				</html:radio>
				</p>
				<p>				
				<html:radio name="parkingForm" property="declaration2" value="ELECTRONIC_DELIVERY" onclick="changeElementsDisplay('declaration2Div','declaration2File', 'block', 'none')">
					<bean:message key="label.deliverOnlineDocument" bundle="PARKING_RESOURCES" arg0="<%=authorizationDeclaration%>"/>
				</html:radio>
				</p>
				<div id="declaration2File">
				<logic:notEmpty name="<%= factoryName %>" property="secondDeclarationAuthorizationFileName">
					<p>
					<span class="warning0 mtop05">
						<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=authorizationDeclaration%>"/>:
						<bean:write name="<%= factoryName %>" property="secondDeclarationAuthorizationFileName"/>
					</span>
					</p>
				</logic:notEmpty>
				</div>
			</td>					
		</tr>
		</table>
		</div>
		<div id="declaration2Student" style="display:none">
		<logic:notEmpty name="<%= factoryName %>" property="secondDeclarationAuthorizationFileName">
			<table class="tstyle8 thright thlight mbottom0 mtop0 tstylepark">
				<tr>
					<th class="width150px"/>
					<td>						
						<span class="warning0 mtop025">
							<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=authorizationDeclaration%>"/>:
							<bean:write name="<%= factoryName %>" property="secondDeclarationAuthorizationFileName"/>
						</span>											
					</td>
				</tr>
			</table>
		</logic:notEmpty>
		</div>
			<div id="declaration2Div">
				<fr:edit id="declaration2FR" name="<%= factoryName %>" schema="<%= action+".parkingRequestFactory.secondCarAuthorization.notOwnCar"%>"
					type="<%= type %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle8 thright thlight mtop0 mbottom0"/>
						<fr:property name="columnClasses" value="width150px,,noborder"/>
					</fr:layout>				
				</fr:edit>

				<span class="error0 mtop0"><html:messages id="message" property="secondDeclarationAuthorizationMessage" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/>
				</html:messages></span>

			</div>

			<span class="error0 mtop0"><html:messages id="message" property="secondDeclarationAuthorizationDeliveryMessage" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>

	<div class="separator1"></div>
			</div>

		</div>

		
		<logic:notEmpty name="<%= factoryName %>" property="parkingParty.submitAsRoles">
			<bean:size id="size" name="<%= factoryName %>" property="parkingParty.submitAsRoles"/>
			<logic:notEqual name="size" value="1">
			<p class="mtop2">
			<div class="separator1"></div>		
				<fr:edit name="<%= factoryName %>" schema="<%= action+".parkingRequestFactory.submitAs" %>" type="<%= type %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle8 thright thlight mtop0 mbottom0"/>
						<fr:property name="columnClasses" value="width150px,,noborder"/>
					</fr:layout>	
				</fr:edit>			
			<div class="separator1"></div>
			</p>
			</logic:notEqual>
		</logic:notEmpty>
		
		<bean:define id="person" name="<%= factoryName %>" property="parkingParty.party" type="net.sourceforge.fenixedu.domain.Person"/>
		<logic:notEqual name="person" property="partyClassification" value="TEACHER">
		<logic:notEqual name="person" property="partyClassification" value="EMPLOYEE">		
		<p class="mtop2">
			<logic:notPresent name="allowToChoose">
			<span class="infoop2">
				<bean:message key="message.requestQuotasFinishedPeriod" bundle="PARKING_RESOURCES"/>
			</span>
			</logic:notPresent>
			
			<logic:present name="allowToChoose">		
			<span class="infoop2">
				<bean:define id="link"><html:link page="/parking.do?method=downloadParkingRegulamentation"><bean:message key="link.regulation" bundle="PARKING_RESOURCES"/></html:link></bean:define>
				<bean:message key="message.requestQuotas" bundle="PARKING_RESOURCES" arg0="<%= link %>"/>
			</span>
			<div class="separator1"></div>		
				<fr:edit name="<%= factoryName %>" schema="<%= action+".parkingRequestFactory.limitlessAccessCard" %>" type="<%= type %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle8 thright thlight mtop0 mbottom0"/>
						<fr:property name="columnClasses" value="width150px,,noborder"/>
					</fr:layout>	
				</fr:edit>			
			<div class="separator1"></div>
			</logic:present>
		</p>
		</logic:notEqual>
		</logic:notEqual>
		
		<p class="mtop2">
			<input type="submit" value="<%= submit.toString() %>" />		
		</p>

		
	</fr:form>

</logic:present>

	<script type="text/javascript">
		hideInputBoxes();
	</script>

	<logic:present name="student">
		<script type="text/javascript">
			hideStudentRadioButtons();
		</script>
	</logic:present>