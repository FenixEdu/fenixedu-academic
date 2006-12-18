<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<%@ page import="net.sourceforge.fenixedu.util.Money" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<bean:define id="receiptYear" name="creditNote" property="receipt.year" />
	<bean:define id="receiptNumber" name="creditNote" property="receipt.number" />
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.creditNotes"
		arg0="<%=receiptNumber.toString()%>" arg1="<%=receiptYear.toString()%>" /></h2>
	<hr />
	<br />

	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
	</logic:messagesPresent>

	<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" />:</strong>
	<bean:define id="receipt" name="creditNote" property="receipt" />
	<fr:view name="receipt" property="person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>

	<br />
	<table>
		<tr>
			<td><fr:view name="creditNote" property="creditNoteEntries" schema="CreditNoteEntry.view">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle4" />
					</fr:layout>
				</fr:view>
			</td>
		</tr>
		<tr>
			<td align="right">
				<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.totalAmount" /></strong>:
				<bean:define id="totalAmount" name="creditNote" property="totalAmount" type="Money" />&nbsp;<%=totalAmount.toPlainString()%>&nbsp;
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.currencySymbol" />
			</td>
		</tr>
	</table>
	<br />
	<e:labelValues id="creditNoteStates"
		enumeration="net.sourceforge.fenixedu.domain.accounting.CreditNoteState"
		bundle="ENUMERATION_RESOURCES" />

	<logic:equal name="creditNote" property="allowedToChangeState" value="true">
		<html:form action="/payments.do?method=changeCreditNoteState">
			<fr:edit id="receipt" name="creditNote" property="receipt" visible="false" />
			<fr:edit id="creditNote" name="creditNote" visible="false" />
			<table>
				<tr>
					<td colspan="2">
						<strong><bean:message key="label.state" bundle="ACADEMIC_OFFICE_RESOURCES" />:</strong>&nbsp;&nbsp;
						<html:select property="creditNoteState">
							<html:options collection="creditNoteStates" property="value" labelProperty="label" />
						</html:select>
					</td>
					<td>
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
							<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.save" />
						</html:submit>
					</td>
				</tr>
			</table>
		</html:form>
		<br/>
		<table>
			<tr>
				<td>
					<html:form action="/payments.do?method=printCreditNote" target="_blank">
						<fr:edit id="receipt" name="creditNote" property="receipt" visible="false" />
						<fr:edit id="creditNote" name="creditNote" visible="false" /><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
							<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.print" />
						</html:submit>
					</html:form>
				</td>
				<td>
					<html:form action="/payments.do?method=showCreditNotes">
						<fr:edit id="receipt" name="creditNote" property="receipt" visible="false" />
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
							<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back" />
						</html:submit>
					</html:form>
				</td>
			</tr>
		</table>
	</logic:equal>
	<logic:equal name="creditNote" property="allowedToChangeState" value="false">
		<table>
		  <tr>
		    <td>
		    	<logic:notEqual name="creditNote" property="cancelled" value="true">
					<html:form action="/payments.do?method=printCreditNote" target="_blank">
						<fr:edit id="receipt" name="creditNote" property="receipt" visible="false" />
						<fr:edit id="creditNote" name="creditNote" visible="false" />
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
							<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.print" />
						</html:submit>
					</html:form>
			</logic:notEqual>
		    </td>
		    <td>
				<html:form action="/payments.do?method=showCreditNotes">
					<fr:edit id="receipt" name="creditNote" property="receipt" visible="false" />
					<fr:edit id="creditNote" name="creditNote" visible="false" />
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
						<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back" />
					</html:submit>
				</html:form>
			</td>
		  </tr>
		</table>
	</logic:equal>

</logic:present>
