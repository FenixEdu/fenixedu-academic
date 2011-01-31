<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.manage.emails.create" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="process" name="process" />
<bean:define id="processDataBean" name="processDataBean" />

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<p>
	<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&amp;processId=" + processId %>">
		« <bean:message key="label.back" bundle="PHD_RESOURCES" />
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%-- ### Operational Area ### --%>

<logic:present name="processDataBean">
	<fr:view name="processDataBean" schema="PhdMigrationIndividualProcessDataBean">
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop1" />
		</fr:layout>
		
	</fr:view>
</logic:present>
<logic:notPresent name="processDataBean">
	Does not have process bean!
</logic:notPresent>

<logic:present name="personalDataBean">
	<fr:view name="personalDataBean" schema="PhdMigrationIndividualPersonalDataBean">
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop1" />
		</fr:layout>
		
	</fr:view>
</logic:present>
<logic:notPresent name="personalDataBean">
	Does not have personal bean!
</logic:notPresent>

<logic:present name="migrationGuidingBean">
	<fr:view name="migrationGuidingBean" schema="PhdMigrationGuidingBean">
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop1" />
		</fr:layout>
		
	</fr:view>
</logic:present>
<logic:notPresent name="migrationGuidingBean">
	Does not have migration guiding bean!
</logic:notPresent>

<logic:present name="migrationAssistantGuidingBean">
	<fr:view name="migrationAssistantGuidingBean" schema="PhdMigrationGuidingBean">
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop1" />
		</fr:layout>
		
	</fr:view>
</logic:present>
<logic:notPresent name="migrationAssistantGuidingBean">
	Does not have migration assistant guiding bean!
</logic:notPresent>



</logic:present>