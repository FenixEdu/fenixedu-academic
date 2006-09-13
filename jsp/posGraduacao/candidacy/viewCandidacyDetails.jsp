<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><strong><bean:message key="label.candidate.data" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="candidacy" schema="candidacy.show.candidady">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<h2><strong><bean:message key="label.candidacy.title.detail" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<logic:empty name="candidacy" property="registration">
	<fr:view name="candidacy" schema="candidacy.short" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:view>
</logic:empty>
<logic:notEmpty name="candidacy" property="registration">
	<fr:view name="candidacy" schema="candidacy.short.withNumber" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<h2><strong><bean:message key="label.candidacy.title.activeSituation" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="candidacy" property="activeCandidacySituation" schema="candidacySituation.full" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<bean:define id="downloadUrlPrefix" name="fileDownloadUrlFormat" />
<h2><strong><bean:message key="label.candidacy.title.documents" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="candidacyDocuments" schema="candidacyDocuments.view" >
	<fr:layout name="tabular" >
		<fr:property name="linkFormat(download)" value="<%= downloadUrlPrefix + "/${candidacyDocument.file.externalStorageIdentification}/${candidacyDocument.file.filename}"%>"/>
		<fr:property name="key(download)" value="link.common.download"/>
		<fr:property name="bundle(download)" value="APPLICATION_RESOURCES"/>
		<fr:property name="visibleIf(download)" value="isFileUploaded"/>
		<fr:property name="contextRelative(download)" value="false"/>
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>			
	</fr:layout>
</fr:view>

<bean:define id="candidacy_number" name="candidacy" property="number"/>
<h2><strong><bean:message key="label.candidacy.title.operations" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<table class="tstyle4">
	<logic:equal name="candidacy" property="activeCandidacySituation.canRegister" value="true">
		<tr>
			<td class="listClasses">
				<html:link action="<%= "/dfaCandidacy.do?method=prepareRegisterCandidacy&candidacyNumber=" + candidacy_number.toString() %>">
					<bean:message key="link.candidacy.registerCandidacy" bundle="ADMIN_OFFICE_RESOURCES"/>				
				</html:link>					
			</td>
		</tr>
	</logic:equal>
	<tr>
		<td class="listClasses">
			<html:link action="<%= "/dfaCandidacy.do?method=showCandidacyValidateData&candidacyNumber=" + candidacy_number.toString() %>">
				<bean:message key="link.candidacy.viewValidateCandidateData" bundle="ADMIN_OFFICE_RESOURCES"/>				
			</html:link>					
		</td>
	</tr>
	<tr>
		<td class="listClasses">
			<html:link action="<%= "/dfaCandidacy.do?method=prepareAlterCandidacyData&candidacyNumber=" + candidacy_number.toString() %>">
				<bean:message key="link.candidacy.alterCandidateData" bundle="ADMIN_OFFICE_RESOURCES"/>				
			</html:link>					
		</td>	
	</tr>			
	<logic:equal name="candidacy" property="activeCandidacySituation.canGeneratePass" value="true">
		<tr>
			<td class="listClasses">
				<html:link target="_blank" action="<%= "/dfaCandidacy.do?method=generatePass&candidacyNumber=" + candidacy_number.toString() %>">
					<bean:message key="link.candidacy.generate.password" bundle="ADMIN_OFFICE_RESOURCES"/>
				</html:link>
			</td>
		</tr>
	</logic:equal>
</table>
