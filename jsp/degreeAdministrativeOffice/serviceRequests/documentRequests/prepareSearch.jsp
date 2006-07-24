<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<html:xhtml/>

<h2><bean:message key="label.documentRequestsManagement.searchDocumentRequests" /></h2>

<hr><br/>

<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
</logic:messagesPresent>

<html:form action="/documentRequestsManagement.do">
	<html:hidden property="method" value="search" />
	<br/>
	<table>
		<tr>
			<td>
				<bean:message key="label.documentRequestsManagement.searchDocumentRequests.documentRequestType" />:
			</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType" bundle="ENUMERATION_RESOURCES" />
				<html:select property="documentRequestType">
					<html:option value=""><bean:message key="dropDown.Default" bundle="ENUMERATION_RESOURCES"/></html:option>
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.documentRequestsManagement.searchDocumentRequests.requestSituationType" />:
			</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType" bundle="ENUMERATION_RESOURCES"/>
				<html:select property="requestSituationType">
					<html:option value=""><bean:message key="dropDown.Default" bundle="ENUMERATION_RESOURCES"/></html:option>	
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.documentRequestsManagement.searchDocumentRequests.urgent" />:
			</td>
			<td>
				<html:checkbox property="isUrgent" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.documentRequestsManagement.searchDocumentRequests.studentNumber" />:
			</td>
			<td>
				<html:text property="studentNumber" />
			</td>
		</tr>
	
	</table>
	
	<html:submit><bean:message key="label.documentRequestsManagement.search" /></html:submit>
	<html:submit onclick="this.form.method.value='showOperations';" ><bean:message key="label.documentRequestsManagement.back" /></html:submit>

</html:form>