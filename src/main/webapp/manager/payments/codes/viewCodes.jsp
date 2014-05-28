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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<h2><bean:message key="label.paymentCodeMappings" bundle="MANAGER_RESOURCES" /></h2>

<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<fr:edit id="paymentCodeMappingBean" name="paymentCodeMappingBean" schema="PaymentCodeMappingBean.search"
		action="/paymentCodesAttribution.do?method=viewPaymentCodeMappings">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
	<fr:destination name="cancel"  path="/index.do"/>
</fr:edit>

<br/>
<logic:present name="paymentCodeMappingBean" property="executionInterval">
	<bean:define id="executionIntervalId" name="paymentCodeMappingBean" property="executionInterval.externalId" />
	<html:link page='<%= "/paymentCodesAttribution.do?method=prepareCreatePaymentCodeMapping&amp;executionIntervalOid=" + executionIntervalId %>'><bean:message key="label.create" bundle="MANAGER_RESOURCES" /></html:link>
	<br/>
</logic:present>

<logic:empty name="paymentCodeMappings">
	<em><strong><bean:message key="label.paymentCodeMappings.empty.list" bundle="MANAGER_RESOURCES" /></strong></em>
</logic:empty>

<logic:notEmpty name="paymentCodeMappings">
	

	<fr:view name="paymentCodeMappings" schema="PaymentCodeMapping.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2" />

			<fr:property name="linkFormat(delete)" value="/paymentCodesAttribution.do?method=deletePaymentCodeMapping&amp;paymentCodeMappingOid=${externalId}" />
			<fr:property name="key(delete)" value="label.delete"/>
			<fr:property name="bundle(delete)" value="MANAGER_RESOURCES"/>
			<fr:property name="confirmationKey(delete)" value="label.paymentCodeMapping.confirm.delete.message" />
			<fr:property name="confirmationBundle(delete)" value="MANAGER_RESOURCES" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
