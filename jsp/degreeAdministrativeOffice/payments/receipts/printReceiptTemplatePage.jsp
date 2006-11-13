<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ page import="net.sourceforge.fenixedu.util.Money" %>

<logic:present role="DEGREE_ADMINISTRATIVE_OFFICE">

<html>
<head>
<title><bean:message
	key="label.payments.receipts.printReceipt.title" /></title>
</head>

<body>
<table width="100%" height="100%" border="0">
	<tr height="30">
		<td>
		<table width="100%" border="0" valign="top">
			<tr>
				<td height="100" colspan="2">
				<table border="0" width="100%" height="104" align="center"
					cellpadding="0" cellspacing="0">
					<tr>
						<td width="50" height="100"><img
							src="<%= request.getContextPath() %>/images/LogoIST.gif"
							alt="<bean:message key="LogoIST" bundle="IMAGE_RESOURCES" />"
							width="50" height="104" border="0" /></td>
						<td>&nbsp;</td>
						<td>
						<table border="0" width="100%" height="100%">
							<tr align="left">
								<td>&nbsp;<b><bean:message key="label.payments.printTemplates.institutionName.upper.case"/></b><br />
								&nbsp;<b><bean:write name="currentUnit" property="name"/></b><br/>
                      			&nbsp;<b><bean:message key="label.payments.printTemplates.costCenter"/> <bean:write name="currentUnit" property="costCenterCode"/></b>
								<hr size="1">
								</td>
							</tr>
							<tr>
								<td align="right" valign="top"><b><bean:message key="label.payments.printTemplates.receipt.receiptNumber"/> </b> <bean:write
									name="receipt" property="number" />/<bean:write name="receipt"
									property="year" /><br />
								<logic:greaterEqual name="receipt" property="receiptsVersionsCount"
									value="2">
									<em><bean:message  key="label.payments.printTemplates.receipt.secondPrintVersion"/></em>
								</logic:greaterEqual></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr valign="top">
		<td>
		<table width="100%" border="0">
			<tr>
				<td>
				<table width="100%" border="0">
					<tr>
						<td width="20%"><strong><bean:message  key="label.payments.printTemplates.processFrom"/>:</strong></td>
						<td width="80%">&nbsp;</td>
					</tr>
					<tr>
					  <td> <bean:message key="label.net.sourceforge.fenixedu.domain.Person.name" bundle="APPLICATION_RESOURCES" /> </td>
					  <td> <bean:write name="receipt" property="person.name"/> </td>
					</tr>
					<tr>
					  <td> <bean:message key="label.net.sourceforge.fenixedu.domain.Person.idDocumentType" bundle="APPLICATION_RESOURCES"/> </td>
					  <td> <bean:message name="receipt" property="person.idDocumentType.name" bundle="ENUMERATION_RESOURCES"/> </td>
					</tr>
					<tr>
					  <td> <bean:message key="label.net.sourceforge.fenixedu.domain.Person.documentIdNumber" bundle="APPLICATION_RESOURCES" /> </td>
					  <td> <bean:write name="receipt" property="person.documentIdNumber"/> </td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>

					<tr>
						<td width="30%"><strong><bean:message  key="label.payments.printTemplates.receipt.contributor"/>:</strong></td>
						<td width="70%">&nbsp;</td>
					</tr>

					<tr>
						<td><bean:message
							key="label.net.sourceforge.fenixedu.domain.organizationalStructure.Party.name" bundle="APPLICATION_RESOURCES" />:</td>
						<td><bean:write name="receipt"
							property="contributorParty.name" /></td>
					</tr>
					<tr>
						<td><bean:message
							key="label.net.sourceforge.fenixedu.domain.organizationalStructure.Party.socialSecurityNumber" bundle="APPLICATION_RESOURCES" />:</td>
						<td><bean:write name="receipt"
							property="contributorParty.socialSecurityNumber" /></td>
					</tr>
					<tr>
						<td><bean:message key="label.net.sourceforge.fenixedu.domain.organizationalStructure.Party.address" bundle="APPLICATION_RESOURCES" />:</td>
						<td><bean:write name="receipt" property="contributorParty.address" /></td>
					</tr>
					<logic:notEmpty name="receipt" property="contributorParty.areaCode">
					<tr>
						<td><bean:message key="label.net.sourceforge.fenixedu.domain.organizationalStructure.Party.areaCode" bundle="APPLICATION_RESOURCES" /></td>
						<td><bean:write name="receipt" property="contributorParty.areaCode" /> - <bean:write name="receipt" property="contributorParty.areaOfAreaCode" /></td>
					</tr>
					</logic:notEmpty>
				</table>
			</tr>
		</table>
		</td>
	</tr>

	<tr>
		<td>
		<table align="right">
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
			<logic:iterate id="entry" name="sortedEntries">
				<tr>
					<td>
						<app:labelFormatter name="entry" property="description">
	      					<app:property name="enum" value="ENUMERATION_RESOURCES"/>
        					<app:property name="application" value="APPLICATION_RESOURCES"/>
							<app:property name="default" value="APPLICATION_RESOURCES"/>
						</app:labelFormatter>
					</td>
					<td>.........................................&nbsp;</td>
					<td><bean:define id="amount" name="entry" property="amountWithAdjustment"
						type="Money" /> <%=amount.toPlainString()%> &nbsp;<bean:message
						key="label.currencySymbol" /></td>
				</tr>
			</logic:iterate>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><strong><bean:message  key="label.payments.printTemplates.totalAmountToPay"/> </strong></td>
				<td>_____________________&nbsp;</td>
				<td><strong><bean:define id="totalAmount" name="receipt"
					property="totalAmount" type="Money" /><%=totalAmount.toPlainString()%>&nbsp;<bean:message
					key="label.currencySymbol" /></strong></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr valign="bottom">
		<td>
		<table valign="bottom" width="100%" border="0">
			<tr>
				<td>
					<bean:message  key="label.payments.printTemplates.city"/>, <%= new java.text.SimpleDateFormat("dd MMMM yyyy", request.getLocale()).format(new java.util.Date()) %>
				</td>
			</tr>

			<tr>
				<td>&nbsp;</td>
				<td colspan="2" valign="bottom">&nbsp;
				<div align="center">&nbsp;</div>
				<div align="center">&nbsp;</div>
				<div align="center"><b><bean:message  key="label.payments.printTemplates.theEmployee"/></b> <br/>
				<br/>
				<br/>
				</div>
				<hr align="center" width="300" size="1">
				</td>
			</tr>

		</table>
		</td>
	</tr>

	<tr>
		<td><br/><br/><jsp:include page="/degreeAdministrativeOffice/payments/commons/footer.jsp" flush="true" /></td>
	</tr>
</table>

</body>
</html>

</logic:present>
