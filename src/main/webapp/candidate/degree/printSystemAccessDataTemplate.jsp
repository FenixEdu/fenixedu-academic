<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>

<table width="100%" height="90%" border="0">
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
							alt="<bean:message key="istlogo" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="IMAGE_RESOURCES" />"
							width="50" height="104" border="0" /></td>
						<td>&nbsp;</td>
						<td>
						<table border="0" width="100%" height="100%">
							<tr valign="top" align="left">
								<td>&nbsp;<b>INSTITUTO SUPERIOR T�CNICO</b><br />
								&nbsp;<b>Centro de Informática do <%= net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent() %></b><br />
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

<br/><br/>
