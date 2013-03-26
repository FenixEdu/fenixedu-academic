<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manualMigrateProcess" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<bean:define id="processData" name="processData" />
<bean:define id="processNumber" name="processData" property="number" />
<bean:define id="chosenCandidate" name="personalDataBean" property="chosenPersonManually" />

<p>
	<html:link action="<%= "/phdIndividualProgramProcess.do?method=prepareManualMigration&migrationProcessId=" + processNumber %>">
		« <bean:message key="label.back" bundle="PHD_RESOURCES" />
	</html:link>	
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%-- ### Operational Area ### --%>

<p>
	<strong><bean:message key="label.phd.manual.migration.confirmAllData" bundle="PHD_RESOURCES" /></strong>
</p>

<fr:form action="<%= "/phdIndividualProgramProcess.do?method=createCandidacyManualMigration&migrationProcessId=" + processNumber.toString()%>">

	<fr:edit id="processDataBean" name="processDataBean" visible="false" />
	<fr:edit id="personalDataBean" name="personalDataBean" visible="false" />

	<fr:view name="processDataBean" schema="PhdMigrationIndividualProcessDataBean.manualMigration.candidacy">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05" />
			<fr:property name="columnClasses" value="width200px,width500px" />
		</fr:layout>
		
	</fr:view>
		
	<fr:view name="personalDataBean" schema="PhdMigrationIndividualPersonalDataBean.simple.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05" />
			<fr:property name="columnClasses" value="width200px,width500px" />
		</fr:layout>	
	</fr:view>

	<fr:view name="chosenCandidate">
		<fr:schema bundle="PHD_RESOURCES" type="<%= net.sourceforge.fenixedu.domain.Person.class.getName() %>">
			<fr:slot name="name">
				<fr:property name="classes" value="bold nowrap"/>
			</fr:slot>
			<fr:slot name="gender" />
			<fr:slot name="idDocumentType" />
			<fr:slot name="documentIdNumber" />
			<fr:slot name="emissionLocationOfDocumentId" />
			<fr:slot name="socialSecurityNumber" />
		   	<fr:slot name="dateOfBirth" />
			<fr:slot name="districtSubdivisionOfBirth" />
			<fr:slot name="nationality.countryNationality" />
			<fr:slot name="address" />
			<fr:slot name="area" />
			<fr:slot name="areaCode" />
			<fr:slot name="phone" />
			<fr:slot name="mobile" />
			<fr:slot name="email" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05" />
			<fr:property name="columnClasses" value="width200px,width500px" />
		</fr:layout>
	</fr:view>

  	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.phd.migration.create.manual"/></html:submit>	
</fr:form>
