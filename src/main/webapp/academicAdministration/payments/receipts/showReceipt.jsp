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

<bean:define id="personId" name="receipt" property="person.externalId" />
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
	key="label.payments.management" /></h2>


<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true"
			bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<p class="mtop15 mbottom05"><strong><bean:message
	bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
<fr:view name="receipt" property="person"
	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>


<fr:view name="receipt" schema="receipt.view-with-number-and-year">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop15" />
		<fr:property name="columnClasses" value=",tdhl1" />
	</fr:layout>
</fr:view>


<fr:view name="receipt" property="entries" schema="entry.view">
	<fr:layout name="tabular">
		<fr:property name="classes"
			value="tstyle4 mtop05 mbottom0 width700px" />
		<fr:property name="columnClasses"
			value="width8em acenter, width30em acenter,width8em acenter,width15em aright" />
		<fr:property name="sortBy" value="whenRegistered=asc,externalId=asc" />
	</fr:layout>
</fr:view>

<br/>

<fr:form
	action="<%="/paymentsManagement.do?method=showReceipts&amp;personId=" + personId%>">
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel">
		<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
	</html:cancel>
</fr:form>
