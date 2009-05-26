<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.manageProcesses" bundle="PHD_RESOURCES" /></h2>

<%-- ### End of Title ### --%>



<%--  ###  Return Links  ### --%>

<%--  ### Return Links  ### --%>




<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>



<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>

<%--  ### End Of Context Information  ### --%>



<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<br/>
<html:link action="/phdProgramCandidacyProcess.do?method=prepareSearchPerson">
	<bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.academicAdminOffice.createCandidacy"/>
</html:link>
<br/><br/>

<bean:size id="processesCount" name="processes"/>
<bean:message  key="label.phd.process.count" bundle="PHD_RESOURCES" arg0="<%= processesCount.toString() %>"/>
<logic:notEmpty name="processes">
	<fr:view schema="PhdIndividualProgramProcess.view.resume" name="processes">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			<fr:property name="linkFormat(view)" value="/phdIndividualProgramProcess.do?method=viewProcess&processId=${externalId}"/>
			<fr:property name="key(view)" value="label.view"/>
			<fr:property name="bundle(view)" value="PHD_RESOURCES"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="processes">
	<br/>
	<em><bean:message key="label.phd.no.processes" bundle="PHD_RESOURCES" /></em>
	<br/>
</logic:empty>

<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>


</logic:present>


