<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<em><bean:message key="title.scientificCouncil.portalTitle" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="title.protocols.edit" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>



<fr:form action="/editProtocol.do">
<html:hidden bundle="HTMLALT_RESOURCES" name="protocolsForm" property="method" value="deleteProtocolFile"/>
<html:hidden bundle="HTMLALT_RESOURCES" name="protocolsForm" property="fileID"/>
<fr:edit id="protocolFactory" name="protocolFactory" visible="false"/>

<p class="mtop2 mbottom0"><strong><bean:message key="label.protocol.files" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
<logic:notEmpty name="protocolFactory" property="protocol.protocolFiles">
<table class="tstyle1">
	<tr>
		<th><bean:message key="label.name" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>
		<th><bean:message key="label.filePermission" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></th>		
		<th></th>				
	</tr>
	<logic:iterate id="file" name="protocolFactory" property="protocol.protocolFiles" type="net.sourceforge.fenixedu.domain.protocols.ProtocolFile">
	<tr>
		<td><fr:view name="file" layout="link" /></td>
		<td><bean:message name="file" property="filePermissionType" bundle="ENUMERATION_RESOURCES"/></td>
		<td>
			<html:link page="/editProtocol.do?method=deleteProtocolFile" paramId="fileID" paramName="file" paramProperty="idInternal">
				<bean:message key="button.delete" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>
			</html:link>,
			<html:link page="/editProtocol.do?method=changeProtocolFilePermission" paramId="fileID" paramName="file" paramProperty="idInternal">
			Alterar permissões
			</html:link>
		</td>				
						
	</tr>
	</logic:iterate>
</table>
</logic:notEmpty>

<logic:empty name="protocolFactory" property="protocol.protocolFiles">
	<p><em><bean:message key="label.protocol.hasNone" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>.</p>
</logic:empty>
</fr:form>

<fr:form action="/editProtocol.do?method=addProtocolFile" encoding="multipart/form-data">
	<fr:edit name="protocolFactory" schema="edit.protocolFile">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>			
		</fr:layout>
	</fr:edit>

	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.insert" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
		</html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="back">
			<bean:message key="button.cancel" bundle="SCIENTIFIC_COUNCIL_RESOURCES" />
		</html:cancel>
	</p>

</fr:form>