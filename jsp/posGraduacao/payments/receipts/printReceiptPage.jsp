<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
<head>
<title><bean:message
	key="label.masterDegree.administrativeOffice.payments.receipts.printReceipt.title" /></title>
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
							width="50" height="104" border="0" /></td>
						<td>&nbsp;</td>
						<td>
						<table border="0" width="100%" height="100%">
							<tr align="left">
								<td>&nbsp;<b>INSTITUTO SUPERIOR TÉCNICO</b><br>
								&nbsp;<b>Secretaria da Pós-Graduação</b><br>
								&nbsp;<b>Centro de Custo 0212</b>
								<hr size="1">
								</td>
							</tr>
							<tr>
								<td align="right" valign="top"><b>Recibo de Pagamento</b>,<br />
								</td>
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
</table>

</body>
</html>
