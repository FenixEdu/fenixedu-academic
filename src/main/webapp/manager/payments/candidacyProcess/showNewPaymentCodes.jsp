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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<h2>Referências criadas</h2>

<fr:view name="newPaymentCodes">

	<fr:schema type="org.fenixedu.academic.domain.accounting.paymentCodes.IndividualCandidacyPaymentCode" bundle="MANAGER_RESOURCES" >
		<fr:slot name="code" key="label.manager.code"/>
		<fr:slot name="startDate" />
		<fr:slot name="endDate" />
		<fr:slot name="minAmount" key="label.org.fenixedu.academic.ui.struts.action.manager.payments.CandidacyProcessPaymentCodeBean.minAmount"/>
		<fr:slot name="maxAmount" key="label.org.fenixedu.academic.ui.struts.action.manager.payments.CandidacyProcessPaymentCodeBean.maxAmount"/>
		<fr:slot name="type" />
	</fr:schema>

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
	</fr:layout>
</fr:view>
