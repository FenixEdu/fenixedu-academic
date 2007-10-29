<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.parking" /></em>
<h2><bean:message key="title.renewParkingCards"/></h2>
	
<fr:form action="/manageParkingPeriods.do?method=renewParkingCards">
	<fr:edit id="parkingCardSearchBean" name="parkingCardSearchBean" schema="edit.cardsRenewalDate">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop025" />
			<fr:property name="columnClasses" value=",,tderror1 tdclear" />
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="button.renewCards" bundle="PARKING_RESOURCES"/></html:submit>
	<html:submit property="cancel"><bean:message key="button.cancel" bundle="PARKING_RESOURCES"/></html:submit>
	<logic:notEmpty name="parkingCardSearchBean" property="selectedParkingParties">
		<bean:size id="selectedParkingPartiesSize" name="parkingCardSearchBean" property="selectedParkingParties"/>
		<p>
			Foram selecionados <bean:write name="selectedParkingPartiesSize"/> utentes para renovação.<br/>
		</p>
		<fr:view name="parkingCardSearchBean" property="searchedParkingParties" schema="show.searchedParkingCards">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 tdcenter"/>
				<fr:property name="columnClasses" value=",smalltxt color888,,,,aleft"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="parkingCardSearchBean" property="selectedParkingParties">
		<p>Não foram selecionados utentes para renovação.</p>
	</logic:empty>
</fr:form>