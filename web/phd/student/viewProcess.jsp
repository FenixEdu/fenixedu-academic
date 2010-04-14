<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>


<%@page import="net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.DownloadComissionDocument"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess.DownloadReportDocument"%><logic:present role="STUDENT">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.student.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.viewProcess" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<jsp:include page="/phd/alertMessagesNotifier.jsp?global=false" />

<logic:present name="process">
	<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
	<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
	<table>
	  <tr>
	    <td>
			<fr:view schema="PhdIndividualProgramProcess.view" name="process">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop10" />
				</fr:layout>
			</fr:view>
		</td>
	    <td style="vertical-align: top; padding-top: 1em;">
	    	<ul class="operations">
				<li>
					<html:link action="/phdIndividualProgramProcess.do?method=viewProcessAlertMessages" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.alertMessages"/>
					</html:link>
				</li>
			</ul>
	    </td>
	  </tr>
	</table>
	
	<%-- CAT --%>
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
	</logic:notEmpty>
	
	
	<%-- Candidacy Process --%>
	<br/>
	<strong><bean:message  key="label.phd.candidacyProcess" bundle="PHD_RESOURCES"/></strong>
	<table>
	  <tr>
	    <td>
			<fr:view schema="PhdProgramCandidacyProcess.view.simple" name="process" property="candidacyProcess">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop10" />
				</fr:layout>
			</fr:view>
		</td>
	  </tr>
	  <tr>
	    <td>
	    	<bean:define id="candidacyProcess" name="process" property="candidacyProcess" />
	    	<ul class="operations">
	    		<li style="display: inline;">
					<html:link action="/phdProgramCandidacyProcess.do?method=manageCandidacyDocuments" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.manageCandidacyDocuments"/>
					</html:link>
				</li>
			</ul>
	    </td>
	  </tr>
	</table>
	<%--  ### End Of Context Information  ### --%>
</logic:present>

<logic:notPresent name="process">
	<em><bean:message bundle="PHD_RESOURCES" key="label.phd.no.processes" /></em>
</logic:notPresent>

</logic:present>