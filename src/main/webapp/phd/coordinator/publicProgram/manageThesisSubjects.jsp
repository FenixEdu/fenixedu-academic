<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="COORDINATOR">

<h2><bean:message key="label.phd.focusAreas" bundle="PHD_RESOURCES" /></h2>

<bean:define id="focusArea" name="focusArea" type="net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea"/>
<h3><bean:write name="focusArea" property="name.content" /></h3>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<h4><bean:message key="label.phd.thesisSubjects" bundle="PHD_RESOURCES" />:</h4>

<logic:notEmpty name="thesisSubjects">
	<fr:view name="thesisSubjects">
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.ThesisSubject">
			<fr:slot name="name.content" key="label.phd.name"/>
			<fr:slot name="teacher.person.name" key="label.phd.guiding"/>
			<fr:slot name="externalAdvisorName" key="label.phd.guiding.external"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />

			<fr:link name="edit" 
				link="<%= "/candidacies/phdProgramCandidacyProcess.do?method=prepareEditThesisSubject&thesisSubjectId=${externalId}&focusAreaId=" + focusArea.getExternalId() %>" 
				label="label.edit,APPLICATION_RESOURCES" />

			<fr:property name="linkFormat(remove)" value="<%= "/candidacies/phdProgramCandidacyProcess.do?method=removeThesisSubject&thesisSubjectId=${externalId}&focusAreaId=" + focusArea.getExternalId() %>"/>
			<fr:property name="key(remove)" value="label.remove"/>
			<fr:property name="bundle(remove)" value="PHD_RESOURCES"/>
			
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="thesisSubjects">
	<bean:message key="label.phd.thesisSubjects.none" bundle="PHD_RESOURCES" />
</logic:empty>

<h4><bean:message key="label.phd.thesisSubjects.add" bundle="PHD_RESOURCES" />:</h4>

<fr:form action="<%= "/candidacies/phdProgramCandidacyProcess.do?method=addThesisSubject&focusAreaId=" + focusArea.getExternalId() %>">
	<fr:edit id="thesisSubjectBean" name="thesisSubjectBean">
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
		
		<fr:destination name="invalid" path="<%= "/candidacies/phdProgramCandidacyProcess.do?method=addThesisSubjectInvalid&focusAreaId=" + focusArea.getExternalId() %>" />
		<fr:destination name="cancel" path="/candidacies/phdProgramCandidacyProcess.do?method=manageFocusAreas"/>
	</fr:edit>
	<p>
		<html:submit><bean:message key="button.add" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</p>
</fr:form>

</logic:present>
