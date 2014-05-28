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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.receipts" /></h2>

<logic:messagesPresent message="true" property="context">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" property="context" bundle="ACADEMIC_OFFICE_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>
<logic:messagesPresent message="true" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true"  property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>" bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person"/></strong></p>
<fr:view name="person"
	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>

<logic:notEmpty name="receiptsForAdministrativeOffice">
	<bean:define id="personId" name="person" property="externalId"/>
	<fr:view name="receiptsForAdministrativeOffice" schema="receipt.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 mtop15" />
			<fr:property name="sortBy" value="year=desc,numberWithSeries=desc"/>
			<fr:property name="linkFormat(view)" value="<%="/receipts.do?method=prepareShowReceipt&amp;receiptID=${externalId}&amp;personId=" + personId %>"/>
			<fr:property name="key(view)" value="label.payments.show"/>
			<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="linkFormat(edit)" value="<%="/receipts.do?method=prepareEditReceipt&amp;receiptID=${externalId}&amp;personId=" + personId %>"/>
			<fr:property name="key(edit)" value="label.payments.edit"/>
			<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="visibleIf(edit)" value="active"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="receiptsForAdministrativeOffice">
		<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.noReceipts"/></em>.
</logic:empty>

<bean:define id="personId" name="person" property="externalId"/>
<html:form action='<%= "/receipts.do?method=showOperations&amp;personId=" + personId %>'>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:submit>
</html:form>
