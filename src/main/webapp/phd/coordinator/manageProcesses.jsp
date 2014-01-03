<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<logic:present role="role(COORDINATOR)">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.coordinator.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.manageProcesses" bundle="PHD_RESOURCES" /></h2>

<%-- ### End of Title ### --%>



<%--  ###  Return Links  ### --%>

<%--  ### Return Links  ### --%>




<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>



<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<ul>
	<li><jsp:include page="/phd/alertMessagesNotifier.jsp?global=true" /></li>

<%--  ### End Of Context Information  ### --%>



<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

	<li>
		<html:link action="/phdIndividualProgramProcess.do?method=viewInactiveProcesses">
			<bean:message bundle="PHD_RESOURCES" key="label.viewInactiveProcesses"/>
		</html:link>
	</li>
 	
	<li>
		<html:link action="/phdIndividualProgramProcess.do?method=managePhdEmails">
			<bean:message bundle="PHD_RESOURCES" key="label.phd.prepare.email.management" />
		</html:link>
	</li>
	
</ul>

<jsp:include page="/phd/common/manageProcesses.jsp"/>

</logic:present>
