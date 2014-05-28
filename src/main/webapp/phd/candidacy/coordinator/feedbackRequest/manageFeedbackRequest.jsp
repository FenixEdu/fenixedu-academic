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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestElement"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess.EditSharedDocumentTypes"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess.AddPhdCandidacyFeedbackRequestElements"%>

<html:xhtml/>

<logic:present role="role(COORDINATOR)">

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manage.candidacy.feedback.request" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<bean:define id="individualProgramProcessId" name="process" property="individualProgramProcess.externalId" />

<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProgramProcessId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<table>
  <tr style="vertical-align: top;">
    <td style="width: 55%">
    	<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="PhdIndividualProgramProcess.view.simple" name="process" property="individualProgramProcess">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
	</td>
    <td>
	    <strong><bean:message  key="label.phd.candidacyProcess" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="PhdProgramCandidacyProcess.view.simple" name="process">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
    </td>
  </tr>
</table>


<%--  ### End Of Context Information  ### --%>

<logic:empty name="process" property="feedbackRequest">
	<logic:empty name="elementBean">
		<br/>
		<jsp:include page="/phd/candidacy/coordinator/feedbackRequest/editSharedDocumentTypes.jsp" />
	</logic:empty>
</logic:empty>

<logic:notEmpty name="process" property="feedbackRequest">

<%--  ################################### --%>
<%--  ###    add users to request     ### --%>

	<bean:define id="feedbackRequest" name="process" property="feedbackRequest" />
	
	<phd:activityAvailable process="<%= feedbackRequest %>" activity="<%= EditSharedDocumentTypes.class %>" >
		<logic:empty name="elementBean">
			<jsp:include page="/phd/candidacy/coordinator/feedbackRequest/editSharedDocumentTypes.jsp" />
		</logic:empty>
	</phd:activityAvailable>

	<phd:activityAvailable process="<%= feedbackRequest %>" activity="<%= AddPhdCandidacyFeedbackRequestElements.class %>" >
		<logic:empty name="elementBean">
			<br/>
			<br/>
			+ <html:link action="/phdCandidacyFeedbackRequest.do?method=prepareAddCandidacyFeedbackRequestElement" paramId="processId" paramName="process" paramProperty="externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.feedback.add.element"/>
			</html:link>
		</logic:empty>
		
		<jsp:include page="/phd/candidacy/coordinator/feedbackRequest/addCandidacyFeedbackRequestElement.jsp" />
	</phd:activityAvailable>
	
<%--  ###   End add users to request  ### --%>
<%--  ################################### --%>


<%--  ################################### --%>
<%--  ###      display elements       ### --%>

	<logic:empty name="elementBean">
	
		<fr:view name="process" property="feedbackRequest.elements">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
				<fr:property name="columnClasses" value=",,acenter,"/>

				<fr:link name="delete" label="label.delete,PHD_RESOURCES" condition="!feedbackSubmitted"
					confirmation="message.phd.candidacy.feedback.remove.element.confirmation,PHD_RESOURCES"
					link="/phdCandidacyFeedbackRequest.do?method=deleteCandidacyFeedbackRequestElement&elementOid=${externalId}&processId=${candidacyProcess.externalId}" />
			</fr:layout>
			
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdCandidacyFeedbackRequestElement.class.getName() %>">
				<fr:slot name="nameWithTitle" />
				<fr:slot name="email" />
				<fr:slot name="feedbackSubmitted" layout="boolean-icon">
					<fr:property name="falseIconPath" value="/images/icon_exclamation.gif" />
				</fr:slot>
				<fr:slot name="lastFeedbackDocument" layout="null-as-label">
					<fr:property name="subLayout" value="link" />
				</fr:slot>
			</fr:schema>
			
		</fr:view>
		
		<bean:size id="feedbackDocuments" name="process" property="feedbackRequest.submittedCandidacyFeedbackDocuments" />
		<logic:greaterEqual name="feedbackDocuments" value="1">
			<p>
			<span class="compress">
			<html:link action="/phdCandidacyFeedbackRequest.do?method=downloadSubmittedCandidacyFeedbackDocuments" paramId="processId" paramName="processId">
				<bean:message key="label.phd.documents.download.all" bundle="PHD_RESOURCES" />
			</html:link>
			</span>
			</p>
		</logic:greaterEqual>
		
	</logic:empty>
<%--  ###   End of display elements   ### --%>
<%--  ################################### --%>

</logic:notEmpty>

</logic:present>
