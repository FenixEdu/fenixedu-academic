<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>


<logic:present role="MANAGER">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.manageMigratedProcesses" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<p>
	<html:link action="/phdIndividualProgramProcess.do?method=manageProcesses">
		« <bean:message key="label.back" bundle="PHD_RESOURCES" />
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%-- ### Operational Area ### --%>

<%--  ### Search Criteria  ### --%>
<fr:form id="search" action="/phdIndividualProgramProcess.do?method=viewMigratedProcesses">
	<input type="hidden" name="sortBy" value="" />

	<fr:edit id="searchMigrationProcessBean"
		name="searchMigrationProcessBean"
		schema="SearchPhdMigrationProcessBean.edit">
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,hidden" />
		</fr:layout>
	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.search"/></html:submit>

	<logic:empty name="migrationProcesses">
		<p>
		<bean:message key="label.phd.missing.migrationProcess" bundle="PHD_RESOURCES" />
		</p>
	</logic:empty> 
	
	<logic:notEmpty name="migrationProcesses">
		<p>
		<bean:size id="processesCount" name="migrationProcesses"/>
		<bean:message  key="label.phd.process.count" bundle="PHD_RESOURCES" arg0="<%= processesCount.toString() %>"/>
		</p>
		
		<fr:view name="migrationProcesses" schema="PhdMigrationIndividualProcessData.simple">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle2 thlight mtop05" />
				<fr:property name="linkFormat(view)" value="/phdIndividualProgramProcess.do?method=viewMigrationProcess&migrationProcessId=${number}"/>
				<fr:property name="key(view)" value="label.view"/>
				<fr:property name="bundle(view)" value="PHD_RESOURCES"/>
				
				<fr:link name="process" label="label.phd.migration.associated.process,PHD_RESOURCES" order="2" condition="migratedToIndividualProgramProcess"
 					link="/phdIndividualProgramProcess.do?method=viewProcess&processId=${migratedIndividualProgramProcess.externalId}" />
	
				<fr:property name="sortFormId" value="search"/>
				<fr:property name="sortActionLink" value="true"/>
				<fr:property name="sortParameter" value="sortBy"/>
	            <fr:property name="sortBy" value="<%= request.getParameter("sortBy") != null ? request.getParameter("sortBy"): "number"  %>"/>
				<fr:property name="ascendingImage" value="/images/upArrow.gif"/>
	        	<fr:property name="descendingImage" value="/images/downArrow.gif"/>
				<fr:property name="sortableSlots" value="processBean.processNumber,migrationStatus,migrationDate" />
			</fr:layout>
		</fr:view>
		
	</logic:notEmpty>

</fr:form>

</logic:present>