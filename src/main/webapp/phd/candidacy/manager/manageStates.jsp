<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<logic:present role="role(MANAGER)">

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
<html:link action="<%= "/phdProgramCandidacyProcess.do?method=viewIndividualProgramProcess&processId=" + processId %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.candidacy" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="PhdProgramCandidacyProcess.view" name="process">
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
		<fr:slot name="stateDate" key="label.net.sourceforge.fenixedu.domain.phd.PhdProcessState" layout="null-as-label" />
		<fr:slot name="type.localizedName" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState.type" />
		<fr:slot name="remarks" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState.remarks" />
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
		<fr:property name="sortBy" value="whenCreated=desc" />
	</fr:layout>
</fr:view>

<%-- ### End of List of Process States ### --%>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<%--  ### End of Operation Area  ### --%>


<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>
</logic:present>