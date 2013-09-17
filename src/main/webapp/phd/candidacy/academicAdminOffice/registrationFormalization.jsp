<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.RegistrationFormalizationBean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice.PhdProgramCandidacyProcessDA"%><html:xhtml/>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.registrationFormalization" bundle="PHD_RESOURCES" /></h2>
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


<%--  ### End Of Context Information  ### --%>

<bean:define id="processId" name="process" property="externalId" />
<br/>

<div class="warning1 mbottom05" style="width: 700px;">
<table>
	<tr>
		<td><bean:message  key="label.phd.registrationFormalization.identification.document" bundle="PHD_RESOURCES"/>: </td>
		<td class="acenter"><logic:equal name="idDocument" value="true"><html:img src="<%= request.getContextPath() + "/images/correct.gif" %>"/></logic:equal><logic:equal name="idDocument" value="false"><html:img src="<%= request.getContextPath() + "/images/incorrect.gif" %>"/></logic:equal></td>
	</tr>
	<tr>
		<td><bean:message  key="label.phd.registrationFormalization.photo" bundle="PHD_RESOURCES"/>: </td>
		<td class="acenter"><logic:equal name="personalPhoto" value="true"><html:img src="<%= request.getContextPath() + "/images/correct.gif" %>"/></logic:equal><logic:equal name="personalPhoto" value="false"><html:img src="<%= request.getContextPath() + "/images/incorrect.gif" %>"/></logic:equal></td>
	</tr>
	<tr>
		<td><bean:message  key="label.phd.registrationFormalization.healthBulletin" bundle="PHD_RESOURCES"/>: </td>
		<td class="acenter"><logic:equal name="healthBulletin" value="true"><html:img src="<%= request.getContextPath() + "/images/correct.gif" %>"/></logic:equal><logic:equal name="healthBulletin" value="false"><html:img src="<%= request.getContextPath() + "/images/incorrect.gif" %>"/></logic:equal></td>
	</tr>
	<tr>
		<td><bean:message  key="label.phd.registrationFormalization.habilitationsCertificates" bundle="PHD_RESOURCES"/>: </td>
		<td class="acenter"><logic:equal name="habilitationsCertificates" value="true"><html:img src="<%= request.getContextPath() + "/images/correct.gif" %>"/></logic:equal><logic:equal name="habilitationsCertificates" value="false"><html:img src="<%= request.getContextPath() + "/images/incorrect.gif" %>"/></logic:equal></td>
	</tr>
</table>
</div>

<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<fr:form action="<%= "/phdProgramCandidacyProcess.do?method=registrationFormalization&processId=" + processId.toString() %>">
  	
  	<fr:edit id="registrationFormalizationBean" name="registrationFormalizationBean" visible="false" /> 
  	
	<fr:edit id="bean.whenStartedStudies" name="registrationFormalizationBean">
		<fr:schema bundle="PHD_RESOURCES" type="<%= RegistrationFormalizationBean.class.getName() %>">
			<fr:slot name="whenStartedStudies" required="true" />
		</fr:schema>

		<fr:layout name="layout">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>
		<fr:destination name="invalid" path="/phdProgramCandidacyProcess.do?method=registrationFormalizationInvalid" />
	</fr:edit>

	<logic:equal name="registrationFormalizationBean" property="selectRegistration" value="true">
		<br/>
		<strong><bean:message key="label.phd.registrationFormalization.choose.registration" bundle="PHD_RESOURCES" />:</strong>
		<fr:edit id="bean.registration" name="registrationFormalizationBean">

			<fr:schema bundle="PHD_RESOURCES" type="<%= RegistrationFormalizationBean.class.getName() %>">
				<fr:slot name="registration" layout="radio-select" required="true">	
					<fr:property name="providerClass" value="<%= PhdProgramCandidacyProcessDA.PhdRegistrationFormalizationRegistrations.class.getName() %>" />
			        <fr:property name="eachLayout" value="values-dash" />
					<fr:property name="eachSchema" value="Registration.degreeNameWithDegreeCurricularPlanName" />		
				</fr:slot>
			</fr:schema>
	
			<fr:layout name="layout">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>

			<fr:destination name="invalid" path="/phdProgramCandidacyProcess.do?method=registrationFormalizationInvalid" />
		</fr:edit>

	</logic:equal>

	
<%--  ### Buttons (e.g. Submit)  ### --%>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.phd.registrationFormalization"/></html:submit>
<%--  ### End of Buttons (e.g. Submit)  ### --%>

</fr:form>

<br/><br/>


<%--  ### End of Operation Area  ### --%>
