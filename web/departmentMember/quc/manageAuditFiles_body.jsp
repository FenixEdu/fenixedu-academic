<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<logic:present name="fileAdded">
	<span class="success0"><bean:message key="label.inquiry.audit.file.addSuccess" bundle="INQUIRIES_RESOURCES"/></span>
</logic:present>
<logic:present name="fileDeleted">
	<span class="success0"><bean:message key="label.inquiry.audit.file.deleteSuccess" bundle="INQUIRIES_RESOURCES"/></span>
</logic:present>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<p>
		<span class="error0"><bean:write name="message"/></span>
	</p>
</html:messages>
<fr:form action="/qucAudit.do" encoding="multipart/form-data">
	<html:hidden property="method" value="uploadFile"/>
	<fr:edit id="executionCourseAudit" name="executionCourseAudit" visible="false"/>
	<fr:edit id="fileBean" name="fileBean">
		<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.research.result.OpenFileBean">
			<fr:slot name="inputStream" key="label.file">
				<fr:property name="fileNameSlot" value="fileName"/>
			</fr:slot>		
		</fr:schema>
		<fr:layout name="tabular">
		    <fr:property name="classes" value="tstyle1 thlight mtop05 thleft"/>
		    <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="button.submit"/></html:submit>
	<html:submit onclick="this.form.method.value='viewProcessDetails'"><bean:message key="button.cancel"/></html:submit>
</fr:form>

<logic:notEmpty name="executionCourseAudit" property="executionCourseAuditFiles">
	<table class="tstyle1">
		<tr>
			<th><bean:message key="label.archive.options.files" bundle="APPLICATION_RESOURCES"/></th>
			<th></th>
		</tr>
		<logic:iterate id="auditFile" name="executionCourseAudit" property="executionCourseAuditFiles">
			<tr>
				<td><fr:view name="auditFile" layout="link"/></td>
				<td>
					<html:link page="/qucAudit.do?method=deleteFile" paramId="executionCourseAuditFileOID" paramName="auditFile" paramProperty="externalId">
						<bean:message key="link.delete" bundle="APPLICATION_RESOURCES"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>