<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="label.parking" /></em>
<h2><bean:message key="label.search" /></h2>

<fr:edit id="searchPartyBean" action="/parking.do?method=showParkingPartyRequests" name="searchPartyBean" schema="search.party">
	<fr:destination name="cancel" path="/index.do"/>	
</fr:edit>

<br/>

<logic:present name="searchPartyBean">
	<logic:notEmpty name="searchPartyBean" property="party">
		<bean:define id="parkingParty" name="searchPartyBean" property="party.parkingParty"/>
		
		<h3><bean:message key="label.parkUserInfo" /></h3>
		<logic:iterate id="occupation" name="parkingParty" property="occupations">
			<i><bean:write name="occupation"/></i><br/>
		</logic:iterate>
		<p><logic:equal name="parkingParty" property="hasFirstCar" value="false">
			<bean:message key="label.newUser" bundle="PARKING_RESOURCES"/>
		</logic:equal></p>
		<p class="mbottom05"><html:link page="">Editar utente</html:link></p>
		<fr:view name="parkingParty" schema="view.parkingParty.personalInfo">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thright thlight mtop025" />
			</fr:layout>
		</fr:view>
		
		<logic:equal name="parkingParty" property="hasFirstCar" value="true">
			<p class="mtop15 mbottom025"><strong><bean:message key="label.driverLicense"
				bundle="PARKING_RESOURCES" /></strong></p>
			<fr:view name="parkingParty" schema="parkingParty.driverLicense">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
				</fr:layout>
			</fr:view>
		
		
			<p class="mtop1 mbottom025"><strong><bean:message key="label.firstCar"
				bundle="PARKING_RESOURCES" /></strong></p>
			<fr:view name="parkingParty" schema="parkingParty.firstCar">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
				</fr:layout>
			</fr:view>
		
			
			<logic:equal name="parkingParty" property="hasSecondCar" value="true">
				<p class="mtop1 mbottom025"><strong><bean:message key="label.secondCar"
					bundle="PARKING_RESOURCES" /></strong></p>
				<fr:view name="parkingParty" schema="parkingParty.secondCar">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thright thlight mtop025 mbottom1" />
					</fr:layout>
				</fr:view>
			</logic:equal>
		</logic:equal>

		<p class="mtop05"><html:link page="">Editar utente</html:link></p>
		<h3><bean:message key="label.requestList" /></h3>
		<logic:notEmpty name="parkingRequests">		
			<fr:view name="parkingRequests" schema="show.parkingRequest.noDetail">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1" />
					<fr:property name="link(view)" value="/parking.do?method=showRequest" />
					<fr:property name="key(view)" value="link.viewRequest" />
					<fr:property name="param(view)" value="idInternal" />
					<fr:property name="bundle(view)" value="PARKING_RESOURCES" />
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
		<logic:empty name="parkingRequests">
			<bean:message key="label.userWithoutRequests" bundle="PARKING_RESOURCES"/>
		</logic:empty>
		
	</logic:notEmpty>
</logic:present>

<logic:notPresent name="searchPartyBean">
	<logic:notEmpty name="partyList">
		<fr:view name="partyList" schema="view.partyName">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
				<fr:property name="link(view)" value="/parking.do?method=showParkingPartyRequests" />
				<fr:property name="key(view)" value="link.view" />
				<fr:property name="param(view)" value="idInternal" />
				<fr:property name="bundle(view)" value="PARKING_RESOURCES" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="partyList">
		<!-- criar utente -->
	</logic:empty>
</logic:notPresent>
