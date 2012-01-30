<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	
	<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.event.reports.request.view" /></h2>
	
	<bean:define id="queueJob" name="queueJob" type="net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob" />
	
	<fr:view name="queueJob">
		<fr:schema type="net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob" bundle="ACADEMIC_OFFICE_RESOURCES">		
			<fr:slot name="beginDate" />
			<fr:slot name="endDate" />
			
		<logic:notEmpty name="queueJob" property="forExecutionYear" >
			<fr:slot name="forExecutionYear.name" />
		</logic:notEmpty>
			<fr:slot name="exportGratuityEvents" />
			<fr:slot name="exportAcademicServiceRequestEvents" />

		<logic:equal name="administrativeOfficeType" value="DEGREE">
			<fr:slot name="exportIndividualCandidacyEvents" />
		</logic:equal>

		<logic:equal name="administrativeOfficeType" value="MASTER_DEGREE">
			<fr:slot name="exportPhdEvents" />
		</logic:equal>

			<fr:slot name="exportOthers" />

		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
		</fr:layout>
		
	</fr:view>
	
	<p>
		<html:link action="/eventReports.do?method=listReports">
			<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
		</html:link>
	</p>
	
</logic:present>