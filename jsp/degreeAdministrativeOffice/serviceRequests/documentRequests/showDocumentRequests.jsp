<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<html:xhtml/>

<logic:present role="DEGREE_ADMINISTRATIVE_OFFICE">

<h2><bean:message key="label.documentRequestsManagement.showDocumentRequests" /></h2>

<hr><br/>

<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
</logic:messagesPresent>

<logic:notEmpty name="documentRequestsResult">

	<bean:define id="url" name="url"/>

	<fr:view name="documentRequestsResult" schema="DocumentRequest.view-without-numberOfPages-documentPurposeTypeInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright" />
			
			<fr:property name="linkFormat(view)" value="<%="/documentRequestsManagement.do?method=viewDocumentRequest&documentRequestId=${idInternal}" + url %>"/>
			<fr:property name="key(view)" value="label.documentRequestsManagement.viewRequest"/>

			<fr:property name="linkFormat(edit)" value="<%="/documentRequestsManagement.do?method=prepareEditDocumentRequest&documentRequestId=${idInternal}" + url %>"/>
			<fr:property name="key(edit)" value="label.documentRequestsManagement.editRequest"/>
			<fr:property name="visibleIf(edit)" value="editable"/>
			
			<fr:property name="sortBy" value="urgentRequest=desc,creationDate=asc"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="documentRequestsResult">
	<bean:message key="label.documentRequestsManagement.noDocumentRequests" />
	<br/>
	<br/>
</logic:empty>

<html:form action="/documentRequestsManagement.do?method=prepareSearch">
	<html:submit styleClass="inputbutton"><bean:message key="label.documentRequestsManagement.back" /></html:submit>
</html:form>

</logic:present>
