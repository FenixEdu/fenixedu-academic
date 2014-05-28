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
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

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
<bean:define id="gratuityEventId" name="event" property="externalId" />

<p class="mtop2 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.gratuityExemptions"/></strong></p>
<logic:notEmpty name="event" property="gratuityExemption">
	<bean:define id="gratuityExemption" name="event" property="gratuityExemption" />
	<bean:define id="gratuityExemptionClassName" name="gratuityExemption" property="class.simpleName"></bean:define>
	<fr:view name="gratuityExemption" schema="<%=gratuityExemptionClassName + ".view"%>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>
	<bean:define id="gratuityExemptionId" name="gratuityExemption" property="externalId" />
	<html:link action="<%="/exemptionsManagement.do?method=deleteExemption&amp;exemptionId=" + gratuityExemptionId %>">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.delete"/>
	</html:link>
</logic:notEmpty>
<logic:empty name="event" property="gratuityExemption">
	<p>
		<em>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.gratuityExemptions.noGratuityExemption" />
		</em>
	</p>
	<html:link action="<%="/exemptionsManagement.do?method=prepareCreateGratuityExemption&amp;personId=" + personId + "&amp;eventId=" + gratuityEventId %>">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.create"/>
	</html:link>
</logic:empty>


<logic:equal name="hasPaymentPlan" value="true">
	<p class="mtop15"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.penaltyExemptions"/></strong></p>
	<logic:notEmpty name="event" property="installmentPenaltyExemptions">
		<fr:view name="event" property="installmentPenaltyExemptions" schema="InstallmentPenaltyExemption.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
				<fr:property name="linkFormat(deletePenaltyExemption)" value="<%="/exemptionsManagement.do?exemptionId=${externalId}&amp;method=deleteExemption&amp;personId=" + personId%>" />
				<fr:property name="key(deletePenaltyExemption)" value="label.delete" />
				<fr:property name="bundle(deletePenaltyExemption)" value="ACADEMIC_OFFICE_RESOURCES" />
				<fr:property name="sortBy" value="installment.startDate=asc" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="event" property="installmentPenaltyExemptions">
		<p>
			<em>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.penaltyExemptions.noPenaltyExemptions" />
			</em>
		</p>
	</logic:empty>
	<html:link action="<%="/exemptionsManagement.do?method=prepareCreateInstallmentPenaltyExemption&amp;personId=" + personId + "&amp;eventId=" + gratuityEventId %>">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.create"/>
	</html:link>
</logic:equal>

<bean:define id="personId" name="person" property="externalId" />
<fr:form action="<%="/exemptionsManagement.do?method=showEventsToApplyExemption&amp;personId=" + personId%>">
	<p class="mtop2">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.back" />
		</html:submit>
	</p>
</fr:form>
