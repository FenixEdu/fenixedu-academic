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

	<fr:form action="/dgesStudentImportationProcess.do?method=createNewImportationProcess">
		<fr:edit id="importation.bean" name="importationBean" visible="false" />
		
		<fr:edit id="importation.bean.edit" name="importationBean">
			<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.manager.student.importation.DgesStudentImportationProcessDA$DgesBaseProcessBean">
				<fr:slot name="executionYear" layout="menu-select" key="label.dges.importation.process.execution.year" required="true" >
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider"/>
					<fr:property name="format" value="${name}" />
					<fr:property name="sortBy" value="name=desc"/>
				</fr:slot>
			</fr:schema>
		</fr:edit>
	</fr:form>				

	
	<p class="mtop15 mbottom05"><strong><bean:message key="title.dges.importation.process.jobs.done" bundle="MANAGER_RESOURCES" /></strong></p>
	
	
	<logic:empty name="importationJobsDone">
		<em><bean:message key="message.dges.importation.process.jobs.done.empty" bundle="MANAGER_RESOURCES" /></em>
	</logic:empty>
		
	<logic:notEmpty name="importationJobsDone" >
		<fr:view name="importationJobsDone" >
			<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.student.importation.DgesStudentImportationProcess">
				<fr:slot name="filename" key="label.dges.importation.process.filename" />
				<fr:slot name="requestDate" key="label.dges.importation.process.request.date" />
				<fr:slot name="jobStartTime" key="label.dges.importation.process.start.time" />
				<fr:slot name="jobEndTime" key="label.dges.importation.process.end.time" />
				<fr:slot name="dgesStudentImportationFile" key="label.dges.importation.process.importation.content" layout="link" />
			</fr:schema>
			
			<fr:layout name="tabular">
		   		<fr:property name="classes" value="tstyle1 mtop05" />
		    	<fr:property name="columnClasses" value=",,,acenter,,,,,," />
			
				<fr:link label="label.dges.importation.process.view,MANAGER_RESOURCES" name="view" link="/downloadQueuedJob.do?method=downloadFile&id=${externalId}" module="" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<p class="mtop15 mbottom05"><strong><bean:message key="title.dges.importation.process.jobs.undone" bundle="MANAGER_RESOURCES" /></strong></p>
	
	<logic:empty name="importationJobsPending">
		<em><bean:message key="message.dges.importation.process.jobs.undone.empty" bundle="MANAGER_RESOURCES" /></em>
	</logic:empty>
	
	<logic:notEmpty name="importationJobsPending">
		<fr:view name="importationJobsPending" >
			<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.student.importation.DgesStudentImportationProcess">
				<fr:slot name="requestDate" key="label.dges.importation.process.request.date" />
				<fr:slot name="jobStartTime" key="label.dges.importation.process.start.time" />
				<fr:slot name="jobEndTime" key="label.dges.importation.process.end.time" />
				<fr:slot name="isNotDoneAndCancelled" key="label.dges.importation.process.cancelled" />
				<fr:slot name="dgesStudentImportationFile" key="label.dges.importation.process.importation.content" layout="link" />
			</fr:schema>
			
			<fr:layout name="tabular">
		   		<fr:property name="classes" value="tstyle1 mtop05" />
		    	<fr:property name="columnClasses" value=",,,acenter,,,,,," />
	
				<fr:link label="label.dges.importation.process.cancel" name="cancel" link="/dgesStudentImportationProcess.do?method=cancelJob&queueJobId=${externalId}" condition="isNotDoneAndNotCancelled"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<logic:equal name="canRequestJobImportationProcess" value="true">
	<p>
		<html:link page="/dgesStudentImportationProcess.do?method=prepareCreateNewImportationProcess">
			<bean:message key="link.dges.importation.process.request" bundle="MANAGER_RESOURCES"/>
		</html:link>
	</p>
	</logic:equal>
	
	
	<hr/>
	
	<h2><bean:message key="title.dges.exportation.degree.candidacies.for.passwords" bundle="MANAGER_RESOURCES" /></h2>
	
	<p class="mtop15 mbottom05"><strong><bean:message key="title.dges.exportation.degree.candidacies.for.passwords.jobs.done" bundle="MANAGER_RESOURCES" /></strong></p>
	
	<logic:empty name="exportationPasswordsDone">
		<em><bean:message key="message.dges.exportation.degree.candidacies.for.passwords.jobs.done.empty" bundle="MANAGER_RESOURCES" /></em>
	</logic:empty>
		
	<logic:notEmpty name="exportationPasswordsDone" >
		<fr:view name="exportationPasswordsDone" >
			<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.student.importation.ExportDegreeCandidaciesByDegreeForPasswordGeneration">
				<fr:slot name="filename" key="label.dges.exportation.degree.candidacies.for.passwords.filename" />
				<fr:slot name="requestDate" key="label.exportation.degree.candidacies.for.passwords.request.date" />
				<fr:slot name="jobStartTime" key="label.exportation.degree.candidacies.for.passwords.start.time" />
				<fr:slot name="jobEndTime" key="label.exportation.degree.candidacies.for.passwords.end.time" />
			</fr:schema>
			
			<fr:layout name="tabular">
		   		<fr:property name="classes" value="tstyle1 mtop05" />
		    	<fr:property name="columnClasses" value=",,,acenter,,,,,," />
			
				<fr:link label="label.dges.exportation.degree.candidacies.for.passwords.view,MANAGER_RESOURCES" name="view" link="/downloadQueuedJob.do?method=downloadFile&id=${externalId}" module="" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<p class="mtop15 mbottom05"><strong><bean:message key="title.dges.exportation.degree.candidacies.for.passwords.jobs.undone" bundle="MANAGER_RESOURCES" /></strong></p>
	
	<logic:empty name="exportationPasswordsPending">
		<em><bean:message key="message.dges.exportation.degree.candidacies.for.passwords.jobs.undone.empty" bundle="MANAGER_RESOURCES" /></em>
	</logic:empty>
	
	<logic:notEmpty name="exportationPasswordsPending">
		<fr:view name="exportationPasswordsPending" >
			<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.student.importation.ExportDegreeCandidaciesByDegreeForPasswordGeneration">
				<fr:slot name="requestDate" key="label.dges.exportation.degree.candidacies.for.passwords.request.date" />
				<fr:slot name="jobStartTime" key="label.dges.exportation.degree.candidacies.for.passwords.start.time" />
				<fr:slot name="jobEndTime" key="label.dges.exportation.degree.candidacies.for.passwords.end.time" />
				<fr:slot name="isNotDoneAndCancelled" key="label.dges.exportation.degree.candidacies.for.passwords.cancelled" />
			</fr:schema>
			
			<fr:layout name="tabular">
		   		<fr:property name="classes" value="tstyle1 mtop05" />
		    	<fr:property name="columnClasses" value=",,,acenter,,,,,," />
	
				<fr:link label="label.dges.exportation.degree.candidacies.for.passwords.cancel" name="cancel" link="/dgesStudentImportationProcess.do?method=cancelJob&queueJobId=${externalId}" condition="isNotDoneAndNotCancelled"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

	<logic:equal name="canRequestJobExportationPasswords" value="true">
	<p>
		<html:link page="/dgesStudentImportationProcess.do?method=prepareCreateNewExportationCandidaciesForPasswordGenerationJob">
			<bean:message key="link.dges.exportation.degree.candidacies.for.passwords.request" bundle="MANAGER_RESOURCES"/>
		</html:link>
	</p>
	</logic:equal>

</logic:present>
