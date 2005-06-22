<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>

<span class="error"><html:errors/></span>

<h2><bean:message key="label.masterDegree.insertApplicationDocuments"/></h2>
	<table>
		<html:form action="/insertApplicationDocumentsService.do" enctype="multipart/form-data">
			<html:hidden property="page" value="1" />
					<%
						if (request.getAttribute("uploadStatus")!=null)
						{
							Object[] ustatus = (Object[])request.getAttribute("uploadStatus");
							
							if (ustatus!=null)
							{
								pageContext.setAttribute("uploadCVStatus",ustatus[0]);
								pageContext.setAttribute("uploadMLStatus",ustatus[1]);
								pageContext.setAttribute("uploadHCStatus",ustatus[2]);					
								%> <%=ustatus[0]%> ; <%=ustatus[1]%> ; <%=ustatus[2]%> <%
							}
						}
					%>
					<logic:present name="uploadCVStatus">
						<bean:message key="label.masterDegree.curriculumVitaeUpload"/>
						<logic:equal name="uploadCVStatus" value="true">
							<font color="green"><bean:message key="label.masterDegree.upload.sucessfull"/></font>
						</logic:equal>
						<logic:notEqual name="uploadCVStatus" value="true">
							<font color="red"><bean:message key="label.masterDegree.upload.unsucessfull"/></font>
						</logic:notEqual>
					</logic:present>
					<logic:present name="uploadMLStatus">
						<bean:message key="label.masterDegree.manifestationLetterUpload"/>
						<logic:equal name="uploadMLStatus" value="true">
							<font color="green"><bean:message key="label.masterDegree.upload.sucessfull"/></font>
						</logic:equal>
						<logic:notEqual name="uploadMLStatus" value="true">
							<font color="red"><bean:message key="label.masterDegree.upload.unsucessfull"/></font>
						</logic:notEqual>
					</logic:present>
					<logic:present name="uploadHCStatus">
						<bean:message key="label.masterDegree.habilitationCertificateUpload"/>
						<logic:equal name="uploadHCStatus" value="true">
							<font color="green"><bean:message key="label.masterDegree.upload.sucessfull"/></font>
						</logic:equal>
						<logic:notEqual name="uploadHCStatus" value="true">
							<font color="red"><bean:message key="label.masterDegree.upload.unsucessfull"/></font>
						</logic:notEqual>
					</logic:present>
		    	<tr>
		    		<td colspan="2" width="50%"><b><bean:message key="label.masterDegree.documentsToSubmit"/></b></td>
		    		<br/>
		    	</tr>
		        <tr>
		          <td><bean:message key="label.masterDegree.curriculumVitaeDocument"/></td>
		          <td><html:file property="cvFile" size="50"/></td>
		        </tr>
		        <tr>
		          <td><bean:message key="label.masterDegree.manifestationLetterDocument"/></td>
		          <td><html:file property="cmiFile" size="50"/></td>
		        </tr>
		        <tr>
		          <td><bean:message key="label.masterDegree.habilitationCertificateDocument"/></td>
		          <td><html:file property="chFile" size="50"/></td>
		        </tr>
		        <tr>
		          <td><bean:message key="label.masterDegree.secondHabilitationCertificateDocument"/></td>
		          <td><html:file property="chFile2" size="50"/></td>
		        </tr>
		        <tr>
		        	<td>
						<html:submit styleClass="inputbutton"><bean:message key="label.masterDegree.applicationDocumentsSubmit"/></html:submit>
		        	</td>
		        </tr>
		</html:form>
	</table>