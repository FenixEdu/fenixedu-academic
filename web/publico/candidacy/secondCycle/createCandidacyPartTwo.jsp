<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@ page import="java.util.Locale"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<a href='<%= fullPath + "?method=beginCandidacyProcessIntro" %>'><bean:write name="application.name"/> </a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>

<p class="steps">
	<span><bean:message key="label.step.one.personal.details" bundle="CANDIDATE_RESOURCES"/></span> &gt; 
	<span class="actual"><bean:message key="label.step.two.habilitations.document.files" bundle="CANDIDATE_RESOURCES"/></span>
</p>

<p class="mtop15"><span><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></span></p>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="captcha.error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES" property="error">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<fr:form id="secondCycleCandidacyForm" action='<%= mappingPath + ".do?userAction=createCandidacy" %>' encoding="multipart/form-data">
		<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
		
		<input type="hidden" id="methodId" name="method" value="submitCandidacy"/>
		<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
		<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
		
		
		
		
		
		<h2 class="mtop1"><bean:message key="title.educational.background" bundle="CANDIDATE_RESOURCES"/></h2>

		<% 
			Locale locale = Language.getLocale();
			if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
		%>
		
		<p class="mbottom05"><bean:message key="label.ist.number.if.former.ist.student" bundle="CANDIDATE_RESOURCES"/>:</p>
		<div class="flowerror">
			<fr:edit id="individualCandidacyProcessBean.formerStudentIstNumber"
				name="individualCandidacyProcessBean"
				schema="PublicCandidacyProcessBean.second.cycle.former.student.ist.number">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:edit>			
		</div>

		<% 
			}		
		%>
		
		<p><strong><bean:message key="title.bachelor.degree.owned" bundle="CANDIDATE_RESOURCES"/></strong></p>
		<p style="margin-bottom: 0.5em;"><bean:message key="label.university.attended.previously" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
		<div class="flowerror">
		<fr:edit id="individualCandidacyProcessBean.institutionUnitName"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.institutionUnitName.manage">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:edit>
		</div>
		
		<p style="margin-bottom: 0.5em;"><bean:message key="label.university.previously.attended.country" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
		<div class="flowerror">
		<fr:edit id="individualCandidacyProcessBean.country"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.institution.country.manage">
		  	<fr:layout name="flow">
				   <fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:edit>			
		</div>
		
		<p style="margin-bottom: 0.5em;"><bean:message key="label.bachelor.degree.previously.enrolled" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
		<div class="flowerror">
		<fr:edit id="individualCandidacyProcessBean.degreeDesignation"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.degreeDesignation.manage">
		  	<fr:layout name="flow">
				   <fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:edit>
		</div>
	
		<p style="margin-bottom: 0.5em;"><bean:message key="label.bachelor.degree.conclusion.date" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
		<div class="flowerror">
		<fr:edit id="individualCandidacyProcessBean.conclusionDate"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.second.cycle.conclusionDate">
		  	<fr:layout name="flow">
				   <fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:edit>
		</div>
		
		<p style="margin-bottom: 0.5em;"><bean:message key="label.bachelor.degree.conclusion.grade" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
		<div class="flowerror">
		<fr:edit id="individualCandidacyProcessBean.conclusionGrade"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.second.cycle.conclusionGrade">
		  	<fr:layout name="flow">
				   <fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:edit>
		</div>
		
		<% 
			if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
		%>
		
		<p class="mtop15 mbottom05"><strong><bean:message key="title.other.academic.titles" bundle="CANDIDATE_RESOURCES"/></strong></p>
		<logic:iterate id="academicTitle" name="individualCandidacyProcessBean" property="formationConcludedBeanList" indexId="index">
			<bean:define id="academicTitleId" name="academicTitle" property="id"/>
			<bean:define id="designationId"><%= "individualCandidacyProcessBean.habilitation.concluded.designation:" + academicTitleId %></bean:define>
			<bean:define id="institutionNameId"><%= "individualCandidacyProcessBean.habilitation.concluded.institutionName:" + academicTitleId %></bean:define>
			<bean:define id="beginYearId"><%= "individualCandidacyProcessBean.habilitation.concluded.begin.year:" + academicTitleId %></bean:define>
			<bean:define id="endYearId"><%= "individualCandidacyProcessBean.habilitation.concluded.end.year:" + academicTitleId %></bean:define>
			<bean:define id="conclusionGradeId"><%= "individualCandidacyProcessBean.habilitation.concluded.conclusion.grade:" + academicTitleId %></bean:define>
			
			<table class="tstyle5 thlight thleft mtop0 mbottom0">
				<tr>
					<th><bean:message key="label.other.academic.titles.program.name" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<div class="flowerror_hide">
							<fr:edit 	id='<%= designationId %>' 
										name="academicTitle"
										schema="PublicCandidacyProcessBean.formation.designation">
								<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
							</fr:edit>
						</div>
					</td>
					<td rowspan="4">
						<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeConcludedHabilitationsEntry\"; document.getElementById(\"secondCycleCandidacyForm\").submit();" %>' href="#" ><bean:message key="label.remove" bundle="CANDIDATE_RESOURCES"/></a></p>
					</td>
					<td class="tdclear">
						<span class="error0"><fr:message for="<%= designationId %>"/></span>
					</td>
				</tr>
				<tr>
					<th><bean:message key="label.other.academic.titles.institution" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<div class="flowerror_hide">
							<fr:edit 	id='<%= institutionNameId %>' 
								name="academicTitle"
								schema="PublicCandidacyProcessBean.formation.institutionUnitName">
								<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
							</fr:edit>
						</div>
					</td>
					<td class="tdclear">
						<span class="error0"><fr:message for="<%= institutionNameId %>"/></span>
					</td>					
				</tr>
				<tr>
					<th><bean:message key="label.other.academic.titles.conclusion.date" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<div class="flowerror_hide">
							<fr:edit 	id='<%= endYearId %>'
										name="academicTitle"
										schema="PublicCandidacyProcessBean.formation.conclusion.date">
								<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
							</fr:edit>
							dd/mm/aaaa
						</div>
					</td>
					<td class="tdclear">
						<span class="error0"><fr:message for="<%= endYearId %>"/></span>					
					</td>					
				</tr>
				<tr>
					<th><bean:message key="label.other.academic.titles.conclusion.grade" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<div class="flowerror_hide">
							<fr:edit 	id='<%= conclusionGradeId %>'
										name="academicTitle"
										schema="PublicCandidacyProcessBean.formation.conclusion.grade">
								<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
							</fr:edit>
						</div>
					</td>
					<td class="tdclear">
						<span class="error0"><fr:message for="<%= conclusionGradeId %>"/></span>					
					</td>					
				</tr>
			</table>
		</logic:iterate>
		<p class="mtop05 mbottom2"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addConcludedHabilitationsEntry'; document.getElementById('secondCycleCandidacyForm').submit();" href="#">+ <bean:message key="label.add" bundle="CANDIDATE_RESOURCES"/></a></p>

		<% 
			}		
		%>






		<h2 style="margin-top: 1em;"><bean:message key="title.master.second.cycle.course.choice" bundle="CANDIDATE_RESOURCES"/></h2>
			<div class="flowerror mtop1">
			<fr:edit id="individualCandidacyProcessBean.selectedDegree"
				name="individualCandidacyProcessBean"
				schema="PublicCandidacyProcessBean.second.cycle.selectedDegree.manage">
				<fr:layout name="flow">
				  <fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:edit>
			<span class="red">*</span>
		</div>


		<h2 style="margin-top: 1.5em;"><bean:message key="label.documentation" bundle="CANDIDATE_RESOURCES"/></h2>
		<p><em><bean:message key="message.max.file.size" bundle="CANDIDATE_RESOURCES"/></em></p>
		
		<p style="margin-bottom: 0.5em;"><bean:message key="label.curriculum.vitae.academic.professional" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
		<fr:edit id="individualCandidacyProcessBean.document.file.curriculum.vitae"
			name="individualCandidacyProcessBean"
			property="curriculumVitaeDocument"
			schema="PublicCandidacyProcessBean.documentUploadBean">
		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.legalized.transcript" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
		<logic:iterate id="document" name="individualCandidacyProcessBean" property="habilitationCertificateList" indexId="index">
			<bean:define id="documentId" name="document" property="id"/>
			<div class="mbottom05">
			<fr:edit id="<%= "individualCandidacyProcessBean.document.file.qualification.certificate:" + index %>" 
				name="document"
				schema="PublicCandidacyProcessBean.documentUploadBean">
			  <fr:layout name="flow">
			    <fr:property name="labelExcluded" value="true"/>
			  </fr:layout>
			</fr:edit>
			<% if(index > 0) { %>
			<a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeHabilitationCertificateDocumentFileEntry\"; document.getElementById(\"secondCycleCandidacyForm\").submit();" %>' href="#" ><bean:message key="label.remove" bundle="CANDIDATE_RESOURCES"/> </a>
			<% } %>

			</div>	
		</logic:iterate>
		<p class="mtop05"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addHabilitationCertificateDocumentFileEntry'; document.getElementById('secondCycleCandidacyForm').submit();" href="#">+ <bean:message key="label.add" bundle="CANDIDATE_RESOURCES"/></a></p>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.extra.documents.recomendations.portfolios" bundle="CANDIDATE_RESOURCES"/>:</p>
		<logic:iterate id="document" name="individualCandidacyProcessBean" property="reportOrWorkDocumentList" indexId="index">
			<div class="mbottom05">
			<bean:define id="documentId" name="document" property="id"/>
			<fr:edit id="<%= "individualCandidacyProcessBean.document.file.reports.works:" + index.toString() %>" 
				name="document"
				schema="PublicCandidacyProcessBean.documentUploadBean">
			  <fr:layout name="flow">
			    <fr:property name="labelExcluded" value="true"/>
			  </fr:layout>
			</fr:edit>
			<% if(index > 0) { %>
			<a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeReportsOrWorksDocumentFileEntry\"; document.getElementById(\"secondCycleCandidacyForm\").submit();" %>' href="#" ><bean:message key="label.remove" bundle="CANDIDATE_RESOURCES"/>:</a>
			<% } %>
			</div>
		</logic:iterate>
		<p class="mtop05"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addReportsOrWorksDocumentFileEntry'; document.getElementById('secondCycleCandidacyForm').submit();" href="#">+ <bean:message key="label.add" bundle="CANDIDATE_RESOURCES"/></a></p>
		
		<p style="margin-bottom: 0.5em;"><bean:message key="label.copy.document.id" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
		<fr:edit id="individualCandidacyProcessBean.document.file.identity.card.copy"
			name="individualCandidacyProcessBean" 
			property="documentIdentificationDocument"
			schema="PublicCandidacyProcessBean.documentUploadBean">
		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.vat.card.copy" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
		<fr:edit id="individualCandidacyProcessBean.document.file.vat.card.copy"
			name="individualCandidacyProcessBean"
			property="vatCatCopyDocument" 
			schema="PublicCandidacyProcessBean.documentUploadBean">
		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.copy.back.transfer.with.payment" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></p>
		<fr:edit id="individualCandidacyProcessBean.document.file.payment.report"
			name="individualCandidacyProcessBean"
			property="paymentDocument" 
			schema="PublicCandidacyProcessBean.documentUploadBean">
		  <fr:layout name="flow">
		    <fr:property name="labelExcluded" value="true"/>
		  </fr:layout>
		</fr:edit>

		<p style="margin-bottom: 0.5em;"><bean:message key="label.observations" bundle="CANDIDATE_RESOURCES"/>:</p>
		<fr:edit id="individualCandidacyProcessBean.observations"
			name="individualCandidacyProcessBean"
			schema="PublicCandidacyProcessBean.observations">
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
		</div>

		<h3><bean:message key="label.payment.title" bundle="CANDIDATE_RESOURCES"/></h3>
		<p><bean:message key="message.payment.details" bundle="CANDIDATE_RESOURCES"/></p> 
		
		<p><em><bean:message key="message.ist.conditions.note" bundle="CANDIDATE_RESOURCES"/></em></p>

		<div class="mtop15"><bean:message key="message.nape.contacts" bundle="CANDIDATE_RESOURCES"/></div>

		<p class="mtop2">
			<html:submit onclick="document.getElementById('skipValidationId').value='false'; document.getElementById('methodId').value='submitCandidacy'; this.form.submit();"><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /> <bean:message key="label.application" bundle="CANDIDATE_RESOURCES"/></html:submit>
			<html:submit onclick="document.getElementById('skipValidationId').value='false'; document.getElementById('methodId').value='backCandidacyCreation'; this.form.submit();"><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:submit>
		</p>
</fr:form>

