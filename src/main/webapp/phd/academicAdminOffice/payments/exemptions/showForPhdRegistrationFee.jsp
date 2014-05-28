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
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

	<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.exemptions" /></h2>

	<logic:messagesPresent message="true">
		<ul class="nobullet list2">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	
	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
	<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>
	
	<h3>
		<app:labelFormatter name="event" property="description">
			<app:property name="enum" value="ENUMERATION_RESOURCES"/>
			<app:property name="application" value="APPLICATION_RESOURCES"/>
		</app:labelFormatter>
	</h3>

	<bean:define id="personId" name="person" property="externalId" />
	<bean:define id="eventId" name="event" property="externalId" />
	
	<logic:notEmpty name="event" property="phdEventExemption">
		<bean:define id="phdEventExemption" name="event" property="phdEventExemption" />

		<fr:view name="phdEventExemption">

			<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.debts.PhdEventExemption">
				<fr:slot name="description" />
				<fr:slot name="value" />
				<fr:slot name="exemptionJustification.dispatchDate" key="label.net.sourceforge.fenixedu.domain.phd.debts.PhdEventExemption.exemptionJustification.dispatchDate"/>
				<fr:slot name="reason" />
			</fr:schema>
		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 mtop05" />
			</fr:layout>
		</fr:view>

		<bean:define id="phdEventExemptionId" name="phdEventExemption" property="externalId" />
		<html:link action="<%="/exemptionsManagement.do?method=deleteExemption&amp;exemptionId=" + phdEventExemptionId %>">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.delete"/>
		</html:link>
		
	</logic:notEmpty>
	<logic:empty name="event" property="phdEventExemption">
		<p>
			<em>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.exemptions.noExemptions" />
			</em>
		</p>
		<p>
			<html:link action="<%="/exemptionsManagement.do?method=prepareCreatePhdEventExemption&amp;personId=" + personId + "&amp;eventId=" + eventId %>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.create"/>
			</html:link>
		</p>
	</logic:empty>
	
	<p class="mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.penaltyExemptions"/></strong></p>
	<logic:notEmpty name="event" property="phdRegistrationFeePenaltyExemption">
		<bean:define id="penaltyExemption" name="event" property="phdRegistrationFeePenaltyExemption" />
		<fr:view name="penaltyExemption" schema="PhdRegistrationFeePenaltyExemption.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 mtop05" />
			</fr:layout>
		</fr:view>
		<bean:define id="penaltyExemptionId" name="penaltyExemption" property="externalId" />
		<html:link action="<%="/exemptionsManagement.do?method=deleteExemption&amp;exemptionId=" + penaltyExemptionId %>">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.delete"/>
		</html:link>
	</logic:notEmpty>
	<logic:empty name="event" property="phdRegistrationFeePenaltyExemption">
		<p>
			<em>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.penaltyExemptions.noPenaltyExemptions" />
			</em>
		</p>
		<p>
			<html:link action="<%="/exemptionsManagement.do?method=prepareCreatePhdRegistrationFeePenaltyExemption&amp;personId=" + personId + "&amp;eventId=" + eventId %>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.create"/>
			</html:link>
		</p>
	</logic:empty>

	<bean:define id="personId" name="person" property="externalId" />
	<fr:form action="<%="/exemptionsManagement.do?method=showEventsToApplyExemption&amp;personId=" + personId%>">
		<p class="mtop15">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.back" />
			</html:submit>
		</p>
	</fr:form>
