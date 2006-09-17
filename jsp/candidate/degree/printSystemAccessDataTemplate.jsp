<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="<%= request.getContextPath() %>/CSS/dotist_print.css" rel="stylesheet" media="print" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/transitional.css" rel="stylesheet" media="screen" type="text/css" />
</head>

<body>

<table width="100%" height="100%" border="0">
	<tr height="30">
		<td>
		<table width="100%" border="0" valign="top">
			<tr>
				<td height="100" colspan="2">
				<table border="0" width="100%" height="100" align="center"
					cellpadding="0" cellspacing="0">
					<tr>
						<td width="50" height="100"><img
							src="<%= request.getContextPath() %>/images/LogoIST.gif"
							alt="<bean:message key="istlogo" bundle="IMAGE_RESOURCES" />"
							width="50" height="104" border="0" /></td>
						<td>&nbsp;</td>
						<td>
						<table border="0" width="100%" height="100%">
							<tr valign="top" align="left">
								<td>&nbsp;<b>INSTITUTO SUPERIOR TÉCNICO</b><br />
								&nbsp;<b>Centro de Informática do Instituto Superior Técnico</b><br />
								<hr size="1">
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
	<tr valign="top">
		<td><br />
		<br />
		<br />
		<table width="100%" border="0">
			<tr>
				<td>
				<table width="100%" border="0">
					<tr>
						<td><strong><bean:message bundle="CANDIDATE_RESOURCES"
							key="label.name" /></strong></td>
						<td><bean:write name="person" property="name" /></td>
					</tr>
					<tr>
						<td><strong><bean:message bundle="CANDIDATE_RESOURCES"
							key="label.istUsername" /></strong></td>
						<td><bean:write name="person" property="istUsername" /></td>
					</tr>
					<tr>
						<td><strong><bean:message bundle="CANDIDATE_RESOURCES"
							key="label.studentNumber" /></strong></td>
						<td><bean:write name="person" property="student.number" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
				</table>

				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td><p><bean:message
			key="label.candidacy.operation.printSystemAccessData.information.message" /></p></td>
	</tr>
</table>

<br/><br/><br/>
</body>
</html:html>
