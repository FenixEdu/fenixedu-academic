<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<p>
	<html:link page="/qucAudit.do?method=showAuditProcesses">
		<bean:message key="label.return" bundle="APPLICATION_RESOURCES"/>
	</html:link>
</p>

<logic:present name="fileAdded">
	<span class="success0"><bean:message key="label.inquiry.audit.file.addSuccess" bundle="INQUIRIES_RESOURCES"/></span>
</logic:present>
<logic:present name="fileDeleted">
	<span class="success0"><bean:message key="label.inquiry.audit.file.deleteSuccess" bundle="INQUIRIES_RESOURCES"/></span>
</logic:present>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>
<fr:form action="/qucAudit.do" encoding="multipart/form-data">
	<html:hidden property="method" value="uploadFile"/>
	<fr:edit id="executionCourseAudit" name="executionCourseAudit" visible="false"/>
	<fr:edit id="fileBean" name="fileBean">
		<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.commons.OpenFileBean">
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