<%--

    Copyright © 2014 Instituto Superior Técnico

    This file is part of Fenix Parking.

    Fenix Parking is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Fenix Parking is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Fenix Parking.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="title.renewParkingCards"/></h2>
	
<fr:form action="/manageParkingCards.do?method=renewParkingCards">
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
			Foram selecionados <strong><bean:write name="selectedParkingPartiesSize"/></strong> utentes para renovação.<br/>
		</p>
		<fr:view name="parkingCardSearchBean" property="selectedParkingParties" schema="show.selectedParkingCards">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 tdcenter"/>
				<fr:property name="columnClasses" value=",,smalltxt color888,,,aleft"/>
				<fr:property name="checkable" value="true"/>
				<fr:property name="checkboxName" value="parkingCardsToRemove" />
				<fr:property name="checkboxValue" value="externalId"/>
			</fr:layout>
		</fr:view>
		<html:submit property="remove"><bean:message key="button.remove" bundle="PARKING_RESOURCES"/></html:submit>
	</logic:notEmpty>
	<logic:empty name="parkingCardSearchBean" property="selectedParkingParties">
		<p>Não foram selecionados utentes para renovação.</p>
	</logic:empty>
</fr:form>