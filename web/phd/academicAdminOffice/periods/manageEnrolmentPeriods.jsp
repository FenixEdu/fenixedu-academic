<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.ManageEnrolmentsBean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.providers.NotClosedExecutionPeriodsProvider"%>
<%@page import="net.sourceforge.fenixedu.domain.EnrolmentPeriod"%>

<html:xhtml/>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manage.enrolment.periods" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<fr:form action="/phdIndividualProgramProcess.do?method=manageEnrolmentPeriods">

	<fr:edit id="manageEnrolmentsBean" name="manageEnrolmentsBean">
		
		<fr:schema bundle="PHD_RESOURCES" type="<%= ManageEnrolmentsBean.class.getName() %>">
			<fr:slot name="semester" layout="menu-select-postback">
				<fr:property name="providerClass" value="<%= NotClosedExecutionPeriodsProvider.class.getName()  %>"/>
				<fr:property name="format" value="${qualifiedName}" />
			</fr:slot>
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="postback" path="/phdIndividualProgramProcess.do?method=manageEnrolmentPeriods" />
	</fr:edit>
</fr:form>

<html:link action="/phdIndividualProgramProcess.do?method=prepareCreateEnrolmentPeriod" paramId="executionIntervalId" paramName="manageEnrolmentsBean" paramProperty="semester.externalId">
	<bean:message bundle="PHD_RESOURCES" key="label.phd.create.enrolment.period"/>
</html:link>

<logic:empty name="manageEnrolmentsBean" property="enrolmentPeriods">
	<br/>
	<br/>
	<em><bean:message key="label.phd.no.enrolment.periods.found" bundle="PHD_RESOURCES" /></em>
</logic:empty>

<fr:view name="manageEnrolmentsBean" property="enrolmentPeriods">
	
	<fr:schema bundle="PHD_RESOURCES" type="<%= EnrolmentPeriod.class.getName() %>">
		<fr:slot name="degreeCurricularPlan.presentationName" />
		<fr:slot name="startDate" />
		<fr:slot name="endDate" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop10" />
		<fr:property name="sortBy" value="degreeCurricularPlan.presentationName,startDate" />
	
		<fr:link name="edit" label="label.edit,PHD_RESOURCES" order="1" link="/phdIndividualProgramProcess.do?method=prepareEditEnrolmentPeriod&periodId=${externalId}" />
		<fr:link name="delete" label="label.delete,PHD_RESOURCES" order="2" confirmation="label.phd.delete.enrolment.period.confirmation,PHD_RESOURCES" 
				 link="/phdIndividualProgramProcess.do?method=deleteEnrolmentPeriod&periodId=${externalId}" />
	
	</fr:layout>
</fr:view>
