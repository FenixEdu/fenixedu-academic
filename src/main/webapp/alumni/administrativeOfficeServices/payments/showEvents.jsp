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
<html:xhtml/>

<!-- alumni/administrativeOffice/Services/payments/showEvents.jsp -->

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments" /></h2>

<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright" />
	</fr:layout>
</fr:view>


<p class="mtop15 mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.currentEvents" /></strong></p>
<logic:notEmpty name="notPayedEvents">
	<fr:view name="notPayedEvents" schema="AccountingEvent.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 tdleftm mtop025" />
			<fr:property name="columnClasses" value=",," />
			<fr:property name="sortBy" value="whenOccured=asc"/>
			<fr:property name="linkFormat(view)" value="/payments.do?eventId=${externalId}&amp;method=showEventDetails"/>
			<fr:property name="key(view)" value="label.payments.viewDetails"/>
			<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
		</fr:layout>
	</fr:view>
	<br/>
</logic:notEmpty>

<logic:empty name="notPayedEvents">
	<p class="mtop05">
		<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.events.noEvents" /></em>
	</p>
</logic:empty>

	
<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.payedEvents2" /></strong></p>
<logic:notEmpty name="payedEntries">
	<fr:view name="payedEntries" schema="entry.view-for-student">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 tdleft tdcenter mtop025" />
			<fr:property name="columnClasses" value=",," />
			<fr:property name="sortBy" value="whenRegistered=asc"/>
		</fr:layout>
	</fr:view>
	<br/>
</logic:notEmpty>

<logic:empty name="payedEntries">
	<p class="mtop05">
		<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.events.noPayedEvents" />.</em>
	</p>
</logic:empty>