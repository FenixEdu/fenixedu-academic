<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />


<h2><bean:message key="label.payments.depositAmount"
	bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<bean:define id="personId" name="depositAmountBean"
	property="event.person.externalId" />

<fr:hasMessages type="conversion" for="depositAmountBean">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message show="label"/>:<fr:message /></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true"
			bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>


<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
<fr:view name="depositAmountBean" property="event.person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>

<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.event" /></strong></p>
<fr:edit id="depositAmountBean" name="depositAmountBean"
	schema="DepositAmountBean.edit">
	<fr:layout name="tabular">
		<fr:property name="classes"
			value="tstyle2 thmiddle thright thlight mtop05" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
	<fr:destination name="success"	path="<%="/paymentsManagement.do?method=depositAmount&personId=" + personId.toString() %>" />
	<fr:destination name="invalid" path="<%="/paymentsManagement.do?method=prepareDepositAmountInvalid&personId=" + personId.toString()%>"/>
	<fr:destination name="cancel" path="<%="/paymentsManagement.do?method=backToShowEvents&personId=" + personId.toString() %>" />
</fr:edit>
