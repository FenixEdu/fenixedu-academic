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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="receiptYear" name="receipt" property="year"/>
<bean:define id="receiptNumber" name="receipt" property="numberWithSeries"/>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.creditNotes" arg0="<%=receiptNumber.toString()%>" arg1="<%=receiptYear.toString()%>" /></h2>


<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person"/></strong>
<fr:view name="receipt" property="person"
	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
	</fr:layout>
</fr:view>

<bean:define id="personId" name="receipt" property="person.externalId"/>
<logic:notEmpty name="receipt" property="creditNotes">
	<fr:view name="receipt" property="creditNotes" schema="CreditNote.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright" />
			<fr:property name="linkFormat(view)" value="<%="/creditNotes.do?creditNoteId=${externalId}&amp;method=showCreditNote"%>"/>
			<fr:property name="key(view)" value="label.payments.show"/>
			<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="sortBy" value="year=desc,number=desc"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>


<logic:empty name="receipt" property="creditNotes">
	<p>
		<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.noCreditNotes"/></em>.
	</p>
</logic:empty>


<bean:define id="receiptId" name="receipt" property="externalId" />
<html:form action="<%="/creditNotes.do?personId=" + personId + "&amp;receiptID=" + receiptId%>">
	<p class="mtop15">
		<fr:edit id="receipt" name="receipt" visible="false" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='prepareCreateCreditNote';">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.create" />
		</html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='prepareShowReceipt';">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/>
		</html:submit>
	</p>
</html:form>	
