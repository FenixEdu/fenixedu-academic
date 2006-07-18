<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<p class="infoop"><span><h2 class="inline"><bean:message key="label.candidacy.title.detail" /></h2></span></p>
<fr:view name="candidacy" schema="candidacy.short" >
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>

<p class="infoop"><span><h2 class="inline"><bean:message key="label.candidacy.title.activeSituation" /></h2></span></p>
<fr:view name="candidacy" property="activeCandidacySituation" schema="candidacySituation.full" >
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>

<fr:form action="/viewCandidacies.do?method=uploadDocuments" encoding="multipart/form-data">
	<bean:define id="downloadUrlPrefix" name="fileDownloadUrlFormat" />
	<p class="infoop"><span><h2 class="inline"><bean:message key="label.candidacy.title.documents" /></h2></span></p>
	<fr:edit id="candidacyDocuments" name="candidacyDocuments" schema="candidacyDocuments.full" >
		<fr:layout name="tabular-editable" >
<%--			<fr:property name="linkFormat(download)" value="<%= downloadUrlPrefix + "/${candidacyDocument.file.externalStorageIdentification}/${candidacyDocument.file.filename}"%>"/>
			<fr:property name="key(download)" value="link.common.download"/>
			<fr:property name="bundle(download)" value="APPLICATION_RESOURCES"/>
			<fr:property name="visibleIf(download)" value="isFileUploaded"/>
			<fr:property name="contextRelative(download)" value="false"/>
--%>
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="button.submit" /></html:submit>
</fr:form>

	