<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>

<h2><bean:message key="label.parking" bundle="PARKING_RESOURCES" /></h2>


<logic:present name="parkingParty">
	<p class="mbottom025"><bean:message	key="label.person.title.personal.info" /></p>

	<fr:view name="parkingParty" property="party" schema="viewPersonInfo">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thright thlight mtop025" />
		</fr:layout>
	</fr:view>
	<logic:empty name="parkingParty" property="parkingRequests">
		<logic:equal name="parkingParty"
			property="hasAllNecessaryPersonalInfo" value="false">
			<p class="infoop2"><bean:message key="message.personalDataCondition"
				bundle="PARKING_RESOURCES" /></p>
			<p><bean:message key="message.no.necessaryPersonalInfo"
				bundle="PARKING_RESOURCES" /></p>
		</logic:equal>
		<logic:notEqual name="parkingParty"	property="hasAllNecessaryPersonalInfo" value="false">
			<ul class="mvert025">
				<li>
					<html:link page="/parking.do?method=downloadParkingRegulamentation">
						<bean:message key="label.parkingRegulation" bundle="PARKING_RESOURCES" />
					</html:link>
				</li>
			</ul>


			<logic:equal name="parkingParty" property="acceptedRegulation" value="false">				
				<p class="infoop2 mvert1"><bean:message key="message.acceptRegulationCondition" bundle="PARKING_RESOURCES" /></p>
										
				<bean:message key="message.acceptRegulation" bundle="PARKING_RESOURCES" />
				<ul>
					<li><html:link page="/parking.do?method=acceptRegulation">
						<bean:message key="button.acceptRegulation"
							bundle="PARKING_RESOURCES" />
					</html:link></li>
				</ul>				
			</logic:equal>
			<logic:notEqual name="parkingParty" property="acceptedRegulation" value="false">
				
				<div class="simpleblock5"> <%-- message.acceptedRegulation --%>
					<bean:write name="parkingParty" property="parkingAcceptedRegulationMessage" filter="false"/>
				</div>
				
				<ul> <%-- first time - inserir --%>
					<li><html:link page="/parking.do?method=prepareEditParking">
						<bean:message key="label.insertParkingDocuments"
							bundle="PARKING_RESOURCES" />
					</html:link></li>
				</ul>
				
			</logic:notEqual>
		</logic:notEqual>
	</logic:empty>
	<logic:notEmpty name="parkingParty" property="parkingRequests">

		<ul class="mvert025">
			<li>
				<html:link page="/parking.do?method=downloadParkingRegulamentation">
					<bean:message key="label.parkingRegulation" bundle="PARKING_RESOURCES" />
				</html:link>
			</li>
			<%-- editar --%>
			<logic:equal name="canEdit" value="true">
				<li class="mtop05"><html:link page="/parking.do?method=prepareEditParking">
					<bean:message key="label.editParkingDocuments"
						bundle="PARKING_RESOURCES" />
				</html:link></li>
			</logic:equal>
		</ul>


		<logic:notEmpty name="parkingParty" property="firstRequest.requestedAs">
			<p><span class="infoop2"><bean:message key="message.userRequestedAs" bundle="PARKING_RESOURCES"/>
				<strong><bean:message name="parkingParty" property="firstRequest.requestedAs.name" bundle="ENUMERATION_RESOURCES"/></strong>
				</span>
			</p>
		</logic:notEmpty>
		
		<logic:notEmpty name="parkingParty" property="firstRequest.note">
			<p><span class="infoop2"><bean:message key="label.note" bundle="PARKING_RESOURCES"/>:
				<bean:write name="parkingParty" property="firstRequest.note"/>
			</span></p>
		</logic:notEmpty>

		<logic:equal name="parkingParty" property="firstRequest.limitlessAccessCard" value="true">
			<p><span class="infoop2"><bean:message key="label.requestedLimitlessAccessCard" bundle="PARKING_RESOURCES"/>						 
				<strong><bean:message key="label.limitlessAccessCard" bundle="PARKING_RESOURCES"/></strong>
			</span></p>
		</logic:equal>
		
		<logic:equal name="parkingParty" property="firstRequest.hasDriverLicense" value="true">
			<p class="mtop15 mbottom025"><strong><bean:message key="label.driverLicense"
				bundle="PARKING_RESOURCES" /></strong></p>
			<fr:view name="parkingParty" property="firstRequest"
				schema="parkingRequest.driverLicense">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
				</fr:layout>
			</fr:view>
		</logic:equal>

		<logic:equal name="parkingParty" property="firstRequest.hasFirstCar" value="true">
			<p class="mtop1 mbottom025"><strong><bean:message key="label.firstCar"
				bundle="PARKING_RESOURCES" /></strong></p>
			<fr:view name="parkingParty" property="firstRequest"
				schema="parkingRequest.firstCar">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
				</fr:layout>
			</fr:view>
		</logic:equal>

		<logic:equal name="parkingParty" property="firstRequest.hasSecondCar" value="true">
			<p class="mtop1 mbottom025"><strong><bean:message key="label.secondCar"
				bundle="PARKING_RESOURCES" /></strong></p>
			<fr:view name="parkingParty" property="firstRequest"
				schema="parkingRequest.secondCar">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
				</fr:layout>
			</fr:view>
		</logic:equal>

	</logic:notEmpty>
</logic:present>
