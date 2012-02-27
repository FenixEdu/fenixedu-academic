<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="COORDINATOR">

<h2><bean:message key="label.phd.focusAreas" bundle="PHD_RESOURCES" /></h2>

<bean:define id="focusArea" name="focusArea" type="net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea"/>
<h3><bean:write name="focusArea" property="name.content" /></h3>

<p><span class="error0"><!-- Error messages go here --><html:errors bundle="PHD_RESOURCES" /></span></p>

<h4><bean:message key="label.phd.thesisSubjects" bundle="PHD_RESOURCES" />:</h4>

<logic:notEmpty name="thesisSubjects">
	<fr:view name="thesisSubjects">
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.ThesisSubject">
			<fr:slot name="name.content" key="label.phd.name"/>
			<fr:slot name="description.content" key="label.phd.description"/>
			<fr:slot name="teacher.person.name" key="label.phd.guiding"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
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

<fr:edit id="thesisSubjectBean" name="thesisSubjectBean" action="<%= "/candidacies/phdProgramCandidacyProcess.do?method=addThesisSubject&focusAreaId=" + focusArea.getExternalId() %>">
	<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.publicProgram.PublicPhdProgramCandidacyProcessDA$ThesisSubjectBean">
		<fr:slot name="name" key="label.phd.name"/>
		<fr:slot name="description" key="label.phd.description"/>
		<fr:slot name="teacherId" key="label.phd.guiding.username"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
		<fr:destination name="cancel" path="/candidacies/phdProgramCandidacyProcess.do?method=manageFocusAreas"/>
</fr:edit>

</logic:present>
