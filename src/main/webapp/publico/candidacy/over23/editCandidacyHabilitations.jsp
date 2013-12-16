<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<a href='<%= fullPath + "?method=beginCandidacyProcessIntro" %>'><bean:write name="application.name"/> </a> &gt;
	<bean:message key="title.edit.application.qualifications" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>

<bean:define id="individualCandidacyProcess" name="individualCandidacyProcessBean" property="individualCandidacyProcess"/>
<bean:define id="individualCandidacyProcessOID" name="individualCandidacyProcess" property="OID"/>

<p><a href='<%= fullPath + "?method=backToViewCandidacy&individualCandidacyProcess=" + individualCandidacyProcessOID %>'>« <bean:message key="button.back" bundle="CANDIDATE_RESOURCES"/></a></p>


<p><span><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></span></p>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES">
	<p><span class="error0"> <bean:write name="message"/></span></p>
</html:messages>

<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>


<fr:form action='<%= mappingPath + ".do?userAction=editCandidacyQualifications" %>' encoding="multipart/form-data" id="over23CandidacyForm">
		<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
		
		<input type="hidden" id="methodId" name="method" value="submitCandidacy"/>
		<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
		<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
		
		<h2 style="margin-top: 1em;"><bean:message key="title.over23.qualifications" bundle="CANDIDATE_RESOURCES"/></h2>
		
		<h3><bean:message key="label.over23.qualifications.concluded" bundle="CANDIDATE_RESOURCES"/></h3>
		<logic:iterate id="qualification" name="individualCandidacyProcessBean" property="formationConcludedBeanList" indexId="index">
			<bean:define id="qualificationId" name="qualification" property="id"/>
			<bean:define id="designationId"><%= "individualCandidacyProcessBean.habilitation.concluded.designation:" + qualificationId %></bean:define>
			<bean:define id="institutionNameId"><%= "individualCandidacyProcessBean.habilitation.concluded.institutionName:" + qualificationId %></bean:define>
			<bean:define id="beginYearId"><%= "individualCandidacyProcessBean.habilitation.concluded.begin.year:" + qualificationId %></bean:define>
			<bean:define id="endYearId"><%= "individualCandidacyProcessBean.habilitation.concluded.end.year:" + qualificationId %></bean:define>
			<bean:define id="designationErrorId"><%=  "error.individualCandidacyProcessBean.formation.designation:" + qualificationId %></bean:define>
			<bean:define id="institutionErrorId"><%=  "error.individualCandidacyProcessBean.formation.institution:" + qualificationId %></bean:define>
			<bean:define id="durationErrorId"><%=  "error.individualCandidacyProcessBean.formation.duration:" + qualificationId %></bean:define>
			<table class="tstyle5 thlight thleft mbottom0">
				<tr>
					<th><bean:message key="label.over23.qualifications.name" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= designationId %>' 
									name="qualification"
									schema="PublicCandidacyProcessBean.formation.designation">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
					</td>
					<td rowspan="3">
						<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeConcludedHabilitationsEntry\"; document.getElementById(\"over23CandidacyForm\").submit();" %>' href="#" ><bean:message key="label.remove" bundle="CANDIDATE_RESOURCES" /></a></p>
					</td>
					<td class="tdclear">
						<span class="error0"><fr:message for="<%= designationId %>"/></span>
					</td>
				</tr>
				<tr>
					<th><bean:message key="label.over23.school" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= institutionNameId %>' 
							name="qualification"
							schema="PublicCandidacyProcessBean.formation.institutionUnitName">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>					
					</td>
					<td class="tdclear">
						<span class="error0"><fr:message for="<%= institutionNameId %>"/></span>
					</td>					
				</tr>
				<tr>
					<th><bean:message key="label.over23.execution.year.conclusion" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= endYearId %>'
									name="qualification"
									schema="PublicCandidacyProcessBean.over23.execution.year.conclusion">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
					</td>
					<td class="tdclear">
		                <span class="error0"><fr:message for="<%= endYearId %>"/></span>
					</td>					
				</tr>
			</table>
		</logic:iterate>
		<p class="mtop05 mbottom2"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addConcludedHabilitationsEntry'; document.getElementById('over23CandidacyForm').submit();" href="#"><bean:message key="label.add" bundle="CANDIDATE_RESOURCES" /></a></p>
		
		
		<h3><bean:message key="label.over23.qualifications.non.concluded" bundle="CANDIDATE_RESOURCES"/></h3>
		<logic:iterate id="qualification" name="individualCandidacyProcessBean" property="formationNonConcludedBeanList" indexId="index">
			<bean:define id="qualificationId" name="qualification" property="id"/>
			<bean:define id="designationId"><%= "individualCandidacyProcessBean.habilitation.concluded.designation:" + qualificationId %></bean:define>
			<bean:define id="institutionNameId"><%= "individualCandidacyProcessBean.habilitation.concluded.institutionName:" + qualificationId %></bean:define>
			<bean:define id="beginYearId"><%= "individualCandidacyProcessBean.habilitation.concluded.begin.year:" + qualificationId %></bean:define>
			<bean:define id="endYear"><%= "individualCandidacyProcessBean.habilitation.concluded.end.year:" + qualificationId %></bean:define>
			<bean:define id="designationErrorId"><%=  "error.individualCandidacyProcessBean.formation.designation:" + qualificationId %></bean:define>
			<bean:define id="institutionErrorId"><%=  "error.individualCandidacyProcessBean.formation.institution:" + qualificationId %></bean:define>
			<bean:define id="durationErrorId"><%=  "error.individualCandidacyProcessBean.formation.duration:" + qualificationId %></bean:define>
		
			<table class="tstyle5 thlight thleft mbottom0">
				<tr>
					<th><bean:message key="label.over23.qualifications.name" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= designationId %>' 
									name="qualification"
									schema="PublicCandidacyProcessBean.formation.designation">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>
					</td>
					<td rowspan="3">
						<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeNonConcludedHabilitationsEntry\"; document.getElementById(\"over23CandidacyForm\").submit();" %>' href="#" ><bean:message key="label.remove" bundle="CANDIDATE_RESOURCES" /></a></p>
					</td>
					<td class="tdclear">
		                <span class="error0"><fr:message for="<%= designationId %>"/></span>
					</td>
				</tr>
				<tr>
					<th><bean:message key="label.over23.school" bundle="CANDIDATE_RESOURCES"/>: <span class="red">*</span></th>
					<td>
						<fr:edit 	id='<%= institutionNameId %>' 
							name="qualification"
							schema="PublicCandidacyProcessBean.formation.institutionUnitName">
							<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
						</fr:edit>					
					</td>
					<td class="tdclear">
		                <span class="error0"><fr:message for="<%= institutionNameId %>"/></span>
					</td>
				</tr>
			</table>
		</logic:iterate>
		<p class="mtop05 mbottom2"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addNonConcludedHabilitationsEntry'; document.getElementById('over23CandidacyForm').submit();" href="#"><bean:message key="label.add" bundle="CANDIDATE_RESOURCES" /></a></p>


		<h3><bean:message key="label.over23.languages" bundle="CANDIDATE_RESOURCES"/></h3>
		<p class="mbottom05"><bean:message key="label.over23.languages.read" bundle="CANDIDATE_RESOURCES"/>:</p>
		<div class="flowerror">
		<fr:edit 	id='PublicCandidacyProcessBean.over23.languages.read' 
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.languages.read">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>
		</div>	
		
		
		<p class="mbottom05"><bean:message key="label.over23.languages.write" bundle="CANDIDATE_RESOURCES"/>:</p>
		<div class="flowerror">
		<fr:edit 	id='PublicCandidacyProcessBean.over23.languages.write' 
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.languages.write">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>
		</div>	

		<p class="mbottom05"><bean:message key="label.over23.languages.speak" bundle="CANDIDATE_RESOURCES"/>:</p>
		<div class="flowerror">
		<fr:edit 	id='PublicCandidacyProcessBean.over23.languages.speak' 
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.languages.speak">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>
		</div>	

		
		<h2 style="margin-top: 1em;"><bean:message key="title.over23.bachelor.first.cycle.choice" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="CANDIDATE_RESOURCES"/></h2>
		
		<fr:edit 	id='PublicCandidacyProcessBean.degrees'
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.degrees">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>
		<span class="red">*</span>
		<p class="mtop05"><a onclick="document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='addSelectedDegreesEntry'; document.getElementById('over23CandidacyForm').submit();" href="#"><bean:message key="label.over23.select" bundle="CANDIDATE_RESOURCES"/></a></p>
		
		
		<logic:notEmpty name="individualCandidacyProcessBean" property="selectedDegrees">
		<ol class="mtop05">
		<logic:iterate id="degree" name="individualCandidacyProcessBean" property="selectedDegrees" indexId="index">
			<li>
				<fr:view name="degree" >
				    <fr:schema type="net.sourceforge.fenixedu.domain.Degree" bundle="APPLICATION_RESOURCES">
						<fr:slot name="nameI18N" key="label.degree.name" />
						<fr:slot name="sigla" key="label.sigla" />
					</fr:schema>
					<fr:layout name="flow">
						<fr:property name="labelExcluded" value="true"/>
					</fr:layout> 
				</fr:view> | 
				<a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeSelectedDegreesEntry\"; document.getElementById(\"over23CandidacyForm\").submit();" %>' href="#" ><bean:message key="label.over23.remove" bundle="CANDIDATE_RESOURCES"/> </a>
			</li>
		</logic:iterate>
		</ol>
		</logic:notEmpty>
	
		<h2 style="margin-top: 1em;"><bean:message key="label.over23.disabilities" bundle="CANDIDATE_RESOURCES"/></h2>
		<p><bean:message key="message.over23.disabilities.detail" bundle="CANDIDATE_RESOURCES"/>:</p>
		<div class="flowerror">
		<fr:edit 	id="individualCandidacyProcessBean.disabilities"
					name="individualCandidacyProcessBean"
					schema="PublicCandidacyProcessBean.over23.disabilities">
			<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
		</fr:edit>
		</div>
		
		<p class="mtop15">
			<html:submit onclick="document.getElementById('methodId').value='editCandidacyHabilitations'; this.form.submit();"><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
		</p>
</fr:form>

