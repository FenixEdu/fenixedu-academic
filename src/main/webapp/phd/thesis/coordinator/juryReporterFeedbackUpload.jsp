<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<logic:present role="role(COORDINATOR)">

<bean:define id="individualProcessId" name="process" property="individualProgramProcess.externalId" />

<%-- ### Title #### --%>
	<em><bean:message  key="label.phd.coordinator.breadcrumb" bundle="PHD_RESOURCES"/></em>
	<h2><bean:message key="label.phd.thesis.jury.feedback.upload.document" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
	<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProcessId.toString() %>">
		<bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
	<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
	<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
	<fr:view schema="PhdIndividualProgramProcess.view.resume" name="process" property="individualProgramProcess">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
		</fr:layout>
	</fr:view>
<%--  ### End Of Context Information  ### --%>

<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
	<jsp:include page="/phd/thesis/juryReporterFeedbackUploadForm.jsp" />
<%--  ### End of Operation Area  ### --%>

</logic:present>
