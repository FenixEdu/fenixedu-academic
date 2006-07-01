<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.masterDegree.administrativeOffice.payments.receipt" /></h2>

<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
</logic:messagesPresent>

<br/>
<fr:view name="receipt" schema="receipt.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>
<br/>
<strong><bean:message key="label.masterDegree.administrativeOffice.payments.person" /></strong>:
<fr:view name="receipt" property="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>

<br/>
<strong><bean:message key="label.masterDegree.administrativeOffice.payments.contributor" /></strong>:
<fr:view name="receipt" property="contributor" schema="contributor.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>

<br/>
<strong><bean:message key="label.masterDegree.administrativeOffice.payments" /></strong>:
<table>
  <tr>
  	<td>
	  	<fr:view name="receipt" property="entries" schema="entry.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
			<fr:property name="sortBy" value="whenBooked=desc"/>
		</fr:layout>
		</fr:view>
	</td>
  </tr>
  <tr>
    <td  align="right"><strong><bean:message key="label.masterDegree.administrativeOffice.payments.totalAmount"/></strong>:<bean:define id="totalAmount" name="receipt" property="totalAmount" type="java.math.BigDecimal"/>&nbsp;<%= totalAmount.toPlainString() %>&nbsp;<bean:message key="label.masterDegree.administrativeOffice.payments.currencySymbol"/></td>
  </tr>
</table>

<html:form action="/payments.do" target="_blank">
	<html:hidden property="method" value="printReceipt" />
	<br/>
	<fr:edit id="receipt" name="receipt" visible="false" />
	<html:submit styleClass="inputbutton"><bean:message key="label.masterDegree.administrativeOffice.payments.print"/></html:submit>
</html:form>
