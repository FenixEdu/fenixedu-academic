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

<bean:define id="candidacyID" name="candidacy" property="idInternal" />
<fr:form action="<%="/viewCandidacies.do?candidacyID=" +candidacyID %>" encoding="multipart/form-data">
	<p class="infoop"><span><h2 class="inline"><bean:message key="label.candidacy.title.documents" /></h2></span></p>
	<fr:edit id="candidacyDocuments" name="candidacyDocuments" schema="candidacyDocuments.full">
		<fr:layout name="tabular-editable" >
<--			<fr:property name="linkFormat(download)" value="${candidacyDocument.file.downloadUrl}"/>
			<fr:property name="key(download)" value="link.common.download"/>
			<fr:property name="bundle(download)" value="APPLICATION_RESOURCES"/>
			<fr:property name="visibleIf(download)" value="isFileUploaded"/>
			<fr:property name="contextRelative(download)" value="false"/>
--%>
		</fr:layout>
	</fr:edit>
	
	<html:hidden name="candidacyForm" property="method" value=""/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='uploadDocuments'"><bean:message key="button.submit"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='viewDetail'"><bean:message key="button.back"/></html:cancel>
</fr:form>

	