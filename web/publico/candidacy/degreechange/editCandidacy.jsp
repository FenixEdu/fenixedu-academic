<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<!--LANGUAGE SWITCHER -->
<div id="version">
	<img class="activeflag" src="Candidato%20%20Licenciatura%20_%20IST_files/icon_pt.gif" alt="Português">
	<a href="http://www.ist.utl.pt/en/htm/profile/pstudent/lic/"><img src="Candidato%20%20Licenciatura%20_%20IST_files/icon_en.gif" alt="English" border="0"></a>
</div>
<!--END LANGUAGE SWITCHER -->

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="#">IST</a> &gt;
	<a href="#">Candidato</a> &gt;
	<a href="#">Candidaturas</a> &gt;
	<a href='<%= fullPath + "?method=candidacyIntro" %>'>Licenciaturas</a> &gt;
	<a href='<%= fullPath + "?method=beginCandidacyProcessIntro" %>'><bean:write name="candidacyName"/> </a> &gt;
	Editar Candidatura
</div>

<h1><bean:message key="label.candidacy" bundle="APPLICATION_RESOURCES"/>: <bean:message key="label.change.degree" bundle="APPLICATION_RESOURCES"/></h1>
<p><span><bean:message key="label.all.fields.are.required" bundle="APPLICATION_RESOURCES"/></span>.</p>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="captcha.error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="individualCandidacyProcess" name="individualCandidacyProcessBean" property="individualCandidacyProcess"/>
<bean:define id="individualCandidacyProcessOID" name="individualCandidacyProcess" property="OID"/>
<fr:form action='/candidacies/caseHandlingDegreeChangeIndividualCandidacyProcess.do?method=editCandidacyProcess' encoding="multipart/form-data">
		<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	
		<h2 style="margin-top: 1em;"><bean:message key="label.person.title.personal.info" bundle="CANDIDATE_RESOURCES"/></h2>

		<fr:edit id="candidacyProcess.personalDataBean"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcess.candidacyDataBean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='/candidacies/caseHandlingDegreeChangeIndividualCandidacyProcess.do?method=executeEditCandidacyPersonalInformationInvalid' />
			<fr:destination name="cancel" path='<%= "/candidacies/caseHandlingDegreeChangeIndividualCandidacyProcess.do?method=backToViewCandidacy&individualCandidacyProcess=" +  individualCandidacyProcessOID %>' />
		</fr:edit>
		
		<h2 style="margin-top: 1em;"><bean:message key="label.habilitation" bundle="CANDIDATE_RESOURCES"/></h2>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.last.institution.enrolled" bundle="CANDIDATE_RESOURCES"/>:</p>
		<div class="flowerror">
		<fr:edit id="individualCandidacyProcessBean.institutionUnitName"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.institutionUnitName.manage">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:edit>
		</div>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.last.degree.name.enrolled" bundle="CANDIDATE_RESOURCES"/>:</p>
		<div class="flowerror">
		<fr:edit id="individualCandidacyProcessBean.degreeDesignation" oid="1"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.degreeDesignation.manage">
	  		<fr:layout name="flow">
		   		<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:edit>
		</div>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.selected.degree.candidacy" bundle="CANDIDATE_RESOURCES"/>:</p>
		<div class="flowerror">
		<fr:edit id="individualCandidacyProcessBean.selectedDegree" oid="1"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.selectedDegree.manage">
			<fr:layout name="flow">
		  		<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:edit>

		<p/>

		<div class="mtop1 mbottom1">
			<label for="captcha"><bean:message key="label.captcha" bundle="ALUMNI_RESOURCES" />:</label></p>
			<div class="mbottom05">
				<img src="<%= request.getContextPath() + "/publico/jcaptcha.do" %>" style="border: 1px solid #bbb; padding: 5px;"/>
			</div>
			<span class="color777"><bean:message key="label.captcha.process" bundle="ALUMNI_RESOURCES" /></span><br/>
			<input type="text" name="j_captcha_response" size="30" style="margin-bottom: 1em;"/>
			
			<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="captcha.error">
				<span class="error0"><bean:write name="message"/></span>
			</html:messages>
			
			<br/>
			
			<logic:present name="captcha.unknown.error">
				<p style="margin: 0;">
					<span class="error0">
						<bean:message key="captcha.unknown.error" bundle="ALUMNI_RESOURCES" />
					</span>
				</p>
			</logic:present>
		</div>
	
	<html:submit><bean:message key="button.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>
</fr:form>
