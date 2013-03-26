<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


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
