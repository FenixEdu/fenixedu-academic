<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:present role="COORDINATOR">

<h2><bean:message key="label.phd.focusAreas" bundle="PHD_RESOURCES" /></h2>

<bean:define id="focusArea" name="focusArea" type="net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea"/>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<h4><bean:message key="label.phd.thesisSubjects" bundle="PHD_RESOURCES" />:</h4>

<bean:define id="focusAreaId" name="focusArea" property="externalId" />
<bean:define id="thesisSubjectId" name="thesisSubject" property="externalId" />

<fr:form action="<%= String.format("/candidacies/phdProgramCandidacyProcess.do?method=editThesisSubject&focusAreaId=%s&thesisSubjectId=%s", focusAreaId, thesisSubjectId) %>">
	<fr:edit id="bean" name="bean">
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.publicProgram.PublicPhdProgramCandidacyProcessDA$ThesisSubjectBean">
			<fr:slot name="name" key="label.phd.name" >
				<fr:property name="size" value="80" />
				<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredMultiLanguageStringValidator" />
			</fr:slot>
			<fr:slot name="teacher" key="label.phd.guiding" layout="autoComplete">
				<fr:property name="size" value="80"/>
				<fr:property name="labelField" value="person.name"/>
				<fr:property name="format" value="${person.name}"/>
				<fr:property name="indicatorShown" value="true"/>
				<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchTeachersByName"/>
				<fr:property name="args" value="slot=person.name"/>
				<fr:property name="minChars" value="3"/>
				<fr:property name="errorStyleClass" value="error0"/>
				<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator" />
			</fr:slot>
			<fr:slot name="externalAdvisorName" key="label.phd.guiding.external" >
				<fr:property name="size" value="80" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight mtop15" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="invalid" path="<%= String.format("/candidacies/phdProgramCandidacyProcess.do?method=editThesisSubjectInvalid&focusAreaId=%s&thesisSubjectId=%s", focusAreaId, thesisSubjectId) %>" />
		<fr:destination name="cancel" path="<%= "/candidacies/phdProgramCandidacyProcess.do?method=manageThesisSubjects&focusAreaId=" + focusAreaId %>" />
	</fr:edit>
	<p>
		<html:submit><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</p>
</fr:form>

</logic:present>
