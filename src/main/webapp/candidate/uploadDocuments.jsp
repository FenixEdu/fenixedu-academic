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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<p class="infoop"><span><h2 class="inline"><bean:message key="label.candidacy.title.detail" /></h2></span></p>
<fr:view name="candidacy" schema="candidacy.short" >
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>

<bean:define id="candidacyID" name="candidacy" property="externalId" />
<fr:form action="<%="/viewCandidacies.do?candidacyID=" +candidacyID %>" encoding="multipart/form-data">
	<p class="infoop"><span><h2 class="inline"><bean:message key="label.candidacy.title.documents" /></h2></span></p>
	<fr:edit id="candidacyDocuments" name="candidacyDocuments" schema="candidacyDocuments.full">
		<fr:layout name="tabular-editable" >
			<fr:property name="linkFormat(download)" value="${candidacyDocument.file.downloadUrl}"/>
			<fr:property name="key(download)" value="link.common.download"/>
			<fr:property name="bundle(download)" value="APPLICATION_RESOURCES"/>
			<fr:property name="visibleIf(download)" value="isFileUploaded"/>
			<fr:property name="contextRelative(download)" value="false"/>
		</fr:layout>
	</fr:edit>
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="candidacyForm" property="method" value=""/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='uploadDocuments'"><bean:message key="button.submit"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='viewDetail'"><bean:message key="button.back"/></html:cancel>
</fr:form>

	