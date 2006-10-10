<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="net.sourceforge.fenixedu.domain.Language"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>
<h2>
	<bean:message key="label.section"/>
	<fr:view name="section" property="name" />
</h2>

<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=editSection&sectionID=<bean:write name="section" property="idInternal"/></bean:define>
<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
	<bean:message key="button.editSection"/>
</html:link>

&nbsp;&nbsp;

<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=deleteSection&sectionID=<bean:write name="section" property="idInternal"/></bean:define>
<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
	<bean:message key="button.deleteSection"/>
</html:link>

&nbsp;&nbsp;

<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=createSection&sectionID=<bean:write name="section" property="idInternal"/></bean:define>
<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
	<bean:message key="button.insertSubSection"/>
</html:link>

&nbsp;&nbsp;

<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=createItem&sectionID=<bean:write name="section" property="idInternal"/></bean:define>
<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
	<bean:message key="button.insertItem"/>
</html:link>

<br/>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<logic:notEmpty name="section" property="associatedItems">
	<logic:iterate id="item" name="section" property="orderedItems" type="net.sourceforge.fenixedu.domain.Item">
		<h3>
			<bean:message key="label.item"/>:&nbsp;
			<fr:view name="item" property="name" />
		</h3>
		
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=editItem&itemID=<bean:write name="item" property="idInternal"/></bean:define>
		<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message key="button.editItem"/>
		</html:link>

		&nbsp;&nbsp;

		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=deleteItem&itemID=<bean:write name="item" property="idInternal"/></bean:define>
		<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message key="button.deleteItem"/>
		</html:link>

		&nbsp;&nbsp;

		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
		<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=uploadFile&itemID=<bean:write name="item" property="idInternal"/></bean:define>
		<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message key="button.insertFile"/>
		</html:link>

		<br/>
		<br/>

		<logic:present name="item" property="information">
			<fr:view name="item" property="information">
				<fr:layout>
					<fr:property name="escaped" value="false" />
					<fr:property name="newlineAware" value="false" />
				</fr:layout>
			</fr:view>
		</logic:present>
		<logic:notEmpty name="item" property="fileItems">
			<br/>
			<br/>
			<table>
				<logic:iterate id="fileItem" name="item" property="sortedFileItems">
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
						<td>
							<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
							<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=deleteFile&itemID=<bean:write name="item" property="idInternal"/>&amp;sectionID=<bean:write name="section" property="idInternal"/>&amp;fileItemId=<%= fileItemId %></bean:define>
							<bean:define id="delConfirmationMessageScript" type="java.lang.String">return confirm('Tem a certeza que deseja apagar o ficheiro <%= displayName %>?')</bean:define>
							<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal"
									onclick="<%= delConfirmationMessageScript %>"
									>
								<bean:message key="label.teacher.siteAdministration.viewSection.deleteItemFile"/>
							</html:link>
						</td>
						<td>
							&nbsp;&nbsp;<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
							<bean:define id="url" type="java.lang.String">/editItemFilePermissions.do?method=prepareEditItemFilePermissions&itemID=<bean:write name="item" property="idInternal"/>&amp;sectionID=<bean:write name="section" property="idInternal"/>&amp;fileItemId=<%= fileItemId %></bean:define>
							<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
								<bean:message key="label.teacher.siteAdministration.viewSection.editItemFilePermissions"/>
							</html:link>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
	</logic:iterate>
</logic:notEmpty>