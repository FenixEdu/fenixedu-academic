<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.Enrolment"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.ManageEnrolmentsBean"%>

<logic:present role="COORDINATOR">

<em><bean:message key="label.phd.coordinator.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.manage.enrolments" bundle="PHD_RESOURCES" /></h2>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>


<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<bean:define id="processId" name="process" property="externalId" />

<fr:form action="<%= "/phdIndividualProgramProcess.do?method=manageEnrolments&processId=" + processId %>">

	<fr:edit id="manageEnrolmentsBean" name="manageEnrolmentsBean">
		<fr:schema bundle="PHD_RESOURCES" type="<%= ManageEnrolmentsBean.class.getName() %>">
			<fr:slot name="semester" layout="menu-select-postback">
				<fr:property name="providerClass" value="<%= ManageEnrolmentsBean.PhdManageEnrolmentsExecutionSemestersProviders.class.getName()  %>"/>
				<fr:property name="format" value="${qualifiedName}" />
			</fr:slot>
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="postback" path="<%= "/phdIndividualProgramProcess.do?method=manageEnrolments&processId=" + processId %>" />
	</fr:edit>
</fr:form>

<logic:empty name="manageEnrolmentsBean" property="enrolments">
	<em><bean:message key="label.phd.no.enrolments.found" bundle="PHD_RESOURCES" /></em>
</logic:empty>

<fr:view name="manageEnrolmentsBean" property="enrolments">
	<fr:schema bundle="PHD_RESOURCES" type="<%= Enrolment.class.getName() %>">
		<fr:slot name="name" />
		<fr:slot name="ectsCredits" />
		<fr:slot name="executionPeriod.qualifiedName" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop10" />
		<fr:property name="sortBy" value="name=asc" />
	</fr:layout>
</fr:view>


</logic:present>
