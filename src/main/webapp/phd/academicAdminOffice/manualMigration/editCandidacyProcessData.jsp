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

<bean:define id="processData" name="processData" />
<bean:define id="processNumber" name="processData" property="number" />
<bean:define id="processDataBean" name="processData" property="processBean" />

<strong><p><bean:message key="label.phd.manual.migration.fixCandidacyInfo" bundle="PHD_RESOURCES" /></strong></p> 

<fr:form action="<%= "/phdIndividualProgramProcess.do?method=editMigrationPersonalData&migrationProcessId=" + processNumber.toString()%>">
	<input type="hidden" name="method" />
	
	<fr:edit id="processDataBean" name="processDataBean" schema="PhdMigrationIndividualProcessDataBean.manualMigration.candidacy">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05" />
			<fr:property name="columnClasses" value="width200px,width500px" />
		</fr:layout>
		
	</fr:edit>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.continue"/></html:submit>
</fr:form>
