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

<h2><bean:message key="label.create" bundle="MANAGER_RESOURCES" /></h2>

<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<fr:edit id="paymentCodeMappingBean" name="paymentCodeMappingBean" schema="PaymentCodeMappingBean.create"
		action="/paymentCodesAttribution.do?method=createPaymentCodeMapping">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
	<fr:destination name="cancel" path="/paymentCodesAttribution.do?method=prepareViewPaymentCodeMappings" />
	<fr:destination name="invalid" path="/paymentCodesAttribution.do?method=createPaymentCodeMappingInvalid" />
</fr:edit>
