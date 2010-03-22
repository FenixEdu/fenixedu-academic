<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.edit.candidacy.information" bundle="PHD_RESOURCES" /></h2>
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


<%--  ### Context Information  ### --%>
<%--  ### End Of Context Information  ### --%>


<br/>

<logic:equal name="candidacyInformationBean" property="valid" value="true">
	<p class="bluetxt">
		<em><bean:message key="label.candidacy.information.is.valid" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	</p>
</logic:equal>
<logic:equal name="candidacyInformationBean" property="valid" value="false">
	<p class="redtxt">
		<em><bean:message key="label.candidacy.information.not.valid" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
	</p>
</logic:equal>


<bean:define id="processId" name="process" property="externalId" />

<fr:form action="<%= "/phdProgramCandidacyProcess.do?method=editCandidacyInformation&processId=" + processId %>">

	<fr:edit id="candidacyInformationBean" name="candidacyInformationBean" visible="false" />

	<fr:edit id="candidacyInformationBean.editPersonalInformation"
		name="candidacyInformationBean" schema="CandidacyInformationBean.editPersonalInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thleft mtop15" />
			<fr:property name="columnClasses" value="width300px,,tdclear tderror1"/>
			
			<fr:destination name="invalid" path="<%= "/phdProgramCandidacyProcess.do?method=prepareEditCandidacyInformationInvalid&processId=" + processId %>" />
			<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProgramProcessId.toString() %>" />
		</fr:layout>
	</fr:edit>
	
	<br/>
	<strong><bean:message  key="label.previous.degree.information" bundle="STUDENT_RESOURCES"/></strong>
	<fr:edit id="candidacyInformationBean.editPrecedentDegreeInformation"
		name="candidacyInformationBean" schema="CandidacyInformationBean.editPrecedentDegreeInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thleft mtop15" />
			<fr:property name="columnClasses" value="width300px,,tdclear tderror1"/>
			
			<fr:destination name="invalid" path="<%= "/phdProgramCandidacyProcess.do?method=prepareEditCandidacyInformationInvalid&processId=" + processId %>" />
			<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProgramProcessId.toString() %>" />
		</fr:layout>
	</fr:edit>	

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/></html:submit>
	<html:cancel><bean:message bundle="APPLICATION_RESOURCES" key="label.back"/></html:cancel>	

</fr:form>

</logic:present>

