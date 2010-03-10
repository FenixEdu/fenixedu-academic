<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="COORDINATOR">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.coordinator.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.candidacy.feedback.request" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<bean:define id="individualProgramProcessId" name="process" property="individualProgramProcess.externalId" />

<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProgramProcessId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<table>
  <tr style="vertical-align: top;">
    <td style="width: 55%">
    	<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="PhdIndividualProgramProcess.view.simple" name="process" property="individualProgramProcess">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
	</td>
    <td>
	    <strong><bean:message  key="label.phd.candidacyProcess" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="PhdProgramCandidacyProcess.view.simple" name="process">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
    </td>
  </tr>
</table>

<br/>
<%--  ### End Of Context Information  ### --%>


<%--  ################################### --%>
<%--  ###      Shared documents       ### --%>
<jsp:include page="/phd/candidacy/coordinator/feedbackRequest/editSharedDocumentTypes.jsp" />
<%--  ###    End Shared documents     ### --%>
<%--  ################################### --%>


<logic:notEmpty name="process" property="feedbackRequest">

<%--  ################################### --%>
<%--  ###    add users to request     ### --%>
	<br/>
	<br/>
	+ <html:link action="/phdCandidacyFeedbackRequest.do?method=prepareAddCandidacyFeedbackRequestElement" paramId="processId" paramName="process" paramProperty="externalId">
		<bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.feedback.add.element"/>
	</html:link>
	
	<jsp:include page="/phd/candidacy/coordinator/feedbackRequest/addCandidacyFeedbackRequestElement.jsp" />
<%--  ###   End add users to request  ### --%>
<%--  ################################### --%>

	<logic:empty name="elementBean">
		<%-- SHOW ELEMENTS HERE --%>
	</logic:empty>	

</logic:notEmpty>

</logic:present>
