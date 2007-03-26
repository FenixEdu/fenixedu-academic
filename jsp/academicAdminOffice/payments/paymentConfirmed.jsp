<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<bean:define id="personId" name="person" property="idInternal"/>


<em><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.paymentConfirmed" /></h2>

<p class="mtop2 mbottom15">
	<span class="success0"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.paymentConfirmed"/></span>
</p>


<table>
  <tr>
    <td>
		<html:form action="<%="/receipts.do?method=showPaymentsWithoutReceipt&amp;personId=" + personId %>">			
			<fr:edit id="entriesToSelect" name="entriesToSelect" visible="false" nested="true"/>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.createReceipt"/>
			</html:submit>
		</html:form>
	</td>
	<td>
		<html:form action="<%="/payments.do?method=backToShowOperations&amp;personId=" + personId %>">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/>
			</html:submit>
		</html:form>
	</td>
  </tr>
</table>

</logic:present>
