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
<html:link action="/phdCandidacyPeriodManagement.do?method=list">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
 
<p><strong><bean:message  key="title.phd.candidacy.periods" bundle="PHD_RESOURCES"/></strong></p>

<fr:view name="phdCandidacyPeriod">
	<fr:schema type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod" bundle="PHD_RESOURCES">
			<fr:slot name="type" />
			<fr:slot name="executionInterval.name" key="label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriod.executionYear"/>
			<fr:slot name="start" />
			<fr:slot name="end" />
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15 thleft" />
	</fr:layout>
</fr:view>

<p><strong><bean:message key="title.phd.candidacies" bundle="PHD_RESOURCES" /></strong></p>

<logic:empty name="phdCandidacyPeriod" property="phdProgramCandidacyProcesses" >
		<p><em><bean:message key="message.phd.candidacies.is.empty" bundle="PHD_RESOURCES" /></em></p>
</logic:empty>

<logic:notEmpty name="phdCandidacyPeriod" property="phdProgramCandidacyProcesses" >
<fr:view name="phdCandidacyPeriod" property="phdProgramCandidacyProcesses">
	<fr:schema type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess" bundle="PHD_RESOURCES">
		<fr:slot name="processNumber" key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.phdIndividualProcessNumber" />
		<fr:slot name="person.name" key="label.net.sourceforge.fenixedu.dataTransferObject.person.PersonBean.name"/>
		<fr:slot name="individualProgramProcess.phdProgram.name" key="label.net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriodBean.phdProgram" />
		<fr:slot name="candidacyDate" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15 thleft" />	
		<fr:link name="view" link="/phdIndividualProgramProcess.do?method=viewProcess&processId=${individualProgramProcess.externalId}" label="label.view,PHD_RESOURCES"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>
