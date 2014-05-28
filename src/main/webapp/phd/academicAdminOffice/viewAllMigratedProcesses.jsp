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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%-- ### Title #### --%>
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
	
				<fr:link name="migrate" label="label.phd.migration.create.manual,PHD_RESOURCES" order="3" condition="notMigrated"
 					link="/phdIndividualProgramProcess.do?method=prepareManualMigration&migrationProcessId=${number}" />
	
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
