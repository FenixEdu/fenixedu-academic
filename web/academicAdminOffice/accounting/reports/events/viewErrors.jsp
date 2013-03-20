<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.event.reports.request.errors" /></h2>

<bean:define id="queueJob" name="queueJob" type="net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob" />

<fr:view name="queueJob">
	<fr:schema type="net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob" bundle="ACADEMIC_OFFICE_RESOURCES">		
		<fr:slot name="errors" />
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
