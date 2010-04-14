<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess"%>

<logic:notEmpty name="process" property="seminarProcess">
<logic:equal name="process" property="activeState.active" value="true">

	<br/>
	<strong><bean:message  key="label.phd.publicPresentationSeminarProcess" bundle="PHD_RESOURCES"/></strong>
	<fr:view schema="PublicPresentationSeminarProcess.view" name="process" property="seminarProcess">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop10" />
		</fr:layout>
	</fr:view>
	<bean:define id="seminarProcess" name="process" property="seminarProcess" />
	<ul class="operations">
		
		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= PublicPresentationSeminarProcess.SubmitComission.class %>">
		<li style="display: inline;">
			<html:link action="/publicPresentationSeminarProcess.do?method=prepareSubmitComission" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.submit.public.presentation.seminar.comission"/>
			</html:link>
		</li>
		</phd:activityAvailable>
		
		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= PublicPresentationSeminarProcess.ValidateComission.class %>">
		<li style="display: inline;">
			<html:link action="/publicPresentationSeminarProcess.do?method=prepareValidateComission" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.validate.public.presentation.seminar.comission"/>
			</html:link>
		</li>
		</phd:activityAvailable>
		
		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= PublicPresentationSeminarProcess.SchedulePresentationDate.class %>">
		<li style="display: inline;">
			<html:link action="/publicPresentationSeminarProcess.do?method=prepareSchedulePresentationDate" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.schedule.public.presentation.seminar.date"/>
			</html:link>
		</li>
		</phd:activityAvailable>
		
		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= PublicPresentationSeminarProcess.UploadReport.class %>">
		<li style="display: inline;">
			<html:link action="/publicPresentationSeminarProcess.do?method=prepareUploadReport" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.upload.public.presentation.seminar.report"/>
			</html:link>
		</li>
		</phd:activityAvailable>
		
		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= PublicPresentationSeminarProcess.ValidateReport.class %>">
		<li style="display: inline;">
			<html:link action="/publicPresentationSeminarProcess.do?method=prepareValidateReport" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.validate.public.presentation.seminar.report"/>
			</html:link>
		</li>
		</phd:activityAvailable>
		
		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= PublicPresentationSeminarProcess.DownloadComissionDocument.class %>">
		<li style="display: inline;">
			<bean:define id="comissionDocumentUrl" name="seminarProcess" property="comissionDocument.downloadUrl" />
			<a href="<%= comissionDocumentUrl.toString() %>"><bean:message  key="label.phd.public.presentation.seminar.comission.document" bundle="PHD_RESOURCES"/></a>
		</li>
		</phd:activityAvailable>
		
		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= PublicPresentationSeminarProcess.DownloadReportDocument.class %>">
		<li style="display: inline;">
			<bean:define id="reportDocumentUrl" name="seminarProcess" property="reportDocument.downloadUrl" />
			<a href="<%= reportDocumentUrl.toString() %>"><bean:message  key="label.phd.public.presentation.seminar.report.document" bundle="PHD_RESOURCES"/></a>
		</li>
		</phd:activityAvailable>
	
	</ul>
</logic:equal>
<br/>
</logic:notEmpty>
