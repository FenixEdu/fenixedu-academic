<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="COORDINATOR">

<h2><bean:message key="label.phd.focusAreas" bundle="PHD_RESOURCES" /></h2>

<fr:view name="focusAreas">
	<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.PhdProgramFocusArea">
		<fr:slot name="name.content" key="label.phd.institution.public.candidacy.focus.area">
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
		<fr:property name="linkFormat(thesisSubjects)" value="/candidacies/phdProgramCandidacyProcess.do?method=manageThesisSubjects&focusAreaId=${externalId}"/>
		<fr:property name="key(thesisSubjects)" value="link.phd.thesisSubjects"/>
		<fr:property name="bundle(thesisSubjects)" value="PHD_RESOURCES"/>
	</fr:layout>
</fr:view>

</logic:present>
