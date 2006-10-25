<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="DEGREE_ADMINISTRATIVE_OFFICE">

<h2><bean:message key="label.payments.receipt" /></h2>
<hr/>
<br/>

<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
</logic:messagesPresent>

<table>
  <tr>
  	<td colspan="2" align="right">
  		<fr:view name="receipt" schema="receipt.view-with-number-and-year">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>
  	</td>
  </tr>
  <tr>
    <td>
<strong><bean:message key="label.payments.person" /></strong>:
<fr:view name="receipt" property="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>
    </td>
    <td>
<strong><bean:message key="label.payments.contributor" /></strong>:
<fr:view name="receipt" property="contributorParty" schema="contributor.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>
    </td>
  </tr>
  <tr>
  	<td colspan="2">
<br/>
<strong><bean:message key="label.payments" /></strong>:
<table>
  <tr>
  	<td>
	  	<fr:view name="receipt" property="entries" schema="entry.view">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
			<fr:property name="sortBy" value="whenRegistered=desc"/>
		</fr:layout>
		</fr:view>
	</td>
  </tr>
  <tr>
    <td  align="right"><strong><bean:message key="label.payments.totalAmount"/></strong>:<bean:define id="totalAmount" name="receipt" property="totalAmount" type="java.math.BigDecimal"/>&nbsp;<%= totalAmount.setScale(2).toPlainString() %>&nbsp;<bean:message key="label.payments.currencySymbol"/></td>
  </tr>
</table>
  	</td>
  </tr>
</table>

<bean:define id="personId" name="receipt" property="person.idInternal"/>

<html:form action='<%= "/payments.do?personId=" + personId %>' target="_blank">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="printReceipt" />
	<br/>
	<fr:edit id="receipt" name="receipt" visible="false" />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="label.payments.print"/></html:submit>
	<%--<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='backToShowOperations';"><bean:message key="button.payments.no"/></html:submit> --%>
</html:form>

</logic:present>
