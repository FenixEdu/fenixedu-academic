<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
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
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + processId.toString() %>">
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

	<ul>
		<li>
			<html:link action="/phdAccountingEventsManagement.do?method=createPhdRegistrationFee" paramId="processId" paramName="process" paramProperty="externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.accounting.events.create.registration.fee"/>
			</html:link>
		</li>
		<li>
			<html:link action="/phdAccountingEventsManagement.do?method=prepareCreateInsuranceEvent" paramId="processId" paramName="process" paramProperty="externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.accounting.events.create.insurance.event"/>
			</html:link>
		</li>
		<%-- <logic:equal name="process" property="inWorkDevelopment" value="true" > --%>		
		<li>
			<html:link action="/phdAccountingEventsManagement.do?method=prepareCreateGratuityEvent" paramId="processId" paramName="process" paramProperty="externalId">
				Criar dívida de Propina
			</html:link>
		</li>
		<%-- </logic:equal>--%>
	</ul>
	
	

<%--  ### End of Operation Area  ### --%>

</logic:present>