<%@ page language="java" %>
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
	<a href="<%= fullPath + "?method=candidacyIntro" %>">Licenciaturas</a> &gt;
	<a href="<%= fullPath + "?method=beginCandidacyProcessIntro" %>"><bean:write name="candidacyName"/></a> &gt;
	Submeter Candidatura
</div>

<h1><bean:message key="label.candidacy" bundle="APPLICATION_RESOURCES"/>: <bean:write name="candidacyName"/></h1>
<p><bean:message key="label.all.fields.are.required" bundle="APPLICATION_RESOURCES"/></p>

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

<fr:form action='<%= mappingPath + ".do?method=submitCandidacy" %>' encoding="multipart/form-data" >
		<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	
 		<h2 style="margin-top: 1em;"><bean:message key="label.person.title.personal.info" bundle="CANDIDATE_RESOURCES"/></h2>

		<fr:edit id="candidacyProcess.personalDataBean"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcess.candidacyDataBean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= mappingPath + ".do?method=executeCreateCandidacyPersonalInformationInvalid" %>' />
			<fr:destination name="cancel" path='<%= mappingPath + ".do?method=beginCandidacyProcessIntro" %>' />
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
		<fr:edit id="individualCandidacyProcessBean.degreeDesignation"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.degreeDesignation.manage">
		  	<fr:layout name="flow">
				   <fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:edit>
		</div>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.selected.degree.candidacy" bundle="CANDIDATE_RESOURCES"/>:</p>
		<div class="flowerror">
		<fr:edit id="individualCandidacyProcessBean.selectedDegree"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.selectedDegree.manage">
			<fr:layout name="flow">
			  <fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:edit>
		</div>
		
		<h2 style="margin-top: 1em;"><bean:message key="label.documentation" bundle="CANDIDATE_RESOURCES"/></h2>
		
		<p style="margin-bottom: 0.5em;"><bean:message key="label.access.first.cycle.habilitation.document" bundle="CANDIDATE_RESOURCES"/>:</p>
		<fr:edit id="individualCandidacyProcessBean.documents"
			name="individualCandidacyProcessBean"
			property="firstCycleAccessHabilitationDocument"
			schema="PublicCandidacyProcessBean.documentUploadBean">
		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.habilitation.certificate.discriminated" bundle="CANDIDATE_RESOURCES"/>:</p>
		<fr:edit id="individualCandidacyProcessBean.habilitation.certificate" oid="1"
			name="individualCandidacyProcessBean"
			property="habilitationCertificationDocument"
			schema="PublicCandidacyProcessBean.documentUploadBean">
		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.identity.card.copy" bundle="CANDIDATE_RESOURCES"/>:</p>
		<fr:edit id="individualCandidacyProcessBean.identity.card.copy" oid="1"
			name="individualCandidacyProcessBean"
			property="documentIdentificationDocument"
			schema="PublicCandidacyProcessBean.documentUploadBean">
		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.vat.card.copy" bundle="CANDIDATE_RESOURCES"/>:</p>
		<fr:edit id="individualCandidacyProcessBean.vat.card.copy" oid="1"
			name="individualCandidacyProcessBean"
			property="vatCatCopyDocument"
			schema="PublicCandidacyProcessBean.documentUploadBean">
		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.payment.report" bundle="CANDIDATE_RESOURCES"/>:</p>
		<fr:edit id="individualCandidacyProcessBean.payment.report" oid="1"
			name="individualCandidacyProcessBean"
			property="paymentDocument"
			schema="PublicCandidacyProcessBean.documentUploadBean">
		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>

		<div class="mtop1 mbottom1">
			<label for="captcha"><bean:message key="label.captcha" bundle="ALUMNI_RESOURCES" />:</label>
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

		<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /> Candidatura</html:submit>
		<html:cancel><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
</fr:form>
