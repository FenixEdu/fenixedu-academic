<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<html:xhtml/>

<logic:present role="DEGREE_ADMINISTRATIVE_OFFICE">

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
	<table class="tstyle4 thlight thright"">
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
				<html:select property="isUrgent">
					<html:option value=""><bean:message key="dropDown.Default" bundle="ENUMERATION_RESOURCES"/></html:option>
					<html:option value="true"><bean:message key="label.documentRequestsManagement.searchDocumentRequests.urgent.yes"/></html:option>
					<html:option value="false"><bean:message key="label.documentRequestsManagement.searchDocumentRequests.urgent.no"/></html:option>
				</html:select>
				
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
	
	<html:submit styleClass="inputbutton"><bean:message key="label.documentRequestsManagement.search" /></html:submit>
	<html:submit onclick="this.form.method.value='showOperations';" styleClass="inputbutton"><bean:message key="label.documentRequestsManagement.back" /></html:submit>

</html:form>

</logic:present>
