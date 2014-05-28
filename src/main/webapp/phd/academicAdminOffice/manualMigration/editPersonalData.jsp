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
<h2><bean:message key="label.phd.manualMigrateProcess" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<bean:define id="processData" name="processData" type="net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationIndividualProcessData"/>
<bean:define id="processNumber" name="processData" property="number" />
<bean:define id="personalDataBean" name="processData" property="phdMigrationIndividualPersonalData.personalBean" />

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
	<strong><bean:message key="label.phd.manual.migration.confirmProcessData" bundle="PHD_RESOURCES" /></strong>
</p>

<fr:form action="<%= "/phdIndividualProgramProcess.do?migrationProcessId=" + processData.getNumber() %>">
	<input type="hidden" name="method" />
	
	<fr:edit id="processDataBean" name="processDataBean" visible="false"/>
	
	<fr:view name="processDataBean" schema="PhdMigrationIndividualProcessDataBean.manualMigration.candidacy">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05" />
			<fr:property name="columnClasses" value="width200px,width500px" />
		</fr:layout>	
	</fr:view>
	
	<% if(processData.isRegistered()) { %>
		<p><strong><bean:message key="label.phd.manual.migration.studentAlreadyExists" bundle="PHD_RESOURCES" /></strong></p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='createCandidacyManualMigration';"><bean:message bundle="PHD_RESOURCES" key="label.continue"/></html:submit>
	<% } else if(processData.isNotRegisteredAndNoSimilarsExist()) { %>
		<strong><p><bean:message key="label.phd.manual.migration.student.haveEnoughInfo" bundle="PHD_RESOURCES" /></strong></p>
		<fr:edit id="personalDataBean" name="personalDataBean" schema="PhdMigrationIndividualPersonalDataBean.manualMigration.candidacy">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft mtop05" />
				<fr:property name="columnClasses" value="width200px,width500px" />
			</fr:layout>
			
		</fr:edit>	
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='createCandidacyManualMigration';"><bean:message bundle="PHD_RESOURCES" key="label.continue"/></html:submit>
	<% } else if(processData.isThereAnySimilarRegistration()) { %>
		<strong><p><bean:message key="label.phd.manual.migration.student.notEnoughInfo" bundle="PHD_RESOURCES" /></strong></p>
		
		<fr:view name="personalDataBean" schema="PhdMigrationIndividualPersonalDataBean.simple.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft mtop05" />
				<fr:property name="columnClasses" value="width200px,width500px" />
			</fr:layout>	
		</fr:view>
		
		<fr:edit id="personalDataBean" name="personalDataBean" >
			<fr:schema type="net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationIndividualPersonalDataBean" bundle="PHD_RESOURCES">
				<fr:slot name="chosenPersonManually" layout="autoComplete" key="label.selectStudent.nameOrID" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredAutoCompleteSelectionValidator">
					<fr:property name="size" value="35" />
					<fr:property name="labelField" value="name" />
					<fr:property name="format" value="${name} - <strong>${istUsername}</strong>" />
					<fr:property name="args" value="slot=name,size=20" />
					<fr:property name="minChars" value="3" />
					<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchPeopleByNameOrISTID" />
					<fr:property name="indicatorShown" value="true" />
					<fr:property name="className" value="net.sourceforge.fenixedu.domain.Person" />
					<fr:property name="required" value="true"/>
				</fr:slot>
				
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight thleft mtop05" />
					<fr:property name="columnClasses" value="width200px,width500px" />
				</fr:layout>
			</fr:schema>
		</fr:edit>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='verifyChosenCandidate';"><bean:message bundle="PHD_RESOURCES" key="label.continue"/></html:submit>
	<% } %>	
</fr:form>
