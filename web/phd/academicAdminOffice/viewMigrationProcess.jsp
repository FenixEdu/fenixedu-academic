<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.viewMigrationProcess" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<logic:present name="process">
	<bean:define id="processId" name="process" property="externalId" />
	<p>
		<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&amp;processId=" + processId %>">
			« <bean:message key="label.back" bundle="PHD_RESOURCES" />
		</html:link>
	</p>
</logic:present>
<logic:notPresent name="process">
	<p>
		<html:link action="/phdIndividualProgramProcess.do?method=viewMigratedProcesses">
			« <bean:message key="label.back" bundle="PHD_RESOURCES" />
		</html:link>	
	</p>
</logic:notPresent>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%-- ### Operational Area ### --%>

<p class="mtop15 mbottom05"><strong><bean:message key="label.phd.migration.processData" bundle="PHD_RESOURCES" /></strong></p>

<logic:present name="processDataBean">
	<fr:view name="processDataBean" schema="PhdMigrationIndividualProcessDataBean">
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05" />
			<fr:property name="columnClasses" value="width200px,width500px" />
		</fr:layout>
		
	</fr:view>
</logic:present>
<logic:notPresent name="processDataBean">
	<em><bean:message key="label.phd.missing.migrationProcessBean" bundle="PHD_RESOURCES"/></em>
</logic:notPresent>

<p class="mtop15 mbottom05"><strong><bean:message key="label.phd.migration.personalData" bundle="PHD_RESOURCES" /></strong></p>

<logic:present name="personalDataBean">
	<fr:view name="personalDataBean" schema="PhdMigrationIndividualPersonalDataBean">
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05" />
			<fr:property name="columnClasses" value="width200px,width500px" />
		</fr:layout>
		
	</fr:view>
</logic:present>
<logic:notPresent name="personalDataBean">
	<em><bean:message key="label.phd.missing.migrationPersonalBean" bundle="PHD_RESOURCES"/></em>
</logic:notPresent>

<p class="mtop15 mbottom05"><strong><bean:message key="label.phd.migration.guidingData" bundle="PHD_RESOURCES" /></strong></p>

<logic:present name="migrationGuidingBean">
	<fr:view name="migrationGuidingBean" schema="PhdMigrationGuidingBean">
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05" />
			<fr:property name="columnClasses" value="width200px,width500px" />
		</fr:layout>
		
	</fr:view>
</logic:present>
<logic:notPresent name="migrationGuidingBean">
	<em><bean:message key="label.phd.missing.migrationGuidingBean" bundle="PHD_RESOURCES"/></em>
</logic:notPresent>

<p class="mtop15 mbottom05"><strong><bean:message key="label.phd.migration.assistantGuidingData" bundle="PHD_RESOURCES" /></strong></p>

<logic:present name="migrationAssistantGuidingBean">
	<fr:view name="migrationAssistantGuidingBean" schema="PhdMigrationGuidingBean">
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05" />
			<fr:property name="columnClasses" value="width200px,width500px" />
		</fr:layout>
		
	</fr:view>
</logic:present>
<logic:notPresent name="migrationAssistantGuidingBean">
	<em><bean:message key="label.phd.missing.migrationAssistantGuidingBean" bundle="PHD_RESOURCES"/></em>
</logic:notPresent>



</logic:present>