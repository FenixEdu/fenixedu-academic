<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<fr:form action="/studentHighPerformance.do?method=listRequests">
	<fr:create id="job" type="net.sourceforge.fenixedu.domain.StudentHighPerformanceQueueJob">
		<fr:schema bundle="PEDAGOGICAL_COUNCIL" type="net.sourceforge.fenixedu.domain.StudentHighPerformanceQueueJob">
			<fr:slot name="executionInterval" layout="menu-select" required="true" key="label.highPerformance.executionInterval">
				<fr:property name="providerClass"
					value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ActiveAcademicIntervalProvider" />
				<fr:property name="format" value="${pathName}" />
				<fr:property name="nullOptionHidden" value="true" />
			</fr:slot>
		</fr:schema>
	</fr:create>
	<html:submit>
		<bean:message bundle="COMMON_RESOURCES" key="button.highPerformance.request" />
	</html:submit>
</fr:form>

<br/>

<fr:view name="jobs">
	<fr:schema type="net.sourceforge.fenixedu.domain.StudentHighPerformanceQueueJob" bundle="PEDAGOGICAL_COUNCIL">
		<fr:slot name="executionInterval.pathName" key="label.highPerformance.executionInterval" />
		<fr:slot name="requestDate" key="label.highPerformance.requestDate" />
		<fr:slot name="person.name" key="label.highPerformance.requestor" />
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 mtop05" />

		<fr:property name="link(download)" value="/downloadQueuedJob.do?method=downloadFile" />
		<fr:property name="param(download)" value="externalId/id" />
		<fr:property name="bundle(download)" value="GEP_RESOURCES" />
		<fr:property name="visibleIf(download)" value="done" />
		<fr:property name="module(download)" value="" />

		<fr:property name="link(resend)" value="/pedagogicalCouncil/studentHighPerformance.do?method=resendJob" />
		<fr:property name="param(resend)" value="externalId/id" />
		<fr:property name="key(resend)" value="label.sendJob" />
		<fr:property name="bundle(resend)" value="GEP_RESOURCES" />
		<fr:property name="visibleIf(resend)" value="isNotDoneAndCancelled" />
		<fr:property name="module(resend)" value="" />

		<fr:property name="link(cancel)" value="/pedagogicalCouncil/studentHighPerformance.do?method=cancelJob" />
		<fr:property name="param(cancel)" value="externalId/id" />
		<fr:property name="key(cancel)" value="label.cancel" />
		<fr:property name="bundle(cancel)" value="GEP_RESOURCES" />
		<fr:property name="visibleIf(cancel)" value="isNotDoneAndNotCancelled" />
		<fr:property name="module(cancel)" value="" />
	</fr:layout>
</fr:view>
