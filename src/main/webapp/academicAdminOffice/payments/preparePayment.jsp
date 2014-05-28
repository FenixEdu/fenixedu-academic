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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="personId" name="paymentsManagementDTO" property="person.externalId"/>
<fr:form action="<%="/payments.do?personId=" + personId %>">
	

	<input type="hidden" name="method" value=""/>
	
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.preparePayment" /></h2>

	<logic:messagesPresent message="true" property="context">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true" property="context" bundle="ACADEMIC_OFFICE_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	<logic:messagesPresent message="true"  property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>" bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	
	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person"/></strong></p>
	<fr:view name="paymentsManagementDTO" property="person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>
	

	<fr:edit id="paymentsManagementDTO-edit" name="paymentsManagementDTO" visible="false" />
	
	<logic:equal name="paymentsManagementDTO" property="usingContributorParty" value="true">
		<fr:edit id="paymentsManagementDTO.edit.with.contributorParty" 
				name="paymentsManagementDTO" 
				schema="paymentsManagementDTO.edit.with.contributorParty">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright" />
				<fr:property name="columnClasses" value=",,tdclear" />
			</fr:layout>
			<fr:destination name="invalid" path="/payments.do?method=preparePaymentInvalid"/>
			<fr:destination name="usingContributorPartyPostback" path="/payments.do?method=preparePaymentUsingContributorPartyPostback" />
			
		</fr:edit>
	</logic:equal>
	<logic:notEqual name="paymentsManagementDTO" property="usingContributorParty" value="true">
		<fr:edit 	id="paymentsManagementDTO.edit.with.contributorName"
					name="paymentsManagementDTO"
					schema="paymentsManagementDTO.edit.with.contributorName">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright" />
				<fr:property name="columnClasses" value=",,tdclear" />
				<fr:destination name="invalid" path="/payments.do?method=preparePaymentInvalid" />
				<fr:destination name="usingContributorPartyPostback" path="/payments.do?method=preparePaymentUsingContributorPartyPostback" />
			</fr:layout>
		</fr:edit>
	</logic:notEqual>
	
	<fr:view name="paymentsManagementDTO" property="selectedEntries" schema="entryDTO.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 mtop05 mbottom0" />
			<fr:property name="columnClasses" value="width30em acenter,width15em aright"/>
		</fr:layout>
	</fr:view>


	<table class="tstyle4 mtop0">
		<tr>
			<td class="width30em"></td>
			<td class="width15em aright" style="background-color: #fdfbdd;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.totalAmount"/>: <bean:write name="paymentsManagementDTO" property="totalAmountToPay" />&nbsp;<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.currencySymbol"/></td>
		</tr>
	</table>

	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='doPayment';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.pay"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='backToShowOperations';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:cancel>
	
</fr:form>
