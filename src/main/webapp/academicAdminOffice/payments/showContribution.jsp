<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%-- ### Title #### --%>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.scolarships.external" /></h2>

<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
	<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>

<p class="mtop15 mbottom05"><strong><bean:message key="label.scolarships" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
<logic:equal name="event" property="open" value="true">
	<fr:view name="event">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="org.fenixedu.academic.domain.accounting.events.gratuity.ExternalScholarshipGratuityContributionEvent">
			<fr:slot name="externalScholarshipGratuityExemption.description" key="label.org.fenixedu.academic.domain.accounting.Event.description"/>
			<fr:slot name="totalValue" key="label.total.amount"/>
			<fr:slot name="amountToPay" key="label.amount.still.missing"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		</fr:layout>
	</fr:view>

	<bean:define id="personId" name="person" property="externalId" />
<p><em><bean:message key="label.pay.debt" bundle="ACADEMIC_OFFICE_RESOURCES"/></em></p>

	<fr:form action="/payments.do" >
			<html:hidden property="method" value="liquidate"/>
			<html:hidden name="event" property="externalId"/>
			<fr:edit id="bean" name="bean">
				<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="org.fenixedu.academic.ui.struts.action.administrativeOffice.payments.ExternalScholarshipManagementDebtsDA$AmountBean">
					<fr:slot name="value" key="label.payments.gratuityExemptions.amount"/>
					<fr:slot name="paymentDate" key="label.payments.gratuityExemptions.paymentDate"/>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight" />
					<fr:property name="columnClasses" value="nowrap," />
				</fr:layout>
			</fr:edit>
		<html:submit><bean:message key="label.payments.liquidate" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
		<html:button property="value"
					 onclick="this.form.method.value='cancel';this.form.submit()"><bean:message
				key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:button>
	</fr:form>
</logic:equal>
<logic:equal name="event" property="open" value="false">
	<fr:view name="exemption">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="org.fenixedu.academic.domain.accounting.events.gratuity.ExternalScholarshipGratuityContributionEvent">
			<fr:slot name="externalScholarshipGratuityExemption.description" key="label.org.fenixedu.academic.domain.accounting.Event.description"/>
			<fr:slot name="totalValue" key="label.total.amount"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		</fr:layout>
	</fr:view>
</logic:equal>
