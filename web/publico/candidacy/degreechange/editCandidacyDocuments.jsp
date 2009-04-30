<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<!-- START MAIN PAGE CONTENTS HERE -->

<!--LANGUAGE SWITCHER -->
<div id="version">
	<img class="activeflag" src="Candidato%20%20Licenciatura%20_%20IST_files/icon_pt.gif" alt="Português">
	<a href="http://www.ist.utl.pt/en/htm/profile/pstudent/lic/"><img src="Candidato%20%20Licenciatura%20_%20IST_files/icon_en.gif" alt="English" border="0"></a>
</div>
<!--END LANGUAGE SWITCHER -->

<div class="breadcumbs">
	<a href="#">IST</a> &gt;
	<a href="#">Candidato</a> &gt;
	<a href="#">Candidaturas</a> &gt;
	<a href="<%= request.getContextPath() + "/publico/candidacies/caseHandlingDegreeChangeIndividualCandidacyProcess.do?method=candidacyIntro" %>">Licenciaturas</a> &gt;
	<a href="<%= request.getContextPath() + "/publico/candidacies/caseHandlingDegreeChangeIndividualCandidacyProcess.do?method=beginCandidacyProcessIntro" %>">Mudanças de Curso</a> &gt;
	Editar Documentos da Candidatura
</div>

<h1><bean:message key="label.candidacy" bundle="APPLICATION_RESOURCES"/>: <bean:message key="label.change.degree" bundle="APPLICATION_RESOURCES"/></h1>

<h2 style="margin-top: 1em;"><bean:message key="label.documentation" bundle="CANDIDATE_RESOURCES"/></h2>

<bean:define id="individualCandidacyProcess" name="candidacyDocumentUploadBean" property="individualCandidacyProcess"/>
<bean:define id="individualCandidacyProcessOID" name="individualCandidacyProcess" property="OID"/>
<fr:form action='/candidacies/caseHandlingDegreeChangeIndividualCandidacyProcess.do?method=editCandidacyDocuments' encoding="multipart/form-data">
	<fr:edit id="individualCandidacyProcessBean.documents"
		name="candidacyDocumentUploadBean" 
		schema="PublicCandidacyProcessBean.documentUpload.edit">
		<fr:destination name="cancel" path='<%= "/candidacies/caseHandlingDegreeChangeIndividualCandidacyProcess.do?method=backToViewCandidacy&individualCandidacyProcess=" +  individualCandidacyProcessOID %>' />
	</fr:edit>

	<p/>
	<html:submit><bean:message key="button.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>		
</fr:form>

<p/>
<table class="tstyle8">
	<tr>
		<th><bean:message key="label.candidacy.document.kind" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.dateTime.submission" bundle="CANDIDATE_RESOURCES"/></th>
		<th><bean:message key="label.document.file.name" bundle="CANDIDATE_RESOURCES"/></th>
	</tr>

	<logic:iterate id="documentFile" name="individualCandidacyProcess" property="candidacy.documents">
	<tr>
		<td><fr:view name="documentFile" property="candidacyFileType"/></td>
		<td><fr:view name="documentFile" property="uploadTime"/></td>
		<td><fr:view name="documentFile" property="filename"/></td>
	</tr>	
	</logic:iterate>
</table>

