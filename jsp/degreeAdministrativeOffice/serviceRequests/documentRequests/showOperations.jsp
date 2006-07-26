<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="DEGREE_ADMINISTRATIVE_OFFICE">

<h2><bean:message key="label.documentRequestsManagement.operations" /></h2>

<hr><br/>
<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>
<table>
	<tr>
		<td>
			<html:link action="/documentRequestsManagement.do?method=viewNewDocumentRequests">
					<bean:message key="label.documentRequestsManagement.newDocumentRequests" />
			</html:link>
		</td>
	</tr>
	<tr>
		<td>
			<html:link action="/documentRequestsManagement.do?method=prepareSearch">
					<bean:message key="label.documentRequestsManagement.searchDocumentRequests" />
			</html:link>
		</td>
	</tr>
</table>

</logic:present>
