<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="org.apache.struts.action.ActionMessages" %>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<a href='<%= fullPath + "?method=beginCandidacyProcessIntro" %>'><bean:write name="application.name"/> </a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:write name="application.name"/></h1>

<p class="steps">
	<span class="actual"><bean:message key="label.step.one.personal.details" bundle="CANDIDATE_RESOURCES"/></span> > 
	<span><bean:message key="label.step.two.habilitations.document.files" bundle="CANDIDATE_RESOURCES"/></span>
</p>

<p class="mtop15"><span><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></span></p>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="<%= ActionMessages.GLOBAL_MESSAGE %>">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES" property="individualCandidacyMessages">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<fr:form id="over23CandidacyForm" action='<%= mappingPath + ".do?method=continueCandidacyCreation" %>' encoding="multipart/form-data">
		<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
		
 		<h2 style="margin-top: 1em;"><bean:message key="title.personal.data" bundle="CANDIDATE_RESOURCES"/></h2>

		<fr:edit id="candidacyProcess.personalDataBean"
			name="individualCandidacyProcessBean" 
			schema="PublicCandidacyProcess.candidacyDataBean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
		        <fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path='<%= mappingPath + ".do?method=executeCreateCandidacyPersonalInformationInvalid" %>' />
		</fr:edit>
		
		<p>&nbsp;</p>
		<table>
			<tr>
				<td colspan="2">
					<bean:message key="message.ist.former.student.employee.username.required" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="CANDIDATE_RESOURCES"/>
				</td>
			</tr>
			<tr>
				<td class="width175px">
					<bean:message key="label.studentOrEmployeeNumber" bundle="CANDIDATE_RESOURCES"/>:
				</td>
				<td>
					<fr:edit id="individualCandidacyProcessBean.istUsername"
						name="individualCandidacyProcessBean" 
						schema="PublicCandidacyProcess.candidacyDataBean.personNumber">
						<fr:layout name="flow">
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout>
					</fr:edit>
				</td>
			</tr>			
		</table>

		<p class="mtop15">
			<html:submit><bean:message key="button.continue" bundle="APPLICATION_RESOURCES" /> <bean:message key="label.application" bundle="CANDIDATE_RESOURCES"/></html:submit>
		</p>
</fr:form>
