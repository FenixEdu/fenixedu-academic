<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<h2><bean:message key="title.protocols.create" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>


<h3 class="mtop15"><bean:message key="label.protocol.files" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>

<fr:form action="/createProtocol.do" encoding="multipart/form-data">
<html:hidden bundle="HTMLALT_RESOURCES" name="protocolsForm" property="method" value="removeFile"/>
<html:hidden bundle="HTMLALT_RESOURCES" name="protocolsForm" property="fileName"/>
<fr:edit id="protocolFactory" name="protocolFactory" visible="false"/>

<logic:notEmpty name="protocolFactory" property="fileBeans">
<table class="tstyle1">
	<tr>
		<th><bean:message key="label.name" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th><bean:message key="label.filePermission" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>		
		<th></th>				
	</tr>
	<logic:iterate id="fileBean" name="protocolFactory" property="fileBeans" type="net.sourceforge.fenixedu.dataTransferObject.protocol.ProtocolFileBean">
	<tr>
		<td><bean:write name="fileBean" property="fileName"/></td>
		<td><bean:message name="fileBean" property="filePermissionTypeName" bundle="ENUMERATION_RESOURCES"/></td>
		<td>
			<html:submit onclick="<%= "this.form.fileName.value='" + fileBean.getFileName() + "'"%>">
				<bean:message key="button.remove" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
			</html:submit>
		</td>				
	</tr>
	</logic:iterate>
</table>
</logic:notEmpty>

<logic:empty name="protocolFactory" property="fileBeans">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em></p>
</logic:empty>
<br/>
</fr:form>

<fr:form action="/createProtocol.do?method=addFile" encoding="multipart/form-data">
	<fr:edit name="protocolFactory" schema="edit.protocolFile">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>			
		</fr:layout>
	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.insert" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="back">
		<bean:message key="button.back" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:cancel>
	<html:cancel bundle="HTMLALT_RESOURCES" property="createProtocol">
		<bean:message key="button.createProtocol" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
	</html:cancel>
</fr:form>