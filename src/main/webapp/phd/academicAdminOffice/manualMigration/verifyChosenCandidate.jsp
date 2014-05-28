<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

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
