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

	<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.exemptions" /></h2>
	<br />

	<logic:messagesPresent message="true" property="context">
		<ul>
			<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="context">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
	</logic:messagesPresent>
	
	<logic:messagesPresent message="true" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
		<ul>
			<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
	</logic:messagesPresent>
	
	<fr:hasMessages for="exemptionBean" type="conversion">
		<ul>
			<fr:messages>
				<li><span class="error0"><fr:message /></span></li>
			</fr:messages>
		</ul>
	</fr:hasMessages>

	<bean:define id="event" name="exemptionBean" property="event" />
	<bean:define id="person" name="event" property="person" />

	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" />:</strong>
	<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>

	<br />

	<bean:define id="eventId" name="event" property="externalId" />
	<fr:form action='<%="/exemptionsManagement.do?eventId=" + eventId %>'>
		<html:hidden property="method" value="" />

		<fr:edit id="exemptionBean" name="exemptionBean">
			<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.phd.PhdEventExemptionBean">
				<fr:slot name="justificationType" layout="radio-select" required="true">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.phd.PhdEventExemptionJustificationTypeProvider" />
			        <fr:property name="saveOptions" value="true"/>
				</fr:slot>
				<fr:slot name="dispatchDate" required="true" />
				<fr:slot name="value" required="true" />
				<fr:slot name="reason" layout="longText" required="true">
					<fr:property name="rows" value="4" />
					<fr:property name="columns" value="40" />
				</fr:slot>
			</fr:schema>
		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		
			<fr:destination name="invalid" path='<%="/exemptionsManagement.do?method=prepareCreatePhdEventExemptionInvalid&amp;eventId=" + eventId %>'/>
			<fr:destination name="postback" path='<%= "/exemptionsManagement.do?method=changeForm&amp;eventId=" + eventId %>'/>
		</fr:edit>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='createPhdEventExemption';">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.create" />
		</html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" onclick="this.form.method.value='showExemptions';">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.cancel" />
		</html:cancel>

	</fr:form>
