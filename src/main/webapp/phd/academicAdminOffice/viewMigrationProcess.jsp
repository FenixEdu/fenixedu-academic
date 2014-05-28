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
