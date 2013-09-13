<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditPhdParticipant"%>

<%-- ### Title #### --%>
<h2><bean:message key="title.phd.candidacy.periods" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
 
<p><strong><bean:message  key="title.phd.candidacy.periods" bundle="PHD_RESOURCES"/></strong></p>

<logic:empty name="phdCandidacyPeriods">
	<em><bean:message key="message.phd.candidacy.periods.empty" bundle="PHD_RESOURCES" /></em>
</logic:empty>

<logic:notEmpty  name="phdCandidacyPeriods">
	<fr:view name="phdCandidacyPeriods">
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod">
			<fr:slot name="type" />
			<fr:slot name="executionInterval.name" key="label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod.executionYear"/>
			<fr:slot name="start" />
			<fr:slot name="end" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15 thleft" />
	
			<fr:link 	label="link.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod.view,PHD_RESOURCES" 
						name="view"
						link="/phdCandidacyPeriodManagement.do?method=view&amp;phdCandidacyPeriodId=${externalId}" />
			<fr:link 	label="link.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod.edit,PHD_RESOURCES" 
						name="edit"
						link="/phdCandidacyPeriodManagement.do?method=prepareEditPhdCandidacyPeriod&amp;phdCandidacyPeriodId=${externalId}" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<p>
	<html:link action="/phdCandidacyPeriodManagement.do?method=prepareCreatePhdCandidacyPeriod">
		<bean:message key="link.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod.create" bundle="PHD_RESOURCES" />
	</html:link>
</p>
