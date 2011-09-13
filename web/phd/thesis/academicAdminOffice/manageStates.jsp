<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.manage.states" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="processId" name="process" property="externalId" />

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--
<div class="breadcumbs">
	<span class="actual">Step 1: Step Name</span> > 
	<span>Step N: Step name </span>
</div>
--%>
<html:link action="<%= "/phdThesisProcess.do?method=viewIndividualProgramProcess&processId=" + processId %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.thesisProcess" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="PhdThesisProcess.view" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop10" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>


<%-- ### List of Process States ### --%>
<strong><bean:message key="label.phd.states" bundle="PHD_RESOURCES" /></strong>
<fr:view name="process" property="states">
	<fr:schema type="net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState" bundle="PHD_RESOURCES" >
		<fr:slot name="whenCreated" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState.whenCreated" />
		<fr:slot name="stateDate" layout="null-as-label" />
		<fr:slot name="type.localizedName" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState.type" />
		<fr:slot name="remarks" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState.remarks" />
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
		<fr:property name="sortBy" value="whenCreated=desc" />

		<fr:link 	name="removeState" 
					link="<%= String.format("/phdThesisProcess.do?method=removeLastState&processId=%s", processId)  %>" 
					label="link.net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState.removeState,PHD_RESOURCES"
					condition="last" 
					confirmation="message.net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState.removeState.confirmation,PHD_RESOURCES"/>
	</fr:layout>
</fr:view>

<%-- ### End of List of Process States ### --%>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<p class="mtop15 mbottom05"><strong><bean:message  key="label.phd.modify.state" bundle="PHD_RESOURCES"/></strong></p>
<fr:form action="<%= "/phdThesisProcess.do?method=addState&processId=" + processId %>">

	<fr:edit id="thesisProcessBean" name="thesisProcessBean" >
		<fr:schema type="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean" bundle="PHD_RESOURCES">
			<fr:slot name="processState" required="true" layout="menu-select" >
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.phd.providers.PhdThesisProcessStateProvider" />
				<fr:property name="format" value="${localizedName}" />
			</fr:slot>
			<fr:slot name="stateDate" required="true" >
				<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.LocalDateValidator" />
			</fr:slot>
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="invalid" path="<%= "/phdThesisProcess.do?method=addStateInvalid&processId=" + processId %>" />
		<fr:destination name="cancel" path="<%= "/phdThesisProcess.do?method=viewIndividualProgramProcess&processId=" + processId %>" />
	</fr:edit>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" ><bean:message bundle="PHD_RESOURCES" key="label.add"/></html:submit>
</fr:form>

<%--  ### End of Operation Area  ### --%>


<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>
</logic:present>