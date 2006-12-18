<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@page import="pt.utl.ist.fenix.tools.file.FileManagerFactory"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component"/>
	<bean:define id="section" name="component" property="section" />

	<h2><bean:write name="section" property="name" /></h2>

	<logic:notEmpty name="component" property="items">
		<logic:iterate id="item" name="component" property="items">
			<logic:equal name="item" property="urgent" value="true">
				<font color="red">
			</logic:equal>
			<h3><bean:write name="item" property="name"/></h3>
			<bean:write name="item" property="information" filter="false"/><br/>
			<logic:equal name="item" property="urgent" value="true">
				</font>
			</logic:equal>
			<logic:present name="item" property="infoFileItems">
				<br/>
				<br/>
				<table>
				<logic:iterate id="infoFileItem" name="item" property="infoFileItems">
				<bean:define id="itemCode" name="item" property="idInternal" type="java.lang.Integer"/>
				<bean:define id="displayName" name="infoFileItem" property="displayName" type="java.lang.String"/>
				<bean:define id="externalStorageIdentification" name="infoFileItem" property="externalStorageIdentification" type="java.lang.String"/>
				<bean:define id="filename" name="infoFileItem" property="htmlFriendlyFilename" type="java.lang.String"/>
				<bean:define id="fileItemId" name="infoFileItem" property="idInternal" type="java.lang.Integer"/>
					<tr>
						<td><img src="<%= request.getContextPath() %>/images/list-bullet.gif" alt="<bean:message key="list-bullet" bundle="IMAGE_RESOURCES" />" /></td>
						<td>
							<html:link href="<%= FileManagerFactory.getFileManager().formatDownloadUrl(externalStorageIdentification,filename) %>" ><bean:write name="infoFileItem" property="displayName"/></html:link>
						</td>
					</tr>	
				</logic:iterate>
				</table>
			</logic:present>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>