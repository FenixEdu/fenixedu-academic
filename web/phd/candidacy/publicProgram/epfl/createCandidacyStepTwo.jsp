<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/about-IST/global-cooperation/IST-EPFL/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<jsp:include page="/phd/candidacy/publicProgram/epfl/createCandidacyStepsBreadcrumb.jsp?step=2"></jsp:include>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean.focus.area" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Operation Area ### --%>

<fr:form id="candidacyForm" action="/applications/epfl/phdProgramCandidacyProcess.do" >

	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<input type="hidden" id="methodId" name="method" value="createCandidacyStepThree"/>
	<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>

	<div class="fs_form">
		<p class="mvert05"><span><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></span></p>
	</div>

	<div class="fs_form">
	<fieldset style="display: block;">
		<legend><bean:message key="title.public.phd.focus.area" bundle="PHD_RESOURCES"/></legend>
	
		<fr:edit id="candidacyBean.focus.area" name="candidacyBean" 
				 schema="Public.PhdProgramCandidacyProcessBean.focus.area">
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createCandidacyStepTwoInvalid" />
			<fr:destination name="focusAreaPostBack" path="/applications/epfl/phdProgramCandidacyProcess.do?method=prepareCreateCandidacyStepTwoFocusAreaPostback" />
		</fr:edit>
	</fieldset>
	</div>
	
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="<%= "document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='returnCreateCandidacyStepOne'; document.getElementById('candidacyForm').submit();" %>">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.submit"/> »</html:submit>
	</p>

</fr:form>
