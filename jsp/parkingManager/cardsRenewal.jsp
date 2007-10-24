<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.parking" /></em>
<h2><bean:message key="link.cardsRenewal" /></h2>

<p class="mtop15 mbottom05">	
	<span class="error0">
		<html:messages id="message" message="true" bundle="PARKING_RESOURCES">
			<bean:write name="message" />
		</html:messages>
	</span>
</p>
	
<fr:edit id="parkingCardsRenewalBean" name="parkingCardsRenewalBean" schema="edit.parkingCardsRenewal"
	 action="/manageParkingPeriods.do?method=renewCards">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop025" />
		<fr:property name="columnClasses" value=",,tderror1 tdclear" />
	</fr:layout>
</fr:edit>

<logic:equal name="parkingCardsRenewalBean" property="processed" value="true">
	<bean:size id="notRenewedCards" name="parkingCardsRenewalBean" property="notRenewedParkingParties"/>
	<p>
		O grupo <bean:write name="parkingCardsRenewalBean" property="parkingGroup.groupName"/> possuí <bean:write name="parkingCardsRenewalBean" property="groupMembersCount"/> pessoas.<br/>
		A data de validade do cartão não foi actualizada para <bean:write name="notRenewedCards"/> pessoas.<br/>
	</p>
	<fr:view name="parkingCardsRenewalBean" property="notRenewedParkingParties" schema="show.parkingParty.notUpdatedEndDate">
		<fr:layout name="tabular">
			<fr:property name="class" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</logic:equal>