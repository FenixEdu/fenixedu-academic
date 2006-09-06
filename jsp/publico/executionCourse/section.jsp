<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><fr:view name="section" property="name" /></h2>

<logic:notEmpty name="section" property="associatedItems">
	<logic:iterate id="item" name="section" property="orderedItems">
		<h3><fr:view name="item" property="name"/></h3>
		<logic:notEmpty name="item" property="information">
			<fr:view name="item" property="information">
				<fr:layout>
					<fr:property name="escaped" value="false" />
					<fr:property name="newlineAware" value="false" />
				</fr:layout>
			</fr:view>
			<br/>
			<br/>
		</logic:notEmpty>
		<logic:notEmpty name="item" property="sortedFileItems">
			<br/>
			<table>
				<logic:iterate id="fileItem" name="item" property="fileItems">
					<bean:define id="itemCode" name="item" property="idInternal" type="java.lang.Integer"/>
					<bean:define id="displayName" name="fileItem" property="displayName" type="java.lang.String"/>
					<bean:define id="externalStorageIdentification" name="fileItem" property="externalStorageIdentification" type="java.lang.String"/>
					<bean:define id="originalFilename" name="fileItem" property="filename" type="java.lang.String"/>
					<bean:define id="filename" type="java.lang.String"><%= originalFilename.replaceAll("&", "&amp;").replaceAll(" ", "%20") %></bean:define>
					<bean:define id="fileItemId" name="fileItem" property="idInternal" type="java.lang.Integer"/>
					<tr>
						<td><img src="<%= request.getContextPath() %>/images/list-bullet.gif" alt="<bean:message key="list-bullet" bundle="IMAGE_RESOURCES" />" /></td>
						<td>
							<html:link href="<%= pt.utl.ist.fenix.tools.file.FileManagerFactory.getFileManager().getDirectDownloadUrlFormat() + "/" + externalStorageIdentification + "/" + filename %>" ><bean:write name="fileItem" property="displayName"/></html:link>
						</td>
					</tr>	
				</logic:iterate>
			</table>
		</logic:notEmpty>
	</logic:iterate>
</logic:notEmpty>
