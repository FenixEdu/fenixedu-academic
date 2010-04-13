<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="COORDINATOR">

<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer"%>


<%-- ### Title #### --%>
<em><bean:message key="label.phd.coordinator.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.view.curriculum" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<fr:view schema="PhdIndividualProgramProcess.view.simple" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>

<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<fr:edit name="studentCurricularPlan">
	<fr:layout>
		<fr:property name="organizedBy" value="<%= StudentCurricularPlanRenderer.OrganizationType.GROUPS.toString() %>" />
		<fr:property name="enrolmentStateFilter" value="<%= StudentCurricularPlanRenderer.EnrolmentStateFilterType.ALL.toString() %>" />
		<fr:property name="viewType" value="<%= StudentCurricularPlanRenderer.ViewType.ALL.toString()%>" />
		<fr:property name="detailed" value="<%= Boolean.FALSE.toString() %>" />
	</fr:layout>
</fr:edit>

</logic:present>