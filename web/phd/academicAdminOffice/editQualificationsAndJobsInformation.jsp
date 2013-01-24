<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.editQualificationsAndJobsInformation" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="processId" name="process" property="externalId" />
<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&amp;processId=" + processId %>">
	<bean:message key="label.back" bundle="PHD_RESOURCES" />
</html:link>
<br/><br/>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=qualification&amp;viewStateId=job" />
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


	<%-- ### Qualifications ### --%>
	<br/>
	<strong><bean:message key="label.phd.editQualificationsInformation" bundle="PHD_RESOURCES" /></strong>
	<logic:notPresent name="qualification">
		<html:link action="<%= "/phdIndividualProgramProcess.do?method=prepareAddQualification&amp;processId=" + processId %>">
			<bean:message key="label.add" bundle="PHD_RESOURCES" />
		</html:link>
	</logic:notPresent>
	
	<logic:present name="qualification">
		<fr:form action="<%= "/phdIndividualProgramProcess.do?method=addQualification&amp;processId=" + processId %>">
			<fr:edit id="qualification" name="qualification" schema="Phd.QualificationBean.manage">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
					<fr:property name="columnClasses" value=",,error0" />
					<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=addQualificationInvalid&amp;processId=" + processId %>" />
				<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=prepareEditQualificationsAndJobsInformation&amp;processId=" + processId %>" />
			</fr:edit>
			<html:submit><bean:message key="label.add" bundle="PHD_RESOURCES" /></html:submit>
			<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>
		</fr:form>
	</logic:present>
	
	<fr:view name="qualifications" schema="Phd.Qualification.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />

			<fr:property name="sortBy" value="type=asc,degree=asc,school=asc" />
	
			<fr:property name="link(delete)" value="<%= "/phdIndividualProgramProcess.do?method=deleteQualification&amp;processId=" + processId %>" />
			<fr:property name="param(delete)" value="externalId/qualificationId" />
			<fr:property name="key(delete)" value="label.delete" />
			<fr:property name="bundle(delete)" value="PHD_RESOURCES" />
			<fr:property name="confirmationKey(delete)" value="label.confirmation.delete.message" />
			<fr:property name="confirmationBundle(delete)" value="PHD_RESOURCES" />
		</fr:layout>
	</fr:view>
	<%-- ### End of Qualifications ### --%>

	<%-- ### Jobs ### --%>
	<br/>
	<strong><bean:message key="label.phd.editJobsInformation" bundle="PHD_RESOURCES" /></strong>
	<logic:notPresent name="job">
		<html:link action="<%= "/phdIndividualProgramProcess.do?method=prepareAddJobInformation&amp;processId=" + processId %>">
			<bean:message key="label.add" bundle="PHD_RESOURCES" />
		</html:link>
	</logic:notPresent>

	<logic:present name="job">
		<fr:form action="<%= "/phdIndividualProgramProcess.do?method=addJobInformation&amp;processId=" + processId %>">
			<fr:edit id="job" name="job" schema="Phd.JobBean.manage">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
					<fr:property name="columnClasses" value=",,error0" />
					<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=prepareEditQualificationsAndJobsInformation&amp;processId=" + processId %>" />
				<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=addJobInformationInvalid&amp;processId=" + processId %>" />
				<fr:destination name="searchChilds" path="<%= "/phdIndividualProgramProcess.do?method=addJobInformationPostback&amp;processId=" + processId %>" />
			</fr:edit>
			<html:submit><bean:message key="label.add" bundle="PHD_RESOURCES" /></html:submit>
			<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>
		</fr:form>
	</logic:present>

	<fr:view name="jobs" schema="Phd.Job.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />

			<fr:property name="sortBy" value="employerName=asc,position=asc" />
	
			<fr:property name="link(delete)" value="<%= "/phdIndividualProgramProcess.do?method=deleteJobInformation&amp;processId=" + processId %>" />
			<fr:property name="param(delete)" value="externalId/jobId" />
			<fr:property name="key(delete)" value="label.delete" />
			<fr:property name="bundle(delete)" value="PHD_RESOURCES" />
			<fr:property name="confirmationKey(delete)" value="label.confirmation.delete.message" />
			<fr:property name="confirmationBundle(delete)" value="PHD_RESOURCES" />
		</fr:layout>
	</fr:view>
	<%-- ### End of Jobs ### --%>

<%--  ### End of Operation Area  ### --%>
