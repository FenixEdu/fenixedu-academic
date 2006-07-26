<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.documentRequestsManagement.newDocumentRequests" /></h2>

<hr><br/>
<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<logic:notEmpty name="academicServiceRequestList">

	<html:form action="/documentRequestsManagement.do">
		<html:hidden property="method"/>
		
		<fr:view name="academicServiceRequestList" schema="DocumentRequest.view-without-numberOfPages-academicServiceRequestSituationType">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thcenter" />
				<fr:property name="checkable" value="true" />
				<fr:property name="checkboxName" value="documentIdsToProcess" />
				<fr:property name="checkboxValue" value="idInternal" />		
			</fr:layout>
		</fr:view>
		<html:submit onclick="this.form.method.value='processNewDocuments';" styleClass="inputbutton"><bean:message key="label.documentRequestsManagement.process" /></html:submit>
		<html:submit onclick="this.form.method.value='showOperations';" styleClass="inputbutton"><bean:message key="label.documentRequestsManagement.back" /></html:submit>
	</html:form>
</logic:notEmpty>

<logic:empty name="academicServiceRequestList">
	<br/>
	<span class="error0"><bean:message key="label.documentRequestsManagement.noNewDocumentRequest"/></span>
</logic:empty>