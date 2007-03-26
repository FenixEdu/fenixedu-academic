<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="TREASURY">

<h2><bean:message bundle="TREASURY_RESOURCES" key="label.payments.paymentConfirmed" /></h2>

<strong><bean:write name="administrativeOfficeUnit" property="nameWithAcronym"/></strong>
<br/>

<p class="mtop2 mbottom15">
	<span class="success0"><bean:message bundle="TREASURY_RESOURCES" key="label.payments.paymentConfirmed"/></span>
</p>

<bean:define id="personId" name="person" property="idInternal"/>
<bean:define id="administrativeOfficeId" name="administrativeOffice" property="idInternal" />
<bean:define id="administrativeOfficeUnitId" name="administrativeOfficeUnit" property="idInternal" />
<table>
	<tr>
		<td><html:form action="<%="/receipts.do?method=showPaymentsWithoutReceipt&amp;personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId%>">
				<fr:edit id="entriesToSelect" name="entriesToSelect" visible="false" nested="true"/>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message bundle="TREASURY_RESOURCES" key="label.payments.createReceipt"/>
				</html:submit>
			</html:form>
		</td>
		<td>
			<html:form action="<%="/payments.do?method=backToShowOperations&amp;personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId%>">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.back"/>
				</html:submit>
			</html:form>
		</td>
	</tr>
</table>


</logic:present>
