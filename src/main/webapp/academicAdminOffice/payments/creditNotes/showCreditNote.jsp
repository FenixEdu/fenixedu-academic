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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>

<%@ page import="net.sourceforge.fenixedu.util.Money" %>

<bean:define id="receiptYear" name="creditNote" property="receipt.year" />
<bean:define id="receiptNumber" name="creditNote" property="receipt.numberWithSeries" />
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.creditNotes"
	arg0="<%=receiptNumber.toString()%>" arg1="<%=receiptYear.toString()%>" /></h2>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong>
<bean:define id="receipt" name="creditNote" property="receipt" />
<fr:view name="receipt" property="person"
	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
	</fr:layout>
</fr:view>

<fr:view name="creditNote" property="creditNoteEntries" schema="CreditNoteEntry.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 width50em mbottom0" />
		<fr:property name="columnClasses" value=",aright" />
	</fr:layout>
</fr:view>

<table class="tstyle4 width50em mtop0">
	<tr>
		<td class="aright">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.totalAmount" />:
			<bean:define id="totalAmount" name="creditNote" property="totalAmount" type="Money" />&nbsp;<%=totalAmount.toPlainString()%><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.currencySymbol" />
		</td>
	</tr>
</table>


<e:labelValues id="creditNoteStates"
	enumeration="net.sourceforge.fenixedu.domain.accounting.CreditNoteState"
	bundle="ENUMERATION_RESOURCES" />

<logic:equal name="creditNote" property="allowedToChangeState" value="true">
	<html:form action="/creditNotes.do?method=changeCreditNoteState">
		<fr:edit id="receipt" name="creditNote" property="receipt" visible="false" />
		<fr:edit id="creditNote" name="creditNote" visible="false" />
		<table class="tstyle5">
			<tr>
				<td>
					<bean:message key="label.state" bundle="ACADEMIC_OFFICE_RESOURCES" />: 
					<html:select property="creditNoteState">
						<html:options collection="creditNoteStates" property="value" labelProperty="label" />
					</html:select>
				</td>
				<td>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.save" />
					</html:submit>
				</td>
			</tr>
		</table>
	</html:form>
</logic:equal>
<table>
	<tr>
		<td>
			<html:form action="/creditNotes.do?method=printCreditNote" target="_blank">
				<fr:edit id="receipt" name="creditNote" property="receipt" visible="false" />
				<fr:edit id="creditNote" name="creditNote" visible="false" /><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.print" />
				</html:submit>
			</html:form>
		</td>
		<td>
			<html:form action="/creditNotes.do?method=showCreditNotes">
				<fr:edit id="receipt" name="creditNote" property="receipt" visible="false" />
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back" />
				</html:submit>
			</html:form>
		</td>
	</tr>
</table>	
