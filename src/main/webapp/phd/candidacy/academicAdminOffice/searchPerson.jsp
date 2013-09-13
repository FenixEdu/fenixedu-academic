<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<%-- ### Title #### --%>
<h2><bean:message key="label.phd.candidacy.academicAdminOffice.createCandidacy" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<jsp:include page="createCandidacyStepsBreadcrumb.jsp?step=1"></jsp:include>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=createCandidacyBean.choosePersonBean.edit" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>

<%--  ### End Of Context Information  ### --%>



<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<fr:form action="/phdProgramCandidacyProcess.do">

<input type="hidden" name="method" value="" /> 

<fr:edit id="createCandidacyBean" name="createCandidacyBean" visible="false" />

<fr:edit id="createCandidacyBean.choosePersonBean.edit"
	name="createCandidacyBean"
	property="choosePersonBean"
	schema="choose.person">

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
		<fr:property name="requiredMarkShown" value="true" />
	</fr:layout>
</fr:edit>


<%--  ### End of Operation Area  ### --%>



<br/>
<%--  ### Buttons (e.g. Submit)  ### --%>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='searchPerson';"><bean:message bundle="PHD_RESOURCES" key="label.continue"/></html:submit>
<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='cancelCreateCandidacy';"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>

<%--  ### End of Buttons (e.g. Submit)  ### --%>

</fr:form>
