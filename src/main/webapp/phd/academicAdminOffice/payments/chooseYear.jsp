<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.dataTransferObject.accounting.events.AccountingEventCreateBean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider"%>

<html:xhtml/>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.accounting.events.create" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>



<bean:define id="processId" name="process" property="externalId" />

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--
<div class="breadcumbs">
	<span class="actual">Step 1: Step Name</span> > 
	<span>Step N: Step name </span>
</div>
--%>
<html:link action="<%= "/phdAccountingEventsManagement.do?method=prepare&processId=" + processId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
	<fr:edit id="yearBean" name="yearBean" action="<%= "/phdAccountingEventsManagement.do?method=createGratuityEvent&processId=" + processId.toString() %>">
		
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice.PhdAccountingEventsManagementDA$PhdGratuityCreationInformation">
			<fr:slot name="year"  key="label.civil.year"></fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight" />
			<fr:property name="columnClasses" value="nowrap," />
		</fr:layout>
		
		<fr:destination name="invalid" path="<%= "/phdAccountingEventsManagement.do?method=createGratuityEvent&processId=" + processId.toString() %>" />
		<fr:destination name="cancel" path="<%= "/phdAccountingEventsManagement.do?method=prepare&processId=" + processId.toString() %>" />
	</fr:edit>
<%--  ### End of Operation Area  ### --%>
