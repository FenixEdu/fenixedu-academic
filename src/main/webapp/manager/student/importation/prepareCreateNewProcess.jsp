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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<logic:present role="role(MANAGER)">

	<h2><bean:message key="title.dges.importation.process" bundle="MANAGER_RESOURCES" /></h2>

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>

	<p class="mtop15 mbottom05"><strong><bean:message key="title.dges.importation.process.jobs.create" bundle="MANAGER_RESOURCES" /></strong></p>

	<fr:form action="/dgesStudentImportationProcess.do?method=createNewImportationProcess" encoding="multipart/form-data">
		<fr:edit id="importation.bean" name="importationBean" visible="false" />
		
		<fr:edit id="importation.bean.edit" name="importationBean">
			<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.manager.student.importation.DgesStudentImportationProcessDA$DgesBaseProcessBean">
				<fr:slot name="executionYear" layout="menu-select" key="label.dges.importation.process.execution.year" required="true" >
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider"/>
					<fr:property name="format" value="${name}" />
					<fr:property name="sortBy" value="name=desc"/>
				</fr:slot>
				
				<fr:slot name="campus" layout="menu-select" key="label.dges.importation.process.campus" required="true" >
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.spaceManager.CampusProvider" />
					<fr:property name="format" value="${name}" />
					<fr:property name="sortBy" value="name=desc"/>
				</fr:slot>
				
				<fr:slot name="phase" layout="menu-select" key="label.dges.importation.process.entry.phase" required="true" >
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.manager.student.importation.DgesStudentImportationProcessDA$EntryPhaseProvider" />
					<fr:property name="format" value="${localizedName}" />
					<fr:property name="sortBy" value="localizedName"/>
				</fr:slot>
				
				
				<fr:slot name="stream" key="label.dges.importation.process.file">
					<fr:property name="fileNameSlot" value="filename"/>
					<fr:property name="fileSizeSlot" value="filesize"/>
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.FileValidator">
						<fr:property name="maxSize" value="3698688"/>
						<fr:property name="acceptedTypes" value="text/plain" />			
					</fr:validator>		
				</fr:slot>
			</fr:schema>
			
			<fr:destination name="invalid" path="/dgesStudentImportationProcess.do?method=createNewImportationProcessInvalid"/>
			<fr:destination name="cancel" path="/dgesStudentImportationProcess.do?method=list"/>			
		</fr:edit>
		
		<p>
			<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES" /></html:submit>
			<html:cancel><bean:message key="button.cancel" bundle="MANAGER_RESOURCES" /></html:cancel>
		</p>
	</fr:form>
</logic:present>