<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="label.uploadFiles"/></h2>
<span class="error"><html:errors/></span>

<bean:define id="fileToUpload" name="file"/>
<p>
<b>
	<bean:message key="label.uploadFiles.uploading" />
	<logic:equal name="fileToUpload" value="sibs">
		<bean:message key="label.uploadFiles.SIBS" />
	</logic:equal>

</b>
<p>
<html:form action="/uploadFiles.do?method=uploadGratuityFile" enctype="multipart/form-data">  
	<html:hidden property="page" value="1" />
		
	<html:hidden property="file" value="<%=fileToUpload.toString() %>" />
	<table>
		<tr>
			<td>
				<bean:message key="label.file"/>
			</td>
			<td>
				<html:file property="uploadedFile" size="30"/>
			</td>
		</tr>	
	</table>
	<p />
	<html:submit styleClass="inputbutton">
		<bean:message key="button.uploadFiles.upload"/>
	</html:submit>
</html:form>