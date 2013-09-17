<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/about-IST/global-cooperation/IST-EPFL/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.epfl.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<fr:form id="editPhdInformationForm" action="/applications/epfl/phdProgramCandidacyProcess.do">
	<html:hidden name="method" property="method" value="editPhdIndividualProgramProcessInformation"/>
	<html:hidden property="order" value="1"/>
	
	<bean:define id="hash" name="candidacyBean" property="candidacyHashCode.value" type="java.lang.String"/>
	
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false">
		<fr:destination name="cancel" path="<%= "/applications/epfl/phdProgramCandidacyProcess.do?method=viewCandidacy&hash=" + hash %>" />
	</fr:edit>
	<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>	
	
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<logic:equal name="canEditCandidacy" value="true">

	<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp" />
	<%--  ### End of Error Messages  ### --%>
	
	<h2 style="margin-top: 1em;"><bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation" bundle="PHD_RESOURCES" /></h2>
	
	<logic:notPresent name="candidacyBean">
		<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
		<a href="#" onclick="javascript:document.getElementById('skipValidationId').value='true';javascript:document.getElementById('method').value='backToViewCandidacy';document.getElementById('editPhdInformationForm').submit();">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></a>
	</logic:notPresent>
	
	<logic:present name="candidacyBean">
		<div class="fs_form">
		<fieldset style="display: block;">
			<legend><bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation.edit" bundle="PHD_RESOURCES" /></legend>
			<p class="mtop15"><span><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></span></p>
			
			<fr:edit id="individualProcessBean" name="individualProcessBean" schema="Public.PhdIndividualProgramProcessBean.editDetails">		
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="thlight thleft"/>
					<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=editPhdIndividualProgramProcessInformationInvalid" />
				<fr:destination name="postBack" path="/applications/epfl/phdProgramCandidacyProcess.do?method=prepareEditPhdIndividualProgramProcessInformationFocusAreaPostback" />
				<fr:destination name="cancel" path="<%= "/applications/epfl/phdProgramCandidacyProcess.do?method=viewCandidacy&hash=" + hash %>" />
			</fr:edit>
			
			<logic:notEmpty name="individualProcessBean" property="thesisSubjectBeans">
			<logic:present name="individualProcessBean" property="phdProgram">
			<logic:present name="individualProcessBean" property="externalPhdProgram">
			
			<h3 class="mtop3"><bean:message key="title.phd.thesis.subject.ranking" bundle="PHD_RESOURCES" /></h3>
			<p class="mtop1"><em><bean:message key="message.phd.thesis.subject.rank" bundle="PHD_RESOURCES" />:</em></p>

			<table class="tstyle1 thlight mtop15">
				<tr>
					<td class="aright"><bean:message key="label.order" bundle="PHD_RESOURCES"/></td>
					<td><bean:message key="label.net.sourceforge.fenixedu.domain.phd.ThesisSubject.name" bundle="PHD_RESOURCES"/></td>
					<td><bean:message key="label.phd.guiding" bundle="PHD_RESOURCES"/></td>
					<td><bean:message key="label.phd.guiding" bundle="PHD_RESOURCES"/></td>
				</tr>
				<logic:iterate id="thesisSubjectBean" name="individualProcessBean" property="thesisSubjectBeans" type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdThesisSubjectOrderBean">
					<tr>
						<td class="aright"><fr:view name="thesisSubjectBean" property="order"/></td>
						<td><fr:view name="thesisSubjectBean" property="thesisSubject.name.content"/></td>
						<td><fr:view name="thesisSubjectBean" property="thesisSubject.teacher.person.name"/></td>
						<td>
							<logic:notEmpty name="thesisSubjectBean" property="thesisSubject.externalAdvisorName">
								<fr:view name="thesisSubjectBean" property="thesisSubject.externalAdvisorName"/>
							</logic:notEmpty>
							<logic:empty name="thesisSubjectBean" property="thesisSubject.externalAdvisorName">
								-
							</logic:empty>
						</td>
						<td>
							<html:image onclick="<%=	"javascript:" +
														"var form = document.getElementById('editPhdInformationForm');" +
														"form.method.value='moveUpThesisSubjectForEditPhdInformation';" +
														"form.order.value='" + thesisSubjectBean.getOrder() + "';" +
														"form.submit();" %>"
										border="0" src="<%= request.getContextPath() + "/images/move_up.gif"%>" altKey="up" bundle="IMAGE_RESOURCES"/>
																
							<html:image onclick="<%=	"javascript:" +
														"var form = document.getElementById('editPhdInformationForm');" +
														"form.method.value='moveDownThesisSubjectForEditPhdInformation';" +
														"form.order.value='" + thesisSubjectBean.getOrder() + "';" +
														"form.submit();" %>"
										border="0" src="<%= request.getContextPath() + "/images/move_down.gif"%>" altKey="down" bundle="IMAGE_RESOURCES"/>
						</td>
					</tr>
				</logic:iterate>
			</table>
			</logic:present>
			</logic:present>
			</logic:notEmpty>
			
			</fieldset>
			</div>
			
			<p>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.edit"/></html:submit>
				<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.cancel"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>
			</p>
	</logic:present>
</logic:equal>

</fr:form>
