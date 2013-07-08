<%@page import="net.sourceforge.fenixedu.domain.person.RoleType"%>
<%@page import="net.sourceforge.fenixedu.applicationTier.IUserView"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:notPresent name="infoCostCenter" scope="request">
	<html:img altKey="title.progectsManagement" src="<%= request.getContextPath() + "/images/projectsManagement.gif"%>" bundle="IMAGE_RESOURCES" />
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

<p>
	<span class="error">Esta interface vai ser descontinuada brevemente.</span>
</p>
<p>
	Todas as informa&#231;&#245;es relativas aos projetos podem ser consultados na p&#225;gina da respetiva unidade nos seguintes endere&#231;os, conforme a institui&#231;&#227;o em que o projeto decorre:
	<ul>
		<li>
			<a href="https://dot.ist.utl.pt/">https://dot.ist.utl.pt/</a> - para projetos do IST
		</li>
		<li>
			<a href="https://dot.ist-id.ist.utl.pt/">https://dot.ist-id.ist.utl.pt/</a>  - para projetos do IST-ID
		</li>
		<li>
			<a href="https://dot.adist.ist.utl.pt/">https://dot.adist.ist.utl.pt/</a> - para projetos da ADIST
		</li>
	</ul>
</p>
