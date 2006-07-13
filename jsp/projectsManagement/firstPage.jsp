<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<logic:notPresent name="infoCostCenter" scope="request">
	<html:img altKey="title.progectsManagement" src="<%= request.getContextPath() + "/images/projectsManagement.gif"%>" altKey="projectsManagement" bundle="IMAGE_RESOURCES" />
</logic:notPresent>
<logic:present name="infoCostCenter" scope="request">
	<html:img altKey="title.institucionalProgectsManagement" src="<%= request.getContextPath() + "/images/institucionalProjectsManagement.gif"%>" />
	<logic:notEmpty name="infoCostCenter" property="description" scope="request">
		<br />
		<br />
		<br />
		<h3><bean:write name="infoCostCenter" property="description" /></h3>
	</logic:notEmpty>
</logic:present>
<br />
<br />
<br />
<%--WIDTH="600" BORDER="0" align="center" CELLPADDING="0" CELLSPACING="0" --%>
<table cellspacing="0" cellpadding="0" align="center">
	<tr>
		<td><html:img src="<%= request.getContextPath() + "/images/imagemCentral_01.jpg"%>" altKey="imagemCentral_01" bundle="IMAGE_RESOURCES" /></td>
		<td><html:img src="<%= request.getContextPath() + "/images/imagemCentral_02.jpg"%>" altKey="imagemCentral_02" bundle="IMAGE_RESOURCES" /></td>
	</tr>
	<tr>
		<td><html:img src="<%= request.getContextPath() + "/images/imagemCentral_03.jpg"%>" altKey="imagemCentral_03" bundle="IMAGE_RESOURCES" /></td>
		<td><html:img src="<%= request.getContextPath() + "/images/imagemCentral_04.jpg"%>" altKey="imagemCentral_04" bundle="IMAGE_RESOURCES" /></td>
	</tr>
</table>
<br />
<br />
<br />
