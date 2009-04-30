<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<!--LANGUAGE SWITCHER -->
<div id="version">
	<img class="activeflag" src="Candidato%20%20Licenciatura%20_%20IST_files/icon_pt.gif" alt="PortuguÃªs">
	<a href="http://www.ist.utl.pt/en/htm/profile/pstudent/lic/"><img src="Candidato%20%20Licenciatura%20_%20IST_files/icon_en.gif" alt="English" border="0"></a>
</div>
<!--END LANGUAGE SWITCHER -->

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="#">IST</a> &gt;
	<a href="#">Candidato</a> &gt;
	<a href="#">Candidaturas</a> &gt;
	<a href="<%= fullPath + "?method=candidacyIntro" %>">Licenciaturas</a> &gt;
	<a href="<%= fullPath + "?method=beginCandidacyProcessIntro" %>"><bean:write name="candidacyName"/></a> &gt;
	Visualizar processo de Candidatura
</div>

<h1><bean:message key="label.candidacy" bundle="APPLICATION_RESOURCES"/>: <bean:write name="candidacyName"/></h1>
<p><bean:message key="label.all.fields.are.required" bundle="APPLICATION_RESOURCES"/></p>

<h2 style="margin-top: 1em;"><bean:message key="label.person.title.personal.info" bundle="CANDIDATE_RESOURCES"/></h2>

<fr:view name="individualCandidacyProcessBean" 
	schema="PublicCandidacyProcess.candidacyDataBean">
	<fr:layout name="tabular">
		<fr:property name="classes" value="thlight thleft"/>
        <fr:property name="columnClasses" value=""/>
	</fr:layout>
</fr:view>
	
<h2 style="margin-top: 1em;"><bean:message key="label.habilitation" bundle="CANDIDATE_RESOURCES"/></h2>

<p><bean:message key="label.last.institution.enrolled" bundle="CANDIDATE_RESOURCES"/>:
<fr:view name="individualCandidacyProcessBean"
	schema="PublicCandidacyProcessBean.institutionUnitName.manage">
	<fr:layout name="flow">
		<fr:property name="labelExcluded" value="true"/>
	</fr:layout>
</fr:view></p>

<p><bean:message key="label.last.degree.name.enrolled" bundle="CANDIDATE_RESOURCES"/>:
<fr:view name="individualCandidacyProcessBean"
	schema="PublicCandidacyProcessBean.degreeDesignation.manage">
	<fr:layout name="flow">
		<fr:property name="labelExcluded" value="true"/>
	</fr:layout>
</fr:view></p>

<p><bean:message key="label.selected.degree.candidacy" bundle="CANDIDATE_RESOURCES"/>:
<fr:view name="individualCandidacyProcessBean"
	schema="PublicCandidacyProcessBean.selectedDegree.manage">
	<fr:layout name="flow">
		<fr:property name="labelExcluded" value="true"/>
        <fr:property name="columnClasses" value=""/>
	</fr:layout>
</fr:view></p>

<h2 style="margin-top: 1em;"><bean:message key="label.documentation" bundle="CANDIDATE_RESOURCES"/></h2>

<bean:define id="individualCandidacyProcess" name="individualCandidacyProcessBean" property="individualCandidacyProcess"/>
<logic:notEmpty name="individualCandidacyProcess" property="candidacy.documents">
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
</logic:notEmpty>

<logic:empty name="individualCandidacyProcess" property="candidacy.documents">
	<p><em>Não existem documentos.</em></p>
</logic:empty>

<p/>

<fr:form action='<%= mappingPath + ".do" %>' id="editCandidacyForm">
	<input type="hidden" name="method" id="methodForm"/>
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	<noscript>
	<html:submit onclick="this.form.method.value='prepareEditCandidacyProcess';"><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:submit onclick="this.form.method.value='prepareEditCandidacyDocuments';"><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /> Documentos</html:submit>
	<html:cancel><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</noscript>
	
	<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyProcess';document.getElementById('editCandidacyForm').submit();"><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /> Candidatura</a> | 
	<a href="#" onclick="javascript:document.getElementById('methodForm').value='prepareEditCandidacyDocuments';document.getElementById('editCandidacyForm').submit();"><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /> Documentos</a>
</fr:form>
