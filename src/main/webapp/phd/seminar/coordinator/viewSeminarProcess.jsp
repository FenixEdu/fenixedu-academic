<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.SubmitComission"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.SchedulePresentationDate"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.DownloadComissionDocument"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.DownloadReportDocument"%>

<br/>

<logic:notEmpty name="process" property="seminarProcess">

	<strong><bean:message  key="label.phd.publicPresentationSeminarProcess" bundle="PHD_RESOURCES"/></strong>
	<fr:view schema="PublicPresentationSeminarProcess.view.simple" name="process" property="seminarProcess">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop10" />
		</fr:layout>
	</fr:view>

	<bean:define id="seminarProcess" name="process" property="seminarProcess" />

	<ul class="operations">

		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= SubmitComission.class %>">
		<li style="display: inline;">
			<html:link action="/publicPresentationSeminarProcess.do?method=prepareSubmitComission" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.submit.public.presentation.seminar.comission"/>
			</html:link>
		</li>
		</phd:activityAvailable>

		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= SchedulePresentationDate.class %>">
		<li style="display: inline;">
			<html:link action="/publicPresentationSeminarProcess.do?method=prepareSchedulePresentationDate" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.schedule.public.presentation.seminar.date"/>
			</html:link>
		</li>
		</phd:activityAvailable>

		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= DownloadComissionDocument.class %>">
		<li style="display: inline;">
			<bean:define id="comissionDocumentUrl" name="seminarProcess" property="comissionDocument.downloadUrl" />
			<a href="<%= comissionDocumentUrl.toString() %>"><bean:message  key="label.phd.public.presentation.seminar.comission.document" bundle="PHD_RESOURCES"/></a>
		</li>
		</phd:activityAvailable>

		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= DownloadReportDocument.class %>">
		<li style="display: inline;">
			<bean:define id="reportDocumentUrl" name="seminarProcess" property="reportDocument.downloadUrl" />
			<a href="<%= reportDocumentUrl.toString() %>"><bean:message  key="label.phd.public.presentation.seminar.report.document" bundle="PHD_RESOURCES"/></a>
		</li>
		</phd:activityAvailable>

	</ul>
	<br/>
	<br/>
</logic:notEmpty>
