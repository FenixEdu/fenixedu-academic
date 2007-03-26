<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@ page import="net.sourceforge.fenixedu.util.Money" %>

<logic:present role="TREASURY">

<h2><bean:message bundle="TREASURY_RESOURCES" key="label.payments.receipts.receipt" /></h2>

<strong><bean:write name="administrativeOfficeUnit" property="nameWithAcronym"/></strong>
<br/><br/>

<logic:messagesPresent message="true" property="context">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" property="context" bundle="TREASURY_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>
<logic:messagesPresent message="true" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>">
	<ul class="nobullet list6">
			<html:messages id="messages" message="true" property="<%=org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE%>" bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
</logic:messagesPresent>


<fr:view name="receipt" schema="receipt.view-with-number-and-year">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop15" />
		<fr:property name="columnClasses" value=",tdhl1" />
	</fr:layout>
</fr:view>


<p class="mbottom05"><strong><bean:message bundle="TREASURY_RESOURCES" key="label.payments.person" /></strong></p>
<fr:view name="receipt" property="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
	</fr:layout>
</fr:view>


<p class="mbottom05"><strong><bean:message bundle="TREASURY_RESOURCES" key="label.payments.contributor" /></strong></p>
<fr:view name="receipt" property="contributorParty" schema="contributor.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
	</fr:layout>
</fr:view>


<p class="mbottom05"><strong><bean:message bundle="TREASURY_RESOURCES" key="label.payments" /></strong></p>
	  	<fr:view name="receipt" property="entries" schema="entry.view">
		<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4 mtop05 mbottom0 width700px" />
		<fr:property name="columnClasses" value="width8em acenter, width30em acenter,width8em acenter,width15em aright"/>
			<fr:property name="sortBy" value="whenRegistered=desc"/>
		</fr:layout>
		</fr:view>

<table class="tstyle4 mtop0" style="width: 700px;">
	<tr>
		<td class="aright" colspan="4"><span style="background-color: #fdfbdd;"><bean:message bundle="TREASURY_RESOURCES" key="label.payments.totalAmount"/>: <bean:define id="totalAmount" name="receipt" property="totalAmount" type="Money"/>&nbsp;<%= totalAmount.toPlainString() %>&nbsp;<bean:message bundle="APPLICATION_RESOURCES" key="label.currencySymbol"/></span></td>
	</tr>
</table>
<bean:define id="personId" name="receipt" property="person.idInternal"/>
<bean:define id="administrativeOfficeId" name="administrativeOffice" property="idInternal" />
<bean:define id="administrativeOfficeUnitId" name="administrativeOfficeUnit" property="idInternal" />
<logic:equal name="receipt" property="active" value="true">
<table>
		<tr>
			<td>
				<html:form action='<%= "/receipts.do?personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId%>' target="_blank">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="printReceipt" />
						<fr:edit id="receipt" name="receipt" visible="false" />
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="TREASURY_RESOURCES" key="label.payments.print"/></html:submit>
				</html:form>
			</td>
			<%--
			<td>
				<html:form action="<%="/payments.do?method=showCreditNotes&amp;personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId%>">
					<fr:edit id="receipt" name="receipt" visible="false" />
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message bundle="TREASURY_RESOURCES" key="button.payments.creditNotes"/>
					</html:submit>
				</html:form>
			</td>
			 --%>
			<td>
				<html:form action="<%="/receipts.do?method=showReceipts&amp;personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId%>">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message bundle="APPLICATION_RESOURCES" key="label.back"/>
					</html:submit>
				</html:form>	
			</td>
			<td>
			</td>
		</tr>
	</table>
</logic:equal>
<logic:notEqual name="receipt" property="active" value="true">
	<table>
		<tr>
			<td>
				<html:form action="<%="/receipts.do?method=showReceipts&amp;personId=" + personId + "&amp;administrativeOfficeId=" + administrativeOfficeId + "&amp;administrativeOfficeUnitId=" + administrativeOfficeUnitId%>">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message bundle="TREASURY_RESOURCES" key="label.back"/>
					</html:submit>
				</html:form>	
			</td>
			<td>
			</td>
		</tr>
	</table>
</logic:notEqual>
</logic:present>
