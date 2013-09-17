<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />


<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<fr:form action='<%="/caseHandling" + processName + ".do?processId=" + processId.toString() %>' encoding="multipart/form-data" id="candidacyFormId">
	<input type="hidden" name="method" id="methodId" value="uploadDocument"/>
	<input type="hidden" name="documentFileOid" id="documentFileOidId" />
	
	<fr:edit id="individualCandidacyProcessBean.document"
		name="candidacyDocumentUploadBean" 
		schema="CandidacyProcessBean.documentUpload.edit">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thleft"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<span class="error0"><fr:messages for="individualCandidacyProcessBean.document" type="global"/></span>
	
	<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>		


<bean:define id="individualCandidacyProcess" name="process"/>
 	
<logic:empty name="individualCandidacyProcess" property="candidacy.documents">
	<p><em><bean:message key="message.documents.empty" bundle="CANDIDATE_RESOURCES"/>.</em></p>
</logic:empty>

<logic:notEmpty name="individualCandidacyProcess" property="candidacy.documents">
<table class="tstyle4 thlight thcenter">
	<tr>
		<th><bean:message key="label.candidacy.document.kind" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.dateTime.submission" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.document.file.name" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.document.file.active" bundle="CANDIDATE_RESOURCES"/></th>
		<th></th>
	</tr>

	
	<logic:iterate id="documentFile" name="individualCandidacyProcess" property="candidacy.documents">
	<bean:define id="documentOid" name="documentFile" property="externalId"/>
	<tr>
		<td><fr:view name="documentFile" property="candidacyFileType"/></td>
		<td><fr:view name="documentFile" property="uploadTime"/></td>
		<td><fr:view name="documentFile" property="filename"/></td>
		<td><fr:view name="documentFile" property="candidacyFileActive"/></td>
		<td><fr:view name="documentFile" layout="link"/></td>
		<td><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><html:link href="#" onclick='<%= "document.getElementById('methodId').value='revokeDocumentFile'; document.getElementById('documentFileOidId').value='" + documentOid + "'; document.getElementById('candidacyFormId').submit(); " %>'><bean:message key="label.document.file.revoke" bundle="CANDIDATE_RESOURCES"/></html:link></td>
	</tr>	
	</logic:iterate>
</table>
</logic:notEmpty>
 </fr:form>
 