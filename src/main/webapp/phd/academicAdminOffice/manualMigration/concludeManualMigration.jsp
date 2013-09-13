<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manualMigrateProcess" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<p>
	<html:link action="/phdIndividualProgramProcess.do?method=viewMigratedProcesses">
		« <bean:message key="label.back" bundle="PHD_RESOURCES" />
	</html:link>	
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%-- ### Operational Area ### --%>


<logic:notEmpty name="migratedProcess">
	<bean:define id="migratedProcess" name="migratedProcess" />

	<bean:define id="migratedProcessId" name="migratedProcess" property="externalId"/>
	<p>
		<html:link action="<%="/phdIndividualProgramProcess.do?method=viewProcess&processId=" + migratedProcessId %>">
			<bean:message bundle="PHD_RESOURCES" key="label.view.migrated.process"/>
		</html:link>
	</p>
</logic:notEmpty>

