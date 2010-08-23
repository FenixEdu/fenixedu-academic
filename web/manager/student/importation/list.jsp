<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<logic:present role="MANAGER">

	<h2><bean:message key="title.dges.importation.process" bundle="MANAGER_RESOURCES" /></h2>

	<fr:form action="/dgesStudentImportationProcess.do?method=createNewImportationProcess">
		<fr:edit id="importation.bean" name="importationBean" visible="false" />
		
		<fr:edit id="importation.bean.edit" name="importationBean">
			<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.manager.student.importation.DgesStudentImportationProcessDA$DgesStudentImportationProcessBean">
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
	
				<fr:link label="label.dges.importation.process.cancel" name="cancel" link="/dgesStudentImportationProcess.do?method=cancelImportationProcess&queueJobId=${externalId}" condition="isNotDoneAndNotCancelled"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<logic:equal name="canRequestJob" value="true">
	<p>
		<html:link page="/dgesStudentImportationProcess.do?method=prepareCreateNewImportationProcess">
			<bean:message key="link.dges.importation.process.request" bundle="MANAGER_RESOURCES"/>
		</html:link>
	</p>
	</logic:equal>

</logic:present>
