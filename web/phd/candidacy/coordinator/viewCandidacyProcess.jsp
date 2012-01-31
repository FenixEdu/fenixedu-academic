<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/phd.tld" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.activities.UploadCandidacyReview"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess.UploadCandidacyFeedback"%>

<bean:define id="candidacyProcess" name="process" property="candidacyProcess" />

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
			<phd:activityAvailable process="<%= candidacyProcess %>" activity="<%= UploadCandidacyReview.class %>">
			<li style="display: inline;">
				<html:link action="/phdProgramCandidacyProcess.do?method=manageCandidacyReview" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.manageCandidacyReview"/>
				</html:link>
			</li>
			</phd:activityAvailable>
			<li style="display: inline;">
				<html:link action="/phdCandidacyFeedbackRequest.do?method=manageFeedbackRequest" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.manage.candidacy.feedback.request"/>
				</html:link>
			</li>
			<logic:notEmpty name="candidacyProcess" property="feedbackRequest">
				<bean:define id="feedbackRequest" name="candidacyProcess" property="feedbackRequest" />
				<phd:activityAvailable process="<%= feedbackRequest %>" activity="<%= UploadCandidacyFeedback.class %>">
					<li style="display: inline;">
						<html:link action="/phdCandidacyFeedbackRequest.do?method=prepareUploadCandidacyFeedback" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
							<bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.feedback.teacher"/>
						</html:link>
					</li>
				</phd:activityAvailable>
			</logic:notEmpty>
		</ul>
    </td>
  </tr>
</table>
