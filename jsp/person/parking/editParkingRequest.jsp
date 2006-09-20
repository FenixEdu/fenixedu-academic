<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>

<script language="Javascript" type="text/javascript">
<!--

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
	if(document.getElementsByName('insurance1')[1].checked==false){
		document.getElementById('insurance1Div').style.display='none';
		document.getElementById('insurance1File').style.display='none';		
		document.getElementById('insurance1DivTop').style.display='block';
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
	if(document.getElementsByName('insurance2')[1].checked==false){
		document.getElementById('insurance2Div').style.display='none';
		document.getElementById('insurance2File').style.display='none';		
		document.getElementById('insurance2DivTop').style.display='block';
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
				<bean:define id="url" type="java.lang.String">
					<html:link page="/parking.do?method=downloadAuthorizationDocument">
						<bean:message key="title.anexIV.label" bundle="PARKING_RESOURCES" />
					</html:link>
				</bean:define>
				<li><bean:message key="title.anexIV" bundle="PARKING_RESOURCES" arg0="<%= url %>"/></li>
			</ul>
		</li>
	</ul>
	<p class="mvert025"><bean:message key="title.documentDelivery" bundle="PARKING_RESOURCES"/>:</p>
	<ul>
		<li><bean:message key="title.documentDeliveryOnline" bundle="PARKING_RESOURCES"/></li>
		<li><bean:message key="title.documentDeliveryPaper" bundle="PARKING_RESOURCES"/></li>
	</ul>
	<p class="mvert025">Para submeter os documentos necessários:</p>
	<ul>
		<li>os ficheiros não podem exceder 2MB de tamanho</li>
		<li>os ficheiros têm de ser imagens no formato gif ou jpg</li>
		<li>escreva o caminho completo na caixa de texto (ex: <i>c:\documentos\carta_condução.jpg</i>)	ou carregue em <i>Browse</i> 
			e escolha o documento correspondente</li>
	</ul>
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
	<bean:define id="insuranceLabel">
		<bean:message  key="title.insurance" bundle="PARKING_RESOURCES"/>
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

	

	<fr:form action="<%= "/parking.do?method="+method%>" encoding="multipart/form-data">
	<p class="mbottom0"><strong><bean:message key="label.driverLicense" bundle="PARKING_RESOURCES" /></strong>:</p>	
		<table class="tstyle1 thright thlight mbottom0 tstylepark">
		<tr>
			<th class="parking"><div id="driverLicenseDivTop" style="display:none"><bean:message key="label.driverLicenseDocument" bundle="PARKING_RESOURCES"/>:</div></th>
			<td class="parking">
				<html:radio name="parkingForm" property="driverLicense" value="ALREADY_DELIVERED_HARD_COPY" onclick="changeElementsDisplay('driverLicenseDiv','driverLicenseFile', 'none', 'block')">
					<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES" arg0="<%=driverLicenseLabel%>"/>
				</html:radio>
				<br/>
				<html:radio name="parkingForm" property="driverLicense" value="WILL_DELIVER_HARD_COPY" onclick="changeElementsDisplay('driverLicenseDiv','driverLicenseFile', 'none', 'block')">
					<bean:message key="label.willdeliverDocument" bundle="PARKING_RESOURCES" arg0="<%=driverLicenseLabel%>"/>
				</html:radio>
				<br/>				
				<html:radio name="parkingForm" property="driverLicense" value="ELECTRONIC_DELIVERY" onclick="changeElementsDisplay('driverLicenseDiv','driverLicenseFile', 'block', 'none')">
					<bean:message key="label.deliverOnlineDocument" bundle="PARKING_RESOURCES" arg0="<%=driverLicenseLabel%>"/>
				</html:radio>
				<div id="driverLicenseFile">
				<logic:notEmpty name="<%= factoryName %>" property="driverLicenseFileName">
					<span class="warning0 mtop025">
						<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=driverLicenseLabel%>"/>:
						<bean:write name="<%= factoryName %>" property="driverLicenseFileName"/>
					</span>					
				</logic:notEmpty>
				</div>
			</td>
		</tr>
		</table>
			<div id="driverLicenseDiv">
				<fr:edit id="driverLicenseFR" name="<%= factoryName %>" schema="<%= action+".parkingRequestFactory.driverLicense"%>"
					type="<%= type %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value="parking,parking,noborder"/>
					</fr:layout>
				</fr:edit>			

				<span class="error0 mtop0"><html:messages id="message" property="driverLicenseMessage" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/>
				</html:messages></span>
			</div>
			<span class="error0 mtop0"><html:messages id="message" property="driverLicenseDeliveryMessage" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>
			<br/>

	<!-- FIRST CAR BEGINING -->
	<p class="mbottom0"><strong><bean:message key="label.firstCar" bundle="PARKING_RESOURCES" /></strong>:</p>
		<table class="tstyle1 thright thlight mtop025 mbottom0 tstylepark">
		<tr>
			<th class="parking"><bean:message key="label.firstCarMake" bundle="PARKING_RESOURCES"/>:</th>
			<td class="parking"><fr:edit id="firstCarMakeFR" name="<%= factoryName %>" slot="firstCarMake" 
				type="<%= type %>">
			</fr:edit></td>
			<td class="noborder"><span class="error0 mtop025">
				<html:messages id="message" property="firstCarMakePT" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/>
				</html:messages></span>
			</td>
		</tr>
		<tr>
			<th class="parking"><bean:message key="label.firstCarPlateNumber" bundle="PARKING_RESOURCES"/>:</th>
			<td class="parking"><fr:edit id="firstCarPlateNumberFR" name="<%= factoryName %>" slot="firstCarPlateNumber"
				type="<%= type %>">
			</fr:edit> (aa-bb-cc)</td>
			<td class="noborder"><span class="error0 mtop025">
				<html:messages id="message" property="firstCarPlateNumberPT" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/></span>
				</html:messages>
			</td>
		</tr>
		<tr>
			<th class="parking"><div id="registry1DivTop" style="display:none"><bean:message key="label.firstCarPropertyRegistry" bundle="PARKING_RESOURCES"/>:</div></th>
			<td class="parking">
				<html:radio name="parkingForm" property="registry1" value="ALREADY_DELIVERED_HARD_COPY" onclick="changeElementsDisplay('registry1Div','registry1File', 'none', 'block')">
					<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>
				</html:radio>
				<br/>
				<html:radio name="parkingForm" property="registry1" value="WILL_DELIVER_HARD_COPY" onclick="changeElementsDisplay('registry1Div','registry1File', 'none', 'block')">
					<bean:message key="label.willdeliverDocument" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>
				</html:radio>
				<br/>				
				<html:radio name="parkingForm" property="registry1" value="ELECTRONIC_DELIVERY" onclick="changeElementsDisplay('registry1Div','registry1File', 'block', 'none')">
					<bean:message key="label.deliverOnlineDocument" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>
				</html:radio>
				<div id="registry1File">
				<logic:notEmpty name="<%= factoryName %>" property="firstCarPropertyRegistryFileName">
					<span class="warning0 mtop025">				
						<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>:
						<bean:write name="<%= factoryName %>" property="firstCarPropertyRegistryFileName"/>
					</span>
				</logic:notEmpty>
				</div>
			</td>	
		</tr>
		</table>

			<div id="registry1Div">
				<fr:edit id="registry1FR" name="<%= factoryName %>"  schema="<%= action+".parkingRequestFactory.firstCarRegistry"%>"
					type="<%= type %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value="parking,parking,noborder"/>
					</fr:layout>
				</fr:edit>					
				<span class="error0 mtop025"><html:messages id="message" property="firstCarPropertyRegistryMessage" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/>
				</html:messages></span>				
			</div>
			<span class="error0 mtop0"><html:messages id="message" property="firstCarPropertyRegistryDeliveryMessage" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>
			
		<table class="tstyle1 thright thlight mtop0 mbottom0 tstylepark">
		<tr>
			<th class="parking"><div id="insurance1DivTop" style="display:none"><bean:message key="label.firstInsurance" bundle="PARKING_RESOURCES"/>:</div></th>
			<td class="parking">		
				<html:radio name="parkingForm" property="insurance1" value="WILL_DELIVER_HARD_COPY" onclick="changeElementsDisplay('insurance1Div','insurance1File', 'none', 'block')">
					<bean:message key="label.willdeliverDocument" bundle="PARKING_RESOURCES" arg0="<%=insuranceLabel%>"/>
				</html:radio>
				<br/>					
				<html:radio name="parkingForm" property="insurance1" value="ELECTRONIC_DELIVERY" onclick="changeElementsDisplay('insurance1Div','insurance1File', 'block', 'none')">
					<bean:message key="label.deliverOnlineDocument" bundle="PARKING_RESOURCES" arg0="<%=insuranceLabel%>"/>
				</html:radio>
				<div id="insurance1File">
				<logic:notEmpty name="<%= factoryName %>" property="firstInsuranceFileName">
					<span class="warning0 mtop025">
						<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=insuranceLabel%>"/>:
						<bean:write name="<%= factoryName %>" property="firstInsuranceFileName"/>
					</span>
				</logic:notEmpty>				
				</div>
			</td>	
		</tr>
		</table>
			<div id="insurance1Div">
				<fr:edit id="insurance1FR" name="<%= factoryName %>" schema="<%= action+".parkingRequestFactory.firstCarInsurance"%>"
					type="<%= type %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value="parking,parking,noborder"/>
					</fr:layout>				
				</fr:edit>				
				<span class="error0 mtop025"><html:messages id="message" property="firstInsuranceMessage" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/>
				</html:messages></span>
			</div>
			<span class="error0 mtop0"><html:messages id="message" property="firstInsuranceDeliveryMessage" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>			
			<p>
				<bean:message key="label.ownVehicle" bundle="PARKING_RESOURCES"/>
				<html:radio styleId="ownCar1radio1" name="parkingForm" property="ownVehicle1" value="true" onclick="document.getElementById('ownCar1').style.display='none'"/>
					<bean:message key="label.yes" bundle="PARKING_RESOURCES"/>
				<html:radio styleId="ownCar1radio2" name="parkingForm" property="ownVehicle1" value="false" onclick="document.getElementById('ownCar1').style.display='block'"/>
					<bean:message key="label.no" bundle="PARKING_RESOURCES"/>				
			</p>
			<div id="ownCar1">
		<table class="tstyle1 thright thlight mtop025 mbottom0 tstylepark">
		<tr>
			<th class="parking"><div id="Id1DivTop" style="display:none"><bean:message key="label.firstCarOwnerId" bundle="PARKING_RESOURCES"/>:</div></th>
			<td class="parking">	
				<html:radio name="parkingForm" property="Id1" value="ALREADY_DELIVERED_HARD_COPY" onclick="changeElementsDisplay('Id1Div','Id1File', 'none', 'block')">
					<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>
				</html:radio>
				<br/>
				<html:radio name="parkingForm" property="Id1" value="WILL_DELIVER_HARD_COPY" onclick="changeElementsDisplay('Id1Div','Id1File', 'none', 'block')">
					<bean:message key="label.willdeliverDocument" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>
				</html:radio>
				<br/>					
				<html:radio name="parkingForm" property="Id1" value="ELECTRONIC_DELIVERY" onclick="changeElementsDisplay('Id1Div','Id1File', 'block', 'none')">
					<bean:message key="label.deliverOnlineDocument" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>
				</html:radio>
				<div id="Id1File">
				<logic:notEmpty name="<%= factoryName %>" property="firstCarOwnerIdFileName">
					<span class="warning0 mtop025">
						<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>:
						<bean:write name="<%= factoryName %>" property="firstCarOwnerIdFileName"/>
					</span>
				</logic:notEmpty>
				</div>
			</td>	
		</tr>
		</table>
			<div id="Id1Div">
				<fr:edit id="Id1FR" name="<%= factoryName %>" schema="<%= action+".parkingRequestFactory.firstCarId.notOwnCar"%>"
					type="<%= type %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value="parking,parking,noborder"/>
					</fr:layout>					
				</fr:edit>	
				<span class="error0 mtop025"><html:messages id="message" property="firstCarOwnerIdMessage" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/>
				</html:messages></span>
			</div>
			<span class="error0 mtop0"><html:messages id="message" property="firstCarOwnerIdDeliveryMessage" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>	
		<table class="tstyle1 thright thlight mtop0 mbottom0 tstylepark">
		<tr>
			<th class="parking"><div id="declaration1DivTop" style="display:none"><bean:message key="label.firstDeclarationAuthorization" bundle="PARKING_RESOURCES"/>:</div></th>
			<td class="parking">	
				<html:radio name="parkingForm" property="declaration1" value="WILL_DELIVER_HARD_COPY" onclick="changeElementsDisplay('declaration1Div','Id1File', 'none', 'block')">
					<bean:message key="label.willdeliverDocument" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>
				</html:radio>
				<br/>						
				<html:radio name="parkingForm" property="declaration1" value="ELECTRONIC_DELIVERY" onclick="changeElementsDisplay('declaration1Div','declaration1File', 'block', 'none')">
					<bean:message key="label.deliverOnlineDocument" bundle="PARKING_RESOURCES" arg0="<%=authorizationDeclaration%>"/>
				</html:radio>
				<div id="declaration1File">
				<logic:notEmpty name="<%= factoryName %>" property="firstDeclarationAuthorizationFileName">
					<span class="warning0 mtop025">
						<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=authorizationDeclaration%>"/>:
						<bean:write name="<%= factoryName %>" property="firstDeclarationAuthorizationFileName"/>
					</span>	
				</logic:notEmpty>				
				</div>
			</td>					
		</tr>
		</table>
			<div id="declaration1Div">
				<fr:edit id="declaration1FR" name="<%= factoryName %>" schema="<%= action+".parkingRequestFactory.firstCarAuthorization.notOwnCar"%>"
					type="<%= type %>">
				
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value="parking,parking,noborder"/>
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
			<br/><br/>
					
	<!-- SECOND CAR BEGINING -->

<p class="mbottom0">
	<strong><bean:message key="label.secondCar" bundle="PARKING_RESOURCES" /></strong>:
	<html:radio styleId="hasVehicle2radio1" name="parkingForm" property="vehicle2" value="true" onclick="document.getElementById('hasVehicle2').style.display='block'"/>
	<bean:message key="label.yes" bundle="PARKING_RESOURCES"/>
	<html:radio styleId="hasVehicle2radio2" name="parkingForm" property="vehicle2" value="false" onclick="document.getElementById('hasVehicle2').style.display='none'"/>
	<bean:message key="label.no" bundle="PARKING_RESOURCES"/>		
</p>
<div id="hasVehicle2">
		<table class="tstyle1 thright thlight mtop025 mbottom0 tstylepark">
		<tr>
			<th class="parking"><bean:message key="label.secondCarMake" bundle="PARKING_RESOURCES"/>:</th>
			<td class="parking"><fr:edit id="secondCarMakeFR" name="<%= factoryName %>" slot="secondCarMake" 
					type="<%= type %>">
			</fr:edit></td>
			<td class="noborder"><span class="error0 mtop025">
				<html:messages id="message" property="secondCarMakePT" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/></span>
				</html:messages>
			</td>
		</tr>
		<tr>
			<th class="parking"><bean:message key="label.secondCarPlateNumber" bundle="PARKING_RESOURCES"/>:</th>
			<td class="parking"><fr:edit id="secondCarPlateNumberFR" name="<%= factoryName %>" slot="secondCarPlateNumber"
					type="<%= type %>">
			</fr:edit> (aa-bb-cc)</td>		
			<td class="noborder"><span class="error0 mtop025">
				<html:messages id="message" property="secondCarPlateNumberPT" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/></span>
				</html:messages>
			</td>	
		</tr>
		<tr>
			<th class="parking"><div id="registry2DivTop" style="display:none"><bean:message key="label.secondCarPropertyRegistry" bundle="PARKING_RESOURCES"/>:</div></th>
			<td class="parking">		
				<html:radio name="parkingForm" property="registry2" value="ALREADY_DELIVERED_HARD_COPY" onclick="changeElementsDisplay('registry2Div','registry2File', 'none', 'block')">
					<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>
				</html:radio>
				<br/>
				<html:radio name="parkingForm" property="registry2" value="WILL_DELIVER_HARD_COPY" onclick="changeElementsDisplay('registry2Div','registry2File', 'none', 'block')">
					<bean:message key="label.willdeliverDocument" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>
				</html:radio>
				<br/>						
				<html:radio name="parkingForm" property="registry2" value="ELECTRONIC_DELIVERY" onclick="changeElementsDisplay('registry2Div','registry2File', 'block', 'none')">
					<bean:message key="label.deliverOnlineDocument" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>
				</html:radio>
				<div id="registry2File">
				<logic:notEmpty name="<%= factoryName %>" property="secondCarPropertyRegistryFileName">
					<span class="warning0 mtop025">
						<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>:
						<bean:write name="<%= factoryName %>" property="secondCarPropertyRegistryFileName"/>
					</span>
				</logic:notEmpty>				
				</div>
			</td>	
		</tr>
		</table>

			<div id="registry2Div">
				<fr:edit id="registry2FR" name="<%= factoryName %>" schema="<%= action+".parkingRequestFactory.secondCarRegistry"%>"
					type="<%= type %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value="parking,parking,noborder"/>
					</fr:layout>
				</fr:edit>		
				<span class="error0 mtop025"><html:messages id="message" property="secondCarPropertyRegistryMessage" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/><br/>
				</html:messages></span>
			</div>
			<span class="error0 mtop0"><html:messages id="message" property="secondCarPropertyRegistryDeliveryMessage" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>
		<table class="tstyle1 thright thlight mtop0 mbottom0 tstylepark">
		<tr>
			<th class="parking"><div id="insurance2DivTop" style="display:none"><bean:message key="label.secondInsurance" bundle="PARKING_RESOURCES"/>:</div></th>
			<td class="parking">		
				<html:radio name="parkingForm" property="insurance2" value="WILL_DELIVER_HARD_COPY" onclick="changeElementsDisplay('insurance2Div','insurance2File', 'none', 'block')">
					<bean:message key="label.willdeliverDocument" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>
				</html:radio>
				<br/>					
				<html:radio name="parkingForm" property="insurance2" value="ELECTRONIC_DELIVERY" onclick="changeElementsDisplay('insurance2Div','insurance2File', 'block', 'none')">
					<bean:message key="label.deliverOnlineDocument" bundle="PARKING_RESOURCES" arg0="<%=insuranceLabel%>"/>
				</html:radio>
				<div id="insurance2File">
				<logic:notEmpty name="<%= factoryName %>" property="secondInsuranceFileName">
					<span class="warning0 mtop025">
						<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=insuranceLabel%>"/>:
						<bean:write name="<%= factoryName %>" property="secondInsuranceFileName"/>
					</span>
				</logic:notEmpty>				
				</div>
			</td>	
		</tr>
		</table>
			<div id="insurance2Div">
				<fr:edit id="insurance2FR" name="<%= factoryName %>" schema="<%= action+".parkingRequestFactory.secondCarInsurance"%>"
					type="<%= type %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value="parking,parking,noborder"/>
					</fr:layout>				
				</fr:edit>
				<span class="error0 mtop025"><html:messages id="message" property="secondInsuranceMessage" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/><br/><br/>
				</html:messages></span>
			</div>
			<span class="error0 mtop0"><html:messages id="message" property="secondInsuranceDeliveryMessage" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>
			
			<p>
				<bean:message key="label.ownVehicle" bundle="PARKING_RESOURCES"/>
				<html:radio styleId="ownCar2radio1" name="parkingForm" property="ownVehicle2" value="true" onclick="document.getElementById('ownCar2').style.display='none'"/>
					<bean:message key="label.yes" bundle="PARKING_RESOURCES"/>
				<html:radio styleId="ownCar2radio2" name="parkingForm" property="ownVehicle2" value="false" onclick="document.getElementById('ownCar2').style.display='block'"/>
					<bean:message key="label.no" bundle="PARKING_RESOURCES"/>	
			</p>
			<div id="ownCar2">
		<table class="tstyle1 thright thlight mtop025 mbottom0 tstylepark">
		<tr>
			<th class="parking"><div id="Id2DivTop" style="display:none"><bean:message key="label.secondCarOwnerId" bundle="PARKING_RESOURCES"/>:</div></th>
			<td class="parking">	
				<html:radio name="parkingForm" property="Id2" value="ALREADY_DELIVERED_HARD_COPY" onclick="changeElementsDisplay('Id2Div','Id2File', 'none', 'block')">
					<bean:message key="label.deliveredDocument" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>
				</html:radio>
				<br/>
				<html:radio name="parkingForm" property="Id2" value="WILL_DELIVER_HARD_COPY" onclick="changeElementsDisplay('Id2Div','Id2File', 'none', 'block')">
					<bean:message key="label.willdeliverDocument" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>
				</html:radio>
				<br/>						
				<html:radio name="parkingForm" property="Id2" value="ELECTRONIC_DELIVERY" onclick="changeElementsDisplay('Id2Div','Id2File', 'block', 'none')">
					<bean:message key="label.deliverOnlineDocument" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>
				</html:radio>
				<div id="Id2File">
				<logic:notEmpty name="<%= factoryName %>" property="secondCarOwnerIdFileName">
					<span class="warning0 mtop025">				
						<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=ownerId%>"/>:
						<bean:write name="<%= factoryName %>" property="secondCarOwnerIdFileName"/>
					</span>
				</logic:notEmpty>				
				</div>
			</td>	
		</tr>
		</table>
			<div id="Id2Div">
				<fr:edit id="Id2FR" name="<%= factoryName %>" schema="<%= action+".parkingRequestFactory.secondCarId.notOwnCar"%>"
					type="<%= type %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value="parking,parking,noborder"/>
					</fr:layout>					
				</fr:edit>
				<span class="error0 mtop025"><html:messages id="message" property="secondCarOwnerIdMessage" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/><br/>
				</html:messages></span>
			</div>
			<span class="error0 mtop0"><html:messages id="message" property="secondCarOwnerIdDeliveryMessage" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>
		<table class="tstyle1 thright thlight mtop0 mbottom0 tstylepark">
		<tr>
			<th class="parking"><div id="declaration2DivTop" style="display:none"><bean:message key="label.firstDeclarationAuthorization" bundle="PARKING_RESOURCES"/>:</div></th>
			<td class="parking">		
				<html:radio name="parkingForm" property="declaration2" value="WILL_DELIVER_HARD_COPY" onclick="changeElementsDisplay('declaration2Div','declaration2File', 'none', 'block')">
					<bean:message key="label.willdeliverDocument" bundle="PARKING_RESOURCES" arg0="<%=propertyRegisterLabel%>"/>
				</html:radio>
				<br/>					
				<html:radio name="parkingForm" property="declaration2" value="ELECTRONIC_DELIVERY" onclick="changeElementsDisplay('declaration2Div','declaration2File', 'block', 'none')">
					<bean:message key="label.deliverOnlineDocument" bundle="PARKING_RESOURCES" arg0="<%=authorizationDeclaration%>"/>
				</html:radio>
				<div id="declaration2File">
				<logic:notEmpty name="<%= factoryName %>" property="secondDeclarationAuthorizationFileName">
					<span class="warning0 mtop025">
						<bean:message key="label.currentFile" bundle="PARKING_RESOURCES" arg0="<%=authorizationDeclaration%>"/>:
						<bean:write name="<%= factoryName %>" property="secondDeclarationAuthorizationFileName"/>
					</span>
				</logic:notEmpty>
				</div>
			</td>					
		</tr>
		</table>
			<div id="declaration2Div">
				<fr:edit id="declaration2FR" name="<%= factoryName %>" schema="<%= action+".parkingRequestFactory.secondCarAuthorization.notOwnCar"%>"
					type="<%= type %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thright thlight mtop0 mbottom0 tstylepark"/>
						<fr:property name="columnClasses" value="parking,parking,noborder"/>
					</fr:layout>				
				</fr:edit>			
				<span class="error0 mtop0"><html:messages id="message" property="secondDeclarationAuthorizationMessage" message="true" bundle="PARKING_RESOURCES">
					<bean:write name="message"/><br/><br/>
				</html:messages></span>
			</div>
			<span class="error0 mtop0"><html:messages id="message" property="secondDeclarationAuthorizationDeliveryMessage" message="true" bundle="PARKING_RESOURCES">
				<bean:write name="message"/><br/>
			</html:messages></span>
			</div>
			</div>
			<br/>
		
		<input type="submit" value="<%= submit.toString() %>" />		

		</fr:form>

</logic:present>

	<script type="text/javascript">
		hideInputBoxes();
	</script>

