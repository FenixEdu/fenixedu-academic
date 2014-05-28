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
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<h2><bean:message key="label.payments.transferPayments"
	bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>


<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true"
			bundle="ACADEMIC_OFFICE_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>


<p class="mtop15 mbottom05"><strong><bean:message
	bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
<fr:view name="transferPaymentsBean" property="sourceEvent.person"
	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>

<bean:define id="personId" name="transferPaymentsBean"
	property="sourceEvent.person.externalId" />

<fr:form action="<%="/paymentsManagement.do?personId=" + personId.toString() %>">
	<input type="hidden" name="method" value="" />

	<logic:empty name="transferPaymentsBean" property="sourceEvent.positiveEntries">
		<bean:message key="label.payments.not.found"
			bundle="ACADEMIC_OFFICE_RESOURCES" />
		<br/><br/>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel"
			onclick="this.form.method.value='backToShowEvents';">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.back" />
		</html:cancel>
	</logic:empty>

	<logic:notEmpty name="transferPaymentsBean" property="sourceEvent.positiveEntries">
	
		<fr:view name="transferPaymentsBean"
		property="sourceEvent.positiveEntries" schema="entry.view-with-payment-mode">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 mtop05" />
				<fr:property name="columnClasses" value=",,,aright,aright,aright,acenter" />
				<fr:property name="sortBy" value="whenRegistered=desc"/>
			</fr:layout>
		</fr:view>
	
		<fr:edit id="transferPaymentsBean" name="transferPaymentsBean" schema="TransferPaymentsToOtherEventAndCancelBean.edit">
			<fr:layout name="tabular">
				<fr:property name="classes"
					value="tstyle2 thmiddle thright thlight mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
					

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
			onclick="this.form.method.value='transferPaymentsToOtherEventAndCancel';">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.submit" />
		</html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel"
			onclick="this.form.method.value='backToShowEvents';">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.back" />
		</html:cancel>
	</logic:notEmpty>

</fr:form>
