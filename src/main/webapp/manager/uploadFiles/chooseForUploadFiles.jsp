<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<h2><bean:message bundle="MANAGER_RESOURCES" key="label.uploadFiles"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<bean:define id="fileToUpload" name="file"/>
<p>
<b>
	<bean:message bundle="MANAGER_RESOURCES" key="label.uploadFiles.uploading" />
	<logic:equal name="fileToUpload" value="sibs">
		<bean:message bundle="MANAGER_RESOURCES" key="label.uploadFiles.SIBS" />
	</logic:equal>

</b>
<p>
<html:form action="/uploadFiles.do?method=uploadGratuityFile" enctype="multipart/form-data">  
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
		
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.file" property="file" value="<%=fileToUpload.toString() %>" />
	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.file"/>
			</td>
			<td>
				<html:file bundle="HTMLALT_RESOURCES" altKey="file.uploadedFile" property="uploadedFile" size="30"/>
			</td>
		</tr>	
	</table>
	<p />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.uploadFiles.upload"/>
	</html:submit>
</html:form>