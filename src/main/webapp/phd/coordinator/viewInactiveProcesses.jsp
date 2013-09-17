<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<logic:present role="COORDINATOR">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.coordinator.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.manageInactiveProcesses" bundle="PHD_RESOURCES" /></h2>

<%-- ### End of Title ### --%>

<%--  ###  Return Links  ### --%>

<p>
<html:link action="/phdIndividualProgramProcess.do?method=manageProcesses">
	« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
</p>

<%--  ### Return Links  ### --%>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>


<jsp:include page="/phd/common/viewInactiveProcesses.jsp"/>


</logic:present>
