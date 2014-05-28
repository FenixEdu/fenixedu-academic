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

<bean:define id="personId" name="paymentsManagementDTO" property="person.externalId" />
<fr:form action='<%= "/payments.do?personId=" + personId %>'>

	<input type="hidden" name="method" value=""/>

	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.payments.currentEvents" /></h2>
	
	<logic:messagesPresent message="true" property="context">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true" property="context" bundle="ACADEMIC_OFFICE_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	<logic:messagesPresent message="true" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true"  bundle="APPLICATION_RESOURCES" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	
	<fr:hasMessages for="paymentsManagementDTO" type="conversion">
		<ul class="nobullet list6">
			<fr:messages>
				<li><span class="error0"><fr:message/></span></li>
			</fr:messages>
		</ul>
	</fr:hasMessages>
	
	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
	<fr:view name="paymentsManagementDTO" property="person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>

	<p class="mbottom05 mtop15"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.currentEvents" /></strong></p>
	<logic:notEmpty name="paymentsManagementDTO" property="entryDTOs">
		<fr:edit id="paymentsManagementDTO" name="paymentsManagementDTO"
			visible="false" />

		<fr:edit id="payment-entries" name="paymentsManagementDTO"
			property="entryDTOs" schema="entryDTO.edit">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight mtop05" />
				<fr:property name="columnClasses" value=",,,aright,aright,aright,acenter" />
			</fr:layout>
			<fr:destination name="invalid" path="/payments.do?method=prepareShowEventsInvalid"/>
		</fr:edit>
		<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='preparePrintGuide';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.guide"/></html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='preparePayment';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.preparePayment"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='backToShowOperations';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:cancel>
		</p>
	</logic:notEmpty>

	<logic:empty name="paymentsManagementDTO" property="entryDTOs">
		<p>
			<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.events.noEvents" />.</em>
		</p>
		<br/>
		<p>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='backToShowOperations';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:cancel>
		</p>
	</logic:empty>

</fr:form>
