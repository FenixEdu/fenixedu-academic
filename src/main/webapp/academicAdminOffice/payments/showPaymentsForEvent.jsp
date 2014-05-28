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

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.extract" /></h2>

<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>

<logic:notEmpty name="event" property="positiveEntries">	
	<fr:view name="event" property="positiveEntries" schema="entry.view-with-payment-mode">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 tdleftm mtop05" />
			<fr:property name="columnClasses" value=",acenter,aright,aright" />
			<fr:property name="sortBy" value="whenRegistered=desc"/>
		</fr:layout>
	</fr:view>	
	
</logic:notEmpty>

	
<bean:define id="personId" name="person" property="externalId" />
<fr:form action='<%= "/payments.do?method=showEventsWithPayments&amp;personId=" + personId %>'>
<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:cancel>
</fr:form>
