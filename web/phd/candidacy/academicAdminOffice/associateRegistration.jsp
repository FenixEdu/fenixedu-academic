<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.RegistrationFormalizationBean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice.PhdProgramCandidacyProcessDA"%><html:xhtml/>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.registrationFormalization.associate.registration" bundle="PHD_RESOURCES" /></h2>
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

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<logic:empty name="registrationFormalizationBean" property="availableRegistrationsToAssociate">
	<em><bean:message key="label.phd.registrationFormalization.no.registrations.to.choose" bundle="PHD_RESOURCES" /></em>
</logic:empty>

<logic:notEmpty name="registrationFormalizationBean" property="availableRegistrationsToAssociate">
	<fr:form action="<%= "/phdProgramCandidacyProcess.do?method=associateRegistration&processId=" + processId.toString() %>">
	  	
	  	<fr:edit id="registrationFormalizationBean" name="registrationFormalizationBean" visible="false" /> 
	  	
		<strong><bean:message key="label.phd.registrationFormalization.choose.registration" bundle="PHD_RESOURCES" />:</strong>
		<fr:edit id="bean.registration" name="registrationFormalizationBean">
			
			<fr:schema bundle="PHD_RESOURCES" type="<%= RegistrationFormalizationBean.class.getName() %>">
				
				<fr:slot name="whenStartedStudies" readOnly="true" />
				
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
			<fr:destination name="invalid" path="/phdProgramCandidacyProcess.do?method=associateRegistrationInvalid" />
		</fr:edit>
		<%--  ### Buttons (e.g. Submit)  ### --%>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.phd.registrationFormalization.associate.registration"/></html:submit>
		<%--  ### End of Buttons (e.g. Submit)  ### --%>
	</fr:form>
</logic:notEmpty>

<br/><br/>


<%--  ### End of Operation Area  ### --%>
