<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />


<logic:present role="MANAGER">

	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.event.reports.request.create" /></h2>
	
	<logic:messagesPresent message="true" property="error">
		<div class="error3 mbottom05" style="width: 700px;">
			<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="error">
				<p class="mvert025"><bean:write name="messages" /></p>
			</html:messages>
		</div>
	</logic:messagesPresent>
	
	
	<p><strong><bean:message key="message.event.reports.request.parameters" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
	
	
	<fr:form action="/eventReports.do?method=createReportRequest">
		
		<fr:edit id="bean" name="bean" visible="false" />
		
		<fr:edit id="bean-edit" name="bean">
		
			<fr:schema type="net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJobBean" bundle="ACADEMIC_OFFICE_RESOURCES" >
				<fr:slot name="beginDate" required="true" >
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DateValidator" />
				</fr:slot>
				<fr:slot name="endDate" required="true" >
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DateValidator" />
				</fr:slot>
				<fr:slot name="administrativeOffice" layout="menu-select-postback" key="label.academicAdminOffice">
					<fr:property name="from" value="availableOfficesForManager" />
					<fr:property name="format" value="${unit.name}" />
				</fr:slot>
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
				<fr:property name="columnClasses" value=",,tderror1 tdclear" />
			</fr:layout>
		
			<fr:destination name="postback" path="/eventReports.do?method=createReportRequestPostback" />
			<fr:destination name="invalid" path="/eventReports.do?method=createReportRequestInvalid" />
			<fr:destination name="cancel" path="/eventReports.do?method=listReports" />

		</fr:edit>

		<logic:present name="bean" property="administrativeOffice">

		<fr:edit id="bean-edit-options" name="bean">
		
			<fr:schema type="net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJobBean" bundle="ACADEMIC_OFFICE_RESOURCES" >
				
				<fr:slot name="executionYear" layout="menu-select">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
					<fr:property name="format" value="${name}" />
				</fr:slot>
				
				<fr:slot name="exportGratuityEvents" required="true" />
				<fr:slot name="exportAcademicServiceRequestEvents" required="true" />
				<fr:slot name="exportIndividualCandidacyEvents" required="true" />
				<fr:slot name="exportPhdEvents" required="true" />
				<fr:slot name="exportResidenceEvents" required="true" />
				<fr:slot name="exportOthers" required="true" />
			</fr:schema>

			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
				<fr:property name="columnClasses" value=",,tderror1 tdclear" />
			</fr:layout>
		
		</fr:edit>

		<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES" /></html:submit>
		</logic:present>

		<html:cancel><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</fr:form>
	
</logic:present>