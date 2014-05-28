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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

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
