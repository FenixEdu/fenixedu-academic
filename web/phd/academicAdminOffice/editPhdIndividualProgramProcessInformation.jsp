<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
<bean:define id="processId" name="process" property="externalId" />

<%-- ### Title #### --%>
	<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
	<h2><bean:message key="label.phd.editPhdIndividualProgramProcessInformation" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
	<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + processId.toString() %>">
		<bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
	
	<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=phdIndividualProgramProcessBean" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
	<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
	<fr:view schema="PhdIndividualProgramProcess.view.simple" name="process">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
		</fr:layout>
	</fr:view>
<%--  ### End Of Context Information  ### --%>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

	<fr:form action="<%= "/phdIndividualProgramProcess.do?processId=" + processId %>">
		<input type="hidden" name="method" value="" />

		<strong><bean:message key="label.phd.editPhdIndividualProgramProcessInformation.details" bundle="PHD_RESOURCES" /></strong>
		<fr:edit id="phdIndividualProgramProcessBean" name="phdIndividualProgramProcessBean" schema="PhdIndividualProgramProcessBean.editDetails">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
				<fr:property name="columnClasses" value=",,error0" />
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
		
			<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=editPhdIndividualProgramProcessInformationInvalid&amp;processId=" + processId %>" />
			<fr:destination name="editDetailsPostback" path="<%= "/phdIndividualProgramProcess.do?method=editPhdIndividualProgramProcessInformationPostback&amp;processId=" + processId %>" />
			<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=viewProcess&amp;processId=" + processId %>" />
		</fr:edit>
		
		<logic:equal name="phdIndividualProgramProcessBean" property="individualProgramProcess.inWorkDevelopment" value="true">

			<strong><bean:message key="label.phd.editPhdIndividualProgramProcessInformation.candidacy.details" bundle="PHD_RESOURCES" /></strong>
			<fr:edit id="phdIndividualProgramProcessBean-candidacy-details" name="phdIndividualProgramProcessBean">
				<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean">
					<fr:slot name="whenRatified" required="true" />
				</fr:schema>
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
					<fr:property name="columnClasses" value=",,error0" />
					<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>

				<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=editPhdIndividualProgramProcessInformationInvalid&amp;processId=" + processId %>" />
				<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=viewProcess&amp;processId=" + processId %>" />
			</fr:edit>
			
			<strong><bean:message key="label.phd.editPhdIndividualProgramProcessInformation.phdIndividualProgramProcess.details" bundle="PHD_RESOURCES" /></strong>
			<fr:edit id="phdIndividualProgramProcessBean-phdIndividualprogramProcess-details" name="phdIndividualProgramProcessBean">
				<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean">
					<fr:slot name="whenFormalizedRegistration" required="true" />
				</fr:schema>

				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
					<fr:property name="columnClasses" value=",,error0" />
					<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>

				<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=editPhdIndividualProgramProcessInformationInvalid&amp;processId=" + processId %>" />
				<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=viewProcess&amp;processId=" + processId %>" />
			</fr:edit>
		</logic:equal>

		<logic:equal name="phdIndividualProgramProcessBean" property="individualProgramProcess.registrationFormalized" value="true">
			
		</logic:equal>
		
		<html:submit onclick="this.form.method.value='editPhdIndividualProgramProcessInformation'"><bean:message key="label.edit" bundle="PHD_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>

	</fr:form>

<%--  ### End of Operation Area  ### --%>

</logic:present>
